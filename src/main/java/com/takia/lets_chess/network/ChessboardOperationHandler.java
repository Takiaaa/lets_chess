package com.takia.lets_chess.network;

import com.takia.lets_chess.Letschess;
import com.takia.lets_chess.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

public class ChessboardOperationHandler {

    private static final String[][] CHESS_OPENING = {
            {"black_rook", "black_knight", "black_bishop", "black_queen",
                    "black_king", "black_bishop", "black_knight", "black_rook"},
            {"black_pawn", "black_pawn", "black_pawn", "black_pawn",
                    "black_pawn", "black_pawn", "black_pawn", "black_pawn"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"white_pawn", "white_pawn", "white_pawn", "white_pawn",
                    "white_pawn", "white_pawn", "white_pawn", "white_pawn"},
            {"white_rook", "white_knight", "white_bishop", "white_queen",
                    "white_king", "white_bishop", "white_knight", "white_rook"}
    };

    private static final String[][] XIANGQI_OPENING = new String[19][17];
    static {
        for (int r = 0; r < 19; r++) {
            Arrays.fill(XIANGQI_OPENING[r], "");
        }
        String[] redBack = {"red_chariot", "red_horse", "red_bishop", "red_advisor", "red_general", "red_advisor", "red_bishop", "red_horse", "red_chariot"};
        for (int c = 0; c < 9; c++) XIANGQI_OPENING[0][c * 2] = redBack[c];
        XIANGQI_OPENING[4][1 * 2] = "red_cannon";
        XIANGQI_OPENING[4][7 * 2] = "red_cannon";
        for (int c = 0; c < 9; c += 2) XIANGQI_OPENING[6][c * 2] = "red_soldier";

        String[] blackBack = {"black_chariot", "black_horse", "black_bishop", "black_advisor", "black_general", "black_advisor", "black_bishop", "black_horse", "black_chariot"};
        for (int c = 0; c < 9; c++) XIANGQI_OPENING[18][c * 2] = blackBack[c];
        XIANGQI_OPENING[14][1 * 2] = "black_cannon";
        XIANGQI_OPENING[14][7 * 2] = "black_cannon";
        for (int c = 0; c < 9; c += 2) XIANGQI_OPENING[12][c * 2] = "black_soldier";
    }

    private static final String[][] FLIGHTCHESS_OPENING = new String[15][15];
    static {
        for (int r = 0; r < 15; r++) {
            Arrays.fill(FLIGHTCHESS_OPENING[r], "");
        }
        FLIGHTCHESS_OPENING[0][1] = "yellow"; FLIGHTCHESS_OPENING[0][2] = "yellow";
        FLIGHTCHESS_OPENING[1][1] = "yellow"; FLIGHTCHESS_OPENING[1][2] = "yellow";
        FLIGHTCHESS_OPENING[1][13] = "blue"; FLIGHTCHESS_OPENING[1][14] = "blue";
        FLIGHTCHESS_OPENING[2][13] = "blue"; FLIGHTCHESS_OPENING[2][14] = "blue";

        FLIGHTCHESS_OPENING[12][0] = "red"; FLIGHTCHESS_OPENING[12][1] = "red";
        FLIGHTCHESS_OPENING[13][0] = "red"; FLIGHTCHESS_OPENING[13][1] = "red";
        FLIGHTCHESS_OPENING[13][12] = "green"; FLIGHTCHESS_OPENING[13][13] = "green";
        FLIGHTCHESS_OPENING[14][12] = "green"; FLIGHTCHESS_OPENING[14][13] = "green";
    }

    private static final Map<String, Block> CHESS_PIECE_MAP = new HashMap<>();
    private static final Map<String, Block> XIANGQI_PIECE_MAP = new HashMap<>();
    private static final Map<String, Block> FLIGHTCHESS_PIECE_MAP = new HashMap<>();
    private static final Map<String, Block> ALL_PIECE_MAP = new HashMap<>();

    static {
        CHESS_PIECE_MAP.put("white_king", ModBlocks.CHESS_WHITE_KING.get());
        CHESS_PIECE_MAP.put("white_queen", ModBlocks.CHESS_WHITE_QUEEN.get());
        CHESS_PIECE_MAP.put("white_bishop", ModBlocks.CHESS_WHITE_BISHOP.get());
        CHESS_PIECE_MAP.put("white_knight", ModBlocks.CHESS_WHITE_KNIGHT.get());
        CHESS_PIECE_MAP.put("white_rook", ModBlocks.CHESS_WHITE_ROOK.get());
        CHESS_PIECE_MAP.put("white_pawn", ModBlocks.CHESS_WHITE_PAWN.get());
        CHESS_PIECE_MAP.put("black_king", ModBlocks.CHESS_BLACK_KING.get());
        CHESS_PIECE_MAP.put("black_queen", ModBlocks.CHESS_BLACK_QUEEN.get());
        CHESS_PIECE_MAP.put("black_bishop", ModBlocks.CHESS_BLACK_BISHOP.get());
        CHESS_PIECE_MAP.put("black_knight", ModBlocks.CHESS_BLACK_KNIGHT.get());
        CHESS_PIECE_MAP.put("black_rook", ModBlocks.CHESS_BLACK_ROOK.get());
        CHESS_PIECE_MAP.put("black_pawn", ModBlocks.CHESS_BLACK_PAWN.get());

        XIANGQI_PIECE_MAP.put("red_general", ModBlocks.XIANGQI_RED_GENERAL.get());
        XIANGQI_PIECE_MAP.put("red_advisor", ModBlocks.XIANGQI_RED_ADVISOR.get());
        XIANGQI_PIECE_MAP.put("red_bishop", ModBlocks.XIANGQI_RED_BISHOP.get());
        XIANGQI_PIECE_MAP.put("red_horse", ModBlocks.XIANGQI_RED_HORSE.get());
        XIANGQI_PIECE_MAP.put("red_chariot", ModBlocks.XIANGQI_RED_CHARIOT.get());
        XIANGQI_PIECE_MAP.put("red_cannon", ModBlocks.XIANGQI_RED_CANNON.get());
        XIANGQI_PIECE_MAP.put("red_soldier", ModBlocks.XIANGQI_RED_SOLDIER.get());
        XIANGQI_PIECE_MAP.put("black_general", ModBlocks.XIANGQI_BLACK_GENERAL.get());
        XIANGQI_PIECE_MAP.put("black_advisor", ModBlocks.XIANGQI_BLACK_ADVISOR.get());
        XIANGQI_PIECE_MAP.put("black_bishop", ModBlocks.XIANGQI_BLACK_BISHOP.get());
        XIANGQI_PIECE_MAP.put("black_horse", ModBlocks.XIANGQI_BLACK_HORSE.get());
        XIANGQI_PIECE_MAP.put("black_chariot", ModBlocks.XIANGQI_BLACK_CHARIOT.get());
        XIANGQI_PIECE_MAP.put("black_cannon", ModBlocks.XIANGQI_BLACK_CANNON.get());
        XIANGQI_PIECE_MAP.put("black_soldier", ModBlocks.XIANGQI_BLACK_SOLDIER.get());

        FLIGHTCHESS_PIECE_MAP.put("yellow", ModBlocks.FLIGHTCHESS_YELLOW.get());
        FLIGHTCHESS_PIECE_MAP.put("blue", ModBlocks.FLIGHTCHESS_BLUE.get());
        FLIGHTCHESS_PIECE_MAP.put("red", ModBlocks.FLIGHTCHESS_RED.get());
        FLIGHTCHESS_PIECE_MAP.put("green", ModBlocks.FLIGHTCHESS_GREEN.get());

        ALL_PIECE_MAP.putAll(CHESS_PIECE_MAP);
        ALL_PIECE_MAP.putAll(XIANGQI_PIECE_MAP);
        ALL_PIECE_MAP.putAll(FLIGHTCHESS_PIECE_MAP);

        Letschess.LOGGER.info("[Handler] 棋子映射初始化完成: chess={}, xiangqi={}, flightchess={}, total={}",
                CHESS_PIECE_MAP.size(), XIANGQI_PIECE_MAP.size(), FLIGHTCHESS_PIECE_MAP.size(), ALL_PIECE_MAP.size());
    }

    private static String detectBoardType(Level level, BlockPos boardPos) {
        BlockState state = level.getBlockState(boardPos);
        if (state.is(ModBlocks.MINI_CHESSBOARD_XIANGQI.get())) return "xiangqi";
        if (state.is(ModBlocks.MINI_CHESSBOARD_FLIGHTCHESS.get())) return "flightchess";
        return "chess";
    }

    private static BlockPos getWorkAreaMin(BlockPos boardPos) {
        return boardPos.offset(1, 0, 1);
    }

    private static BlockPos getWorkAreaMax(BlockPos boardPos, String boardType) {
        return switch (boardType) {
            case "xiangqi" -> boardPos.offset(17, 0, 19);
            case "flightchess" -> boardPos.offset(15, 0, 15);
            default -> boardPos.offset(8, 0, 8);
        };
    }

    private static Map<String, Block> getPieceMap(String boardType) {
        return switch (boardType) {
            case "xiangqi" -> XIANGQI_PIECE_MAP;
            case "flightchess" -> FLIGHTCHESS_PIECE_MAP;
            default -> CHESS_PIECE_MAP;
        };
    }

    private static String[][] getOpening(String boardType) {
        return switch (boardType) {
            case "xiangqi" -> XIANGQI_OPENING;
            case "flightchess" -> FLIGHTCHESS_OPENING;
            default -> CHESS_OPENING;
        };
    }

    public static void handleTakeBack(ServerPlayer player, BlockPos boardPos) {
        Level level = player.level();
        String boardType = detectBoardType(level, boardPos);
        BlockPos min = getWorkAreaMin(boardPos);
        BlockPos max = getWorkAreaMax(boardPos, boardType);
        Map<String, Block> pieceMap = getPieceMap(boardType);

        Letschess.LOGGER.info("[Handler-回收] 开始回收: boardPos={}, boardType={}, 工作区域=({})到({}), 维度={}",
                boardPos, boardType, min, max, level.dimension());

        List<ItemStack> collectedItems = new ArrayList<>();
        int removedCount = 0;
        int totalScanned = 0;

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                BlockPos pos = new BlockPos(x, min.getY(), z);
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();
                totalScanned++;

                if (pieceMap.containsValue(block)) {
                    ItemStack itemStack = new ItemStack(block.asItem());
                    if (!itemStack.isEmpty()) {
                        collectedItems.add(itemStack);
                        removedCount++;
                        Letschess.LOGGER.info("[Handler-回收] 发现棋子: pos={}, block={}, item={}", pos, block, itemStack);
                    }
                    level.destroyBlock(pos, false);
                    Letschess.LOGGER.info("[Handler-回收] 已破坏方块: pos={}", pos);
                }
            }
        }

        Letschess.LOGGER.info("[Handler-回收] 扫描完成: 总计{}格, 发现{}个棋子", totalScanned, removedCount);

        if (removedCount == 0) {
            sendResult(player, boardPos,
                    Component.literal("§e回收提示：工作区域内没有棋子需要回收"), true);
            return;
        }

        Inventory inventory = player.getInventory();
        List<ItemStack> remaining = new ArrayList<>();

        for (ItemStack stack : collectedItems) {
            if (!inventory.add(stack)) {
                remaining.add(stack);
            }
        }

        if (!remaining.isEmpty()) {
            for (ItemStack stack : remaining) {
                player.drop(stack, false);
            }
            sendResult(player, boardPos,
                    Component.translatable("message.lets_chess.takeback_success_partial"), true);
        } else {
            sendResult(player, boardPos,
                    Component.translatable("message.lets_chess.takeback_success"), true);
        }
    }

    public static void handleArrange(ServerPlayer player, BlockPos boardPos) {
        Level level = player.level();
        String boardType = detectBoardType(level, boardPos);
        BlockPos min = getWorkAreaMin(boardPos);
        BlockPos max = getWorkAreaMax(boardPos, boardType);
        Map<String, Block> pieceMap = getPieceMap(boardType);
        String[][] opening = getOpening(boardType);

        Letschess.LOGGER.info("[Handler-摆放] 开始摆放: boardPos={}, boardType={}, 工作区域=({})到({}), 维度={}",
                boardPos, boardType, min, max, level.dimension());

        int nonReplaceableCount = 0;
        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                BlockPos pos = new BlockPos(x, min.getY(), z);
                BlockState state = level.getBlockState(pos);
                if (!state.isAir() && !state.canBeReplaced()) {
                    nonReplaceableCount++;
                    Letschess.LOGGER.info("[Handler-摆放] 发现不可替代方块: pos={}, block={}", pos, state.getBlock());
                }
            }
        }

        if (nonReplaceableCount > 0) {
            Letschess.LOGGER.info("[Handler-摆放] 摆放失败: 发现{}个不可替代方块", nonReplaceableCount);
            sendResult(player, boardPos,
                    Component.translatable("message.lets_chess.arrange_failed_obstacle"), true);
            return;
        }

        boolean isCreative = player.isCreative();

        if (!isCreative) {
            Inventory inventory = player.getInventory();
            Map<String, Integer> required = countRequiredPieces(opening);
            Map<String, Integer> available = countAvailablePieces(inventory, pieceMap);

            Letschess.LOGGER.info("[Handler-摆放] 需要棋子: {}", required);
            Letschess.LOGGER.info("[Handler-摆放] 拥有棋子: {}", available);

            for (Map.Entry<String, Integer> entry : required.entrySet()) {
                String pieceKey = entry.getKey();
                int need = entry.getValue();
                int have = available.getOrDefault(pieceKey, 0);
                if (have < need) {
                    Letschess.LOGGER.info("[Handler-摆放] 棋子不足: key={}, 需要={}, 拥有={}", pieceKey, need, have);
                    sendResult(player, boardPos,
                            Component.translatable("message.lets_chess.arrange_failed_insufficient"), true);
                    return;
                }
            }

            for (Map.Entry<String, Integer> entry : required.entrySet()) {
                String pieceKey = entry.getKey();
                int need = entry.getValue();
                consumePieces(inventory, pieceKey, need, pieceMap);
                Letschess.LOGGER.info("[Handler-摆放] 消耗棋子: key={}, 数量={}", pieceKey, need);
            }
        } else {
            Letschess.LOGGER.info("[Handler-摆放] 创造模式: 跳过物品栏检查和消耗");
        }

        int placedCount = 0;
        for (int row = 0; row < opening.length; row++) {
            for (int col = 0; col < opening[row].length; col++) {
                String pieceKey = opening[row][col];
                if (pieceKey.isEmpty()) continue;

                Block block = pieceMap.get(pieceKey);
                if (block == null) {
                    Letschess.LOGGER.warn("[Handler-摆放] 棋子映射为null: key={}", pieceKey);
                    continue;
                }

                BlockPos targetPos = new BlockPos(
                        min.getX() + col,
                        min.getY(),
                        min.getZ() + row
                );

                BlockState placeState = block.defaultBlockState();
                if (boardType.equals("xiangqi")) {
                    Direction facing = pieceKey.startsWith("red_") ? Direction.NORTH : Direction.SOUTH;
                    placeState = placeState.setValue(HorizontalDirectionalBlock.FACING, facing);
                }

                level.setBlock(targetPos, placeState, 3);
                placedCount++;
                Letschess.LOGGER.info("[Handler-摆放] 放置棋子: pos={}, block={}, row={}, col={}", targetPos, block, row, col);
            }
        }

        Letschess.LOGGER.info("[Handler-摆放] 摆放完成: 共放置{}个棋子", placedCount);
        sendResult(player, boardPos,
                Component.translatable("message.lets_chess.arrange_success"), true);
    }

    private static Map<String, Integer> countRequiredPieces(String[][] opening) {
        Map<String, Integer> count = new HashMap<>();
        for (int row = 0; row < opening.length; row++) {
            for (int col = 0; col < opening[row].length; col++) {
                String key = opening[row][col];
                if (!key.isEmpty()) {
                    count.put(key, count.getOrDefault(key, 0) + 1);
                }
            }
        }
        return count;
    }

    private static Map<String, Integer> countAvailablePieces(Inventory inventory, Map<String, Block> pieceMap) {
        Map<String, Integer> count = new HashMap<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) continue;
            Item item = stack.getItem();
            if (!(item instanceof BlockItem blockItem)) continue;
            Block block = blockItem.getBlock();

            for (Map.Entry<String, Block> entry : pieceMap.entrySet()) {
                if (entry.getValue().equals(block)) {
                    count.put(entry.getKey(), count.getOrDefault(entry.getKey(), 0) + stack.getCount());
                    break;
                }
            }
        }
        return count;
    }

    private static void consumePieces(Inventory inventory, String pieceKey, int amount, Map<String, Block> pieceMap) {
        int remaining = amount;
        Block targetBlock = pieceMap.get(pieceKey);
        if (targetBlock == null) return;

        for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) continue;
            if (!(stack.getItem() instanceof BlockItem blockItem)) continue;
            if (!blockItem.getBlock().equals(targetBlock)) continue;

            int toRemove = Math.min(remaining, stack.getCount());
            stack.shrink(toRemove);
            remaining -= toRemove;
            if (stack.isEmpty()) {
                inventory.setItem(i, ItemStack.EMPTY);
            }
            Letschess.LOGGER.debug("[Handler] 消耗物品: slot={}, removed={}, remaining={}", i, toRemove, remaining);
        }
    }

    private static void sendResult(ServerPlayer player, BlockPos boardPos, Component message, boolean toActionBar) {
        Letschess.LOGGER.info("[Handler] 发送结果: player={}, message={}", player.getName().getString(), message.getString());
        PacketDistributor.sendToPlayer(player, new OperationResultS2CPacket(boardPos, message));
        if (toActionBar) {
            player.displayClientMessage(message, true);
        }
    }
}