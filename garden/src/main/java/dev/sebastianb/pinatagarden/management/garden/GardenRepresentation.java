package dev.sebastianb.pinatagarden.management.garden;

import dev.sebastianb.hibernateorm.db.DatabaseSetup;
import dev.sebastianb.hibernateorm.db.ITable;
import dev.sebastianb.pinatagarden.PinataGarden;
import dev.sebastianb.pinatagarden.management.garden.chunk.EmbeddedChunkPosition;
import jakarta.persistence.*;
import net.minecraft.world.level.ChunkPos;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "garden_representation")
public class GardenRepresentation implements ITable {

    @GeneratedValue
    @Id
    @Column(name = "garden_uuid")
    private UUID gardenUUID;

    @Column(name = "starting_chunk_uuid")
    private UUID startingChunkUUID;


    @Column(name = "garden_creation_date")
    private LocalDate gardenCreationDate = LocalDate.now();

    @Override
    public SessionFactory getSessionFactory() {
        return DatabaseSetup.getSessionFactory(PinataGarden.GARDEN_SESSION_DATASTORE_KEY);
    }


    @Column(name = "name")
    private String name;


    @Embedded
    @Column(name = "initial_chunk_pos")
    private EmbeddedChunkPosition initialChunkPos; // this is the first chunk that the garden is in, it's the starting position for the garden

    public GardenRepresentation() {

    }

    public GardenRepresentation(EmbeddedChunkPosition initialChunkPos) {
        // we probably want to check if the chunk pos is already in the database, if so, throw an exception
        this.initialChunkPos = initialChunkPos;
        this.startingChunkUUID = initialChunkPos.generateUUID();
    }

    public GardenRepresentation setName(String name) {
        this.name = name;
        return this;
    }



    public void build() {
        this.writePersistentData(this);
    }


    public static GardenRepresentation fetchGardenRepresentation(UUID uuid) {
        return new GardenRepresentation().getSessionFactory().getCurrentSession().find(GardenRepresentation.class, uuid);
    }

}
