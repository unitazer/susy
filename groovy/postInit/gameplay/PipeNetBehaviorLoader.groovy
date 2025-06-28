package postInit.gameplay

import preInit.PipeNetWalkerBehaviour
import gregtech.common.items.ToolItems

// Register PipeNetWalkerBehaviours for wrenchs & wire cutters
[
        ToolItems.WRENCH.toolStats,
        ToolItems.WRENCH_LV.toolStats,
        ToolItems.WRENCH_HV.toolStats,
        ToolItems.WRENCH_IV.toolStats,
        ToolItems.WIRE_CUTTER.toolStats,
        ToolItems.WIRECUTTER_LV.toolStats,
        ToolItems.WIRECUTTER_HV.toolStats,
        ToolItems.WIRECUTTER_IV.toolStats,
].each { defination ->
    defination.metaClass.delegate.makeMutable('behaviors')
    defination.@behaviors = defination.@behaviors.collect().with {
        add 0, PipeNetWalkerBehaviour.instance
        asImmutable()
    }
}

