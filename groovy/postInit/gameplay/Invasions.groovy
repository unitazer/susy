import supersymmetry.api.event.MobHordeEvent;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import techguns.entities.npcs.Bandit;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound

//zombie waves
//can happen at any time, for any reason
//great at keeping the tension up while not being too world-ending

new MobHordeEvent((player) -> {EntityZombie zombie = new EntityZombie(player.world);
    zombie.addPotionEffect(new PotionEffect(MobEffects.SPEED, 999999, 0));
    return zombie;}, 2, 4, "zombie_small")
        .setNightOnly(true)
        .setTimer(144000, 216000)		// 2 - 3 hours
        .setCanUsePods(false)

new MobHordeEvent((player) -> {EntityZombie zombie = new EntityZombie(player.world);
    zombie.addPotionEffect(new PotionEffect(MobEffects.SPEED, 999999, 1));
    zombie.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 999999, 0));
    return zombie;}, 5, 10, "zombie_medium")
        .setNightOnly(true)
        .setTimer(144000, 216000)		// 2 - 3 hours
        .setCanUsePods(false)

new MobHordeEvent((player) -> {EntityZombie zombie = new EntityZombie(player.world);
    zombie.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 999999, 1));
    zombie.addPotionEffect(new PotionEffect(MobEffects.SPEED, 999999, 1));
    return zombie;}, 8, 18, "zombie_large")
        .setNightOnly(true)
        .setTimer(144000, 216000)		// 2 - 3 hours
        .setCanUsePods(false)

new MobHordeEvent((player) -> {EntityZombie zombie = new EntityZombie(player.world);
    zombie.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 999999, 1));
    zombie.addPotionEffect(new PotionEffect(MobEffects.SPEED, 999999, 1));
    zombie.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 999999, 2));
    return zombie;}, 20, 30, "zombie_massive")
        .setNightOnly(true)
        .setTimer(144000, 216000)		// 2 - 3 hours
        .setCanUsePods(false)


//bandits
//todo
//add HATE mechanic - DONE
//make several raids scaling with said mechanic
//add one scripted event when you get first assembler

//HATE
// invading raiders have negative hate, that hate gets subtracted when they get killed
// when the raid ends and there are raiders alive, the hate gets inverted and added instead
// this means that the player will slowly build up hate the longer they ignore invasions for
// only applies to human factions, zombies are :dimbass: and don't understand hate
// if hate is not meant to change, just set it to 0 (important for sieges)

// Why this system? "Weakness invites aggression" - Ronald Reagan
// if the bandits think they can get away with raiding yo shit they will keep doing it :troll:

//HATE advancement Reg, for progression

MobHordeEvent.baseline(new ResourceLocation("gregtech:steam/12_electronic_circuit"), 1)
MobHordeEvent.baseline(new ResourceLocation("gregtech:low_voltage/root_lv"), 3)
MobHordeEvent.baseline(new ResourceLocation("gregtech:low_voltage/23_lv_assembler"), 5)
MobHordeEvent.baseline(new ResourceLocation("gregtech:low_voltage/27_electric_blast_furnace"), 10)
MobHordeEvent.baseline(new ResourceLocation("gregtech:medium_voltage/root_mv"), 15)
MobHordeEvent.baseline(new ResourceLocation("gregtech:medium_voltage/42_kanthal_coil"), 20)
MobHordeEvent.baseline(new ResourceLocation("gregtech:high_voltage/root_hv"), 23)
MobHordeEvent.baseline(new ResourceLocation("gregtech:medium_voltage/37_advanced_integrated_logic_circuit"), 25)
MobHordeEvent.baseline(new ResourceLocation("gregtech:high_voltage/44_distillation_tower"), 35)
MobHordeEvent.baseline(new ResourceLocation("gregtech:extreme_voltage/root_ev"), 40)
MobHordeEvent.baseline(new ResourceLocation("gregtech:high_voltage/40_workstation"), 50)
MobHordeEvent.baseline(new ResourceLocation("gregtech:insane_voltage/root_iv"), 65)

//Human invasions

//Scripted
//Ivan, the tutorial raider


//Random
/**
 scouts
 1-2 people
 combat knives
 bandit helmets only
 very weak, early game enemies
**/
new MobHordeEvent((player) -> {
    Bandit bandit = new Bandit(player.world);
    //hate
    NBTTagCompound root = bandit.getEntityData().getCompoundTag("susy");
    root.setString("faction", "Bandits");
    root.setInteger("hate", -1);
    bandit.getEntityData().setTag("susy", root);

    return bandit;
}, 1, 2, "bandit_scouts")
        .setPostSpawnModifier({ entity ->
            NBTTagCompound nbt = new NBTTagCompound()
            def armor = new net.minecraft.nbt.NBTTagList()
            ["boots","leggings","chestplate","helmet"].eachWithIndex { name, idx ->
                def tag = new NBTTagCompound()
                if (idx == 3) {
                    tag.setString("id", "techguns:t1_scout_helmet")
                    tag.setByte("Count", (byte)1)
                }
                armor.appendTag(tag)
            }
            def hands = new net.minecraft.nbt.NBTTagList()
            def main = new NBTTagCompound()
            main.setString("id", "techguns:combatknife")
            main.setByte("Count", (byte)1)
            hands.appendTag(main)
            hands.appendTag(new NBTTagCompound())
            nbt.setTag("ArmorItems", armor)
            nbt.setTag("HandItems", hands)
            entity.readEntityFromNBT(nbt)
            return entity
        })
        .setTimer(144000, 216000)
        .minHate("Bandits", 5)

/**
 small raiding band
 3-6 people
 weapons: combat knives, revolvers, bolt action rifles
 random bandit armor
 small raiding party
 congratulations player, you have made enough noise to be noticed!
 **/

new MobHordeEvent((player) -> {
    Bandit bandit = new Bandit(player.world);
    //hate
    NBTTagCompound root = bandit.getEntityData().getCompoundTag("susy");
    root.setString("faction", "Bandits");
    root.setInteger("hate", -1);
    bandit.getEntityData().setTag("susy", root);

    return bandit;
}, 3, 6, "bandit_small_raid")
        .setTimer(144000, 216000)
        .minHate("Bandits", 15)
        .setPostSpawnModifier(entity -> {
            if (entity instanceof Bandit) {
                NBTTagCompound nbt = new NBTTagCompound();

                // List of possible weapons
                String[] possibleWeapons = new String[]{
                        "techguns:combatknife",
                        "techguns:revolver",
                        "techguns:boltaction"
                };

                // Pick a random weapon
                String chosenWeapon = possibleWeapons[(int) (Math.random() * possibleWeapons.length)];
                net.minecraft.nbt.NBTTagList hands = new net.minecraft.nbt.NBTTagList();
                net.minecraft.nbt.NBTTagCompound main = new net.minecraft.nbt.NBTTagCompound();
                main.setString("id", chosenWeapon);
                main.setByte("Count", (byte)1);
                hands.appendTag(main);
                hands.appendTag(new net.minecraft.nbt.NBTTagCompound()); // offhand empty

                nbt.setTag("HandItems", hands);

                entity.readEntityFromNBT(nbt);
            }
            return entity;
        });










/*
// Commands for pods
String command1 = "#gen Test";
String command2 = "#gen Test1";
String command0 = "say init"
String command3 = "say 1";
String command4 = "say 2"
String command5 = "say 3"

new MobHordeEvent((player) -> null, 20, 20, "debug_invasion")
        .runCommandOnLanding(command0)
        .addPattern(
                t -> {
                    double radius = 20;
                    double angle = t * 2 * Math.PI;
                    return new MobHordeEvent.Vec2(radius * Math.cos(angle), radius * Math.sin(angle));
                },
                null,                        // pattern-specific commands
                null,                        // supplierOverride (null = default)
                null                         // postSpawnModifier (null = default)
        )

        .addPattern(
                t -> {
                    double radius = 30;
                    double angle = t * 2 * Math.PI;
                    return new MobHordeEvent.Vec2(radius * Math.cos(angle), radius * Math.sin(angle));
                },
                Arrays.asList(command3, command4, command5),
                player -> {
                    Bandit bandit = new Bandit(player.world);
                    bandit.addRandomArmor(1);
                    return bandit;
                },
                entity -> {
                    // optional post-spawn modifier per entity
                    return entity;
                }
        )
        .allignBlock()
        .setDistribution(50.0,50.0);

 */
/*
// Example 1: Square
new MobHordeEvent({ player -> null }, 10, 10, "square_invasion")
        .addPattern({ t ->
            double n = 2
            double angle = t * 2 * Math.PI
            double radius = 20;
            new MobHordeEvent.Vec2(
                    (Math.cos(angle) < 0 ? -1 : 1) * Math.pow(Math.abs(Math.cos(angle)), 2/n) * radius,
                    (Math.sin(angle) < 0 ? -1 : 1) * Math.pow(Math.abs(Math.sin(angle)), 2/n) * radius
            )
        })

// Example 2: Trapezoid
new MobHordeEvent({ player -> null }, 12, 12, "trapezoid_invasion")
        .addPattern({ t ->
            double n = 2
            double angle = t * 2 * Math.PI
            double xScale = 25   // wider base
            double zScale = 15   // narrower top
            new MobHordeEvent.Vec2(
                    (Math.cos(angle) < 0 ? -1 : 1) * Math.pow(Math.abs(Math.cos(angle)), 2/n) * xScale,
                    (Math.sin(angle) < 0 ? -1 : 1) * Math.pow(Math.abs(Math.sin(angle)), 2/n) * zScale
            )
        })
        .allignBlock()

// Example 3: 5-point star
new MobHordeEvent({ player -> null }, 15, 15, "star_invasion")
        .addPattern({ t ->
            double points = 5
            double angle = t * 2 * Math.PI
            double radiusOuter = 20
            double radiusInner = 10
            double r = (Math.sin(angle * points) >= 0) ? radiusOuter : radiusInner
            new MobHordeEvent.Vec2(
                    Math.cos(angle) * r,
                    Math.sin(angle) * r
            )
        })
        .allignBlock()
 */
