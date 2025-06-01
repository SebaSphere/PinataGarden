package dev.sebastianb.hibernateorm.db;

import com.zaxxer.hikari.HikariDataSource;
import net.fabricmc.loader.api.FabricLoader;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.Properties;

public class DatabaseSetup {

    private final HikariDataSource dataSource;
    private final DatabaseConfig.DatabaseConfigBuilder databaseConfigBuilder;
    private final ArrayList<Class<?>> annotatedClasses;


    private SessionFactory sessionFactory;
    private ServiceRegistry serviceRegistry;
    private final Configuration configuration = new Configuration();


    private DatabaseSetup(HikariDataSource dataSource, DatabaseConfig.DatabaseConfigBuilder databaseConfigBuilder) {
        this.dataSource = dataSource;
        this.databaseConfigBuilder = databaseConfigBuilder;
        this.annotatedClasses = databaseConfigBuilder.getAnnotatedTables();
    }

    public void setupSession() {
        if (sessionFactory != null) return;
        try {
            this.serviceRegistry = setupDatabaseRegistry(configuration, this.dataSource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Hibernate", e);
        }
    }


    public void finalizeSessionSetup() {
        if (sessionFactory != null) return;
        try {
            // you should call all your annotated classes here
            for (Class<?> annotatedClass : annotatedClasses) {
                configuration.addAnnotatedClass(annotatedClass);
            }

            this.sessionFactory = configuration.buildSessionFactory(this.serviceRegistry);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Hibernate", e);
        }
    }

    private static ServiceRegistry setupDatabaseRegistry(Configuration configuration, HikariDataSource dataSource) {
        // Setup properties using existing HikariDataSource
        Properties settings = new Properties();

        settings.put(Environment.DATASOURCE, dataSource); // your Hikari source

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            settings.put(Environment.SHOW_SQL, true);
            settings.put(Environment.HBM2DDL_AUTO, "update"); // or validate, create, etc.
        }
        configuration.setProperties(settings);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return serviceRegistry;
    }


    public static DatabaseSetup init(HikariDataSource dataSource, DatabaseConfig.DatabaseConfigBuilder databaseConfigBuilder) {
        return new DatabaseSetup(dataSource, databaseConfigBuilder);
    }

}
