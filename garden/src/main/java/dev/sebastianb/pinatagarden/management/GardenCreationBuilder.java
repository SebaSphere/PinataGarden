package dev.sebastianb.pinatagarden.management;

import net.minecraft.world.level.ChunkPos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class GardenCreationBuilder {

    private final UUID gardenUUID;
    private final LocalDate creationDate;

    // be sure to also include "gardenChunkStartingPosition"
    private final ArrayList<ChunkPos> allChunksToInitiallyClaim = new ArrayList<>();

    private String gardenName;

    private ChunkPos gardenChunkStartingPosition;

    // should be used for first setup
    public GardenCreationBuilder() {
        // auto init creation date, etc
        this.gardenUUID = UUID.randomUUID();
        this.creationDate = LocalDate.now();
    }

    // only call this once
    public void setGardenChunkStartingPosition(ChunkPos gardenChunkStartingPosition) {
        if (gardenChunkStartingPosition != null) {
            throw new IllegalArgumentException("Garden chunk start position has already been set");
        }
        this.gardenChunkStartingPosition = gardenChunkStartingPosition;
        allChunksToInitiallyClaim.add(gardenChunkStartingPosition);
    }

    public void setGardenName(String gardenName) {
        this.gardenName = gardenName;
    }

    // this should save everything about the garden to hibernate and build an actual garden instance
    // TODO: change the return type to a new garden instance created in code
    public GardenCreationBuilder build() {
        return this;
    }

}
