package dev.sebastianb.hibernateorm.mixin;


import dev.sebastianb.hibernateorm.db.DatabaseConfig;
import dev.sebastianb.hibernateorm.db.DatabaseSetup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.LoggerChunkProgressListener;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow public abstract Path getServerDirectory();

    @Shadow public abstract ServerLevel overworld();

    @Shadow @Final protected LevelStorageSource.LevelStorageAccess storageSource;

    @Inject(method = "prepareLevels", at = @At("HEAD"))
    private void start(CallbackInfo ci) {
        // this *should* only call once per world load

        // we probably want people to register their own paths

        for (var dataPath : DatabaseConfig.getModID2DatasetConfig().asMap().entrySet()) {
            String modID = dataPath.getKey();
            for (var databaseBuilder : dataPath.getValue()) {
                databaseBuilder.setLevelStorageAccess(this.storageSource);

                DatabaseConfig databaseConfig = DatabaseConfig.setup(this.storageSource, Path.of(modID, databaseBuilder.getResolvedConfigPath().toString()), modID, databaseBuilder);
                databaseConfig.setupConfigFile(false);

                var databaseSetup = DatabaseSetup.init(databaseConfig.getDataSource(), databaseBuilder);
                databaseSetup.setupSession();
                databaseSetup.finalizeSessionSetup();
            }
        }


    }


}
