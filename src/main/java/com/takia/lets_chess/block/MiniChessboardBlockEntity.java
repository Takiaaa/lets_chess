package com.takia.lets_chess.block;

import com.takia.lets_chess.Letschess;
import com.takia.lets_chess.block.entity.ModBlockEntities;
import com.takia.lets_chess.gui.MenuTypes;
import com.takia.lets_chess.gui.MiniChessboardMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MiniChessboardBlockEntity extends BlockEntity implements MenuProvider {

    public static final String TYPE_CHESS = "chess";
    public static final String TYPE_XIANGQI = "xiangqi";
    public static final String TYPE_FLIGHTCHESS = "flightchess";

    private static final int SIZE_CHESS = 8;
    private static final int SIZE_XIANGQI = 17;
    private static final int SIZE_XIANGQI_HEIGHT = 19;
    private static final int SIZE_FLIGHTCHESS = 15;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> worldPosition.getX();
                case 1 -> worldPosition.getY();
                case 2 -> worldPosition.getZ();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public MiniChessboardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MINI_CHESSBOARD.get(), pos, state);
    }

    public String getBoardType() {
        BlockState state = getBlockState();
        if (state.is(ModBlocks.MINI_CHESSBOARD_CHESS.get())) {
            return TYPE_CHESS;
        } else if (state.is(ModBlocks.MINI_CHESSBOARD_XIANGQI.get())) {
            return TYPE_XIANGQI;
        } else if (state.is(ModBlocks.MINI_CHESSBOARD_FLIGHTCHESS.get())) {
            return TYPE_FLIGHTCHESS;
        }
        return TYPE_CHESS;
    }

    public BlockPos getWorkAreaMin() {
        return this.worldPosition.offset(1, 0, 1);
    }

    public BlockPos getWorkAreaMax() {
        String type = getBoardType();
        if (TYPE_CHESS.equals(type)) {
            return this.worldPosition.offset(SIZE_CHESS, 0, SIZE_CHESS);
        } else if (TYPE_XIANGQI.equals(type)) {
            return this.worldPosition.offset(SIZE_XIANGQI, 0, SIZE_XIANGQI_HEIGHT);
        } else if (TYPE_FLIGHTCHESS.equals(type)) {
            return this.worldPosition.offset(SIZE_FLIGHTCHESS, 0, SIZE_FLIGHTCHESS);
        }
        return this.worldPosition.offset(SIZE_CHESS, 0, SIZE_CHESS);
    }

    public int getWorkAreaWidth() {
        String type = getBoardType();
        if (TYPE_CHESS.equals(type)) return SIZE_CHESS;
        if (TYPE_XIANGQI.equals(type)) return SIZE_XIANGQI;
        if (TYPE_FLIGHTCHESS.equals(type)) return SIZE_FLIGHTCHESS;
        return SIZE_CHESS;
    }

    public int getWorkAreaHeight() {
        String type = getBoardType();
        if (TYPE_XIANGQI.equals(type)) return SIZE_XIANGQI_HEIGHT;
        return getWorkAreaWidth();
    }

    public void openGUI(ServerPlayer player) {
        player.openMenu(this, (buf) -> buf.writeBlockPos(this.worldPosition));
    }

    @Override
    public Component getDisplayName() {
        BlockState state = getBlockState();
        if (state.is(ModBlocks.MINI_CHESSBOARD_XIANGQI.get())) {
            return Component.translatable("container.lets_chess.mini_chessboard.xiangqi");
        } else if (state.is(ModBlocks.MINI_CHESSBOARD_CHESS.get())) {
            return Component.translatable("container.lets_chess.mini_chessboard.chess");
        } else if (state.is(ModBlocks.MINI_CHESSBOARD_FLIGHTCHESS.get())) {
            return Component.translatable("container.lets_chess.mini_chessboard.flightchess");
        }
        return Component.translatable("container.lets_chess.mini_chessboard");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, net.minecraft.world.entity.player.Player player) {
        return new MiniChessboardMenu(containerId, playerInventory, this.data, this.worldPosition, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
    }
}