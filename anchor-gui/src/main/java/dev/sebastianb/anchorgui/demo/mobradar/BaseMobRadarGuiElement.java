package dev.sebastianb.anchorgui.demo.mobradar;

import dev.sebastianb.anchorgui.AnchorGUI;
import dev.sebastianb.anchorgui.layout.BaseAnchorPoint;
import dev.sebastianb.anchorgui.layout.GuiElement;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

// example demo element, we're gonna attempt a mob radar
public class BaseMobRadarGuiElement implements GuiElement {

    ResourceLocation BASE_MOD_RADAR = ResourceLocation.fromNamespaceAndPath(AnchorGUI.MOD_ID, "textures/gui/base_mob_searcher_odometer.png");

    @Override
    public BaseAnchorPoint getBaseAnchorPoint() {
        return BaseAnchorPoint.CENTER_LEFT;
    }

    @Override
    public AbstractWidget getWidget() {
        // we put 0 into the position x/y, width/height respectively as this will be handled by the mod
        // TODO: determine what happens if we put our own values, I'd assume if would be our own positions?
        return ImageWidget.texture(100,100, BASE_MOD_RADAR, 100, 100);
    }

    @Override
    public float getPercentageWidth() {
        return 20.0F; // 20% of the screen width
    }

    @Override
    public List<GuiElement> getChildren() {



        return GuiElement.super.getChildren();
    }
}
