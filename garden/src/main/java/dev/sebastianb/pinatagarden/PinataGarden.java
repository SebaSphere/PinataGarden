package dev.sebastianb.pinatagarden;

import dev.sebastianb.hibernateorm.db.DatabaseConfig;
import dev.sebastianb.pinatagarden.management.garden.GardenRepresentation;
import net.fabricmc.api.ModInitializer;

import java.nio.file.Path;

public class PinataGarden implements ModInitializer {

    @Override
    public void onInitialize() {

        DatabaseConfig
                .getModID2DatasetConfig()
                .put("pinatagarden", new DatabaseConfig.DatabaseConfigBuilder()
                        .setResolvedConfigPath(Path.of("management/garden"))
                        .addAnnotatedTable(GardenRepresentation.class)
                );

    }
}
