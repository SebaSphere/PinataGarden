package dev.sebastianb.anchorgui.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import dev.sebastianb.anchorgui.layout.GuiElement;
import dev.sebastianb.anchorgui.mixin_duck.CurrentWidgetHolder;
import dev.sebastianb.anchorgui.util.GuiScreenInjector;
import dev.sebastianb.anchorgui.util.InstanceTracker;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
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

import java.time.Duration;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectInit(Minecraft minecraft, ItemInHandRenderer itemInHandRenderer, ResourceManager resourceManager, RenderBuffers renderBuffers, CallbackInfo ci) {

    }


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void injectRender(DeltaTracker deltaTracker, boolean bl, CallbackInfo ci, ProfilerFiller profilerFiller, boolean bl2, int i, int j, Window window, RenderTarget renderTarget, Matrix4f matrix4f, Matrix4fStack matrix4fStack, GuiGraphics guiGraphics) {


        for (GuiElement baseParent : GuiScreenInjector.getGuiElements()) {

            // it has no parent elements (I hope)
            setupElementRendering(guiGraphics, baseParent);

            for (GuiElement childElement : baseParent.getChildren()) {
                baseParent.getParent().setParent(childElement);
                recurseGuiElements(childElement, guiGraphics);
            }

        }

    }

    // this should handle all rendering logic
    @Unique
    private static void setupElementRendering(GuiGraphics guiGraphics, GuiElement guiElement) {
        var widget = guiElement.getWidget();
        widget.active = false;

        // FIXME: if it's something like a string widget, it seems to apply a temp grey background? idk why but we need to fix that, maybe I could just make a new widget if I can't fix


        // TODO: eventually we need to handle input, we're going to abuse the blit render location to bypass with our own system for handling

        // just before rendering, set the object reference for blitting
        CurrentWidgetHolder.setCurrentWidget(widget, guiElement); // im concerned

        InstanceTracker.setCaller(widget);

        guiGraphics.pose().pushPose();
        // Get screen size
        Window window = Minecraft.getInstance().getWindow();
        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();


        int moveWidthScale = (int) (Minecraft.getInstance().getWindow().getGuiScaledWidth() * (CurrentWidgetHolder.getCurrentGuiElement().getPercentageWidth() / 100));

        float previousAspectRation = (float) widget.getWidth() / (float) widget.getHeight();

        // TODO: maybe we could have a mode for if the center of the element applies?
        // Get widget size
        int widgetWidth = moveWidthScale;
        int widgetHeight = moveWidthScale < screenWidth ? (int) (moveWidthScale / previousAspectRation) : screenHeight;

        // Compute centered position
        float centeredX = (screenWidth - widgetWidth) / 2f;
        float centeredY = (screenHeight - widgetHeight) / 2f;


        // we are going to matrices translate, this is gonna be so bad
        guiGraphics.pose().translate(centeredX, centeredY, 0);

        // should be the position, we probably want to pass the element relative position instead
        widget.render(guiGraphics, 0, 0, LightTexture.FULL_BRIGHT);

        guiGraphics.pose().popPose();


    }

    // handle children recursively
    @Unique
    private void recurseGuiElements(GuiElement childElement, GuiGraphics guiGraphics) {
        setupElementRendering(guiGraphics, childElement);

        // we need to render first before the base case
        if (childElement.getChildren().isEmpty()) {
            return;
        }

        for (GuiElement introspected : childElement.getChildren()) {
            childElement.getParent().setParent(introspected);
            recurseGuiElements(introspected, guiGraphics);
        }
    }


}
