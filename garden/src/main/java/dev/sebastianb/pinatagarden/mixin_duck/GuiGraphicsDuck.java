package dev.sebastianb.pinatagarden.mixin_duck;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public interface GuiGraphicsDuck {

    void blitPattern(Function<ResourceLocation, RenderType> renderLayers, ResourceLocation sprite, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color, int rows, int columns);

    void blitPattern(Function<ResourceLocation, RenderType> renderLayers, ResourceLocation sprite, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int rows, int columns);

    void blitPattern(Function<ResourceLocation, RenderType> renderLayers, ResourceLocation sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int rows, int columns);

    void blitPattern(Function<ResourceLocation, RenderType> renderLayers, ResourceLocation sprite, int x, int y, float u, float v, int width, int height, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color, int rows, int columns);

    // this should be used to pattern together GUI elements efficiently in one buffer
    void innerBlitPattern(Function<ResourceLocation, RenderType> renderLayers, ResourceLocation sprite, int x1, int x2, int y1, int y2, float u1, float u2, float v1, float v2, int color, int rows, int columns);
}
