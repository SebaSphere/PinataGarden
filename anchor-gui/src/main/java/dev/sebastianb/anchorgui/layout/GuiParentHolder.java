package dev.sebastianb.anchorgui.layout;

public class GuiParentHolder {

    private GuiElement parent;

    public GuiParentHolder() {

    }

    public void setParent(GuiElement parent) {
        this.parent = parent;
    }

    public GuiElement getParent() {
        return parent;
    }
}
