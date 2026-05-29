import globals.Globals

import static prePostInit.Recipemaps.*
import static gregtech.api.GTValues.*

def EDMDielectricFluids = [
        [fluid: fluid('lubricating_oil'), duration: 6],
        [fluid: fluid('polychlorinated_biphenyl'), duration: 3],
        [fluid: fluid('ester_base_oil'), duration: 2],
        [fluid: fluid('polyalphaolefin'), duration: 1]
]

def mapRange = { idIn, idOut, range ->
    range.collect { meta ->
        [input: item(idIn, meta), output: item(idOut, meta)]
    }
}



EDMDielectricFluids.each { DielectricFluid ->
    EDM.recipeBuilder()
            .inputs(metaitem('plateDoubleVanadiumSteel') * 2)
            .fluidInputs(DielectricFluid.fluid * (DielectricFluid.duration * 1000))//the parentheses around the duration * 1000 are necessary
            .outputs(metaitem('turbine_blade_shape_die'))
            .circuitMeta(1)
            .duration(250 * DielectricFluid.duration)
            .EUt(VA[EV])
            .buildAndRegister()

    EDM.recipeBuilder()
            .inputs(metaitem('plateDoubleVanadiumSteel') * 2)
            .fluidInputs(DielectricFluid.fluid * (DielectricFluid.duration * 1000))
            .outputs(metaitem('turbine_blade_core_die'))
            .circuitMeta(2)
            .duration(250 * DielectricFluid.duration)
            .EUt(VA[EV])
            .buildAndRegister()

    EDM.recipeBuilder()
            .inputs(metaitem('milled_gas_turbine_blade'))
            .fluidInputs(DielectricFluid.fluid * (DielectricFluid.duration * 500))
            .outputs(metaitem('surface_finished_gas_turbine_blade'))
            .duration(150 * DielectricFluid.duration)
            .EUt(VA[EV])
            .buildAndRegister()

    EDM.recipeBuilder()
            .inputs(metaitem('milled_low_pressure_steam_turbine_blade'))
            .fluidInputs(DielectricFluid.fluid * (DielectricFluid.duration * 500))
            .outputs(metaitem('low_pressure_steam_turbine_blade'))
            .duration(150 * DielectricFluid.duration)
            .EUt(VA[EV])
            .buildAndRegister()

    EDM.recipeBuilder()
            .inputs(metaitem('milled_high_pressure_steam_turbine_blade'))
            .fluidInputs(DielectricFluid.fluid * (DielectricFluid.duration * 500))
            .outputs(metaitem('high_pressure_steam_turbine_blade'))
            .duration(150 * DielectricFluid.duration)
            .EUt(VA[EV])
            .buildAndRegister()
}

