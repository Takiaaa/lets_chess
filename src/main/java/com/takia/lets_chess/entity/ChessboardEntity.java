package com.takia.lets_chess.entity;

import com.takia.lets_chess.block.BadukTableBlock;
import com.takia.lets_chess.block.FlightchessTableBlock;
import com.takia.lets_chess.block.XiangqiTableBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ChessboardEntity extends Entity {
    private static final EntityDataAccessor<String> BOARD_TYPE =
            SynchedEntityData.defineId(ChessboardEntity.class, EntityDataSerializers.STRING);

    public static final String TYPE_XIANGQI = "xiangqi";
    public static final String TYPE_BADUK = "baduk";
    public static final String TYPE_FLIGHTCHESS = "flightchess";

    private static final int CHECK_INTERVAL = 20; // 每20个tick（1秒）检查一次方块有效性
    private int tickCounter = 0;

    public ChessboardEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BOARD_TYPE, TYPE_XIANGQI);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.getEntityData().set(BOARD_TYPE, compound.getString("BoardType"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putString("BoardType", this.getBoardType());
    }

    public String getBoardType() {
        return this.getEntityData().get(BOARD_TYPE);
    }

    public void setBoardType(String type) {
        this.getEntityData().set(BOARD_TYPE, type);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            try {
                // 每个tick都修正位置（轻量操作）
                BlockPos pos = this.blockPosition();
                if (pos != null && this.level().isInWorldBounds(pos)) {
                    double targetY = pos.getY() + 0.8;
                    if (Math.abs(this.getY() - targetY) > 0.01) {
                        this.setPos(this.getX(), targetY, this.getZ());
                    }
                }

                // 降低检查频率：每20个tick（1秒）检查一次方块有效性
                tickCounter++;
                if (tickCounter >= CHECK_INTERVAL) {
                    tickCounter = 0;

                    if (pos == null || !this.level().isInWorldBounds(pos)) {
                        this.discard();
                        return;
                    }

                    BlockState state = this.level().getBlockState(pos);
                    if (state == null || state.isAir()) {
                        this.discard();
                        return;
                    }

                    if (!(state.getBlock() instanceof XiangqiTableBlock) && !(state.getBlock() instanceof BadukTableBlock) && !(state.getBlock() instanceof FlightchessTableBlock)) {
                        this.discard();
                        return;
                    }
                }
            } catch (Exception e) {
                this.discard();
            }
        }
        super.tick();
    }
}