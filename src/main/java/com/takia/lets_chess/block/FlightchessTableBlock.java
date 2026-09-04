package com.takia.lets_chess.block;

import com.takia.lets_chess.entity.ChessboardEntity;
import com.takia.lets_chess.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class FlightchessTableBlock extends Block {

    public FlightchessTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide && level.getBlockEntity(pos) == null) {
            ChessboardEntity entity = new ChessboardEntity(ModEntities.CHESSBOARD_ENTITY.get(), level);
            entity.setBoardType(ChessboardEntity.TYPE_FLIGHTCHESS);
            entity.moveTo(pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, 0, 0);
            level.addFreshEntity(entity);
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return InteractionResult.PASS;
    }
}