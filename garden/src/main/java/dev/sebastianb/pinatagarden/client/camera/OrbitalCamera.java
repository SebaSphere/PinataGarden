package dev.sebastianb.pinatagarden.client.camera;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;

// we extend camera, which this class is used to replace with our own logic in the GameRenderer mixins
public class OrbitalCamera extends Camera {

    @Override
    public void setup(BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickProgress) {

        super.setup(area, focusedEntity, thirdPerson, inverseView, tickProgress);

        // this.detached = true;


    }
}
