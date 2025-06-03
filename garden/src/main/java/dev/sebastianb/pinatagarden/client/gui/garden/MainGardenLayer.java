package dev.sebastianb.pinatagarden.client.gui.garden;

import dev.sebastianb.pinatagarden.PinataGarden;
import dev.sebastianb.pinatagarden.client.gui.util.Tiled2DBufferCreator;
import dev.sebastianb.pinatagarden.mixin_duck.GuiGraphicsDuck;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.Function;

import static com.mojang.blaze3d.platform.NativeImage.Format.RGBA;
import static org.lwjgl.opengl.GL11.*;

// this class should be used for the main garden GUI, which will contain the top elements of the garden manager
public class MainGardenLayer extends LayeredDraw {

    private static final ResourceLocation GREEN_TILE_PATTERN = ResourceLocation.fromNamespaceAndPath(PinataGarden.MOD_ID, "textures/gui/green_element.png");


    // TODO: we probably want to make this true if in orbital mode and if not hiding GUI (use &&)
    public MainGardenLayer() {
        this.add(this::testRender);
    }


    private void testRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {

        int centerX = guiGraphics.guiWidth() / 2;
        int centerY = guiGraphics.guiHeight() / 2;

        guiGraphics.pose().pushPose();

        int regionSize = 500;

        ((GuiGraphicsDuck) guiGraphics).blitPattern(
                RenderType::guiTextured,
                GREEN_TILE_PATTERN,
                centerX - (regionSize / 2),
                centerY - (regionSize / 2),
                0,0, regionSize, regionSize, regionSize, regionSize,
                regionSize, regionSize,
                ARGB.colorFromFloat(0.4F,1,1,1),
                400,400
        );
        guiGraphics.pose().popPose();





    }

}
