#!/usr/bin/env python3
import os
import re
import subprocess
import toml
import json
import requests
from collections import defaultdict
from datetime import datetime

# Extract current version from the most recent commit or newest tag
def get_current_version():
    # Try to get from the current tag if we're on a tag
    try:
        current_tag = subprocess.check_output(["git", "describe", "--exact-match", "--tags"]).decode().strip()
        if current_tag and current_tag != "latest":
            return current_tag
    except subprocess.CalledProcessError:
        pass
    
    # If no tag, try to extract from a version file or use a timestamp
    return f"next-{datetime.now().strftime('%Y%m%d')}"

# GitHub API configuration
# NOTE: These will need to be set as GitHub secrets and passed to the script
GITHUB_TOKEN = os.environ.get("GITHUB_TOKEN", "")
GITHUB_REPO = "SymmetricDevs/Supersymmetry"  # Format: owner/repo
GITHUB_ORG = os.environ.get("GITHUB_ORGANIZATION", "SymmetricDevs")  # Default organization
VERSION = os.environ.get("VERSION", get_current_version())

# Find the latest tag (excluding "latest")
def get_latest_tag():
    tags = subprocess.check_output(["git", "tag", "-l", "--sort=-v:refname"]).decode().strip().split("\n")
    for tag in tags:
        if tag != "latest":
            return tag
    return None


# Get mod details from toml file
def parse_mod_file(file_path):
    try:
        data = toml.load(file_path)
        name = data.get("name", "Unknown")
        filename = data.get("filename", "")
        
        # Extract version from filename
        version_match = re.search(r"-([^-]+(?:-[^-]+)*?)\.jar$", filename)
        version = version_match.group(1) if version_match else "Unknown"
        
        return {
            "name": name,
            "version": version,
            "filename": filename
        }
    except Exception as e:
        print(f"Error parsing {file_path}: {e}")
        return None

# Get all mods in a specific git reference
def get_mods_at_ref(ref):
    result = {}
    try:
        # Get list of mod files at the specific ref
        mod_files = subprocess.check_output(
            ["git", "ls-tree", "-r", "--name-only", ref, "mods/"], 
            stderr=subprocess.DEVNULL
        ).decode().strip().split("\n")
        
        for file_path in mod_files:
            if not file_path.endswith(".toml"):
                continue
                
            # Get file content at ref
            file_content = subprocess.check_output(
                ["git", "show", f"{ref}:{file_path}"]
            ).decode()
            
            # Write to temp file for toml parsing
            temp_file = f"/tmp/{os.path.basename(file_path)}"
            with open(temp_file, "w") as f:
                f.write(file_content)
                
            mod_info = parse_mod_file(temp_file)
            if mod_info:
                result[os.path.basename(file_path)] = mod_info
                
            # Clean up temp file
            os.remove(temp_file)
    except subprocess.CalledProcessError:
        print(f"Error getting mod files at ref {ref}")
    
    return result

# Get current mods
def get_current_mods():
    result = {}
    for file_name in os.listdir("mods"):
        if file_name.endswith(".toml"):
            mod_info = parse_mod_file(os.path.join("mods", file_name))
            if mod_info:
                result[file_name] = mod_info
    return result

# Find SusyCore mod versions
def find_susy_core_versions(old_mods, new_mods):
    # Look for SusyCore in both old and new mods
    old_version = None
    new_version = None
    
    for mod_file, mod_info in old_mods.items():
        if "SusyCore" in mod_info["name"]:
            old_version = mod_info["version"]
            break
    
    for mod_file, mod_info in new_mods.items():
        if "SusyCore" in mod_info["name"]:
            new_version = mod_info["version"]
            break
    
    return old_version, new_version

# Generate mod changes section of the changelog
def generate_mod_changelog():
    tag = get_latest_tag()
    if not tag:
        print("No tags found excluding 'latest'")
        return "No previous tag found to compare against.", None, None
    
    print(f"Found latest tag: {tag}")
    
    # Get mods at the tag and current state
    tag_mods = get_mods_at_ref(tag)
    current_mods = get_current_mods()
    
    # Get SusyCore versions for additional changelog
    susy_old_version, susy_new_version = find_susy_core_versions(tag_mods, current_mods)
    
    # Compare mods
    changes = {
        "updates": [],
        "additions": [],
        "removals": []
    }
    
    # Find updates and additions
    for file_name, current_mod in current_mods.items():
        if file_name in tag_mods:
            # Mod exists in both - check if version changed
            if current_mod["version"] != tag_mods[file_name]["version"]:
                changes["updates"].append({
                    "name": current_mod["name"],
                    "old_version": tag_mods[file_name]["version"],
                    "new_version": current_mod["version"]
                })
        else:
            # Mod exists only in current - it's an addition
            changes["additions"].append({
                "name": current_mod["name"],
                "version": current_mod["version"]
            })
    
    # Find removals
    for file_name, tag_mod in tag_mods.items():
        if file_name not in current_mods:
            changes["removals"].append({
                "name": tag_mod["name"]
            })
    
    # Generate markdown
    markdown = "## Mod Changes\n\n"
    
    if changes["updates"]:
        markdown += "### Mod Updates\n"
        for update in changes["updates"]:
            markdown += f"* {update['name']} {update['old_version']} -> {update['new_version']}\n"
        markdown += "\n"
    
    if changes["additions"]:
        markdown += "### Mod Additions\n"
        for addition in changes["additions"]:
            markdown += f"* {addition['name']} -> {addition['version']}\n"
        markdown += "\n"
    
    if changes["removals"]:
        markdown += "### Mod Removals\n"
        for removal in changes["removals"]:
            markdown += f"* {removal['name']}\n"
        markdown += "\n"
    
    return markdown, susy_old_version, susy_new_version

# Get the date of a git reference
def get_reference_date(ref):
    date_str = subprocess.check_output(
        ["git", "log", "-1", "--format=%ai", ref]
    ).decode().strip()
    return date_str

# Get merged PRs since the last tag using GitHub API
def get_merged_prs_since_tag(tag, repository=None):
    if not GITHUB_TOKEN:
        print("GitHub token not configured.")
        return []
    
    repo = repository or GITHUB_REPO
    
    tag_date = get_reference_date(tag)
    print(f"Looking for PRs in {repo} merged after {tag_date}")
    
    headers = {
        "Authorization": f"token {GITHUB_TOKEN}",
        "Accept": "application/vnd.github.v3+json"
    }
    
    # Query for merged PRs since the tag date
    url = f"https://api.github.com/repos/{repo}/pulls"
    params = {
        "state": "closed",
        "sort": "updated",
        "direction": "desc",
        "per_page": 100
    }
    
    all_prs = []
    page = 1
    more_pages = True
    
    while more_pages:
        params["page"] = page
        response = requests.get(url, headers=headers, params=params)
        
        if response.status_code != 200:
            print(f"Error getting PRs: {response.status_code}")
            print(response.text)
            return []
        
        prs = response.json()
        if not prs:
            break
            
        for pr in prs:
            # Only include PRs that were merged (not just closed)
            if pr.get("merged_at") and pr.get("merge_commit_sha"):
                # Check if PR was merged after the tag date
                merged_at = pr.get("merged_at")
                if merged_at > tag_date:
                    # Fetch more details about the PR
                    pr_url = pr.get("url")
                    pr_response = requests.get(pr_url, headers=headers)
                    if pr_response.status_code == 200:
                        pr_data = pr_response.json()
                        # Add repo name to PR data
                        pr_data["repository"] = repo
                        all_prs.append(pr_data)
                else:
                    # We've gone past the tag date, no need to check more PRs
                    more_pages = False
                    break
        
        page += 1
        
        # Stop if we've gone through all pages or found older PRs
        if not more_pages or len(prs) < 100:
            break
    
    return all_prs

# Get PRs merged between two SusyCore tags
def get_susy_core_prs(old_version, new_version):
    if not old_version or not new_version:
        print("SusyCore versions not found")
        return []
    
    susy_repo = f"{GITHUB_ORG}/Susy-Core"
    
    print(f"Looking for PRs in {susy_repo} between versions {old_version} and {new_version}")
    
    headers = {
        "Authorization": f"token {GITHUB_TOKEN}",
        "Accept": "application/vnd.github.v3+json"
    }
    
    # First check if the tags exist
    tags_url = f"https://api.github.com/repos/{susy_repo}/tags"
    tags_response = requests.get(tags_url, headers=headers)
    
    if tags_response.status_code != 200:
        print(f"Error fetching tags: {tags_response.status_code}")
        return []
    
    tags = tags_response.json()
    tag_names = [tag["name"] for tag in tags]
    
    # Search for tags that match the versions
    old_tag = None
    new_tag = None
    
    for tag in tag_names:
        if old_version in tag:
            old_tag = tag
        if new_version in tag:
            new_tag = tag
    
    if not old_tag or not new_tag:
        print(f"SusyCore tags not found for versions {old_version} and {new_version}")
        # Fallback to using PRs between dates
        return get_merged_prs_since_tag(get_latest_tag(), susy_repo)
    
    # Get PRs between these tags
    commits_url = f"https://api.github.com/repos/{susy_repo}/compare/{old_tag}...{new_tag}"
    commits_response = requests.get(commits_url, headers=headers)
    
    if commits_response.status_code != 200:
        print(f"Error fetching commits: {commits_response.status_code}")
        return []
    
    # Look through commits to find associated PRs
    commits = commits_response.json().get("commits", [])
    
    prs = []
    for commit in commits:
        commit_sha = commit["sha"]
        # Check if this commit is associated with a PR
        pr_url = f"https://api.github.com/repos/{susy_repo}/commits/{commit_sha}/pulls"
        pr_response = requests.get(pr_url, headers=headers)
        
        if pr_response.status_code == 200:
            commit_prs = pr_response.json()
            for pr in commit_prs:
                # Fetch full PR details
                full_pr_url = pr["url"]
                full_pr_response = requests.get(full_pr_url, headers=headers)
                
                if full_pr_response.status_code == 200:
                    pr_data = full_pr_response.json()
                    # Add repo name to PR data
                    pr_data["repository"] = susy_repo
                    prs.append(pr_data)
    
    # Remove duplicates (a PR might have multiple commits)
    unique_prs = []
    pr_ids = set()
    
    for pr in prs:
        if pr["id"] not in pr_ids:
            pr_ids.add(pr["id"])
            unique_prs.append(pr)
    
    return unique_prs

# Categorize a PR based on its labels or title
def categorize_pr(pr):
    # Check for the "internal" label to skip this PR
    labels = [label["name"].lower() for label in pr.get("labels", [])]
    if "internal" in labels:
        return "internal"
    
    # First, check for specific labels
    if any(label in ["feature", "enhancement"] for label in labels):
        return "new_features"
    elif any(label in ["change", "update", "changed"] for label in labels):
        return "changed_features"
    elif any(label in ["fix", "bug", "bugfix"] for label in labels):
        return "fixes"
    
    # If no matching labels, try to categorize based on title
    title = pr.get("title", "").lower()
    if any(keyword in title for keyword in ["add", "new", "feature", "implement"]):
        return "new_features"
    elif any(keyword in title for keyword in ["change", "update", "improve", "refactor"]):
        return "changed_features"
    elif any(keyword in title for keyword in ["fix", "bug", "issue", "resolve", "solve"]):
        return "fixes"
    
    # If we can't categorize, put in uncategorized
    return "uncategorized"

# Generate the PR section of the changelog
def generate_pr_changelog(prs):
    categorized_prs = {
        "new_features": [],
        "changed_features": [],
        "fixes": [],
        "uncategorized": [],
        "internal": []
    }
    
    # Categorize PRs
    for pr in prs:
        category = categorize_pr(pr)
        if category:
            categorized_prs[category].append(pr)
    
    markdown = "## Changes\n\n"

    # Generate categorized sections
    sections = [
        ("### New Features", categorized_prs["new_features"]),
        ("### Changed Features", categorized_prs["changed_features"]),
        ("### Fixes", categorized_prs["fixes"]),
        ("### TODO: Uncategorized Changes (Please Categorize)", categorized_prs["uncategorized"])
    ]
    
    for section_title, section_prs in sections:
        if section_prs:
            markdown += f"{section_title}\n"
            for pr in section_prs:
                title = pr.get("title", "No title")
                number = pr.get("number", "")
                author = pr.get("user", {}).get("login", "Unknown")
                repo = pr.get("repository", "").split("/")[-1]  # Extract repo name from full path
                
                # Include repository name if it's not the main repo
                if repo != GITHUB_REPO.split("/")[-1]:
                    markdown += f"- [{repo}] {title} (#{number} by @{author})\n"
                else:
                    markdown += f"- {title} (#{number} by @{author})\n"
            markdown += "\n"
    
    # Skip internal PRs in the changelog
    
    return markdown

# Generate the complete changelog
def generate_full_changelog():
    tag = get_latest_tag()
    if not tag:
        return "No previous tag found to compare against."
    
    # Get current version
    current_version = VERSION
    
    # Generate mod changes section and get SusyCore versions
    mod_changes, susy_old_version, susy_new_version = generate_mod_changelog()
    
    # Get merged PRs since the last tag from main repo
    main_prs = get_merged_prs_since_tag(tag)
    
    # Get merged PRs from SusyCore between relevant versions
    susy_prs = []
    if susy_old_version and susy_new_version:
        susy_prs = get_susy_core_prs(susy_old_version, susy_new_version)
    
    # Combine PRs from both repositories
    all_prs = main_prs + susy_prs
    
    # Generate PR changelog section
    pr_changes = generate_pr_changelog(all_prs)
    
    # Combine all sections
    header = f"# UPDATE {current_version}\n\n"
    full_changelog = header + mod_changes + pr_changes
    
    return full_changelog

# Main execution
if __name__ == "__main__":
    changelog = generate_full_changelog()
    print(changelog)
    
    # Output the changelog for GitHub Actions if we're in a GitHub Actions environment
    if "GITHUB_OUTPUT" in os.environ:
        with open(os.environ["GITHUB_OUTPUT"], "a") as f:
            delimiter = "EOF" + os.urandom(8).hex()
            f.write(f"changelog<<{delimiter}\n{changelog}\n{delimiter}\n")
    
    # Also write to a file for easier access
    with open("modpack-changelog.md", "w") as f:
        f.write(changelog)
