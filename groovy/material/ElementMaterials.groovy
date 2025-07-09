package material;

import static material.SuSyMaterials.*;

import gregtech.api.unification.material.Material;
import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.properties.*

import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.Materials.*;

public class ElementMaterials {
    public static void register() {
        log.infoMC("Registering Element Materials!");
        
        

        ElectrodepositedCopper = new Material.Builder(8722, SuSyUtility.susyId('electrodeposited_copper'))
                .ingot(1)
                .color(0xFF8000)
                .iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .components(Copper)
                .build();


    }
}
