package dev.sebastianb.anchorgui.demo.mobradar;

import dev.sebastianb.anchorgui.layout.BaseAnchorPoint;
import dev.sebastianb.anchorgui.layout.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.network.chat.Component;



public class MobRadarPositionElement implements GuiElement {


    @Override
    public BaseAnchorPoint getBaseAnchorPoint() {
        return BaseAnchorPoint.BOTTOM_CENTER;
    }

    @Override
    public AbstractWidget getWidget() {
        // probably gets the right font
        return new MultiLineTextWidget(Component.literal("MEOW TEST"), Minecraft.getInstance().font);
    }

    @Override
    public float getPercantageWidth() {
        return 33.33F; // 33% of the parent element width
    }

    @Override
    public float movePercentageY() {
        return 10.0F; // moves 10% of the parent element height downwards
    }
}
