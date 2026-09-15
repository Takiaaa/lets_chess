package com.takia.lets_chess.block.entity;

import com.takia.lets_chess.Letschess;
import com.takia.lets_chess.block.DiceBlockEntity;
import com.takia.lets_chess.block.ModBlocks;
import com.takia.lets_chess.block.MiniChessboardBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Letschess.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MiniChessboardBlockEntity>> MINI_CHESSBOARD = BLOCK_ENTITIES.register("mini_chessboard",
        () -> BlockEntityType.Builder.of(MiniChessboardBlockEntity::new,
            ModBlocks.MINI_CHESSBOARD_XIANGQI.get(),
            ModBlocks.MINI_CHESSBOARD_CHESS.get(),
            ModBlocks.MINI_CHESSBOARD_FLIGHTCHESS.get()
        ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DiceBlockEntity>> DICE = BLOCK_ENTITIES.register("dice",
        () -> BlockEntityType.Builder.of(DiceBlockEntity::new,
            ModBlocks.DICE_WHITE.get(),
            ModBlocks.DICE_BLACK.get()
        ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}