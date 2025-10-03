import globals.Globals

import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.UnificationEntry;

import groovy.transform.TupleConstructor

POLYMERIZATION_TANK = recipemap('polymerization_tank')
MIXER = recipemap('mixer')
SIFTER = recipemap('sifter')
DRYER = recipemap('dryer')
EXTRACTOR = recipemap('extractor')
VULCANIZER = recipemap("vulcanizing_press")

//REMOVALS
// Raw Rubber Pulp * 2
mods.gregtech.extractor.removeByInput(2, [item('minecraft:slime_ball')], null)
// Raw Rubber Pulp * 3
mods.gregtech.extractor.removeByInput(2, [metaitem('rubber_drop')], null)
// Raw Rubber Pulp * 1
mods.gregtech.extractor.removeByInput(2, [item('gregtech:rubber_sapling')], null)
// Raw Rubber Pulp * 1
mods.gregtech.extractor.removeByInput(2, [item('gregtech:rubber_leaves') * 16], null)
// Raw Rubber Pulp * 1
mods.gregtech.extractor.removeByInput(2, [item('gregtech:rubber_log')], null)
// Liquid Latex * 144
mods.gregtech.extractor.removeByInput(30, [metaitem('dustLatex')], null)

@TupleConstructor
class Catalyst {
    String name
    int speed_bonus
}

@TupleConstructor
class Rubber {
    String name 
    String output
    int amount_required
    int duration
    int yield
    boolean isFluid 
}

@TupleConstructor
class Coagulant {
    String name
    int amount_required
    int speed_bonus
    int yield_bonus
    boolean isFluid
}

class Shape {
    String name
    String shapeName
    int yield

    Shape(name, yield){
        this.name = name
        this.shapeName = name
        this.yield = yield
    }
    
    Shape(name, shapeName, yield) {
        this.name = name
        this.shapeName = shapeName
        this.yield = yield
    }
}

def CoagulationRecipe(coagulant, amount, duration) {
    def COAGULATION_RECIPES = recipemap("coagulation_tank")

    if (coagulant.isFluid) {
        COAGULATION_RECIPES.recipeBuilder()
        .fluidInputs(fluid('latex') * (1000 * amount))
        .notConsumable(fluid(coagulant.name) * (coagulant.amount_required * amount))
        .outputs(metaitem('dustLatex') * (coagulant.yield_bonus * amount))
        .duration(duration.intdiv(coagulant.speed_bonus))
        .buildAndRegister()
    } else {
        COAGULATION_RECIPES.recipeBuilder()
        .fluidInputs(fluid('latex') * (1000 * amount))
        .notConsumable(ore(coagulant.name) * (coagulant.amount_required * amount))
        .outputs(metaitem('dustLatex') * (coagulant.yield_bonus * amount))
        .duration(duration.intdiv(coagulant.speed_bonus))
        .buildAndRegister()
    }
}

def CoagulationRecipe(amount, duration){
    def COAGULATION_RECIPES = recipemap("coagulation_tank")

    COAGULATION_RECIPES.recipeBuilder()
    .fluidInputs(fluid('latex') * (1000 * amount))
    .notConsumable(ore('stickIron'))
    .outputs(metaitem('dustLatex') * amount)
    .duration(duration)
    .buildAndRegister()
}

def rubbers = [
    new Rubber('dustLatex', 'Rubber', 4, 40 * 20, 4, false),
    new Rubber('dustCompoundedPolyisoprene', 'Rubber', 8, 225, 8, false),
    new Rubber('dustCompoundedStyreneIsopreneRubber', 'StyreneIsopreneRubber', 4, 30 * 20, 4, false),
    new Rubber('dustCompoundedStyreneButadieneRubber', 'StyreneButadieneRubber', 4, 30 * 20, 4, false)
]

def catalysts = [
    new Catalyst('dustZincite', 2),
    new Catalyst('dustMagnesia', 2)
]

def coagulants = [
    new Coagulant('dustCalciumChloride', 2, 1, 1, false),
    new Coagulant('acetic_acid', 250, 4, 1, true),
    new Coagulant('sulfuric_acid', 125, 2, 1, true)
]

def shapes = [
    new Shape('plate', 1),
    new Shape('ring', 4),
    new Shape('foil', 4),
    new Shape('ingot', 1),
    new Shape('pipeTinyFluid', 'pipe.tiny', 2)
]

for (rubber in rubbers) {
    for (shape in shapes) {
        if (oreDict.getItems(shape.name + rubber.output).size() == 0) {
            continue;
        }
        if (rubber.isFluid)  {
            VULCANIZER.recipeBuilder()
                .fluidInputs(fluid(rubber.name) * rubber.amount_required * 1000)
                .inputs(ore('dustSulfur'))
                .notConsumable(metaitem('shape.extruder.' + shape.shapeName))
                .circuitMeta(2)
                .outputs(ore(shape.name + rubber.output)[0] * (rubber.yield * shape.yield))
                .duration(rubber.duration)
                .EUt(7)
                .buildAndRegister()
        } 
        else {
            VULCANIZER.recipeBuilder()
                .inputs(ore(rubber.name) * rubber.amount_required)
                .inputs(ore('dustSulfur'))
                .notConsumable(metaitem('shape.extruder.' + shape.shapeName))
                .circuitMeta(2)
                .outputs(ore(shape.name + rubber.output)[0] * (rubber.yield * shape.yield))
                .duration(rubber.duration)
                .EUt(7)
                .buildAndRegister()
        }
        for (catalyst in catalysts) {
            if (rubber.isFluid)  {
                VULCANIZER.recipeBuilder()
                    .fluidInputs(fluid(rubber.name) * rubber.amount_required * 1000)
                    .inputs(ore('dustSulfur'))
                    .notConsumable(metaitem('shape.extruder.' + shape.shapeName))
                    .notConsumable(ore(catalyst.name))
                    .outputs(ore(shape.name + rubber.output)[0] * (rubber.yield * shape.yield))
                    .duration(rubber.duration.intdiv(catalyst.speed_bonus))
                    .EUt(7)
                    .buildAndRegister()
            } 
            else {
                VULCANIZER.recipeBuilder()
                    .inputs(ore(rubber.name) * rubber.amount_required)
                    .inputs(ore('dustSulfur'))
                    .notConsumable(metaitem('shape.extruder.' + shape.shapeName))
                    .notConsumable(ore(catalyst.name))
                    .outputs(ore(shape.name + rubber.output)[0] * (rubber.yield * shape.yield))
                    .duration(rubber.duration.intdiv(catalyst.speed_bonus))
                    .EUt(7)
                    .buildAndRegister()
            }
        }
    }
}

// Ebonite
// currently there are no liquid rubbers, so can skip fluid check
ebonite_duration = 400

for (rubber in rubbers) {
    coal_amount = (rubber.amount_required > 4) ? rubber.amount_required.intdiv(4) : 1
    MIXER.recipeBuilder()
        .inputs(ore(rubber.name) * rubber.amount_required)
        .inputs(ore('dustSulfur') * rubber.amount_required)
        .inputs(ore('dustCoal') * coal_amount)
        .outputs(metaitem('dustCompoundedEbonite') * (rubber.yield * 2))
        .duration(20 * rubber.yield)
        .EUt(Globals.voltAmps[1])
        .buildAndRegister()
}

VULCANIZER.recipeBuilder()
    .inputs(ore('dustCompoundedEbonite'))
    .notConsumable(metaitem('shape.mold.plate'))
    .circuitMeta(2)
    .outputs(metaitem('plateEbonite'))
    .duration(ebonite_duration)
    .EUt(Globals.voltAmps[1])
    .buildAndRegister()
for (catalyst in catalysts) {
    VULCANIZER.recipeBuilder()
        .inputs(ore('dustCompoundedEbonite'))
        .notConsumable(metaitem('shape.mold.plate'))
        .notConsumable(ore(catalyst.name))
        .outputs(metaitem('plateEbonite'))
        .duration(ebonite_duration.intdiv(catalyst.speed_bonus))
        .EUt(Globals.voltAmps[1])
        .buildAndRegister()
}

CoagulationRecipe(1, 150)

for (coagulant in coagulants) {
    CoagulationRecipe(coagulant, 1, 150)
}

// Polyisoprene

POLYMERIZATION_TANK.recipeBuilder()
    .fluidInputs(fluid('isoprene') * 1000)
    .outputs(metaitem('dustPolyisoprene') * 4)
    .notConsumable(metaitem('dustZieglerNattaCatalyst'))
    .duration(200)
    .EUt(120)
    .buildAndRegister()

POLYMERIZATION_TANK.recipeBuilder()
    .fluidInputs(fluid('isoprene') * 1000)
    .outputs(metaitem('dustPolyisoprene') * 6)
    .notConsumable(fluid('butyllithium') * 100)
    .duration(200)
    .EUt(120)
    .buildAndRegister()

POLYMERIZATION_TANK.recipeBuilder()
    .fluidInputs(fluid('purified_isoprene') * 1000)
    .outputs(metaitem('dustPolyisoprene') * 6)
    .notConsumable(metaitem('dustZieglerNattaCatalyst'))
    .duration(200)
    .EUt(120)
    .buildAndRegister()

POLYMERIZATION_TANK.recipeBuilder()
    .fluidInputs(fluid('purified_isoprene') * 1000)
    .outputs(metaitem('dustPolyisoprene') * 8)
    .notConsumable(fluid('butyllithium') * 100)
    .duration(200)
    .EUt(120)
    .buildAndRegister()

MIXER.recipeBuilder()
    .inputs(ore('dustPolyisoprene') * 8)
    .inputs(ore('dustCarbonBlack') * 2)
    .outputs(metaitem('dustCompoundedPolyisoprene') * 10)
    .EUt(120)
    .duration(250)
    .buildAndRegister()

// Styrene-Isoprene rubber

MIXER.recipeBuilder()
    .fluidInputs(fluid('styrene') * 1000)
    .fluidInputs(fluid('isoprene') * 3000)
    .fluidInputs(fluid('ethanol') * 1000)
    .fluidOutputs(fluid('styrene_isoprene_solution') * 5000)
    .EUt(60)
    .duration(200)
    .buildAndRegister()

POLYMERIZATION.recipeBuilder()
    .fluidInputs(fluid('styrene_isoprene_solution') * 5000)
    .notConsumable(fluid('butyllithium') * 100)
    .fluidOutputs(fluid('polymerized_styrene_isoprene_solution') * 5000)
    .EUt(120)
    .duration(533)
    .buildAndRegister()

DRYER.recipeBuilder()
    .fluidInputs(fluid('polymerized_styrene_isoprene_solution') * 5000)
    .fluidOutputs(fluid('ethanol') * 1000)
    .outputs(metaitem('dustRawStyreneIsopreneRubber') * 16)
    .EUt(120)
    .duration(533)
    .buildAndRegister()

MIXER.recipeBuilder()
    .inputs(ore('dustRawStyreneIsopreneRubber') * 8)
    .inputs(ore('dustCarbonBlack') * 2)
    .outputs(metaitem('dustCompoundedStyreneIsopreneRubber') * 10)
    .EUt(120)
    .duration(250)
    .buildAndRegister()

// Styrene-Butadiene rubber

MIXER.recipeBuilder()
    .fluidInputs(fluid('styrene') * 1000)
    .fluidInputs(fluid('butadiene') * 3000)
    .fluidInputs(fluid('ethanol') * 1000)
    .fluidOutputs(fluid('styrene_butadiene_solution') * 5000)
    .EUt(60)
    .duration(200)
    .buildAndRegister()

POLYMERIZATION.recipeBuilder()
    .fluidInputs(fluid('styrene_butadiene_solution') * 5000)
    .notConsumable(fluid('butyllithium') * 100)
    .fluidOutputs(fluid('polymerized_styrene_butadiene_solution') * 5000)
    .EUt(120)
    .duration(533)
    .buildAndRegister()

DRYER.recipeBuilder()
    .fluidInputs(fluid('polymerized_styrene_butadiene_solution') * 5000)
    .fluidOutputs(fluid('ethanol') * 1000)
    .outputs(metaitem('dustRawStyreneButadieneRubber') * 16)
    .EUt(120)
    .duration(533)
    .buildAndRegister()

MIXER.recipeBuilder()
    .inputs(ore('dustRawStyreneButadieneRubber') * 8)
    .inputs(ore('dustCarbonBlack') * 2)
    .outputs(metaitem('dustCompoundedStyreneButadieneRubber') * 10)
    .EUt(120)
    .duration(250)
    .buildAndRegister()
