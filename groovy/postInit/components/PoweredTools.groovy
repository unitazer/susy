import globals.Globals
import postInit.utils.RecyclingHelper

import gregtech.api.capability.GregtechCapabilities
import gregtech.api.capability.IElectricItem
import gregtech.api.capability.impl.ElectricItem

log.infoMC("Running PoweredTools.groovy...")

ASSEMBLER = recipemap('assembler')

ItemStack withMaxChargeFromBatteryItemStack(ItemStack tool, ItemStack battery) {
    IElectricItem eiTool = tool?.getCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null)
    IElectricItem eiBattery = battery?.getCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null)
    if (eiTool == null || eiBattery == null)
        return tool

    eiTool.setMaxChargeOverride(eiBattery.getMaxCharge())
    return tool
}

ItemStack withMaxChargeFromBattery(String tool, String battery) {
    return withMaxChargeFromBatteryItemStack(metaitem(tool), metaitem(battery))
}

def setChargeFromBatteryFn = { output, inputs, info ->
    def batteryTag = inputs['battery']?.getTagCompound()
    if (batteryTag != null) {
        output.getTagCompound().setLong("Charge", batteryTag.getLong("Charge"))
    }
}

// Lead Acid powered items
({ battery_type ->
    def battery = "battery.$battery_type"

    // Item Magnet
    crafting.shapedBuilder()
        .name("gregtech:lv_magnet_$battery_type")
        .output(withMaxChargeFromBattery('item_magnet.lv', battery))
        .shape([
            [ore('stickSteelMagnetic'), ore('toolWrench'), ore('stickSteelMagnetic')],
            [ore('stickSteelMagnetic'), metaitem(battery).mark('battery'), ore('stickSteelMagnetic')],
            [ore('cableGtSingleTin'), ore('plateSteel'), ore('cableGtSingleTin')]
        ])
        .recipeFunction(setChargeFromBatteryFn)
        .register()

    // Power Unit (manual craft)
    crafting.shapedBuilder()
        .name("gregtech:lv_power_unit_$battery_type")
        .output(withMaxChargeFromBattery('power_unit.lv', battery))
        .shape([
            [ore('screwSteel'), null, ore('toolScrewdriver')],
            [ore('gearSmallSteel'), metaitem('electric.motor.lv'), ore('gearSmallSteel')],
            [ore('plateSteel'), metaitem(battery).mark('battery'), ore('plateSteel')]
        ])
        .recipeFunction(setChargeFromBatteryFn)
        .register()

    // Prospector's Scanner
    crafting.shapedBuilder()
        .name("gregtech:prospector_$battery_type")
        .output(withMaxChargeFromBattery('prospector.lv', battery))
        .shape([
            [metaitem('emitter.lv'), ore('plateSteel'), metaitem('sensor.lv')],
            [ore('circuitLv'), ore('plateGlass'), ore('circuitLv')],
            [ore('plateSteel'), metaitem(battery).mark('battery'), ore('plateSteel')]
        ])
        .recipeFunction(setChargeFromBatteryFn)
        .register()

    // NightVision Goggles
    crafting.shapedBuilder()
        .name("gregtech:nightvision_$battery_type")
        .output(withMaxChargeFromBattery('nightvision_goggles', battery))
        .shape([
            [ore('circuitUlv'), metaitem('screwSteel'), ore('circuitUlv')],
            [metaitem('ringRubber'), metaitem(battery).mark('battery'), metaitem('ringRubber')],
            [metaitem('lensGlass'), ore('toolScrewdriver'), metaitem('lensGlass')]
        ])
        .recipeFunction(setChargeFromBatteryFn)
        .register()
}).call('lead_acid')

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
