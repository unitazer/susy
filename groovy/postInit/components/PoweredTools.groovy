import globals.Globals
import postInit.utils.RecyclingHelper

log.infoMC("Running PoweredTools.groovy...")

ASSEMBLER = recipemap('assembler')

// Item Magnet with Lead Acid battery

crafting.shapedBuilder()
    .name('gregtech:lv_magnet_lead_acid')
    .output(metaitem('item_magnet.lv').withNbt(['MaxCharge': Globals.batteryCapacities['lead_acid']]))
    .shape([
        [ore('stickSteelMagnetic'), ore('toolWrench'), ore('stickSteelMagnetic')],
        [ore('stickSteelMagnetic'), metaitem('battery.lead_acid').mark('battery'), ore('stickSteelMagnetic')],
        [ore('cableGtSingleTin'), ore('plateSteel'), ore('cableGtSingleTin')]
    ])
    .recipeFunction { output, inputs, info ->
        def batteryTag = inputs['battery']?.getTagCompound()
        if (batteryTag != null) {
            output.getTagCompound().setLong("Charge", batteryTag.getLong("Charge"))
        }
    }
    .register()

// Power Unit with Lead Acid Battery

crafting.shapedBuilder()
    .name('gregtech:lv_power_unit_lead_acid')
    .output(metaitem('power_unit.lv').withNbt(['MaxCharge': Globals.batteryCapacities['lead_acid']]))
    .shape([
        [ore('screwSteel'), null, ore('toolScrewdriver')],
        [ore('gearSmallSteel'), metaitem('electric.motor.lv'), ore('gearSmallSteel')],
        [ore('plateSteel'), metaitem('battery.lead_acid').mark('battery'), ore('plateSteel')]
    ])
    .recipeFunction { output, inputs, info ->
        def batteryTag = inputs['battery']?.getTagCompound()
        if (batteryTag != null) {
            output.getTagCompound().setLong("Charge", batteryTag.getLong("Charge"))
        }
    }
    .register()

// Prospector's Scanner with Lead Acid battery

crafting.shapedBuilder()
        .name("gregtech:prospector_lead_acid")
        .output(metaitem('prospector.lv').withNbt(['MaxCharge': Globals.batteryCapacities['lead_acid']]))
        .shape([
    [metaitem('emitter.lv'), ore('plateSteel'), metaitem('sensor.lv')],
        [ore('circuitLv'), ore('plateGlass'), ore('circuitLv')],
        [ore('plateSteel'), metaitem('battery.lead_acid').mark('battery'), ore('plateSteel')]
    ])
    .recipeFunction { output, inputs, info ->
        def batteryTag = inputs['battery']?.getTagCompound()
        if (batteryTag != null) {
            output.getTagCompound().setLong("Charge", batteryTag.getLong("Charge"))
        }
    }
    .register()

// NightVision Goggles with other batteries

crafting.shapedBuilder()
    .name('gregtech:nightvision_lead_acid')
    .output(metaitem('nightvision_goggles').withNbt(['MaxCharge': Globals.batteryCapacities['lead_acid']]))
    .shape([
        [ore('circuitUlv'), metaitem('screwSteel'), ore('circuitUlv')],
        [metaitem('ringRubber'), metaitem('battery.lead_acid').mark('battery'), metaitem('ringRubber')],
        [metaitem('lensGlass'), ore('toolScrewdriver'), metaitem('lensGlass')]
    ])
    .recipeFunction { output, inputs, info ->
        def batteryTag = inputs['battery']?.getTagCompound()
        if (batteryTag != null) {
            output.getTagCompound().setLong("Charge", batteryTag.getLong("Charge"))
        }
    }
    .register()

//Power Units

ASSEMBLER.recipeBuilder()
    .inputs(ore('gearSmallSteel') * 2)
    .inputs(ore('screwSteel'))
    .inputs(ore('plateSteel') * 2)
    .inputs(metaitem('electric.motor.lv'))
    .inputs(metaitem('battery.lead_acid'))
    .outputs(metaitem('power_unit.lv').withNbt(['MaxCharge': Globals.batteryCapacities['lead_acid']]))
    .EUt(30)
    .duration(150)
    .buildAndRegister()

ASSEMBLER.recipeBuilder()
    .inputs(ore('gearSmallAluminium') * 2)
    .inputs(ore('screwAluminium'))
    .inputs(ore('plateAluminium') * 2)
    .inputs(metaitem('electric.motor.mv'))
    .inputs(ore('batteryMv'))
    .outputs(metaitem('power_unit.mv'))
    .EUt(120)
    .duration(150)
    .buildAndRegister()

ASSEMBLER.recipeBuilder()
    .inputs(ore('gearSmallStainlessSteel') * 2)
    .inputs(ore('screwStainlessSteel'))
    .inputs(ore('plateStainlessSteel') * 2)
    .inputs(metaitem('electric.motor.hv'))
    .inputs(ore('batteryHv'))
    .outputs(metaitem('power_unit.hv'))
    .EUt(480)
    .duration(150)
    .buildAndRegister()

ASSEMBLER.recipeBuilder()
    .inputs(ore('gearSmallTitanium') * 2)
    .inputs(ore('screwTitanium'))
    .inputs(ore('plateTitanium') * 2)
    .inputs(metaitem('electric.motor.ev'))
    .inputs(ore('batteryEv'))
    .outputs(metaitem('power_unit.ev'))
    .EUt(1920)
    .duration(150)
    .buildAndRegister()

ASSEMBLER.recipeBuilder()
    .inputs(ore('gearSmallTungstenSteel') * 2)
    .inputs(ore('screwTungstenSteel'))
    .inputs(ore('plateTungstenSteel') * 2)
    .inputs(metaitem('electric.motor.iv'))
    .inputs(ore('batteryIv'))
    .outputs(metaitem('power_unit.iv'))
    .EUt(7680)
    .duration(150)
    .buildAndRegister()
