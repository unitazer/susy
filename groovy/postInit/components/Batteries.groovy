import static prePostInit.Recipemaps.*
import static gregtech.api.GTValues.*
import gregtech.api.recipes.recipeproperties.RecipeProperty
import gregtech.api.recipes.RecipeBuilder
import static gregtech.api.recipes.RecipeMaps.*
import gregtech.api.unification.material.Materials.*

import gregtech.api.recipes.ModHandler
import gregtech.api.unification.material.Materials
import gregtech.api.unification.ore.OrePrefix
import gregtech.api.unification.stack.UnificationEntry

import postInit.utils.RecyclingHelper

log.infoMC("Running Batteries.groovy...")

mods.jei.ingredient.yeet(
        metaitem('battery.hull.mv'),
        metaitem('battery.ni_zn.mv'),
        metaitem('battery.ni_zn.hv'),
        metaitem('battery.hull.lv'),
        metaitem('energy.lapotronic_orb'),
        metaitem('energy.lapotronic_orb_cluster'),
        metaitem('energy.module'),
        metaitem('energy.cluster'),
        metaitem('max.battery'),
        metaitem('energy_crystal'),
        metaitem('lapotron_crystal'),
)

/*
 * Remove legacy batteries
 */

[
        metaitem('battery.re.lv.sodium'),
        metaitem('battery.re.lv.cadmium'),
        metaitem('battery.re.lv.lithium'),
        metaitem('battery.re.mv.sodium'),
        metaitem('battery.re.mv.cadmium'),
        metaitem('battery.re.mv.lithium'),
        metaitem('battery.re.hv.sodium')
].each { battery ->
    crafting.removeByInput(battery)
    mods.jei.ingredient.yeet(battery)
}

['lv', 'mv', 'hv', 'ev', 'iv'].each { voltage ->
    crafting.remove('gregtech:battery_hull_' + voltage)
    RecyclingHelper.removeRecyclingRecipes(metaitem('battery.hull.' + voltage))
}

// LV Battery Hull
mods.gregtech.assembler.removeByInput(1, [metaitem('cableGtSingleTin'), metaitem('plateBatteryAlloy')], [fluid('plastic') * 144 * 144])
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.lv.cadmium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.lv.lithium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.lv.sodium')], null)
// MV Battery Hull
mods.gregtech.assembler.removeByInput(2, [metaitem('cableGtSingleCopper') * 2, metaitem('plateBatteryAlloy') * 3], [fluid('plastic') * 432 * 432])
mods.gregtech.assembler.removeByInput(2, [metaitem('cableGtSingleAnnealedCopper') * 2, metaitem('plateBatteryAlloy') * 3], [fluid('plastic') * 432 * 432])
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.mv.cadmium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.mv.lithium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.mv.sodium')], null)
// HV Battery Hull
mods.gregtech.assembler.removeByInput(4, [metaitem('cableGtSingleGold') * 4, metaitem('plateBatteryAlloy') * 9], [fluid('plastic') * 1296 * 1296])
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.hv.cadmium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.hv.lithium')], null)
mods.gregtech.extractor.removeByInput(2, [metaitem('battery.re.hv.sodium')], null)
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
// Energy crystal
mods.gregtech.autoclave.removeByInput(480, [metaitem('energium_dust') * 9], [fluid('water') * 1000])
mods.gregtech.autoclave.removeByInput(320, [metaitem('energium_dust') * 9], [fluid('distilled_water') * 1000])
mods.gregtech.autoclave.removeByInput(256, [metaitem('energium_dust') * 9], [fluid('black_steel') * 288])
mods.gregtech.autoclave.removeByInput(192, [metaitem('energium_dust') * 9], [fluid('blue_steel') * 72])
// Lapotron
mods.gregtech.autoclave.removeByInput(480, [metaitem('dustLapotron') * 15], [fluid('water') * 1000])
mods.gregtech.autoclave.removeByInput(320, [metaitem('dustLapotron') * 15], [fluid('distilled_water') * 1000])
mods.gregtech.autoclave.removeByInput(256, [metaitem('dustLapotron') * 15], [fluid('blue_steel') * 288])
mods.gregtech.autoclave.removeByInput(192, [metaitem('dustLapotron') * 15], [fluid('red_steel') * 72])

// Lead-Acid Batteries

crafting.addShapeless("anode_lead", metaitem('anode.lead'), [
        metaitem('plateLead'), metaitem('cableGtSingleTin')
]);

MIXER.recipeBuilder()
        .inputs(ore('dustLeadIvOxide') * 3)
        .fluidInputs(fluid('diluted_sulfuric_acid') * 1000)
        .outputs(metaitem('cathode.lead_paste'))
        .duration(80)
        .EUt(VA[LV])
        .buildAndRegister()

crafting.addShaped("cathode_lead_frame", metaitem('cathode.lead_frame'), [
        [null, null, null],
        [ore('craftingToolWireCutter'), ore('plateLead'), null],
        [null, null, null]
]);

FORMING_PRESS.recipeBuilder()
        .notConsumable(metaitem('cathode.lead_frame'))
        .inputs(ore('plateLead'))
        .outputs(metaitem('cathode.lead_frame'))
        .duration(80)
        .EUt(VA[LV])
        .buildAndRegister()

crafting.addShaped("cathode_lead", metaitem('cathode.lead'), [
        [null, metaitem('cableGtSingleTin'), ore('craftingToolWireCutter')],
        [null, metaitem('cathode.lead_frame'), null],
        [ore('craftingToolRollingPin'), metaitem('cathode.lead_paste'), null]
]);

crafting.addShaped('primitive_battery_hull_lv', metaitem('battery.primitivehull.lv'), [
        [null,                metaitem('cableGtSingleTin'), null],
        [ore('plateEbonite'), null,                         ore('plateEbonite')],
        [ore('plateEbonite'), null,                         ore('plateEbonite')]
])

ASSEMBLER.recipeBuilder()
        .inputs(metaitem('cableGtSingleTin'))
        .inputs(ore('plateEbonite') * 4)
        .outputs(metaitem('battery.primitivehull.lv'))
        .duration(80)
        .EUt(VA[ULV])
        .buildAndRegister()

ASSEMBLER.recipeBuilder()
        .inputs(metaitem('cableGtSingleCopper') * 2)
        .inputs(ore('plateEbonite') * 6)
        .outputs(metaitem('battery.primitivehull.mv'))
        .duration(80)
        .EUt(VA[LV])
        .buildAndRegister()

crafting.addShaped("battery_lead_acid", metaitem('battery.lead_acid'), [
        [null,                     metaitem('cableGtSingleTin'),          null],
        [metaitem('cathode.lead'), fluid('diluted_sulfuric_acid') * 1000, metaitem('anode.lead')],
        [null,                     metaitem('battery.primitivehull.lv'),  null]
]);

ASSEMBLER.recipeBuilder()
        .inputs(metaitem('battery.primitivehull.lv'))
        .inputs(ore('plateLead'))
        .inputs(metaitem('cathode.lead_frame'))
        .inputs(metaitem('cathode.lead_paste'))
        .fluidInputs(fluid('diluted_sulfuric_acid') * 1000)
        .outputs(metaitem('battery.lead_acid'))
        .duration(200)
        .EUt(VA[LV])
        .buildAndRegister()

ASSEMBLER.recipeBuilder()
        .inputs(metaitem('battery.primitivehull.mv'))
        .inputs(ore('plateLead') * 2)
        .inputs(metaitem('cathode.lead_frame') * 2)
        .inputs(metaitem('cathode.lead_paste') * 2)
        .fluidInputs(fluid('diluted_sulfuric_acid') * 2000)
        .outputs(metaitem('battery.lead_acid.mv'))
        .duration(200)
        .EUt(VA[LV])
        .buildAndRegister()

// Nickel-Iron Batteries

crafting.addShaped("electrode_steel_frame", metaitem('electrode.steel_frame'), [
        [null, null, null],
        [null, ore('plateSteel'), null],
        [ore('craftingToolHardHammer'), null, null]
]);

ASSEMBLER.recipeBuilder()
        .circuitMeta(1)
        .inputs(ore('plateSteel') * 4)
        .outputs(metaitem('battery.steel_hull.lv'))
        .duration(40)
        .EUt(VA[LV])
        .buildAndRegister()

ASSEMBLER.recipeBuilder()
        .circuitMeta(2)
        .inputs(ore('plateSteel') * 6)
        .outputs(metaitem('battery.steel_hull.mv'))
        .duration(40)
        .EUt(VA[LV])
        .buildAndRegister()

ASSEMBLER.recipeBuilder()
        .circuitMeta(3)
        .inputs(ore('plateSteel') * 8)
        .outputs(metaitem('battery.steel_hull.hv'))
        .duration(40)
        .EUt(VA[LV])
        .buildAndRegister()

FORMING_PRESS.recipeBuilder()
        .notConsumable(metaitem('electrode.steel_frame'))
        .inputs(ore('plateSteel'))
        .outputs(metaitem('electrode.steel_frame'))
        .duration(80)
        .EUt(VA[LV])
        .buildAndRegister()

MIXER.recipeBuilder()
        .inputs(ore('dustNickelSulfate') * 30)
        .inputs(ore('dustNickelChloride') * 3)
        .inputs(ore('dustBoricAcid') * 14)
        .fluidInputs(fluid('distilled_water') * 1000)
        .fluidOutputs(fluid('watts_bath_electrolyte') * 1000)
        .duration(160)
        .EUt(VA[LV])
        .buildAndRegister()

ELECTROLYTIC_CELL.recipeBuilder()
        .notConsumable(fluid('watts_bath_electrolyte') * 1000)
        .inputs(ore('foilNickel'))
        .inputs(metaitem('electrode.steel_frame'))
        .outputs(metaitem('electrode.nickeled_steel_frame'))
        .duration(160)
        .EUt(VA[LV])
        .buildAndRegister()

ELECTROLYTIC_CELL.recipeBuilder()
        .notConsumable(fluid('watts_bath_electrolyte') * 1000)
        .inputs(ore('foilNickel') * 4)
        .inputs(metaitem('battery.steel_hull.lv'))
        .outputs(metaitem('battery.ni_fe_hull.lv'))
        .duration(160)
        .EUt(VA[LV])
        .buildAndRegister()

ELECTROLYTIC_CELL.recipeBuilder()
        .notConsumable(fluid('watts_bath_electrolyte') * 1000)
        .inputs(ore('foilNickel') * 6)
        .inputs(metaitem('battery.steel_hull.mv'))
        .outputs(metaitem('battery.ni_fe_hull.mv'))
        .duration(160)
        .EUt(VA[LV])
        .buildAndRegister()

ELECTROLYTIC_CELL.recipeBuilder()
        .notConsumable(fluid('watts_bath_electrolyte') * 1000)
        .inputs(ore('foilNickel') * 8)
        .inputs(metaitem('battery.steel_hull.hv'))
        .outputs(metaitem('battery.ni_fe_hull.hv'))
        .duration(160)
        .EUt(VA[LV])
        .buildAndRegister()

ASSEMBLER.recipeBuilder()
        .inputs(metaitem('battery.ni_fe_hull.lv'))
        .inputs(metaitem('cableGtSingleTin'))
        .inputs(metaitem('electrode.nickeled_steel_frame') * 2)
        .inputs(ore('dustNickelHydroxide') * 5)
        .inputs(ore('dustIronTwoThreeOxide') * 7)
        .fluidInputs(fluid('potassium_hydroxide_solution') * 1000)
        .outputs(metaitem('battery.ni_fe.lv'))
        .duration(200)
        .EUt(VA[MV])
        .buildAndRegister()

ASSEMBLER.recipeBuilder()
        .inputs(metaitem('battery.ni_fe_hull.mv'))
        .inputs(metaitem('cableGtSingleCopper') * 2)
        .inputs(metaitem('electrode.nickeled_steel_frame') * 4)
        .inputs(ore('dustNickelHydroxide') * 10)
        .inputs(ore('dustIronTwoThreeOxide') * 14)
        .fluidInputs(fluid('potassium_hydroxide_solution') * 2000)
        .outputs(metaitem('battery.ni_fe'))
        .duration(200)
        .EUt(VA[MV])
        .buildAndRegister()

ASSEMBLER.recipeBuilder()
        .inputs(metaitem('battery.ni_fe_hull.hv'))
        .inputs(metaitem('cableGtSingleGold') * 4)
        .inputs(metaitem('electrode.nickeled_steel_frame') * 8)
        .inputs(ore('dustNickelHydroxide') * 20)
        .inputs(ore('dustIronTwoThreeOxide') * 28)
        .fluidInputs(fluid('potassium_hydroxide_solution') * 4000)
        .outputs(metaitem('battery.ni_fe.hv'))
        .duration(200)
        .EUt(VA[MV])
        .buildAndRegister()

// HV Battery Hull
ASSEMBLER.recipeBuilder()
        .circuitMeta(5)
        .inputs(ore('plateStainlessSteel') * 8)
        .inputs(ore('platePolycaprolactam'))
        .inputs(ore('foilPolyvinylChloride') * 4)
        .inputs(metaitem('cableGtSingleGold') * 4)
        .fluidInputs(fluid('polypropylene') * 1296)
        .outputs(metaitem('battery.hull.hv'))
        .duration(240)
        .EUt(VA[MV])
        .buildAndRegister()

RecyclingHelper.handleRecycling(metaitem('battery.hull.hv'),
        [ore('plateStainlessSteel') * 8, metaitem('cableGtSingleGold') * 4, ore('dustPolycaprolactam'), ore('dustPolyvinylChloride'), ore('dustPolypropylene') * 9])

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
        .EUt(VA[HV])
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
        .EUt(VA[EV])
        .buildAndRegister()

RecyclingHelper.handleRecycling(metaitem('battery.hull.iv'),
        [ore('plateStainlessSteel') * 32, metaitem('cableGtSinglePlatinum') * 2, ore('dustPolytetrafluoroethylene') * 3])

/* Old recipes */

ROASTER.recipeBuilder()
        .inputs(ore('dustLithiumCarbonate') * 6)
        .fluidInputs(fluid('hydrogen_fluoride') * 2000)
        .fluidOutputs(fluid('dense_steam') * 1000)
        .fluidOutputs(fluid('carbon_dioxide') * 1000)
        .outputs(metaitem('dustLithiumFluoride') * 4)
        .duration(140)
        .EUt(VA[LV])
        .buildAndRegister()

BR.recipeBuilder()
        .inputs(ore('dustLithiumFluoride') * 2)
        .inputs(ore('dustPhosphorusPentachloride') * 6)
        .fluidInputs(fluid('hydrogen_fluoride') * 5000)
        .fluidOutputs(fluid('hydrogen_chloride') * 5000)
        .outputs(metaitem('dustLithiumHexafluorophosphate') * 8)
        .duration(140)
        .EUt(VA[LV])
        .buildAndRegister()

MIXER_RECIPES.recipeBuilder()
        .inputs(ore('dustLithiumHexafluorophosphate') * 8)
        .fluidInputs(fluid('ethylene_carbonate') * 1000)
        .fluidOutputs(fluid('lithium_hexafluorophosphate_electrolyte') * 1000)
        .duration(400)
        .EUt(VA[LV])
        .buildAndRegister()

ROASTER.recipeBuilder()
        .inputs(ore('dustLithiumCarbonate') * 6)
        .inputs(ore('dustHighPurityCobaltOxide') * 4)
        .fluidOutputs(fluid('carbon_monoxide') * 1000)
        .outputs(metaitem('dustLithiumCobaltOxide') * 8)
        .duration(140)
        .EUt(VA[LV])
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
        .inputs(metaitem('battery.hull.hv'))
        .inputs(ore('dustCadmium') * 4)
        .inputs(ore('dustNickelHydroxide') * 10)
        .fluidInputs(fluid('potassium_hydroxide_solution') * 500)
        .outputs(metaitem('battery.re.hv.cadmium'))
        .duration(400)
        .EUt(16)
        .buildAndRegister()




