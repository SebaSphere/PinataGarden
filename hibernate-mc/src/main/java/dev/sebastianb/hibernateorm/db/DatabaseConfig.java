package dev.sebastianb.hibernateorm.db;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.sebastianb.hibernateorm.HibernateMC;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.storage.LevelStorageSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


// we probably want this to apply per world
public class DatabaseConfig {


    private static Multimap<String, DatabaseConfigBuilder> modID2DatasetConfig = MultimapBuilder.hashKeys().hashSetValues().build();


    public static class DatabaseConfigBuilder {

        private String modID;
        private Path resolvedConfigPath;

        private ArrayList<Class<?>> annotatedTables = new ArrayList<>();

        private LevelStorageSource.LevelStorageAccess levelStorageAccess;

        public DatabaseConfigBuilder setResolvedConfigPath(Path resolvedConfigPath) {
            this.resolvedConfigPath = resolvedConfigPath;
            return this;
        }

        public void setLevelStorageAccess(LevelStorageSource.LevelStorageAccess levelStorageAccess) {
            this.levelStorageAccess = levelStorageAccess;
        }

        public DatabaseConfigBuilder addAnnotatedTable(Class<?> annotatedTable) {
            this.annotatedTables.add(annotatedTable);
            return this;
        }

        public ArrayList<Class<?>> getAnnotatedTables() {
            return annotatedTables;
        }

        public Path getResolvedConfigPath() {
            return resolvedConfigPath;
        }

        public LevelStorageSource.LevelStorageAccess getLevelStorageAccess() {
            return levelStorageAccess;
        }

        public String getModID() {
            return modID;
        }
    }

    public static Multimap<String, DatabaseConfigBuilder> getModID2DatasetConfig() {
        return modID2DatasetConfig;
    }

    private HikariDataSource dataSource;


    private Path worldPath;

    private  Path resolvedConfigPath;

    private String modID;

    private DatabaseConfigBuilder databaseConfigBuilder;

    private DatabaseConfig(LevelStorageSource.LevelStorageAccess levelStorageAccess, Path resolvedConfigPath, String modID, DatabaseConfigBuilder databaseConfigBuilder) {
        this.worldPath = levelStorageAccess.getLevelDirectory().path();
        this.resolvedConfigPath = resolvedConfigPath;
        this.modID = modID;
        this.databaseConfigBuilder = databaseConfigBuilder;
    }

    // gets where the world path is and checks if "/garden/database.conf" exists
    // forceSetup is used to force a re-setup of the config file for invalid config
    public void setupConfigFile(boolean forceSetup) {
        try {
            Path configFile = worldPath.resolve("hibernate-orm/" + this.resolvedConfigPath + "/database.conf");

            if (!Files.exists(configFile) || forceSetup) {
                // should replicate the config in the same place with "OLD-FILENAME-timestamp.conf"
                if (Files.exists(configFile)) {
                    // Backup existing config file with timestamp
                    String timestamp = String.valueOf(LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC));
                    Path backupFile = configFile.getParent().resolve("OLD-database-" + timestamp + ".conf");
                    Files.copy(configFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Backed up corrupted config to: " + backupFile);
                    // Delete existing config file after backup
                    Files.delete(configFile);
                }

                // I should get the mod ID and check if there's anything in the resources
                // that match the path. If there's nothing, I should copy the default config file
                AtomicBoolean shouldCopyDefaultConfig = new AtomicBoolean(true);

                FabricLoader.getInstance().getModContainer(modID).ifPresent(modContainer -> {
                    System.out.println(resolvedConfigPath);
                   modContainer.findPath("hibernate-orm/" + resolvedConfigPath + "/database.conf").ifPresent(modIDHibernatePath -> {
                       System.out.println("Found hibernate file: " + modIDHibernatePath);
                       System.out.println("Copying hibernate file to: " + configFile);
                       try {
                           Files.createDirectories(configFile.getParent());
                           Files.copy(modIDHibernatePath, configFile, StandardCopyOption.REPLACE_EXISTING);
                           shouldCopyDefaultConfig.set(false);
                       } catch (IOException e) {
                           System.out.println("Failed to copy hibernate file: " + modIDHibernatePath);
                           e.printStackTrace();
                       }

                   });
                });

                if (shouldCopyDefaultConfig.get()) {
                    Files.createDirectories(configFile.getParent());
                    System.out.println("Created default hibernate config file: " + configFile);
                    Files.copy(HibernateMC.class.getResourceAsStream("/database.conf"), configFile);
                }
            } else {
                System.out.println("Configuration file at " + configFile);
            }
            // should load what's in the disk to the object representation
            loadConfigFromDisk(configFile.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfigFromDisk(File file) {
        try {
            System.out.println(file.getAbsolutePath() + " is real");
            Config config = ConfigFactory.parseFile(file);
            setupSQLConnection(config);
        } catch (ConfigException e) {
            System.out.println("Failed to load database config file:");
            e.printStackTrace();
            this.setupConfigFile(true); // forces setup again
        }

    }


    private void setupSQLConnection(Config config) {
        System.out.println("Setting up database connection for the mod " + modID + " at " + resolvedConfigPath);

        Config dbConfig = config.getConfig("db");
        HikariConfig hikariConfig = new HikariConfig();

        // Apply only if not null
        applyIfNotNull(dbConfig, "connectionPool", hikariConfig::setPoolName);
        applyIfNotNull(dbConfig, "dataSourceClass", hikariConfig::setDataSourceClassName);

        applyIfNotNull(dbConfig, "properties.serverName", v -> hikariConfig.addDataSourceProperty("serverName", v));
        applyIfNotNull(dbConfig, "properties.portNumber", v -> hikariConfig.addDataSourceProperty("portNumber", v));
        applyIfNotNull(dbConfig, "properties.databaseName", v -> hikariConfig.addDataSourceProperty("databaseName", v));
        applyIfNotNull(dbConfig, "properties.user", v -> hikariConfig.addDataSourceProperty("user", v));
        applyIfNotNull(dbConfig, "properties.password", v -> hikariConfig.addDataSourceProperty("password", v));

        // h2 stuff
        applyIfNotNull(dbConfig, "driverClassName", hikariConfig::setDriverClassName);
        applyIfNotNull(dbConfig, "jdbcUrl", hikariConfig::setJdbcUrl);

        if (dbConfig.hasPath("numThreads") && !dbConfig.getIsNull("numThreads")) {
            hikariConfig.setMaximumPoolSize(dbConfig.getInt("numThreads"));
        }

        dataSource = new HikariDataSource(hikariConfig);
    }

    private void applyIfNotNull(Config config, String path, Consumer<String> setter) {
        if (config.hasPath(path) && !config.getIsNull(path)) {
            String value = config.getString(path);
            if (value != null) {
                value = replacePlaceholders(value);
                setter.accept(value);
            }
        }
    }

    private String replacePlaceholders(String input) {
        Map<String, Path> placeholders = Map.of(
                "{db_path}", databaseConfigBuilder.getLevelStorageAccess().getLevelDirectory().path().resolve("hibernate-orm/" + this.resolvedConfigPath)
        );

        for (Map.Entry<String, Path> entry : placeholders.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue().toString());
        }
        return input;
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public static DatabaseConfig setup(LevelStorageSource.LevelStorageAccess levelStorageAccess, Path resolvedConfigPath, String modID, DatabaseConfigBuilder databaseConfigBuilder) {
        return new DatabaseConfig(levelStorageAccess, resolvedConfigPath, modID, databaseConfigBuilder);
    }



}
