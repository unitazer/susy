package globals
import globals.Globals

import gregtech.api.metatileentity.multiblock.CleanroomType

/*
Etchants used:
- Aluminum: Phosphoric Acid, chlorine plasma
- Indium Tin Oxide: Nitration mixture, fluorine plasma
- Chromium: Hydrochloric acid, fluorine plasma
- GaAs: Hydrogen Peroxide,  chlorine plasma
- Gold & Platinum: Aqua Regia, fluorine plasma
- Photoresist: Sulfuric Acid, oxygen plasma
- Silicon: Nitric Acid or Hydrofluoric Acid,  chlorine plasma
- Silica: Hydrofluoric acid, CF4 plasma
- Silicon nitride: phosphoric acid, CF4 plasma
- Titanium: hydrofluoric acid, BCl3 plasma
- Tungsten: Hydrogen peroxide, CF4 plasma
- Copper: Iron III chloride or Sodium bisulfite, fluorine plasma
 */

class Etchants {

    static class Etchant {
        String fluidName
        String materialEtched
        int amountUsed
        int timeUsed

        Etchant(String fluidName, String materialEtched, int amountUsed, int timeUsed) {
            this.fluidName = fluidName
            this.materialEtched = materialEtched
            this.amountUsed = amountUsed
            this.timeUsed = timeUsed
        }
    }

    public static final etchants = [
        new Etchant("plasma.chlorine", "aluminium", 10, 80),
        new Etchant("plasma.carbon_tetrafluoride", "aluminium", 10, 60),
        new Etchant("plasma.boron_trichloride", "aluminium", 10, 60),
        new Etchant("plasma.chlorine", "gallium_arsenide", 10, 80),
        new Etchant("plasma.carbon_tetrafluoride", "gallium_arsenide", 10, 60),
        new Etchant("plasma.boron_trichloride", "gallium_arsenide", 10, 60),
        new Etchant("plasma.oxygen", "photoresist", 10, 60),
        new Etchant("plasma.chlorine", "silicon", 10, 80),
        new Etchant("plasma.carbon_tetrafluoride", "silicon", 10, 60),
        new Etchant("plasma.carbon_tetrafluoride", "silicon_dioxide", 10, 60),
        new Etchant("plasma.nitrogen_trifluoride", "silicon_dioxide", 10, 60),
        new Etchant("plasma.carbon_tetrafluoride", "silicon_nitride", 10, 60),
        new Etchant("plasma.nitrogen_trifluoride", "silicon_nitride", 10, 60),
        new Etchant("plasma.boron_trichloride", "titanium", 10, 60),
        new Etchant("plasma.boron_trichloride", "nickel", 10, 60),
        new Etchant("plasma.carbon_tetrafluoride", "tungsten", 10, 60),

        new Etchant("iron_iii_chloride_solution", "copper", 100, 100),
        new Etchant("sodium_bisulfate_solution", "copper", 100, 100),

        new Etchant("ethylenediamine_pyrocatechol", "silicon", 40, 80),
        new Etchant("tetramethylammonium_hydroxide_solution", "silicon", 40, 80),
        new Etchant("ethylenediamine_pyrocatechol", "silicon_advanced", 40, 80),
        new Etchant("tetramethylammonium_hydroxide_solution", "silicon_advanced", 40, 80),
        new Etchant("phosphoric_acid", "aluminium", 50, 700),
        new Etchant("nitration_mixture", "indium_tin_oxide", 50, 700),
        new Etchant("hydrochloric_acid", "chrome", 50, 700),
        new Etchant("hydrogen_peroxide", "gallium_arsenide", 50, 700),
        new Etchant("aqua_regia", "gold", 50, 700),
        new Etchant("aqua_regia", "platinum", 50, 700),
        new Etchant("hydrogen_peroxide", "photoresist", 50, 500),
        new Etchant("hydrofluoric_acid", "silicon_dioxide", 40, 600),
        new Etchant("phosphoric_acid", "silicon_nitride", 40, 600),
        new Etchant("hydrofluoric_acid", "titanium", 50, 700),
        new Etchant("hydrofluoric_acid", "nickel", 50, 700),
        new Etchant("hydrogen_peroxide", "tungsten", 50, 700),
        new Etchant("nitric_acid", "titanium_nitride", 50, 700),
        new Etchant("hydrofluoric_acid", "titanium_nitride", 50, 700),
        new Etchant("nitric_acid", "silicon", 80, 700),
        new Etchant("hydrofluoric_acid", "silicon", 40, 500)
    ]


    static void generateEtchingRecipes(String input, String product, String materialEtched, int voltageTier, int timeMultiplier, boolean cleanroom) {
        float ok = 0.5f;

        for (etchant in etchants) {
            if (etchant.materialEtched == materialEtched) {
                if (cleanroom) {
                    recipemap('chemical_bath').recipeBuilder()
                            .inputs(metaitem(input))
                            .fluidInputs(fluid(etchant.fluidName) * etchant.amountUsed)
                            .outputs(metaitem(product))
                            .duration(etchant.timeUsed * timeMultiplier)
                            .cleanroom(CleanroomType.CLEANROOM)
                            .EUt(Globals.voltAmps[voltageTier])
                            .buildAndRegister()
                } else {
                    recipemap('chemical_bath').recipeBuilder()
                            .inputs(metaitem(input))
                            .fluidInputs(fluid(etchant.fluidName) * etchant.amountUsed)
                            .outputs(metaitem(product))
                            .duration(etchant.timeUsed * timeMultiplier)
                            .EUt(Globals.voltAmps[voltageTier])
                            .buildAndRegister()
                }
            }
        }
    }

}
