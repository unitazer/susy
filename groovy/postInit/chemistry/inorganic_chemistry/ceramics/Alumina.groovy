import static prePostInit.Recipemaps.*
import static gregtech.api.GTValues.*
import globals.Sintering

// Medium Alumina Refractory

ERF.recipeBuilder()
    .inputs(ore('dustKyanite'))
    .outputs(metaitem('dustMullitizedKyanite'))
    .EUt(VA[MV])
    .blastFurnaceTemp(1200)
    .duration(100)
    .buildAndRegister()

MIXER.recipeBuilder()
    .inputs(ore('dustMullitizedKyanite') * 2)
    .inputs(ore('dustBauxite'))
    .inputs(ore('dustClay'))
    .outputs(metaitem('dustMediumAluminaRefractory') * 4)
    .EUt(VA[ULV])
    .duration(200)
    .buildAndRegister()

SINTERING_OVEN.recipeBuilder()
    .inputs(metaitem('dustMediumAluminaRefractory'))
    .outputs(metaitem('ingotMediumAluminaRefractory'))
    .EUt(VA[MV])
    .duration(50)
    .buildAndRegister()

crafting.addShaped("susy:advanced_refractory", item('susy:susy_multiblock_casing', 9), [
    [ore('ingotMediumAluminaRefractory'), ore('ingotMediumAluminaRefractory')],
    [ore('ingotMediumAluminaRefractory'), ore('ingotMediumAluminaRefractory')]
])


// Calcium Aluminate Cement

Sintering.plasmaFuels().each { fuel ->
    ROTARY_KILN.recipeBuilder()
            .inputs(ore('dustAlumina'))
            .inputs(ore('dustLimestone'))
            .circuitMeta(1)
            .fluidInputs(fluid(fuel.name) * fuel.amountRequired)
            .outputs(metaitem('hot_cac_clinker'))
            .fluidOutputs(fluid(fuel.byproduct) * fuel.byproductAmount)
            .duration(fuel.duration)
            .EUt(VA[HV])
            .buildAndRegister()
}

Sintering.nonPlasmaFuels().each { fuel ->
    Sintering.comburents.each { comburent ->
        ROTARY_KILN.recipeBuilder()
                .inputs(ore('dustAlumina'))
                .inputs(ore('dustLimestone'))
                .circuitMeta(2)
                .fluidInputs(fluid(fuel.name) * fuel.amountRequired)
                .fluidInputs(fluid(comburent.name) * comburent.amountRequired)
                .outputs(metaitem('hot_cac_clinker'))
                .fluidOutputs(fluid(fuel.byproduct) * fuel.byproductAmount)
                .duration(fuel.duration + comburent.duration)
                .EUt(VA[ULV])
                .buildAndRegister()
    }
}

class CoolantGases {
    String name
    String byproduct
    int amount
    int duration

    CoolantGases(name, byproduct, amount, duration) {
        this.name = name
        this.byproduct = byproduct
        this.amount = amount
        this.duration = duration
    }
}

def gases = [
        new CoolantGases('air', 'hot_air', 100, 40)
]

for (gas in gases) {
    mods.gregtech.mixer.recipeBuilder()
            .inputs(metaitem('hot_cac_clinker'))
            .fluidInputs(fluid(gas.name) * gas.amount)
            .outputs(metaitem('cac_clinker'))
            .fluidOutputs(fluid(gas.byproduct) * gas.amount)
            .duration(gas.duration)
            .EUt(VA[ULV])
            .buildAndRegister()
}

mods.gregtech.macerator.recipeBuilder()
        .inputs(metaitem('cac_clinker'))
        .outputs(metaitem('cac_dust') * 16)
        .duration(20)
        .EUt(VA[ULV])
        .buildAndRegister()

// Reactive Alumina

ERF.recipeBuilder()
        .circuitMeta(2)
        .inputs(ore('dustAluminiumHydroxide') * 14)
        .fluidOutputs(fluid('dense_steam') * 3000)
        .outputs(metaitem('dustReactiveAlumina') * 5)
        .duration(100)
        .blastFurnaceTemp(1300)
        .EUt(VA[HV])
        .buildAndRegister()

// Tabular Alumina

Sintering.RotaryKiln.fuels.each { fuel ->
    Sintering.RotaryKiln.comburents.each { comburent ->
        ROTARY_KILN.recipeBuilder()
            .inputs(ore('dustAlumina'))
            .outputs(metaitem('dustTabularAlumina'))
            .fluidInputs(fluid(fuel.name) * fuel.amountRequired)
            .fluidInputs(fluid(comburent.name) * comburent.amountRequired)
            .fluidOutputs(fluid(fuel.byproduct) * fuel.byproductAmount)
            .duration(fuel.duration + comburent.duration)
            .EUt(VA[HV])
            .buildAndRegister()
    }
}

// High-Alumina Refractory

MACEERATOR.recipeBuilder()
        .inputs(metaitem('cac_clinker'))
        .outputs(metaitem('cac_dust') * 16)
        .duration(20)
        .EUt(VA[ULV])
        .buildAndRegister()

MIXER.recipeBuilder()
        .inputs(ore('dustTabularAlumina') * 11)
        .inputs(ore('dustAlumina') * 3)
        .inputs(ore('dustReactiveAlumina') * 2)
        .inputs(metaitem('cac_dust') * 6)
        .fluidInputs(fluid('water') * 3750)
        .fluidOutputs(fluid('castable_alumina_refractory') * 2880)
        .duration(20)
        .EUt(VA[ULV])
        .buildAndRegister()

SINTERING_OVEN.recipeBuilder()
    .inputs(metaitem('dustHighAluminaRefractory'))
    .outputs(metaitem('ingotHighAluminaRefractory'))
    .EUt(VA[EV])
    .duration(50)
    .buildAndRegister()

crafting.addShaped("susy:advanced_refractory", item('susy:susy_multiblock_casing', 9), [
    [ore('ingotHighAluminaRefractory'), ore('ingotHighAluminaRefractory')],
    [ore('ingotHighAluminaRefractory'), ore('ingotHighAluminaRefractory')]
])