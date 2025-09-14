import globals.Globals

import gregtech.api.recipes.recipeproperties.RecipeProperty
import gregtech.api.recipes.RecipeBuilder
import static gregtech.api.recipes.RecipeMaps.*
import gregtech.api.unification.material.Materials.*

import gregtech.api.recipes.ModHandler
import gregtech.api.unification.material.Materials
import gregtech.api.unification.ore.OrePrefix
import gregtech.api.unification.stack.UnificationEntry

import postInit.utils.RecyclingHelper

CSTR = recipemap('continuous_stirred_tank_reactor')
ROASTER = recipemap('roaster')
BR = recipemap('batch_reactor')
BCR = recipemap('bubble_column_reactor')
ELECTROLYTIC_CELL = recipemap('electrolytic_cell')

/*
 * Components
 */

// Pb anode & PbO2 cathode line

crafting.addShapeless("anode_lead", metaitem('anode.lead'), [
        metaitem('plateLead'), metaitem('cableGtSingleTin')
]);

MIXER.recipeBuilder()
        .inputs(ore('dustLead'))
        .fluidInputs(fluid('diluted_sulfuric_acid') * 1000)
        .outputs(metaitem('cathode.lead_paste'))
        .fluidOutputs(fluid('water') * 1000)
        .duration(100)
        .EUt(16)
        .buildAndRegister()

crafting.addShapeless("cathode_lead", metaitem('cathode.lead'), [
        metaitem('anode.lead'), metaitem('cathode.lead_paste')
]);

ELECTROLYTIC_CELL.recipeBuilder()
        .inputs(ore('plateLead'))
        .fluidInputs(fluid('water') * 1000)
        .notConsumable(metaitem('plateCopper'))
        .notConsumable(fluid('sulfuric_acid') * 1000)
        .outputs(metaitem('plateOxideCoatedLead'))
        .fluidOutputs(fluid('hydrogen') * 2000)
        .duration(80)
        .EUt(30)
        .buildAndRegister()

/*
 * Batteries
 */

// Lead-Acid Battery

crafting.addShaped("battery_lead_acid", metaitem('battery.lead_acid'), [
        [metaitem('plateBatteryAlloy'), metaitem('cableGtSingleTin'), metaitem('plateBatteryAlloy')],
        [metaitem('cathode.lead'), fluid('diluted_sulfuric_acid') * 1000, metaitem('anode.lead')],
        [metaitem('plateBatteryAlloy'), metaitem('plateBatteryAlloy'), metaitem('plateBatteryAlloy')]
]);

ASSEMBLER.recipeBuilder()
        .inputs(metaitem('battery.primitivehull.lv'))
        .inputs(ore('plateLead'))
        .inputs(ore('plateOxideCoatedLead'))
        .fluidInputs(fluid('diluted_sulfuric_acid') * 1000)
        .outputs(metaitem('battery.lead_acid'))
        .duration(200)
        .EUt(30)
        .buildAndRegister()

// Temporary allow to craft old sodium MV battery with primitive hull
mods.gregtech.assembler.recipeBuilder()
        .inputs(metaitem('battery.primitivehull.mv'))
        .inputs(ore('dustCarbon') * 2)
        .inputs(ore('dustSodiumCathodeAlloy') * 2)
        .fluidInputs(fluid('dimethyl_carbonate') * 200)
        .outputs(metaitem('battery.re.mv.sodium'))
        .duration(200)
        .EUt(16)
        .buildAndRegister()

/*
 * Hulls
*/

for (voltage in ['lv', 'mv', 'hv', 'ev', 'iv']) {
    crafting.remove('gregtech:battery_hull_' + voltage)
    RecyclingHelper.removeRecyclingRecipes(metaitem('battery.hull.' + voltage))
}

// LV Battery Hull
mods.gregtech.assembler.removeByInput(1, [metaitem('cableGtSingleTin'), metaitem('plateBatteryAlloy')], [fluid('plastic') * 144 * 144])
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.lv.cadmium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.lv.lithium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.lv.sodium')], null)

ASSEMBLER.recipeBuilder()
        .circuitMeta(5)
        .inputs(ore('plateStainlessSteel') * 2)
        .inputs(metaitem('cableGtSingleTin'))
        .fluidInputs(fluid('plastic') * 144)
        .outputs(metaitem('battery.hull.lv'))
        .duration(80)
        .EUt(120)
        .buildAndRegister()
RecyclingHelper.handleRecycling(metaitem('battery.hull.lv'),
        [ore('plateStainlessSteel') * 2, metaitem('cableGtSingleTin'), ore('dustPlastic')])

// MV Battery Hull
mods.gregtech.assembler.removeByInput(2, [metaitem('cableGtSingleCopper') * 2, metaitem('plateBatteryAlloy') * 3], [fluid('plastic') * 432 * 432])
mods.gregtech.assembler.removeByInput(2, [metaitem('cableGtSingleAnnealedCopper') * 2, metaitem('plateBatteryAlloy') * 3], [fluid('plastic') * 432 * 432])
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.mv.cadmium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.mv.lithium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.mv.sodium')], null)

ASSEMBLER.recipeBuilder()
        .circuitMeta(5)
        .inputs(ore('plateStainlessSteel') * 4)
        .inputs(metaitem('cableGtSingleCopper') * 2)
        .fluidInputs(fluid('plastic') * 432)
        .outputs(metaitem('battery.hull.mv'))
        .duration(160)
        .EUt(120)
        .buildAndRegister()
ASSEMBLER.recipeBuilder()
        .circuitMeta(5)
        .inputs(ore('plateStainlessSteel') * 4)
        .inputs(metaitem('cableGtSingleAnnealedCopper') * 2)
        .fluidInputs(fluid('plastic') * 432)
        .outputs(metaitem('battery.hull.mv'))
        .duration(160)
        .EUt(120)
        .buildAndRegister()
RecyclingHelper.handleRecycling(metaitem('battery.hull.mv'),
        [ore('plateStainlessSteel') * 4, metaitem('cableGtSingleCopper') * 2, ore('dustPlastic') * 3])

// HV Battery Hull
mods.gregtech.assembler.removeByInput(4, [metaitem('cableGtSingleGold') * 4, metaitem('plateBatteryAlloy') * 9], [fluid('plastic') * 1296 * 1296])
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.hv.cadmium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.hv.lithium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.hv.sodium')], null)

ASSEMBLER.recipeBuilder()
        .circuitMeta(5)
        .inputs(ore('plateStainlessSteel') * 8)
        .inputs(metaitem('cableGtSingleGold') * 4)
        .fluidInputs(fluid('plastic') * 1296)
        .outputs(metaitem('battery.hull.hv'))
        .duration(240)
        .EUt(120)
        .buildAndRegister()
RecyclingHelper.handleRecycling(metaitem('battery.hull.hv'),
        [ore('plateStainlessSteel') * 8, metaitem('cableGtSingleGold') * 4, ore('dustPlastic') * 9])

// EV Battery Hull
mods.gregtech.assembler.removeByInput(480, [metaitem('cableGtSingleAluminium') * 2, metaitem('plateBlueSteel') * 2], [fluid('polytetrafluoroethylene') * 144 * 144])
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.ev.vanadium')], null)

ASSEMBLER.recipeBuilder()
        .circuitMeta(5)
        .inputs(ore('plateStainlessSteel') * 16)
        .inputs(metaitem('cableGtSingleAluminium') * 2)
        .fluidInputs(fluid('polytetrafluoroethylene') * 144)
        .outputs(metaitem('battery.hull.ev'))
        .duration(240)
        .EUt(480)
        .buildAndRegister()
RecyclingHelper.handleRecycling(metaitem('battery.hull.ev'),
        [ore('plateStainlessSteel') * 16, metaitem('cableGtSingleAluminium') * 2, ore('dustPolytetrafluoroethylene')])

// IV Battery Hull
mods.gregtech.assembler.removeByInput(1920, [metaitem('cableGtSinglePlatinum') * 2, metaitem('plateRoseGold') * 6], [fluid('polytetrafluoroethylene') * 288 * 288])
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.iv.vanadium')], null)

ASSEMBLER.recipeBuilder()
        .circuitMeta(5)
        .inputs(ore('plateStainlessSteel') * 32)
        .inputs(metaitem('cableGtSinglePlatinum') * 2)
        .fluidInputs(fluid('polytetrafluoroethylene') * 432)
        .outputs(metaitem('battery.hull.iv'))
        .duration(240)
        .EUt(1920)
        .buildAndRegister()
RecyclingHelper.handleRecycling(metaitem('battery.hull.iv'),
        [ore('plateStainlessSteel') * 32, metaitem('cableGtSinglePlatinum') * 2, ore('dustPolytetrafluoroethylene') * 3])

// Primitive LV Battery Hull

crafting.addShaped('primitive_battery_hull_lv', metaitem('battery.primitivehull.lv'), [
    [null, metaitem('cableGtSingleTin'), null],
    [null, ore('plateBatteryAlloy'),     null],
    [null, ore('plateBatteryAlloy'),     null]
])
ASSEMBLER.recipeBuilder()
        .inputs(ore('plateBatteryAlloy'))
        .inputs(metaitem('cableGtSingleTin'))
        .fluidInputs(fluid('plastic') * 144)
        .outputs(metaitem('battery.primitivehull.lv'))
        .duration(80)
        .EUt(30)
        .buildAndRegister()
RecyclingHelper.handleRecycling(metaitem('battery.primitivehull.lv'),
        [ore('plateBatteryAlloy'), metaitem('cableGtSingleTin')])

// Primitive MV Battery Hull

crafting.addShaped('primitive_battery_hull_mv', metaitem('battery.primitivehull.mv'), [
    [metaitem('cableGtSingleCopper'), null,                     metaitem('cableGtSingleCopper')],
    [ore('plateBatteryAlloy'),        ore('plateBatteryAlloy'), ore('plateBatteryAlloy')],
    [ore('plateBatteryAlloy'),        ore('plateBatteryAlloy'), ore('plateBatteryAlloy')]
])
ASSEMBLER.recipeBuilder()
        .inputs(ore('plateBatteryAlloy') * 3)
        .inputs(metaitem('cableGtSingleCopper') * 2)
        .fluidInputs(fluid('plastic') * 432)
        .outputs(metaitem('battery.primitivehull.mv'))
        .duration(160)
        .EUt(30)
        .buildAndRegister()
RecyclingHelper.handleRecycling(metaitem('battery.primitivehull.mv'),
        [ore('plateBatteryAlloy') * 3, metaitem('cableGtSingleCopper') * 2])

/* Old recipes */

MIXER_RECIPES.recipeBuilder()
        .fluidInputs(Materials.SulfurTrioxide.getFluid(1000))
        .fluidInputs(Materials.Water.getFluid(1000))
        .fluidOutputs(Materials.SulfuricAcid.getFluid(1000))
        .EUt(7)
        .duration(40)
        .buildAndRegister()

mods.gregtech.electric_blast_furnace.recipeBuilder()
        .inputs(ore('dustSodium') * 4)
        .inputs(ore('dustIron') * 3)
        .inputs(ore('dustManganese') * 3)
        .fluidInputs(fluid('oxygen') * 12000)
        .outputs(metaitem('dustSodiumCathodeAlloy') * 10)
        .blastFurnaceTemp(1250)
        .duration(300)
        .EUt(30)
        .buildAndRegister()

ROASTER.recipeBuilder()
        .inputs(ore('dustLithiumCarbonate') * 6)
        .fluidInputs(fluid('hydrogen_fluoride') * 2000)
        .fluidOutputs(fluid('dense_steam') * 1000)
        .fluidOutputs(fluid('carbon_dioxide') * 1000)
        .outputs(metaitem('dustLithiumFluoride') * 4)
        .duration(140)
        .EUt(30)
        .buildAndRegister()

BR.recipeBuilder()
        .inputs(ore('dustLithiumFluoride') * 2)
        .inputs(ore('dustPhosphorusPentachloride') * 6)
        .fluidInputs(fluid('hydrogen_fluoride') * 5000)
        .fluidOutputs(fluid('hydrogen_chloride') * 5000)
        .outputs(metaitem('dustLithiumHexafluorophosphate') * 8)
        .duration(140)
        .EUt(30)
        .buildAndRegister()

MIXER_RECIPES.recipeBuilder()
        .inputs(ore('dustLithiumHexafluorophosphate') * 8)
        .fluidInputs(fluid('ethylene_carbonate') * 1000)
        .fluidOutputs(fluid('lithium_hexafluorophosphate_electrolyte') * 1000)
        .duration(400)
        .EUt(30)
        .buildAndRegister()

ROASTER.recipeBuilder()
        .inputs(ore('dustLithiumCarbonate') * 6)
        .inputs(ore('dustHighPurityCobaltOxide') * 4)
        .fluidOutputs(fluid('carbon_monoxide') * 1000)
        .outputs(metaitem('dustLithiumCobaltOxide') * 8)
        .duration(140)
        .EUt(30)
        .buildAndRegister()

BR.recipeBuilder()
        .inputs(ore('dustNickelHydroxide') * 10)
        .fluidInputs(fluid('potassium_hydroxide_solution') * 2000)
        .fluidInputs(fluid('chlorine') * 2000)
        .outputs(metaitem('dustNickelOxideHydroxide') * 8)
        .fluidOutputs(fluid('diluted_rock_salt_solution') * 4000)
        .duration(100)
        .EUt(30)
        .buildAndRegister()

mods.gregtech.assembler.recipeBuilder()
        .inputs(metaitem('battery.hull.hv'))
        .inputs(ore('dustCarbon') * 4)
        .inputs(ore('dustSodiumCathodeAlloy') * 4)
        .fluidInputs(fluid('dimethyl_carbonate') * 500)
        .outputs(metaitem('battery.re.hv.sodium'))
        .duration(400)
        .EUt(16)
        .buildAndRegister()

mods.gregtech.assembler.recipeBuilder()
        .inputs(metaitem('battery.hull.mv'))
        .inputs(ore('dustGraphite') * 2)
        .inputs(ore('dustLithiumCobaltOxide') * 2)
        .fluidInputs(fluid('lithium_hexafluorophosphate_electrolyte') * 200)
        .outputs(metaitem('battery.re.mv.lithium'))
        .duration(200)
        .EUt(16)
        .buildAndRegister()

mods.gregtech.assembler.recipeBuilder()
        .inputs(metaitem('battery.hull.hv'))
        .inputs(ore('dustGraphite') * 4)
        .inputs(ore('dustLithiumCobaltOxide') * 4)
        .fluidInputs(fluid('lithium_hexafluorophosphate_electrolyte') * 500)
        .outputs(metaitem('battery.re.hv.lithium'))
        .duration(400)
        .EUt(16)
        .buildAndRegister()

mods.gregtech.assembler.recipeBuilder()
        .inputs(metaitem('battery.hull.mv'))
        .inputs(ore('dustCadmium') * 2)
        .inputs(ore('dustNickelOxideHydroxide') * 2)
        .fluidInputs(fluid('potassium_hydroxide_solution') * 200)
        .outputs(metaitem('battery.re.mv.cadmium'))
        .duration(200)
        .EUt(16)
        .buildAndRegister()

mods.gregtech.assembler.recipeBuilder()
        .inputs(metaitem('battery.hull.hv'))
        .inputs(ore('dustCadmium') * 4)
        .inputs(ore('dustNickelOxideHydroxide') * 4)
        .fluidInputs(fluid('potassium_hydroxide_solution') * 500)
        .outputs(metaitem('battery.re.hv.cadmium'))
        .duration(400)
        .EUt(16)
        .buildAndRegister()

/*
 * Remove legacy batteries
 */

 // Sodium Battery
mods.gregtech.canner.removeByInput(2, [metaitem('battery.hull.lv'), metaitem('dustSodium') * 2], null)
mods.gregtech.canner.removeByInput(2, [metaitem('battery.hull.mv'), metaitem('dustSodium') * 8], null)
mods.gregtech.canner.removeByInput(2, [metaitem('battery.hull.hv'), metaitem('dustSodium') * 16], null)
// Lithium Battery
mods.gregtech.canner.removeByInput(2, [metaitem('battery.hull.lv'), metaitem('dustLithium') * 2], null)
mods.gregtech.canner.removeByInput(2, [metaitem('battery.hull.mv'), metaitem('dustLithium') * 8], null)
mods.gregtech.canner.removeByInput(2, [metaitem('battery.hull.hv'), metaitem('dustLithium') * 16], null)
// Cadmium Battery
mods.gregtech.canner.removeByInput(2, [metaitem('battery.hull.lv'), metaitem('dustCadmium') * 2], null)
mods.gregtech.canner.removeByInput(2, [metaitem('battery.hull.mv'), metaitem('dustCadmium') * 8], null)
mods.gregtech.canner.removeByInput(2, [metaitem('battery.hull.hv'), metaitem('dustCadmium') * 16], null)
// Vanadium Battery
// Naquadria Battery
// Energy crystal
mods.gregtech.autoclave.removeByInput(480, [metaitem('energium_dust') * 9], [fluid('water') * 1000])
mods.gregtech.autoclave.removeByInput(320, [metaitem('energium_dust') * 9], [fluid('distilled_water') * 1000])
mods.gregtech.autoclave.removeByInput(256, [metaitem('energium_dust') * 9], [fluid('black_steel') * 288])
mods.gregtech.autoclave.removeByInput(192, [metaitem('energium_dust') * 9], [fluid('blue_steel') * 72])
mods.jei.ingredient.removeAndHide(metaitem('energy_crystal'));
// Lapotron
mods.gregtech.autoclave.removeByInput(480, [metaitem('dustLapotron') * 15], [fluid('water') * 1000])
mods.gregtech.autoclave.removeByInput(320, [metaitem('dustLapotron') * 15], [fluid('distilled_water') * 1000])
mods.gregtech.autoclave.removeByInput(256, [metaitem('dustLapotron') * 15], [fluid('blue_steel') * 288])
mods.gregtech.autoclave.removeByInput(192, [metaitem('dustLapotron') * 15], [fluid('red_steel') * 72])
mods.jei.ingredient.removeAndHide(metaitem('lapotron_crystal'));
// Other
mods.jei.ingredient.removeAndHide(metaitem('energy.lapotronic_orb'));
mods.jei.ingredient.removeAndHide(metaitem('energy.lapotronic_orb_cluster'));
mods.jei.ingredient.removeAndHide(metaitem('energy.module'));
mods.jei.ingredient.removeAndHide(metaitem('energy.cluster'));
mods.jei.ingredient.removeAndHide(metaitem('max.battery'));
