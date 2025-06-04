package dev.sebastianb.anchorgui.demo;

import dev.sebastianb.anchorgui.demo.mobradar.BaseMobRadarGuiElement;
import dev.sebastianb.anchorgui.demo.mobradar.MobRadarPositionElement;
import dev.sebastianb.anchorgui.util.GuiScreenInjector;
import net.minecraft.client.gui.components.ImageWidget;

public class GuiDemoRegistry {





    public static void register() {
        // we shouldn't inject child elements as they'll repeat but this is just a test
        // GuiScreenInjector.inject(new MobRadarPositionElement());

        // injects a base GUI element into the lever render gui
        GuiScreenInjector.inject(new BaseMobRadarGuiElement());
    }

}
