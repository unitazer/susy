import globals.Globals
import globals.Photoresists
import globals.Etchants

import static gregtech.api.unification.material.Materials.*;
import gregtech.api.metatileentity.multiblock.CleanroomType


def BR = recipemap('batch_reactor')
def CHEMICAL_BATH = recipemap('chemical_bath')
def CURTAIN_COATER = recipemap('curtain_coater')
def DRYER = recipemap('dryer')
def EXTRUDER = recipemap('extruder')
def ELECTROLYTIC_CELL = recipemap('electrolytic_cell')
def FORMING_PRESS = recipemap("forming_press")
def LCR = recipemap('large_chemical_reactor')
def MILLING = recipemap('milling')
def MIXER = recipemap('mixer')
def UV_LIGHT_BOX = recipemap('uv_light_box')


// Roughly follows https://medium.com/@raypcb/how-are-fr4-pcbs-manufactured-c571fd1e4a29

// Epoxy Prepreg 
// (in Epoxy Chain)

// Titanium Cylinder Drum
FORMING_PRESS.recipeBuilder()
        .notConsumable(metaitem('shape.mold.cylinder'))
        .inputs(ore('ingotTitanium') * 8)
        .outputs(metaitem('titanium_cylinder') * 1)
        .EUt(42)
        .duration(320)
        .buildAndRegister();

// ED Copper Foil
ELECTROLYTIC_CELL.recipeBuilder()
        .notConsumable(metaitem('titanium_cylinder'))
        .notConsumable(metaitem('graphite_electrode'))
        .fluidInputs(fluid('copper_sulfate_solution') * 1000)
        .outputs(metaitem('foilElectrodepositedCopper') * 4)
        .fluidOutputs(fluid('sulfuric_acid') * 1000)
        .fluidOutputs(fluid('oxygen') * 1000)
        .EUt(480)
        .duration(20)
        .buildAndRegister();

// Copper Clad Laminate
FORMING_PRESS.recipeBuilder()
        .inputs(metaitem('board.epoxy.prepreg'))
        .inputs(ore('foilElectrodepositedCopper') * 2)
        .outputs(metaitem('board.epoxy.copper_clad'))
        .EUt(30)
        .duration(100)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();

// Patterned
Photoresists.generatePatterningRecipes("board.epoxy.copper_clad", "board.epoxy.patterned", "mask.pcb", 
                                       4 /* EV */, 
                                       2 /* double sided */, 1, 1, true)

// Etched
Etchants.generateEtchingRecipes("board.epoxy.patterned", "board.epoxy.etched", "copper", 
                                4 /* EV */, 
                                2 /* double sided */, true)

// Drilled
MILLING.recipeBuilder()
        .inputs(metaitem('board.epoxy.etched') * 4)
        .outputs(metaitem('board.epoxy.drilled') * 4)
        .EUt(30)
        .duration(300)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();

// Electroless Plating
// Source: 
//      https://www.nmfrc.org/pdf/p0295g.pdf
//      https://www.rsc.org/suppdata/d2/ee/d2ee01427k/d2ee01427k1.pdf
// (the upper one seems to have a typo: 2H2O instead of 2H2)
// Base reaction: HCHO + 3OH- + Cu+2 --EDTA-> HCOO- + 2H2O + Cu° 
LCR.recipeBuilder()
        .notConsumable(fluid('tetrasodium_ethylenediaminetetraacetate_solution') * 1000)
        .inputs(metaitem('board.epoxy.drilled') * 4)
        .fluidInputs(fluid('sodium_hydroxide_solution') * 3000)
        .fluidInputs(fluid('copper_sulfate_solution') * 1000)
        .fluidInputs(fluid('formaldehyde') * 1000)
        .outputs(metaitem('board.epoxy.electroless') * 4)
        .fluidOutputs(fluid('sodium_sulfate_solution') * 1000)
        .fluidOutputs(fluid('sodium_formate_solution') * 1000)
        .fluidOutputs(fluid('water') * 4000)
        .EUt(30)
        .duration(300)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();

LCR.recipeBuilder()
        .notConsumable(fluid('tetrasodium_ethylenediaminetetraacetate_solution') * 1000)
        .inputs(metaitem('board.epoxy.drilled') * 4)
        .inputs(ore('dustCopperIiChloride') * 3)
        .fluidInputs(fluid('sodium_hydroxide_solution') * 3000)
        .fluidInputs(fluid('formaldehyde') * 1000)
        .outputs(metaitem('board.epoxy.electroless') * 4)
        .fluidOutputs(fluid('salt_water') * 2000)
        .fluidOutputs(fluid('sodium_formate_solution') * 1000)
        .fluidOutputs(fluid('water') * 2000)
        .EUt(30)
        .duration(300)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();
        
LCR.recipeBuilder()
        .notConsumable(fluid('tetrasodium_ethylenediaminetetraacetate_solution') * 1000)
        .inputs(metaitem('board.epoxy.drilled') * 4)
        .inputs(ore('dustCopperIiNitrate') * 9)
        .fluidInputs(fluid('sodium_hydroxide_solution') * 3000)
        .fluidInputs(fluid('formaldehyde') * 1000)
        .outputs(metaitem('board.epoxy.electroless') * 4)
        .fluidOutputs(fluid('sodium_nitrate_solution') * 2000)
        .fluidOutputs(fluid('sodium_formate_solution') * 1000)
        .fluidOutputs(fluid('water') * 2000)
        .EUt(30)
        .duration(300)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();

// Electrolytic Plating
// Reference for the mixture: https://patents.google.com/patent/US4242181A/en
ELECTROLYTIC_CELL.recipeBuilder()
        .inputs(metaitem('board.epoxy.electroless'))
        .circuitMeta(1)
        .fluidInputs(fluid('sulfuric_acid') * 2000)
        .fluidInputs(fluid('copper_sulfate_solution') * 600)
        .outputs(metaitem('board.epoxy.electrolytic'))
        .fluidOutputs(fluid('sulfuric_acid') * 2600)
        .fluidOutputs(fluid('oxygen') * 600)
        .EUt(120)
        .duration(480)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();

ELECTROLYTIC_CELL.recipeBuilder()
        .inputs(metaitem('board.epoxy.electroless'))
        .fluidInputs(fluid('sulfuric_acid') * 2000)
        .fluidInputs(fluid('copper_sulfate_solution') * 600)
        .fluidInputs(fluid('gtfo_coffee') * 10) // :tr:
        .outputs(metaitem('board.epoxy.electrolytic'))
        .fluidOutputs(fluid('sulfuric_acid') * 2600)
        .fluidOutputs(fluid('oxygen') * 600)
        .EUt(120)
        .duration(80)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();

// Masking
MIXER.recipeBuilder()
        .inputs(ore('dyeGreen'))
        .fluidInputs(fluid('epoxy') * 288)
        .fluidOutputs(fluid('green_epoxy_pcb_coating') * 288)
        .EUt(40)
        .duration(32)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();

CURTAIN_COATER.recipeBuilder()
        .inputs(metaitem('board.epoxy.electrolytic'))
        .fluidInputs(fluid('green_epoxy_pcb_coating') * 72)
        .outputs(metaitem('board.epoxy.wet_masked'))
        .EUt(30)
        .duration(20)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();

DRYER.recipeBuilder()
        .inputs(metaitem('board.epoxy.wet_masked'))
        .outputs(metaitem('board.epoxy.masked'))
        .EUt(30)
        .duration(60)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();

UV_LIGHT_BOX.recipeBuilder()
        .notConsumable(metaitem('mask.pcb'))
        .inputs(metaitem('board.epoxy.masked'))
        .outputs(metaitem('board.epoxy.mask_affixed'))
        .EUt(30)
        .duration(100)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();

// Surface Finished (apply solder)
CHEMICAL_BATH.recipeBuilder()
        .inputs(metaitem('board.epoxy.mask_affixed'))
        .notConsumable(fluid('soldering_alloy') * 144)
        .fluidInputs(fluid('soldering_alloy') * 144)
        .outputs(metaitem('circuit_board.extreme'))
        .EUt(30)
        .duration(40)
        .cleanroom(CleanroomType.CLEANROOM)
        .buildAndRegister();
