package dev.sebastianb.pinatagarden.mixin;

import dev.sebastianb.pinatagarden.client.camera.OrbitalCamera;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Mutable
    @Shadow @Final private Camera mainCamera;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectContructor(Minecraft client, ItemInHandRenderer itemInHandRenderer, ResourceManager resourceManager, RenderBuffers buffers, CallbackInfo ci) {
        // TODO: we probably want to get from the world on what camera to use
        this.mainCamera = new OrbitalCamera();
    }

}
