package dev.sebastianb.pinatagarden.management.garden;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "garden_representation")
public class GardenRepresentation {

    @Id
    @GeneratedValue
    private UUID uuid;

}
