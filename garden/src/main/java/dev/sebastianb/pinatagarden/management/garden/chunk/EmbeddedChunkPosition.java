package dev.sebastianb.pinatagarden.management.garden.chunk;

import jakarta.persistence.Embeddable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public abstract class EmbeddedChunkPosition extends ChunkPos {
    private int chunkX;
    private int chunkZ;

    // don't use for hibernate
    private transient ResourceLocation dimension;

    // hibernate requires a no-arg constructor
    protected EmbeddedChunkPosition() {
        super(0, 0);
    }

    // TODO: I could make the UUID of one chunk based off the string of the combined chunk positions so it's O(log n) to retrieve
    public EmbeddedChunkPosition(ChunkPos pos, ResourceLocation dimension) {
        super(pos.x, pos.z);
        this.chunkX = pos.x;
        this.chunkZ = pos.z;
        this.dimension = dimension;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EmbeddedChunkPosition that = (EmbeddedChunkPosition) o;
        return chunkX == that.chunkX && chunkZ == that.chunkZ && Objects.equals(dimension, that.dimension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), chunkX, chunkZ, dimension);
    }

    // ideally, this shouldn't be used for anything
    @Deprecated
    public ChunkPos toChunkPos() {
        return new ChunkPos(chunkX, chunkZ);
    }

    @Override
    public String toString() {
        return "ChunkPosition{" + "x=" + chunkX + ", z=" + chunkZ + '}';
    }

    // Getters and setters (required by JPA)
    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    // used to make it faster to retrieve a chunk, it should be O(log n) to retrieve a specific chunk by its UUID
    public UUID generateUUID() {
        return UUID.fromString(String.valueOf(this.hashCode()));
    }

}
