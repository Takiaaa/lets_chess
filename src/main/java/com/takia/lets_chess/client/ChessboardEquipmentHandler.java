package com.takia.lets_chess.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = "lets_chess", value = Dist.CLIENT)
public class ChessboardEquipmentHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        // 工作区域线框显示已移至 WorkAreaRenderer
        // 该渲染器直接检测玩家头盔是否为棋子(ChessPieceItem)
        // 无需在此处手动设置BlockEntity的showingRange
    }
}