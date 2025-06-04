package dev.sebastianb.anchorgui.mixin_duck;

import net.minecraft.client.gui.components.AbstractWidget;

public class CurrentWidgetHolder {

    // this is such a hack
    private static AbstractWidget currentWidget;

    public static void setCurrentWidget(AbstractWidget widget) {
        CurrentWidgetHolder.currentWidget = widget;
    }

    public static AbstractWidget getCurrentWidget() {
        return currentWidget;
    }

}
