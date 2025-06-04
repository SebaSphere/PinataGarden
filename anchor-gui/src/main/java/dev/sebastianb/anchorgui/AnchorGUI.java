package dev.sebastianb.anchorgui;

import dev.sebastianb.anchorgui.demo.GuiDemoRegistry;
import net.fabricmc.api.ModInitializer;

public class AnchorGUI implements ModInitializer {

    public static final String MOD_ID = "anchorgui";

    @Override
    public void onInitialize() {
        System.out.println("Anchoring the GUIs");
        GuiDemoRegistry.register();
    }

}
