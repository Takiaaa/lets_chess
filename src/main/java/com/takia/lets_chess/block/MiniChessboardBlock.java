package com.takia.lets_chess.block;

import com.takia.lets_chess.block.MiniChessboardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MiniChessboardBlock extends CommonBlock implements EntityBlock {
    private static final VoxelShape MINI_BOARD_SHAPE = box(0, 0, 0, 16, 2, 16);

    public MiniChessboardBlock(Properties properties) {
        super(properties.noOcclusion(), MINI_BOARD_SHAPE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MiniChessboardBlockEntity(pos, state);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MiniChessboardBlockEntity miniBoard) {
                miniBoard.openGUI((ServerPlayer) player);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }
}