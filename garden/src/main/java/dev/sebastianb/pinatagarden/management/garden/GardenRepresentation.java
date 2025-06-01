package dev.sebastianb.pinatagarden.management.garden;

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
@Table(name = "garden_representation")
public class GardenRepresentation implements ITable {

    @Override
    public SessionFactory getSessionFactory() {
        return DatabaseSetup.getSessionFactory(PinataGarden.GARDEN_SESSION_DATASTORE_KEY);
    }

    @Id
    @GeneratedValue
    private UUID uuid;



    public static GardenRepresentation fetchGardenRepresentation(UUID uuid) {
        return new GardenRepresentation().getSessionFactory().getCurrentSession().find(GardenRepresentation.class, uuid);
    }

}
