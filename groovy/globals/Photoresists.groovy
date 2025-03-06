package globals
import globals.Globals

import gregtech.api.metatileentity.multiblock.CleanroomType

class Photoresists {

    public static class Photoresist {
        String fluidName
        int amountUsed
        int timeUsed

        Photoresist(String fluidName, int amountUsed, float timeUsed) {
            this.fluidName = fluidName
            this.amountUsed = amountUsed
            this.timeUsed = timeUsed
        }
    }

    public static final photoresists = [
        new Photoresist("novolacs", 50, 300),
        new Photoresist("hydrogen_silsesquioxane_photoresist", 25, 200),
        new Photoresist("pmma", 16, 150),
        new Photoresist("su_eight", 16, 50)
    ]

    static void generatePatterningRecipes(String input, String product, String mask, 
                                         int voltageTier, int timeMultiplier, int outputMultiplier, 
                                         int circ, boolean cleanroom) {
        for (photoresist in photoresists) {
            if (cleanroom) {
                recipemap("uv_light_box").recipeBuilder()
                        .inputs(metaitem(input))
                        .notConsumable(metaitem(mask))
                        .fluidInputs(fluid(photoresist.fluidName) * photoresist.amountUsed)
                        .outputs(metaitem(product) * outputMultiplier)
                        .duration(photoresist.timeUsed * timeMultiplier)
                        .cleanroom(CleanroomType.CLEANROOM)
                        .EUt(Globals.voltAmps[voltageTier])
                        .buildAndRegister()

                recipemap('laser_engraver').recipeBuilder()
                        .inputs(metaitem(input))
                        .circuitMeta(circ)
                        .fluidInputs(fluid(photoresist.fluidName) * (photoresist.amountUsed / 4))
                        .outputs(metaitem(product) * outputMultiplier)
                        .duration((int) (photoresist.timeUsed * timeMultiplier / 10))
                        .cleanroom(CleanroomType.CLEANROOM)
                        .EUt(Globals.voltAmps[voltageTier])
                        .buildAndRegister()
            } else {
                recipemap("uv_light_box").recipeBuilder()
                        .inputs(metaitem(input))
                        .notConsumable(metaitem(mask))
                        .fluidInputs(fluid(photoresist.fluidName) * photoresist.amountUsed)
                        .outputs(metaitem(product) * outputMultiplier)
                        .duration(photoresist.timeUsed * timeMultiplier)
                        .EUt(Globals.voltAmps[voltageTier])
                        .buildAndRegister()

                recipemap('laser_engraver').recipeBuilder()
                        .inputs(metaitem(input))
                        .circuitMeta(circ)
                        .fluidInputs(fluid(photoresist.fluidName) * (photoresist.amountUsed / 4))
                        .outputs(metaitem(product) * outputMultiplier)
                        .duration((int) (photoresist.timeUsed * timeMultiplier / 10))
                        .EUt(Globals.voltAmps[voltageTier])
                        .buildAndRegister()
            }
        }
    }
}
