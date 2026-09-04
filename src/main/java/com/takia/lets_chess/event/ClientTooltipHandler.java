package com.takia.lets_chess.event;

import com.takia.lets_chess.block.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class ClientTooltipHandler {

    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (!(itemStack.getItem() instanceof BlockItem blockItem)) return;

        Block block = blockItem.getBlock();

        if (block instanceof DiceBlock) {
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.dice"));
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.pickup_hint"));
            return;
        }

        if (block instanceof XiangqiBlock) {
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.xiangqi"));
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.pickup_hint"));
        } else if (block instanceof FlightchessBlock) {
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.flightchess"));
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.pickup_hint"));
        } else if (block instanceof BadukBlock) {
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.baduk"));
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.pickup_hint"));
        } else if (block instanceof ChessBlock) {
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.chess"));
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.pickup_hint"));
        } else if (block instanceof XiangqiTableBlock) {
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.chessboard_table_xiangqi"));
        } else if (block instanceof BadukTableBlock) {
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.chessboard_table_baduk"));
        } else if (block instanceof FlightchessTableBlock) {
            event.getToolTip().add(Component.translatable("tooltip.lets_chess.chessboard_table_flightchess"));
        } else {
            return;
        }
    }
}