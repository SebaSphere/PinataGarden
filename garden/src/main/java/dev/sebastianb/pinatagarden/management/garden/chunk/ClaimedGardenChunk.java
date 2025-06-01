package dev.sebastianb.pinatagarden.management.garden.chunk;

import dev.sebastianb.hibernateorm.db.DatabaseSetup;
import dev.sebastianb.hibernateorm.db.ITable;
import dev.sebastianb.pinatagarden.PinataGarden;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.SessionFactory;

import java.util.UUID;


@Entity
@Table(name = "claimed_garden_chunk")
public class ClaimedGardenChunk implements ITable {


    @Id
    @GeneratedValue
    private UUID uuid;



    @Override
    public SessionFactory getSessionFactory() {
        return DatabaseSetup.getSessionFactory(PinataGarden.GARDEN_SESSION_DATASTORE_KEY);
    }


}
