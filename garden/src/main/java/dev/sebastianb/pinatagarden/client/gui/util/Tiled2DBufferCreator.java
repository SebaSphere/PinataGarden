package dev.sebastianb.pinatagarden.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Tiled2DBufferCreator {

    // takes in a texture, a column and row then returns the combined result in one buffer
    private BufferedImage combinedImage;
    private InputStream tiledInputStream;


    private int totalImageWidth;
    private int totalImageHeight;

    public BufferedImage getCombinedImage() {
        return combinedImage;
    }

    public int getTotalImageWidth() {
        return totalImageWidth;
    }

    public int getTotalImageHeight() {
        return totalImageHeight;
    }

    public InputStream getTiledInputStream() {
        return tiledInputStream;
    }

    public Tiled2DBufferCreator(ResourceLocation tiledImageLocation, int imageX, int imageY, int rows, int columns) {

        this.totalImageWidth = imageX * rows;
        this.totalImageHeight = imageY * columns;

        // BufferedImage emptyImage = new BufferedImage(totalImageWidth, totalImageHeight, BufferedImage.TYPE_INT_ARGB);


        Minecraft.getInstance().getResourceManager().getResource(tiledImageLocation).ifPresent(
                tiledImageResource -> {
                    try (InputStream tiledInputStream = tiledImageResource.open()) {
                        this.tiledInputStream = tiledInputStream;

                        this.combinedImage = ImageIO.read(tiledInputStream);
                        System.out.println("TEST!");

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

    }
}
