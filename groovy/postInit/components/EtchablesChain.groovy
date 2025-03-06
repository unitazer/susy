import globals.Globals
import globals.Photoresists
import globals.Etchants
import globals.NDopants
import globals.PDopants

import gregtech.api.metatileentity.multiblock.CleanroomType

ASSEMBLER = recipemap('assembler')
CIRCUIT_ASSEMBLER = recipemap('circuit_assembler')
FORMING_PRESS = recipemap('forming_press')
CHEMICAL_BATH = recipemap('chemical_bath')
MIXER = recipemap('mixer')
LASER_ENGRAVER = recipemap('laser_engraver')
ELECTROLYZER = recipemap('electrolyzer')
CUTTER = recipemap('cutter')
ION_IMPLANTER = recipemap('ion_implantation')
UV_LIGHT_BOX = recipemap('uv_light_box')
CVD = recipemap('cvd')
REACTION_FURNACE = recipemap('reaction_furnace')
CSTR = recipemap('continuous_stirred_tank_reactor')

//REMOVALS

// CPU * 8
mods.gregtech.cutter.removeByInput(120, [metaitem('wafer.central_processing_unit')], [fluid('lubricant') * 84])
// CPU * 8
mods.gregtech.cutter.removeByInput(120, [metaitem('wafer.central_processing_unit')], [fluid('distilled_water') * 253])
// CPU * 8
mods.gregtech.cutter.removeByInput(120, [metaitem('wafer.central_processing_unit')], [fluid('water') * 337])
// RAM * 32
mods.gregtech.cutter.removeByInput(96, [metaitem('wafer.random_access_memory')], [fluid('lubricant') * 67])
// RAM * 32
mods.gregtech.cutter.removeByInput(96, [metaitem('wafer.random_access_memory')], [fluid('distilled_water') * 202])
// RAM * 32
mods.gregtech.cutter.removeByInput(96, [metaitem('wafer.random_access_memory')], [fluid('water') * 270])
// ULPIC * 6
mods.gregtech.cutter.removeByInput(120, [metaitem('wafer.ultra_low_power_integrated_circuit')], [fluid('lubricant') * 84])
// ULPIC * 6
mods.gregtech.cutter.removeByInput(120, [metaitem('wafer.ultra_low_power_integrated_circuit')], [fluid('distilled_water') * 253])
// ULPIC * 6
mods.gregtech.cutter.removeByInput(120, [metaitem('wafer.ultra_low_power_integrated_circuit')], [fluid('water') * 337])
// LPIC * 4
mods.gregtech.cutter.removeByInput(480, [metaitem('wafer.low_power_integrated_circuit')], [fluid('lubricant') * 250])
// LPIC * 4
mods.gregtech.cutter.removeByInput(480, [metaitem('wafer.low_power_integrated_circuit')], [fluid('distilled_water') * 750])
// LPIC * 4
mods.gregtech.cutter.removeByInput(480, [metaitem('wafer.low_power_integrated_circuit')], [fluid('water') * 1000])

// Integrated Circuit * 8
mods.gregtech.cutter.removeByInput(64, [metaitem('wafer.integrated_logic_circuit')], [fluid('lubricant') * 45])
// Integrated Circuit * 8
mods.gregtech.cutter.removeByInput(64, [metaitem('wafer.integrated_logic_circuit')], [fluid('distilled_water') * 135])
// Integrated Circuit * 8
mods.gregtech.cutter.removeByInput(64, [metaitem('wafer.integrated_logic_circuit')], [fluid('water') * 180])

// PIC * 4
mods.gregtech.cutter.removeByInput(1920, [metaitem('wafer.power_integrated_circuit')], [fluid('lubricant') * 250])
// PIC * 4
mods.gregtech.cutter.removeByInput(1920, [metaitem('wafer.power_integrated_circuit')], [fluid('distilled_water') * 750])
// PIC * 4
mods.gregtech.cutter.removeByInput(1920, [metaitem('wafer.power_integrated_circuit')], [fluid('water') * 1000])

// NAND * 32
mods.gregtech.cutter.removeByInput(192, [metaitem('wafer.nand_memory_chip')], [fluid('lubricant') * 135])
// NAND * 32
mods.gregtech.cutter.removeByInput(192, [metaitem('wafer.nand_memory_chip')], [fluid('distilled_water') * 405])
// NAND * 32
mods.gregtech.cutter.removeByInput(192, [metaitem('wafer.nand_memory_chip')], [fluid('water') * 540])
// NOR * 16
mods.gregtech.cutter.removeByInput(192, [metaitem('wafer.nor_memory_chip')], [fluid('lubricant') * 135])
// NOR * 16
mods.gregtech.cutter.removeByInput(192, [metaitem('wafer.nor_memory_chip')], [fluid('distilled_water') * 405])
// NOR * 16
mods.gregtech.cutter.removeByInput(192, [metaitem('wafer.nor_memory_chip')], [fluid('water') * 540])


MIXER.recipeBuilder()
        .inputs(ore('dustTinyPotassiumHydroxide'))
        .inputs(ore('dustSiliconDioxide') * 30)
        .fluidInputs(fluid('demineralized_water') * 10000)
        .fluidOutputs(fluid('silicon_dioxide_slurry') * 10000)
        .duration(200)
        .EUt(Globals.voltAmps[2])
        .buildAndRegister()


def generateCuttingRecipes(input, product, productMultiplier, voltageTier, boolean cleanroom) {
    if (cleanroom) {
        CUTTER.recipeBuilder()
                .fluidInputs(fluid('ultrapure_water') * 100)
                .inputs(metaitem(input))
                .outputs(metaitem(product) * productMultiplier)
                .duration(450)
                .cleanroom(CleanroomType.CLEANROOM)
                .EUt(Globals.voltAmps[voltageTier])
                .buildAndRegister()

        LASER_ENGRAVER.recipeBuilder()
                .inputs(metaitem(input))
                .outputs(metaitem(product) * productMultiplier)
                .duration(60)
                .cleanroom(CleanroomType.CLEANROOM)
                .EUt(Globals.voltAmps[voltageTier])
                .buildAndRegister()
    } else {
        CUTTER.recipeBuilder()
                .fluidInputs(fluid('ultrapure_water') * 100)
                .inputs(metaitem(input))
                .outputs(metaitem(product) * productMultiplier)
                .duration(450)
                .EUt(Globals.voltAmps[voltageTier])
                .buildAndRegister()

        LASER_ENGRAVER.recipeBuilder()
                .inputs(metaitem(input))
                .outputs(metaitem(product) * productMultiplier)
                .duration(60)
                .EUt(Globals.voltAmps[voltageTier])
                .buildAndRegister()
    }
}

//NOVOLACS

CSTR.recipeBuilder()
        .fluidInputs(fluid('formaldehyde') * 75)
        .fluidInputs(fluid('ammonia_solution') * 50)
        .fluidOutputs(fluid('hexamethylenetetramine_solution') * 50)
        .duration(50)
        .EUt(7)
        .buildAndRegister()

CSTR.recipeBuilder()
        .fluidInputs(fluid('formaldehyde') * 50)
        .fluidInputs(fluid('phenol') * 50)
        .fluidInputs(fluid('hexamethylenetetramine_solution') * 5)
        .notConsumable(fluid('sodium_hydroxide_solution') * 50)
        .fluidOutputs(fluid('novolacs') * 100)
        .duration(10)
        .EUt(7)
        .buildAndRegister()

//DOPING

for (ndopant in NDopants.ndopants) {
    ION_IMPLANTER.recipeBuilder()
            .inputs(metaitem('wafer.silicon') * 16 * ndopant.efficiency)
            .circuitMeta(1)
            .inputs(metaitem(ndopant.metaItemName))
            .outputs(metaitem('wafer.n_doped.silicon') * 16 * ndopant.efficiency)
            .duration(300)
            .EUt(60)
            .buildAndRegister()

    ION_IMPLANTER.recipeBuilder()
            .inputs(metaitem('etched.silicon_dioxide') * 16 * ndopant.efficiency)
            .circuitMeta(1)
            .inputs(metaitem(ndopant.metaItemName))
            .outputs(metaitem('wafer.doped.silicon_dioxide') * 16 * ndopant.efficiency)
            .cleanroom(CleanroomType.CLEANROOM)
            .duration(300)
            .EUt(240)
            .buildAndRegister()

    for (pdopant in PDopants.pdopants) {
        int batchSize = pdopant.efficiency * ndopant.efficiency

        ION_IMPLANTER.recipeBuilder()
                .inputs(metaitem('wafer.silicon') * 16 * batchSize)
                .inputs(metaitem(pdopant.metaItemName))
                .inputs(metaitem(ndopant.metaItemName))
                .outputs(metaitem('wafer.doped.silicon') * 16 * batchSize)
                .duration(200)
                .EUt(60)
                .buildAndRegister()

        ION_IMPLANTER.recipeBuilder()
                .inputs(metaitem('etched.polysilicon') * 16 * batchSize)
                .inputs(metaitem(pdopant.metaItemName))
                .inputs(metaitem(ndopant.metaItemName))
                .outputs(metaitem('wafer.doped.polysilicon') * 16 * batchSize)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200)
                .EUt(240)
                .buildAndRegister()
    }
}

//WAFERS


Photoresists.generatePatterningRecipes('wafer.silicon', 'patterned.ram', 'mask.ram', 2, 4, 1, 1, false)
Photoresists.generatePatterningRecipes('wafer.silicon', 'patterned.ulpic', 'mask.ulpic', 2, 4, 1, 2, false)
Photoresists.generatePatterningRecipes('wafer.silicon', 'patterned.ic', 'mask.ic', 2, 4, 1, 3, false)

Photoresists.generatePatterningRecipes('wafer.doped.silicon', 'patterned.ram', 'mask.ram', 2, 4, 2, 1, false)
Photoresists.generatePatterningRecipes('wafer.doped.silicon', 'patterned.cpu', 'mask.cpu', 2, 4, 1, 2, false)
Photoresists.generatePatterningRecipes('wafer.doped.silicon', 'patterned.ulpic', 'mask.ulpic', 2, 4, 2, 3, false)
Photoresists.generatePatterningRecipes('wafer.doped.silicon', 'patterned.lpic', 'mask.lpic', 3, 4, 1, 4, false)
Photoresists.generatePatterningRecipes('wafer.doped.silicon', 'patterned.ic', 'mask.ic', 2, 4, 2, 5, false)

Photoresists.generatePatterningRecipes('wafer.silicon_dioxide', 'patterned.silicon_dioxide', 'mask.advanced', 3, 4, 1, 1, true)
Photoresists.generatePatterningRecipes('wafer.polysilicon', 'patterned.polysilicon', 'mask.advanced', 3, 4, 1, 1, true)
Photoresists.generatePatterningRecipes('wafer.silicon_nitride', 'patterned.silicon_nitride', 'mask.advanced', 3, 4, 1, 1, true)
Photoresists.generatePatterningRecipes('wafer.nickel', 'patterned.nand', 'mask.nand', 3, 4, 1, 1, true)
Photoresists.generatePatterningRecipes('wafer.nickel', 'patterned.nor', 'mask.nor', 3, 4, 1, 2, true)

Etchants.generateEtchingRecipes('patterned.ic', 'etched.ic', 'silicon', 1, 1, false)
Etchants.generateEtchingRecipes('patterned.cpu', 'etched.cpu', 'silicon', 1, 1,false)
Etchants.generateEtchingRecipes('patterned.ram', 'etched.ram', 'silicon', 1, 1, false)
Etchants.generateEtchingRecipes('patterned.ulpic', 'etched.ulpic', 'silicon', 1, 1, false)
Etchants.generateEtchingRecipes('patterned.lpic', 'etched.lpic', 'silicon', 1, 1, false)

Etchants.generateEtchingRecipes('patterned.silicon_dioxide', 'etched.silicon_dioxide', 'silicon_dioxide', 2, 1, true)
Etchants.generateEtchingRecipes('patterned.polysilicon', 'etched.polysilicon', 'silicon', 2, 1, true)
Etchants.generateEtchingRecipes('patterned.silicon_nitride', 'etched.silicon_nitride', 'silicon_nitride', 2, 1, true)
Etchants.generateEtchingRecipes('patterned.nand', 'wafer.nand_memory_chip', 'nickel', 2, 1, true)
Etchants.generateEtchingRecipes('patterned.nor', 'wafer.nor_memory_chip', 'nickel', 2, 1, true)

ELECTROLYZER.recipeBuilder()
        .inputs(metaitem('etched.ic'))
        .inputs(ore('foilCopper'))
        .fluidInputs(fluid('sulfuric_acid') * 100)
        .outputs(metaitem('wafer.integrated_logic_circuit'))
        .duration(200)
        .EUt(30)
        .buildAndRegister()

ELECTROLYZER.recipeBuilder()
        .inputs(metaitem('etched.cpu'))
        .inputs(ore('foilCopper'))
        .fluidInputs(fluid('sulfuric_acid') * 100)
        .outputs(metaitem('wafer.central_processing_unit'))
        .duration(200)
        .EUt(30)
        .buildAndRegister()

ELECTROLYZER.recipeBuilder()
        .inputs(metaitem('etched.ram'))
        .inputs(ore('foilCopper'))
        .fluidInputs(fluid('sulfuric_acid') * 100)
        .outputs(metaitem('wafer.random_access_memory'))
        .duration(200)
        .EUt(30)
        .buildAndRegister()

ELECTROLYZER.recipeBuilder()
        .inputs(metaitem('etched.ulpic'))
        .inputs(ore('foilCopper'))
        .fluidInputs(fluid('sulfuric_acid') * 100)
        .outputs(metaitem('wafer.ultra_low_power_integrated_circuit'))
        .duration(200)
        .EUt(30)
        .buildAndRegister()

ELECTROLYZER.recipeBuilder()
        .inputs(metaitem('etched.lpic'))
        .inputs(ore('foilCopper'))
        .fluidInputs(fluid('sulfuric_acid') * 100)
        .outputs(metaitem('wafer.low_power_integrated_circuit'))
        .duration(200)
        .EUt(30)
        .buildAndRegister()

generateCuttingRecipes('wafer.central_processing_unit', 'plate.central_processing_unit', 8, 2, false)
generateCuttingRecipes('wafer.ultra_low_power_integrated_circuit', 'plate.ultra_low_power_integrated_circuit', 6, 2, false)
generateCuttingRecipes('wafer.integrated_logic_circuit', 'plate.integrated_logic_circuit', 8, 2, false)
generateCuttingRecipes('wafer.random_access_memory', 'plate.random_access_memory', 32, 2, false)
generateCuttingRecipes('wafer.low_power_integrated_circuit', 'plate.low_power_integrated_circuit', 4, 3, true)
generateCuttingRecipes('wafer.power_integrated_circuit', 'plate.power_integrated_circuit', 4, 4, true)
generateCuttingRecipes('wafer.nand_memory_chip', 'plate.nand_memory_chip', 32, 3, true)
generateCuttingRecipes('wafer.nor_memory_chip', 'plate.nor_memory_chip', 16, 3, true)

//PHENOLIC BOARD (TIER 2)

FORMING_PRESS.recipeBuilder()
        .inputs(metaitem('board.phenolic'))
        .inputs(ore('foilCopper'))
        .outputs(metaitem('laminated.board.phenolic'))
        .duration(100)
        .EUt(30)
        .buildAndRegister()

Photoresists.generatePatterningRecipes('laminated.board.phenolic', 'patterned.board.phenolic', 'mask.pcb', 1, 1, 1, 0, false)
Etchants.generateEtchingRecipes('patterned.board.phenolic', 'circuit_board.good', 'copper', 1, 1, false)

//PLASTIC CIRCUIT BOARD (TIER 3)

FORMING_PRESS.recipeBuilder()
        .inputs(ore('platePlastic'))
        .inputs(ore('foilCopper'))
        .outputs(metaitem('board.plastic'))
        .duration(100)
        .EUt(30)
        .buildAndRegister()

FORMING_PRESS.recipeBuilder()
        .inputs(ore('platePolyvinylChloride'))
        .inputs(ore('foilCopper'))
        .outputs(metaitem('board.plastic') * 2)
        .duration(100)
        .EUt(30)
        .buildAndRegister()

FORMING_PRESS.recipeBuilder()
        .inputs(ore('platePolytetrafluoroethylene'))
        .inputs(ore('foilCopper'))
        .outputs(metaitem('board.plastic') * 4)
        .duration(100)
        .EUt(30)
        .buildAndRegister()

FORMING_PRESS.recipeBuilder()
        .inputs(ore('platePolybenzimidazole'))
        .inputs(ore('foilCopper'))
        .outputs(metaitem('board.plastic') * 8)
        .duration(100)
        .EUt(30)
        .buildAndRegister()

Photoresists.generatePatterningRecipes('board.plastic', 'patterned.board.plastic', 'mask.pcb', 2, 1, 1, 0, false)
Etchants.generateEtchingRecipes('patterned.board.plastic', 'circuit_board.plastic', 'copper', 2, 1, false)

//NAND AND NOR

CVD.recipeBuilder()
        .inputs(metaitem('wafer.doped.silicon_dioxide'))
        .fluidInputs(fluid('silane') * 250)
        .outputs(metaitem('wafer.polysilicon'))
        .fluidOutputs(fluid('hydrogen') * 1000)
        .cleanroom(CleanroomType.CLEANROOM)
        .duration(100)
        .EUt(240)
        .buildAndRegister()

CVD.recipeBuilder()
        .inputs(metaitem('wafer.doped.polysilicon'))
        .fluidInputs(fluid('silane') * 750)
        .fluidInputs(fluid('ammonia') * 1000)
        .outputs(metaitem('wafer.silicon_nitride'))
        .fluidOutputs(fluid('hydrogen') * 3000)
        .cleanroom(CleanroomType.CLEANROOM)
        .duration(200)
        .EUt(240)
        .buildAndRegister()

CVD.recipeBuilder()
        .inputs(metaitem('etched.silicon_nitride'))
        .fluidInputs(fluid('nickel_carbonyl') * 500)
        .outputs(metaitem('wafer.nickel'))
        .fluidOutputs(fluid('carbon_monoxide') * 2000)
        .cleanroom(CleanroomType.CLEANROOM)
        .duration(200)
        .EUt(240)
        .buildAndRegister()

//PIC

ASSEMBLER.recipeBuilder()
        .inputs(metaitem('wafer.doped.polysilicon'))
        .inputs(metaitem('wafer.alumina'))
        .outputs(metaitem('wafer.power_integrated_circuit'))
        .cleanroom(CleanroomType.CLEANROOM)
        .duration(200)
        .EUt(120)
        .buildAndRegister()

ASSEMBLER.recipeBuilder()
        .inputs(metaitem('wafer.doped.polysilicon'))
        .inputs(metaitem('wafer.beryllium_oxide'))
        .outputs(metaitem('wafer.power_integrated_circuit'))
        .cleanroom(CleanroomType.CLEANROOM)
        .duration(200)
        .EUt(120)
        .buildAndRegister()
