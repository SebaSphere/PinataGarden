package dev.sebastianb.anchorgui.mixin;

import dev.sebastianb.anchorgui.util.GuiScreenSetup;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BooleanSupplier;

@Mixin(Gui.class)
public class GuiMixin {


    // this should be used to add new elements to the gui
    @Redirect(method = "<init>", at = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDraw;add(Lnet/minecraft/client/gui/LayeredDraw;Ljava/util/function/BooleanSupplier;)Lnet/minecraft/client/gui/LayeredDraw;"))
    public LayeredDraw redirectLayeredGUIElements(LayeredDraw instance, LayeredDraw layeredDraw, BooleanSupplier booleanSupplier) {

        instance.add(layeredDraw, booleanSupplier);

        // after all layers have been added when the mod inits, we should add any layers defined to render after all vanilla layers
        var layers = GuiScreenSetup.getLayers();
        layers.forEach(layer -> {
            instance.add(layer.getLayeredDraw(), layer.getBooleanSupplier());
        });

        return instance;
    }

}
