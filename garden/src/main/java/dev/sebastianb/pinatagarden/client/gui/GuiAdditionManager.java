package dev.sebastianb.pinatagarden.client.gui;

import dev.sebastianb.pinatagarden.client.gui.garden.MainGardenLayer;
import dev.sebastianb.pinatagarden.client.gui.util.GuiScreenSetup;
import net.minecraft.client.Minecraft;

public class GuiAdditionManager {


    public static void register() {

        // takes in the layer, which contains a set of renders to apply and a boolean supplier which determines whether to render or not
        // in this case, we want to render the garden layer if the gui is not hidden (like the f1 keybind)
        GuiScreenSetup.addLayer(new MainGardenLayer(), () -> !Minecraft.getInstance().options.hideGui);


    }

}
