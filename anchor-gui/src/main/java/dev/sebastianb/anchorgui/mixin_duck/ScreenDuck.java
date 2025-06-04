package dev.sebastianb.anchorgui.mixin_duck;

import net.minecraft.client.gui.components.Renderable;

import java.util.List;

public interface ScreenDuck {

    void addRenderable(Renderable renderable);
    List<Renderable> getRenderables();
    void removeRenderable(Renderable renderable);

}
