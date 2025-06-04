package dev.sebastianb.anchorgui.util;

import net.minecraft.client.gui.LayeredDraw;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

public class GuiScreenSetup {

    public static class GuiLayeredDraw {
        private LayeredDraw layeredDraw;
        private BooleanSupplier booleanSupplier;

        public GuiLayeredDraw(LayeredDraw layeredDraw, BooleanSupplier booleanSupplier) {
            this.layeredDraw = layeredDraw;
            this.booleanSupplier = booleanSupplier;
        }

        public LayeredDraw getLayeredDraw() {
            return layeredDraw;
        }

        public BooleanSupplier getBooleanSupplier() {
            return booleanSupplier;
        }

    }

    private static ArrayList<GuiLayeredDraw> layers = new ArrayList<>();

    public static void addLayer(LayeredDraw layeredDraw, BooleanSupplier booleanSupplier) {
        layers.add(new GuiLayeredDraw(layeredDraw, booleanSupplier));
    }

    public static ArrayList<GuiLayeredDraw> getLayers() {
        return layers;
    }
}
