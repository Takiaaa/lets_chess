package com.takia.lets_chess;

import com.takia.lets_chess.client.PlacementPreviewRenderer;
import com.takia.lets_chess.client.ChessboardRenderer;
import com.takia.lets_chess.client.ChessboardEntityRenderer;
import com.takia.lets_chess.client.render.WorkAreaRenderer;
import com.takia.lets_chess.block.ModBlocks;
import com.takia.lets_chess.entity.ModEntities;
import com.takia.lets_chess.event.ClientTooltipHandler;
import com.takia.lets_chess.gui.MenuTypes;
import com.takia.lets_chess.gui.MiniChessboardScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Letschess.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Letschess.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class LetschessClient {
    public LetschessClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(ClientTooltipHandler::onItemTooltip);
        NeoForge.EVENT_BUS.addListener(PlacementPreviewRenderer::onRenderLevel);
        NeoForge.EVENT_BUS.addListener(ChessboardRenderer::onRenderLevel);
        NeoForge.EVENT_BUS.register(com.takia.lets_chess.client.render.WorkAreaRenderer.class);
        EntityRenderers.register(ModEntities.CHESSBOARD_ENTITY.get(), ChessboardEntityRenderer::new);

        event.enqueueWork(() -> {
            net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(ModBlocks.MINI_CHESSBOARD_XIANGQI.get(), RenderType.cutout());
            net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(ModBlocks.MINI_CHESSBOARD_CHESS.get(), RenderType.cutout());
            net.minecraft.client.renderer.ItemBlockRenderTypes.setRenderLayer(ModBlocks.MINI_CHESSBOARD_FLIGHTCHESS.get(), RenderType.cutout());
        });

        Letschess.LOGGER.info("HELLO FROM CLIENT SETUP");
        Letschess.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypes.MINI_CHESSBOARD_MENU.get(), MiniChessboardScreen::new);
    }
}