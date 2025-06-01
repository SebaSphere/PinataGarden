package dev.sebastianb.pinatagarden.mixin;

import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends ClientInput {



}
