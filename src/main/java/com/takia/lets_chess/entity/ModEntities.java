package com.takia.lets_chess.entity;

import com.takia.lets_chess.Letschess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = 
            DeferredRegister.create(Registries.ENTITY_TYPE, Letschess.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<ChessboardEntity>> CHESSBOARD_ENTITY =
            ENTITIES.register("chessboard_entity", () -> EntityType.Builder.<ChessboardEntity>of(ChessboardEntity::new, MobCategory.MISC)
                    .sized(16.0F, 0.05F)
                    .clientTrackingRange(256)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("chessboard_entity"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}