package dev.sebastianb.pinatagarden.client;

import dev.sebastianb.pinatagarden.client.gui.GuiAdditionManager;
import dev.sebastianb.pinatagarden.client.gui.util.GuiScreenSetup;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;

public class PinataGardenClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        GuiAdditionManager.register();

    }



}
