package dev.sebastianb.anchorgui.util;

import dev.sebastianb.anchorgui.layout.GuiElement;

import java.util.ArrayList;

public class GuiScreenInjector {

    private static ArrayList<GuiElement> guiElements = new ArrayList<>();

    public static ArrayList<GuiElement> getGuiElements() {
        return guiElements;
    }

    public static void inject(GuiElement baseGuiElement) {
        guiElements.add(baseGuiElement);
    }

}
