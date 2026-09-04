package com.takia.lets_chess;

import com.takia.lets_chess.client.PlacementPreviewRenderer;
import com.takia.lets_chess.client.ChessboardRenderer;
import com.takia.lets_chess.client.ChessboardEntityRenderer;
import com.takia.lets_chess.entity.ChessboardEntity;
import com.takia.lets_chess.entity.ModEntities;
import com.takia.lets_chess.event.ClientTooltipHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Letschess.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Letschess.MODID, value = Dist.CLIENT)
public class LetschessClient {
    public LetschessClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // 注册物品提示框事件到游戏总线
        NeoForge.EVENT_BUS.addListener(ClientTooltipHandler::onItemTooltip);

        // 注册放置预览渲染器到事件总线
        NeoForge.EVENT_BUS.addListener(PlacementPreviewRenderer::onRenderLevel);

        // 注册棋盘线条渲染器到事件总线（保留作为备用）
        NeoForge.EVENT_BUS.addListener(ChessboardRenderer::onRenderLevel);

        // 注册棋盘实体渲染器
        EntityRenderers.register(ModEntities.CHESSBOARD_ENTITY.get(), ChessboardEntityRenderer::new);

        // Some client setup code
        Letschess.LOGGER.info("HELLO FROM CLIENT SETUP");
        Letschess.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}