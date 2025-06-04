package dev.sebastianb.anchorgui.layout;

import net.minecraft.client.gui.components.AbstractWidget;

import java.util.List;

public interface GuiElement {

    // don't touch this, it's used by the mod internally to set the parent
    default GuiParentHolder getParent() {
        return new GuiParentHolder();
    }

    // the anchor point we're going to base things off
    default BaseAnchorPoint getBaseAnchorPoint() {
        return BaseAnchorPoint.TOP_LEFT;
    }


    AbstractWidget getWidget(); // this is the vanilla widget that's a representation. You should be passing 0,0 into the position (unless you want to move absolute pixel value) to let the mod handle positioning

    // gets the relative width of the element
    float getPercentageWidth();


    // this is -1 since we want the height to autoscale based off the width. We can override this if we want differently
    default float getPercentageHeight() {
        return -1;
    }

    // used tp get the children of this element.
    default List<GuiElement> getChildren() {
        return List.of();
    }


    // these methods are used for how the specific element moves relative to the parent element
    default float movePercentageX() {
        return 0;
    }

    default float movePercentageY() {
        return 0;
    }

}
