package dev.sebastianb.pinatagarden.client;

import dev.sebastianb.pinatagarden.client.gui.GuiAdditionManager;
import net.fabricmc.api.ClientModInitializer;

public class PinataGardenClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        GuiAdditionManager.register();

    }



}
