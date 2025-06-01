package dev.sebastianb.pinatagarden;

import dev.sebastianb.hibernateorm.db.DatabaseConfig;
import dev.sebastianb.pinatagarden.management.garden.GardenRepresentation;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;

public class PinataGarden implements ModInitializer {

    public static final String MOD_ID = "pinatagarden";
    public static final ResourceLocation GARDEN_SESSION_DATASTORE_KEY
            = ResourceLocation.fromNamespaceAndPath(MOD_ID, "management/garden");

    @Override
    public void onInitialize() {

        DatabaseConfig
                .getModID2DatasetConfig()
                .put("pinatagarden", new DatabaseConfig.DatabaseConfigBuilder()
                        .setResolvedConfigPath(Path.of("management/garden"))
                        .addAnnotatedTable(GardenRepresentation.class)
                );

        System.out.println(GARDEN_SESSION_DATASTORE_KEY);
    }
}
