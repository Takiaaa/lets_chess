package com.takia.lets_chess.item;

import com.takia.lets_chess.Letschess;
import com.takia.lets_chess.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Letschess.MODID);

    public static final Supplier<CreativeModeTab> LETS_CHESS_TAB =
            CREATIVE_MODE_TABS.register("lets_chess_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.FLIGHTCHESS_BLUE.get()))
                    .title(Component.translatable("itemGroup.lets_chess_tab"))
                    .displayItems((parameters, output) -> {
                        // 飞行棋
                        output.accept(ModBlocks.FLIGHTCHESS_BLUE);
                        output.accept(ModBlocks.FLIGHTCHESS_RED);
                        output.accept(ModBlocks.FLIGHTCHESS_YELLOW);
                        output.accept(ModBlocks.FLIGHTCHESS_GREEN);
                        // 红色象棋
                        output.accept(ModBlocks.XIANGQI_RED_GENERAL);
                        output.accept(ModBlocks.XIANGQI_RED_ADVISOR);
                        output.accept(ModBlocks.XIANGQI_RED_BISHOP);
                        output.accept(ModBlocks.XIANGQI_RED_HORSE);
                        output.accept(ModBlocks.XIANGQI_RED_CHARIOT);
                        output.accept(ModBlocks.XIANGQI_RED_CANNON);
                        output.accept(ModBlocks.XIANGQI_RED_SOLDIER);
                        // 黑色象棋
                        output.accept(ModBlocks.XIANGQI_BLACK_GENERAL);
                        output.accept(ModBlocks.XIANGQI_BLACK_ADVISOR);
                        output.accept(ModBlocks.XIANGQI_BLACK_BISHOP);
                        output.accept(ModBlocks.XIANGQI_BLACK_HORSE);
                        output.accept(ModBlocks.XIANGQI_BLACK_CHARIOT);
                        output.accept(ModBlocks.XIANGQI_BLACK_CANNON);
                        output.accept(ModBlocks.XIANGQI_BLACK_SOLDIER);
                        // 骰子
                        output.accept(ModBlocks.DICE_WHITE);
                        output.accept(ModBlocks.DICE_BLACK);
                        // 围棋棋子
                        output.accept(ModBlocks.PIECES_WHITE);
                        output.accept(ModBlocks.PIECES_BLACK);
                        // 白方国际象棋
                        output.accept(ModBlocks.CHESS_WHITE_KING);
                        output.accept(ModBlocks.CHESS_WHITE_QUEEN);
                        output.accept(ModBlocks.CHESS_WHITE_BISHOP);
                        output.accept(ModBlocks.CHESS_WHITE_KNIGHT);
                        output.accept(ModBlocks.CHESS_WHITE_ROOK);
                        output.accept(ModBlocks.CHESS_WHITE_PAWN);
                        // 黑方国际象棋
                        output.accept(ModBlocks.CHESS_BLACK_KING);
                        output.accept(ModBlocks.CHESS_BLACK_QUEEN);
                        output.accept(ModBlocks.CHESS_BLACK_BISHOP);
                        output.accept(ModBlocks.CHESS_BLACK_KNIGHT);
                        output.accept(ModBlocks.CHESS_BLACK_ROOK);
                        output.accept(ModBlocks.CHESS_BLACK_PAWN);

                        // 棋盘台
                        output.accept(ModBlocks.CHESSBOARD_TABLE_XIANGQI);
                        output.accept(ModBlocks.CHESSBOARD_TABLE_BADUK);
                        output.accept(ModBlocks.CHESSBOARD_TABLE_FLIGHTCHESS);
                    }).build());
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}