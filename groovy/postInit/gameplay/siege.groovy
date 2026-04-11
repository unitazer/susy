import supersymmetry.common.tileentities.TileEntityFlare

import techguns.entities.npcs.Bandit
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos

System.out.println("grs siege loaded")

TileEntityFlare.metaClass.callGroovySpawn = { String type, player ->

    switch (type) {
        case "bandit_flare":
            spawnBanditRaid(player)
            break
    }
}

def spawnBanditRaid(player) {

    def world = player.world

    for (int attempt = 0; attempt < 32; attempt++) {
        Bandit bandit = new Bandit(world)
        // ===== Custom NBT =====
        NBTTagCompound root = bandit.getEntityData().getCompoundTag("susy")
        root.setString("faction", "Bandits")
        root.setInteger("hate", 0)
        bandit.getEntityData().setTag("susy", root)
        bandit.addRandomArmor(1);

        // ===== Random weapon =====
        String[] weapons = [
                "techguns:combatknife",
                "techguns:revolver",
                "techguns:boltaction"
        ]

        String chosen = weapons[(int)(Math.random() * weapons.length)]

        NBTTagCompound nbt = new NBTTagCompound()
        def hands = new net.minecraft.nbt.NBTTagList()

        def main = new NBTTagCompound()
        main.setString("id", chosen)
        main.setByte("Count", (byte)1)

        hands.appendTag(main)
        hands.appendTag(new NBTTagCompound())

        nbt.setTag("HandItems", hands)
        bandit.readEntityFromNBT(nbt)

        //shamelessly stolen from bruberu
        double angle = Math.random() * 2 * Math.PI
        int radius = 16

        double x = (int)(player.posX + radius * Math.cos(angle)) + 0.5
        double z = (int)(player.posZ + radius * Math.sin(angle)) + 0.5

        BlockPos topPos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z))
        double y = topPos.getY() + 0.01

        bandit.setPosition(x, y, z)

        if (!bandit.isNotColliding()) continue

        world.spawnEntity(bandit)

        return
    }

    System.out.println("Bandit spawn failed after many attempts")
}
