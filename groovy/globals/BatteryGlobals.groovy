package globals

import groovy.transform.TupleConstructor

import baubles.api.BaubleType
import gregtech.integration.baubles.BaubleBehavior
import gregtech.api.GregTechAPI
import gregtech.api.GTValues
import gregtech.api.capability.GregtechCapabilities
import gregtech.api.capability.IElectricItem
import gregtech.api.capability.impl.ElectricItem
import gregtech.api.items.metaitem.ElectricStats
import gregtech.api.items.metaitem.StandardMetaItem
import gregtech.api.unification.material.MarkerMaterials
import gregtech.api.unification.ore.OrePrefix

class BatteryGlobals {
    @TupleConstructor
    public static class Battery {
        String name
        int tier
        long capacity

        public String fetchMetaname() { "battery.$name" }
        public ItemStack fetchMetaitem() { metaitem(fetchMetaname()) }

        public ItemStack imprintCapacity(ItemStack tool) {
            IElectricItem eiTool = tool?.getCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null)
            eiTool?.setMaxChargeOverride(capacity)
            return tool
        }

        public void register(StandardMetaItem customMetaItem, int metaId) {
            def getMarkerTier = { int t ->
                switch (t) {
                    case GTValues.LV: return MarkerMaterials.Tier.LV
                    case GTValues.MV: return MarkerMaterials.Tier.MV
                    case GTValues.HV: return MarkerMaterials.Tier.HV
                    case GTValues.EV: return MarkerMaterials.Tier.EV
                    case GTValues.IV: return MarkerMaterials.Tier.IV
                    // continue when needed
                    default: return MarkerMaterials.Tier.LV
                }
            }

            customMetaItem.addItem(metaId, fetchMetaname())
            .addComponents(ElectricStats.createRechargeableBattery(capacity, tier), new BaubleBehavior(BaubleType.TRINKET))
            .setUnificationData(OrePrefix.battery, getMarkerTier(tier))
            .setModelAmount(8)
            .setCreativeTabs(GregTechAPI.TAB_GREGTECH_TOOLS)
        }
    }

    public static batteries = [
        new Battery('lead_acid', GTValues.LV, 80000L),
        new Battery('ni_fe', GTValues.MV, 320000L)
    ]

    public static def byName(String name) { batteries.find { it.name == name } }
    public static def byTier(int tier) { batteries.findAll { it.tier == tier } }
}
