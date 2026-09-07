package com.takia.lets_chess.gui;

import com.takia.lets_chess.Letschess;
import com.takia.lets_chess.block.MiniChessboardBlockEntity;
import com.takia.lets_chess.network.TakeBackC2SPacket;
import com.takia.lets_chess.network.ArrangeC2SPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class MiniChessboardMenu extends AbstractContainerMenu {
    private final ContainerData data;
    private final net.minecraft.world.entity.player.Player player;
    private final BlockPos chessboardPos;
    private final MiniChessboardBlockEntity blockEntity;

    public MiniChessboardMenu(int containerId, Inventory playerInventory, ContainerData data, BlockPos pos, MiniChessboardBlockEntity blockEntity) {
        super(MenuTypes.MINI_CHESSBOARD_MENU.get(), containerId);
        this.data = data;
        this.player = playerInventory.player;
        this.chessboardPos = pos;
        this.blockEntity = blockEntity;
        this.addDataSlots(data);
        addPlayerInventorySlots(playerInventory);
        Letschess.LOGGER.debug("[Menu] 服务端菜单创建: pos={}, player={}", pos, playerInventory.player.getName().getString());
    }

    public MiniChessboardMenu(int containerId, Inventory playerInventory, BlockPos pos) {
        super(MenuTypes.MINI_CHESSBOARD_MENU.get(), containerId);
        this.data = new SimpleContainerData(3);
        this.player = playerInventory.player;
        this.chessboardPos = pos;
        this.blockEntity = null;
        this.addDataSlots(data);
        addPlayerInventorySlots(playerInventory);
        Letschess.LOGGER.debug("[Menu] 客户端菜单创建: pos={}, player={}", pos, playerInventory.player.getName().getString());
    }

    private void addPlayerInventorySlots(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new net.minecraft.world.inventory.Slot(playerInventory,
                        col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new net.minecraft.world.inventory.Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(net.minecraft.world.entity.player.Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return true;
    }

    public BlockPos getChessboardPosition() {
        return chessboardPos;
    }

    public void sendTakeBackPacket() {
        if (!player.level().isClientSide) return;
        Letschess.LOGGER.debug("[Menu] 发送回收数据包: chessboardPos={}", chessboardPos);
        PacketDistributor.sendToServer(new TakeBackC2SPacket(chessboardPos));
    }

    public void sendArrangePacket() {
        if (!player.level().isClientSide) return;
        Letschess.LOGGER.debug("[Menu] 发送摆放数据包: chessboardPos={}", chessboardPos);
        PacketDistributor.sendToServer(new ArrangeC2SPacket(chessboardPos));
    }
}