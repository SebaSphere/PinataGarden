package dev.sebastianb.pinatagarden.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public class PlayerMixin {

    // we probably want to spawn a cow with no ai that has data attached with the player UUID around the player if it doesn't exist
    // this is temp until a proper garden selection entity works

}
