package com.takia.lets_chess.block;

import java.util.function.Supplier;
import com.takia.lets_chess.Letschess;
import com.takia.lets_chess.item.ChessPieceItem;
import com.takia.lets_chess.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Letschess.MODID);

    public static final DeferredBlock<FlightchessBlock> FLIGHTCHESS_BLUE =
            registerChessPieceBlocks("flightchess_blue", () -> new FlightchessBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_CONCRETE)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.HARP)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)));

    public static final DeferredBlock<FlightchessBlock> FLIGHTCHESS_RED =
            registerChessPieceBlocks("flightchess_red", () -> new FlightchessBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CONCRETE)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.HARP)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)));

    public static final DeferredBlock<FlightchessBlock> FLIGHTCHESS_YELLOW =
            registerChessPieceBlocks("flightchess_yellow", () -> new FlightchessBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_CONCRETE)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.HARP)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)));

    public static final DeferredBlock<FlightchessBlock> FLIGHTCHESS_GREEN =
            registerChessPieceBlocks("flightchess_green", () -> new FlightchessBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_CONCRETE)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.HARP)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)));

    // 红色象棋
    public static final DeferredBlock<XiangqiBlock> XIANGQI_RED_GENERAL =
            registerChessPieceBlocks("xiangqi_red_general", () -> new XiangqiBlock(xiangqiProperties(Blocks.RED_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_RED_ADVISOR =
            registerChessPieceBlocks("xiangqi_red_advisor", () -> new XiangqiBlock(xiangqiProperties(Blocks.RED_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_RED_BISHOP =
            registerChessPieceBlocks("xiangqi_red_bishop", () -> new XiangqiBlock(xiangqiProperties(Blocks.RED_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_RED_HORSE =
            registerChessPieceBlocks("xiangqi_red_horse", () -> new XiangqiBlock(xiangqiProperties(Blocks.RED_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_RED_CHARIOT =
            registerChessPieceBlocks("xiangqi_red_chariot", () -> new XiangqiBlock(xiangqiProperties(Blocks.RED_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_RED_CANNON =
            registerChessPieceBlocks("xiangqi_red_cannon", () -> new XiangqiBlock(xiangqiProperties(Blocks.RED_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_RED_SOLDIER =
            registerChessPieceBlocks("xiangqi_red_soldier", () -> new XiangqiBlock(xiangqiProperties(Blocks.RED_CONCRETE)));

    // 黑色象棋
    public static final DeferredBlock<XiangqiBlock> XIANGQI_BLACK_GENERAL =
            registerChessPieceBlocks("xiangqi_black_general", () -> new XiangqiBlock(xiangqiProperties(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_BLACK_ADVISOR =
            registerChessPieceBlocks("xiangqi_black_advisor", () -> new XiangqiBlock(xiangqiProperties(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_BLACK_BISHOP =
            registerChessPieceBlocks("xiangqi_black_bishop", () -> new XiangqiBlock(xiangqiProperties(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_BLACK_HORSE =
            registerChessPieceBlocks("xiangqi_black_horse", () -> new XiangqiBlock(xiangqiProperties(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_BLACK_CHARIOT =
            registerChessPieceBlocks("xiangqi_black_chariot", () -> new XiangqiBlock(xiangqiProperties(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_BLACK_CANNON =
            registerChessPieceBlocks("xiangqi_black_cannon", () -> new XiangqiBlock(xiangqiProperties(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<XiangqiBlock> XIANGQI_BLACK_SOLDIER =
            registerChessPieceBlocks("xiangqi_black_soldier", () -> new XiangqiBlock(xiangqiProperties(Blocks.BLACK_CONCRETE)));



    // 骰子
    public static final DeferredBlock<DiceBlock> DICE_WHITE =
            registerBlocks("dice_white", () -> new DiceBlock(baseProperties(Blocks.WHITE_CONCRETE)));
    public static final DeferredBlock<DiceBlock> DICE_BLACK =
            registerBlocks("dice_black", () -> new DiceBlock(baseProperties(Blocks.BLACK_CONCRETE)));

    // 棋子
    public static final DeferredBlock<BadukBlock> PIECES_WHITE =
            registerChessPieceBlocks("pieces_white", () -> new BadukBlock(baseProperties(Blocks.WHITE_CONCRETE)));
    public static final DeferredBlock<BadukBlock> PIECES_BLACK =
            registerChessPieceBlocks("pieces_black", () -> new BadukBlock(baseProperties(Blocks.BLACK_CONCRETE)));

    // 象棋棋盘台
    public static final DeferredBlock<XiangqiTableBlock> CHESSBOARD_TABLE_XIANGQI =
            registerBlocks("chessboard_table_xiangqi", () -> new XiangqiTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE)));

    // 围棋棋盘台
    public static final DeferredBlock<BadukTableBlock> CHESSBOARD_TABLE_BADUK =
            registerBlocks("chessboard_table_baduk", () -> new BadukTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE)));

    // 飞行棋棋盘台
    public static final DeferredBlock<FlightchessTableBlock> CHESSBOARD_TABLE_FLIGHTCHESS =
            registerBlocks("chessboard_table_flightchess", () -> new FlightchessTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE)));

    // 小棋盘
    public static final DeferredBlock<MiniChessboardBlock> MINI_CHESSBOARD_XIANGQI =
            registerBlocks("mini_chessboard_xiangqi", () -> new MiniChessboardBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.HARP)));

    public static final DeferredBlock<MiniChessboardBlock> MINI_CHESSBOARD_CHESS =
            registerBlocks("mini_chessboard_chess", () -> new MiniChessboardBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.HARP)));

    public static final DeferredBlock<MiniChessboardBlock> MINI_CHESSBOARD_FLIGHTCHESS =
            registerBlocks("mini_chessboard_flightchess", () -> new MiniChessboardBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.HARP)));

    // 白方国际象棋
    public static final DeferredBlock<ChessBlock> CHESS_WHITE_KING = registerChessPieceBlocks("chess_white_king", () -> new ChessBlock(baseProperties(Blocks.WHITE_CONCRETE)));
    public static final DeferredBlock<ChessBlock> CHESS_WHITE_QUEEN = registerChessPieceBlocks("chess_white_queen", () -> new ChessBlock(baseProperties(Blocks.WHITE_CONCRETE)));
    public static final DeferredBlock<ChessBlock> CHESS_WHITE_BISHOP = registerChessPieceBlocks("chess_white_bishop", () -> new ChessBlock(baseProperties(Blocks.WHITE_CONCRETE)));
    public static final DeferredBlock<ChessBlock> CHESS_WHITE_KNIGHT = registerChessPieceBlocks("chess_white_knight", () -> new ChessBlock(baseProperties(Blocks.WHITE_CONCRETE)));
    public static final DeferredBlock<ChessBlock> CHESS_WHITE_ROOK = registerChessPieceBlocks("chess_white_rook", () -> new ChessBlock(baseProperties(Blocks.WHITE_CONCRETE)));
    public static final DeferredBlock<ChessBlock> CHESS_WHITE_PAWN = registerChessPieceBlocks("chess_white_pawn", () -> new ChessBlock(baseProperties(Blocks.WHITE_CONCRETE)));

    // 黑方国际象棋
    public static final DeferredBlock<ChessBlock> CHESS_BLACK_KING = registerChessPieceBlocks("chess_black_king", () -> new ChessBlock(baseProperties(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<ChessBlock> CHESS_BLACK_QUEEN = registerChessPieceBlocks("chess_black_queen", () -> new ChessBlock(baseProperties(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<ChessBlock> CHESS_BLACK_BISHOP = registerChessPieceBlocks("chess_black_bishop", () -> new ChessBlock(baseProperties(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<ChessBlock> CHESS_BLACK_KNIGHT = registerChessPieceBlocks("chess_black_knight", () -> new ChessBlock(baseProperties(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<ChessBlock> CHESS_BLACK_ROOK = registerChessPieceBlocks("chess_black_rook", () -> new ChessBlock(baseProperties(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<ChessBlock> CHESS_BLACK_PAWN = registerChessPieceBlocks("chess_black_pawn", () -> new ChessBlock(baseProperties(Blocks.BLACK_CONCRETE)));

    private static BlockBehaviour.Properties baseProperties(Block concreteBlock) {
        return BlockBehaviour.Properties.of()
                .mapColor(concreteBlock.defaultMapColor())
                .strength(0.1F, 1.0F)
                .sound(SoundType.STONE)
                .noOcclusion()
                .instrument(NoteBlockInstrument.HARP)
                .isRedstoneConductor((state, level, pos) -> false)
                .isSuffocating((state, level, pos) -> false);
    }

    private static BlockBehaviour.Properties xiangqiProperties(Block concreteBlock) {
        return BlockBehaviour.Properties.of()
                .mapColor(concreteBlock.defaultMapColor())
                .strength(0.1F, 1.0F)
                .sound(SoundType.WOOD)
                .noOcclusion()
                .instrument(NoteBlockInstrument.HARP)
                .isRedstoneConductor((state, level, pos) -> false)
                .isSuffocating((state, level, pos) -> false);
    }

    private static <T extends Block> void registerBlockItems(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> void registerChessPieceItems(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new ChessPieceItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> DeferredBlock<T> registerBlocks(String name, Supplier<T> block) {
        DeferredBlock<T> blocks = BLOCKS.register(name, block);
        registerBlockItems(name, blocks);
        return blocks;
    }

    private static <T extends Block> DeferredBlock<T> registerChessPieceBlocks(String name, Supplier<T> block) {
        DeferredBlock<T> blocks = BLOCKS.register(name, block);
        registerChessPieceItems(name, blocks);
        return blocks;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}