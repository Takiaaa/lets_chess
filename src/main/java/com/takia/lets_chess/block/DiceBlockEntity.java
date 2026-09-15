package com.takia.lets_chess.block;

import com.takia.lets_chess.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DiceBlockEntity extends BlockEntity {

    private static final int ROLLING_TICKS = 15;
    private static final int DISPLAY_TICKS = 5;
    private static final int TOTAL_TICKS = ROLLING_TICKS + DISPLAY_TICKS;

    private int tickCount = 0;

    public DiceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DICE.get(), pos, state);
    }

    public void startRolling() {
        this.tickCount = 1;
        setChanged();
    }

    public boolean isRolling() {
        return this.tickCount > 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DiceBlockEntity blockEntity) {
        if (blockEntity.tickCount <= 0) {
            return;
        }

        if (blockEntity.tickCount <= ROLLING_TICKS) {
            int newFace = level.getRandom().nextIntBetweenInclusive(1, 6);
            level.setBlock(pos, state.setValue(DiceBlock.FACE, newFace), 2);
        }

        blockEntity.tickCount++;
        if (blockEntity.tickCount > TOTAL_TICKS) {
            blockEntity.tickCount = 0;
            level.setBlock(pos, state.setValue(DiceBlock.ROLLING, false), 2);
        }

        blockEntity.setChanged();
    }
}