import gregtech.api.GTValues

POLYMERIZATION = recipemap('polymerization_tank')
EXTRUDER = recipemap('extruder')

// Polyethylene

POLYMERIZATION.recipeBuilder()
        .circuitMeta(1)
        .fluidInputs(fluid('ethylene') * 2000)
        .outputs(metaitem('dustPlastic'))
        .EUt(GTValues.VA[GTValues.LV])
        .duration(300)
        .buildAndRegister()

POLYMERIZATION.recipeBuilder()
        .circuitMeta(2)
        .notConsumable(metaitem('dustPhilipsCatalyst'))
        .fluidInputs(fluid('ethylene') * 1500)
        .outputs(metaitem('dustPlastic'))
        .EUt(GTValues.VA[GTValues.LV])
        .duration(150)
        .buildAndRegister()

POLYMERIZATION.recipeBuilder()
        .circuitMeta(3)
        .notConsumable(metaitem('dustZieglerNattaCatalyst'))
        .fluidInputs(fluid('ethylene') * 1250)
        .outputs(metaitem('dustPlastic'))
        .EUt(GTValues.VA[GTValues.LV])
        .duration(75)
        .buildAndRegister()

POLYMERIZATION.recipeBuilder()
        .circuitMeta(4)
        .notConsumable(metaitem('dustKaminskyCatalyst'))
        .fluidInputs(fluid('ethylene') * 1000)
        .outputs(metaitem('dustPlastic'))
        .EUt(GTValues.VA[GTValues.LV])
        .duration(30)
        .buildAndRegister()

// UHMWPE

// Ultra High Molecular Weight Polyethylene (UHMWPE) Plate * 1
mods.gregtech.compressor.removeByInput(2, [metaitem('dustUltraHighMolecularWeightPolyethylene')], null)
// Ultra High Molecular Weight Polyethylene (UHMWPE) Plate * 1
mods.gregtech.fluid_solidifier.removeByInput(7, [metaitem('shape.mold.plate')], [fluid('ultra_high_molecular_weight_polyethylene') * 144])
// Ultra High Molecular Weight Polyethylene (UHMWPE) * 144
mods.gregtech.extractor.removeByInput(30, [metaitem('plateUltraHighMolecularWeightPolyethylene')], null)

POLYMERIZATION.recipeBuilder()
        .circuitMeta(5)
        .notConsumable(metaitem('dustZieglerNattaCatalyst'))
        .fluidInputs(fluid('ethylene') * 1000)
        .chancedOutput(metaitem('dustUltraHighMolecularWeightPolyethylene'), 5000, 0)
        .EUt(GTValues.VA[GTValues.EV])
        .duration(20)
        .buildAndRegister()

POLYMERIZATION.recipeBuilder()
        .circuitMeta(6)
        .notConsumable(metaitem('dustKaminskyCatalyst'))
        .fluidInputs(fluid('ethylene') * 1000)
        .outputs(metaitem('dustUltraHighMolecularWeightPolyethylene'))
        .EUt(GTValues.VA[GTValues.EV])
        .duration(20)
        .buildAndRegister()

EXTRUDER.recipeBuilder()
        .notConsumable(metaitem('spinneret'))
        .fluidInputs(fluid('ultra_high_molecular_weight_polyethylene') * 144)
        .outputs(metaitem('fiberUltraHighMolecularWeightPolyethylene') * 8)
        .EUt(GTValues.VA[GTValues.HV])
        .duration(20)
        .buildAndRegister()

// Polypropylene

POLYMERIZATION.recipeBuilder()
        .circuitMeta(1)
        .notConsumable(metaitem('dustPhilipsCatalyst'))
        .fluidInputs(fluid('propene') * 2000)
        .outputs(metaitem('dustPolypropylene'))
        .EUt(GTValues.VA[GTValues.LV])
        .duration(500)
        .buildAndRegister()

POLYMERIZATION.recipeBuilder()
        .circuitMeta(2)
        .notConsumable(metaitem('dustZieglerNattaCatalyst'))
        .fluidInputs(fluid('propene') * 1500)
        .outputs(metaitem('dustPolypropylene'))
        .EUt(GTValues.VA[GTValues.LV])
        .duration(200)
        .buildAndRegister()

POLYMERIZATION.recipeBuilder()
        .circuitMeta(3)
        .notConsumable(metaitem('dustKaminskyCatalyst'))
        .fluidInputs(fluid('propene') * 1000)
        .outputs(metaitem('dustPolypropylene'))
        .EUt(GTValues.VA[GTValues.LV])
        .duration(50)
        .buildAndRegister()