package dev.sebastianb.anchorgui.mixin;


import dev.sebastianb.anchorgui.mixin_duck.ScreenDuck;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin implements ScreenDuck {


    @Shadow @Final private List<Renderable> renderables;

    @Inject(method = "render", at = @At("TAIL"))
    public void injectRender(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
//
//        System.out.println("TEST INJECT RENDER");
//        System.out.println(new Throwable().getStackTrace()[6].getClassName() + "::" + new Throwable().getStackTrace()[6].getMethodName());

    }

    @Override
    public void addRenderable(Renderable renderable) {
        renderables.add(renderable);
    }

    @Override
    public List<Renderable> getRenderables() {
        return renderables;
    }

    @Override
    public void removeRenderable(Renderable renderable) {
        renderables.remove(renderable);
    }
}
