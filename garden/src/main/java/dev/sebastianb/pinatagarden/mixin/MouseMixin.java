package dev.sebastianb.pinatagarden.mixin;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {

    @Shadow private boolean mouseGrabbed;

    @Shadow private boolean ignoreFirstMove;

    @Shadow @Final private Minecraft minecraft;

    @Shadow private double xpos;

    @Shadow private double ypos;

    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    public void injectTurnPlayer(double timeDelta, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "releaseMouse", at = @At("HEAD"), cancellable = true)
    public void injectMouseButton(CallbackInfo ci) {
        xpos = minecraft.getWindow().getWidth() / 2.0;
        ypos = minecraft.getWindow().getHeight() / 2.0;
        InputConstants.grabOrReleaseMouse(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR_NORMAL, xpos, ypos);
    }

    @Inject(method = "grabMouse", at = @At("HEAD"), cancellable = true)
    public void injectMouseGrab(CallbackInfo ci) {
        InputConstants.grabOrReleaseMouse(minecraft.getWindow().getWindow(), GLFW.GLFW_CURSOR_NORMAL, xpos, ypos);
    }

}
