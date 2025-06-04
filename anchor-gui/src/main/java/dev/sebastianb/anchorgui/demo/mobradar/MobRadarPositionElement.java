package dev.sebastianb.anchorgui.demo.mobradar;

import dev.sebastianb.anchorgui.AnchorGUI;
import dev.sebastianb.anchorgui.layout.BaseAnchorPoint;
import dev.sebastianb.anchorgui.layout.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


public class MobRadarPositionElement implements GuiElement {

    ResourceLocation BASE_MOD_RADAR = ResourceLocation.fromNamespaceAndPath(AnchorGUI.MOD_ID, "textures/gui/base_mob_searcher_odometer.png");


    @Override
    public BaseAnchorPoint getBaseAnchorPoint() {
        return BaseAnchorPoint.BOTTOM_CENTER;
    }

    @Override
    public AbstractWidget getWidget() {
        // probably gets the right font
        return new StringWidget(Component.literal("Mob Radar Position"), Minecraft.getInstance().font);
    }

    @Override
    public float getPercentageWidth() {
        return 33.33F; // 33% of the parent element width
    }

    @Override
    public float movePercentageY() {
        return 10.0F; // moves 10% of the parent element height downwards
    }
}
