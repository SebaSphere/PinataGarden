package dev.sebastianb.anchorgui.mixin;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sebastianb.anchorgui.mixin_duck.CurrentWidgetHolder;
import dev.sebastianb.anchorgui.mixin_duck.GuiGraphicsDuck;
import dev.sebastianb.anchorgui.util.InstanceTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

// this method will be useful to hook into the graphics class to add tiling
@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin implements GuiGraphicsDuck {


    @Inject(method = "innerBlit", at = @At(value = "HEAD"), cancellable = true)
    private void injectInnerBlit(Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int pixelWidth, int k, int pixelHeight, float f, float g, float h, float m, int n, CallbackInfo ci) {
        // FIXME: we only want to cancel blit on classes that use our GUI implementation, this is bad to cancel but couldn't think of a better way to do this


        Object callingInstance = InstanceTracker.getCaller();

        if (callingInstance instanceof AbstractWidget abstractWidget) {
            int screenWidth = (int) (Minecraft.getInstance().getWindow().getGuiScaledWidth() * (CurrentWidgetHolder.getCurrentGuiElement().getPercentageWidth() / 100));

            float previousAspectRation = (float) pixelWidth / (float) pixelHeight;

            // these variables are the size of the blit
            pixelWidth = screenWidth;
            pixelHeight = (int) (screenWidth * previousAspectRation);

            // sets the caller to null so we don't touch any vanilla stuff
            InstanceTracker.setCaller(null);
        }

        // basically vanilla
        RenderType renderType = function.apply(resourceLocation);
        Matrix4f matrix4f = this.pose.last().pose();
        VertexConsumer vertexConsumer = this.bufferSource.getBuffer(renderType);
        vertexConsumer.addVertex(matrix4f, (float)i, (float)k, 0.0F).setUv(f, h).setColor(n);
        vertexConsumer.addVertex(matrix4f, (float)i, (float)pixelHeight, 0.0F).setUv(f, m).setColor(n);
        vertexConsumer.addVertex(matrix4f, (float)pixelWidth, (float)pixelHeight, 0.0F).setUv(g, m).setColor(n);
        vertexConsumer.addVertex(matrix4f, (float)pixelWidth, (float)k, 0.0F).setUv(g, h).setColor(n);

        ci.cancel();
    }



    @Shadow @Final private PoseStack pose;

    @Shadow @Final private MultiBufferSource.BufferSource bufferSource;


    // we set all these utility methods up so it's the same thing as a blit but we handle infinite tiling
    @Override
    public void blitPattern(Function<ResourceLocation, RenderType> renderLayers, ResourceLocation sprite, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color, int rows, int columns) {
        this.blitPattern(renderLayers, sprite, x, y, u, v, width, height, width, height, textureWidth, textureHeight, color, rows, columns);
    }

    @Override
    public void blitPattern(Function<ResourceLocation, RenderType> renderLayers, ResourceLocation sprite, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int rows, int columns) {
        this.blitPattern(renderLayers, sprite, x, y, u, v, width, height, width, height, textureWidth, textureHeight, rows, columns);
    }

    @Override
    public void blitPattern(Function<ResourceLocation, RenderType> renderLayers, ResourceLocation sprite, int x, int y, float u, float v, int width, int height, int regionWith, int regionHeight, int textureWidth, int textureHeight, int rows, int columns) {
        this.blitPattern(renderLayers, sprite, x, y, u, v, width, height, regionWith, regionHeight, textureWidth, textureHeight, -1, rows, columns);
    }

    @Override
    public void blitPattern(Function<ResourceLocation, RenderType> renderLayers, ResourceLocation sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color, int rows, int columns) {
        this.innerBlitPattern(renderLayers, sprite, x, x + width, y, y + height, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight, color, rows, columns);

    }

    @Override
    public void innerBlitPattern(Function<ResourceLocation, RenderType> renderLayers, ResourceLocation sprite,
                                 int x1, int x2, int y1, int y2,
                                 float u1, float u2, float v1, float v2,
                                 int color, int rows, int columns) {
        RenderType renderType = renderLayers.apply(sprite);
        Matrix4f matrix4f = this.pose.last().pose();
        VertexConsumer vertexConsumer = this.bufferSource.getBuffer(renderType);

        float tileWidth = (x2 - x1) / (float) columns;
        float tileHeight = (y2 - y1) / (float) rows;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                float tileX1 = x1 + col * tileWidth;
                float tileX2 = tileX1 + tileWidth;
                float tileY1 = y1 + row * tileHeight;
                float tileY2 = tileY1 + tileHeight;

                vertexConsumer.addVertex(matrix4f, tileX1, tileY1, 0.0F).setUv(u1, v1).setColor(color);
                vertexConsumer.addVertex(matrix4f, tileX1, tileY2, 0.0F).setUv(u1, v2).setColor(color);
                vertexConsumer.addVertex(matrix4f, tileX2, tileY2, 0.0F).setUv(u2, v2).setColor(color);
                vertexConsumer.addVertex(matrix4f, tileX2, tileY1, 0.0F).setUv(u2, v1).setColor(color);
            }
        }
    }

}
