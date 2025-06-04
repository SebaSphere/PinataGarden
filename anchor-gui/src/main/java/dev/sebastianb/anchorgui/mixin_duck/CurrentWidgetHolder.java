package dev.sebastianb.anchorgui.mixin_duck;

import dev.sebastianb.anchorgui.layout.GuiElement;
import net.minecraft.client.gui.components.AbstractWidget;

public class CurrentWidgetHolder {

    // this is such a hack
    private static AbstractWidget currentWidget;
    private static GuiElement currentGuiElement;

    public static void setCurrentWidget(AbstractWidget widget, GuiElement guiElement) {
        CurrentWidgetHolder.currentWidget = widget;
        CurrentWidgetHolder.currentGuiElement = guiElement;
    }

    public static AbstractWidget getCurrentWidget() {
        return currentWidget;
    }
    public static GuiElement getCurrentGuiElement() {
        return currentGuiElement;
    }

}
