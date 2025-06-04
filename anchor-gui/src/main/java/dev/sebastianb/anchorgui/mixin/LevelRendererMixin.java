package dev.sebastianb.anchorgui.mixin;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import dev.sebastianb.anchorgui.mixin_duck.ScreenDuck;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private RenderBuffers renderBuffers;
    @Unique
    MultiLineTextWidget testWidget = new MultiLineTextWidget(Component.literal("MEOW TEST"), Minecraft.getInstance().font);

    @Inject(method = "renderLevel", at = @At("HEAD"))
    public void injectRender(GraphicsResourceAllocator graphicsResourceAllocator, DeltaTracker deltaTracker, boolean bl, Camera camera, GameRenderer gameRenderer, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {


        if (this.minecraft.screen != null) {


        }


    }

}
