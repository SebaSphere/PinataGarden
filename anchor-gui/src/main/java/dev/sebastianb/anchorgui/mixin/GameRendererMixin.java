package dev.sebastianb.anchorgui.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import dev.sebastianb.anchorgui.AnchorGUI;
import dev.sebastianb.anchorgui.layout.GuiElement;
import dev.sebastianb.anchorgui.mixin_duck.CurrentWidgetHolder;
import dev.sebastianb.anchorgui.mixin_duck.ScreenDuck;
import dev.sebastianb.anchorgui.util.GuiScreenInjector;
import dev.sebastianb.anchorgui.util.InstanceTracker;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectInit(Minecraft minecraft, ItemInHandRenderer itemInHandRenderer, ResourceManager resourceManager, RenderBuffers renderBuffers, CallbackInfo ci) {

    }


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void injectRender(DeltaTracker deltaTracker, boolean bl, CallbackInfo ci, ProfilerFiller profilerFiller, boolean bl2, int i, int j, Window window, RenderTarget renderTarget, Matrix4f matrix4f, Matrix4fStack matrix4fStack, GuiGraphics guiGraphics) {


        for (GuiElement guiElement : GuiScreenInjector.getGuiElements()) {
            var widget = guiElement.getWidget();
            widget.active = true;


            guiGraphics.pose().pushPose();

            // TODO: eventually we need to handle input, we're going to abuse the blit render location to bypass with our own system for handling

            // just before rendering, set the object reference for blitting
            CurrentWidgetHolder.setCurrentWidget(widget, guiElement); // im concerned

            InstanceTracker.setCaller(widget);

            widget.render(guiGraphics, 0, 0, LightTexture.FULL_BRIGHT);

            guiGraphics.pose().popPose();

        }

    }


}
