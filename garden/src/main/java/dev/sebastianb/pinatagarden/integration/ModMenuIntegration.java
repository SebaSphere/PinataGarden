package dev.sebastianb.pinatagarden.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {



    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        System.out.println("TEAST");
        return parent -> null;
    }

}
