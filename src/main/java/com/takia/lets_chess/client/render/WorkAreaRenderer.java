package com.takia.lets_chess.client.render;

import com.mojang.blaze3d.vertex.*;
import com.takia.lets_chess.block.MiniChessboardBlockEntity;
import com.takia.lets_chess.block.ModBlocks;
import com.takia.lets_chess.item.ChessPieceItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Vector4f;

import java.util.*;

public class WorkAreaRenderer {

    private static final Minecraft mc = Minecraft.getInstance();

    private record WorkArea(BlockPos min, BlockPos max, String boardType) {}

    private static final Vector4f CHESS_COLOR = new Vector4f(0.0F, 0.8F, 1.0F, 0.9F);
    private static final Vector4f XIANGQI_COLOR = new Vector4f(1.0F, 0.4F, 0.0F, 0.9F);
    private static final Vector4f FLIGHTCHESS_COLOR = new Vector4f(0.0F, 1.0F, 0.4F, 0.9F);
    private static final Vector4f CENTER_COLOR = new Vector4f(1.0F, 1.0F, 0.0F, 0.9F);

    @net.neoforged.bus.api.SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        if (mc.player == null || mc.level == null) return;

        if (!isWearingChessPiece(mc.player)) return;

        PoseStack poseStack = event.getPoseStack();
        var multiBufferSource = mc.renderBuffers().bufferSource();
        var camPos = event.getCamera().getPosition();
        var consumer = multiBufferSource.getBuffer(RenderType.lines());

        List<WorkArea> workAreas = getNearbyWorkAreas();

        if (!workAreas.isEmpty()) {
            Map<String, Set<BlockPos>> blocksByType = new LinkedHashMap<>();
            for (WorkArea area : workAreas) {
                Set<BlockPos> blocks = blocksByType.computeIfAbsent(area.boardType, k -> new HashSet<>());
                for (int bx = area.min.getX(); bx <= area.max.getX(); bx++) {
                    for (int bz = area.min.getZ(); bz <= area.max.getZ(); bz++) {
                        blocks.add(new BlockPos(bx, area.min.getY(), bz));
                    }
                }
            }

            for (Map.Entry<String, Set<BlockPos>> entry : blocksByType.entrySet()) {
                Vector4f color = switch (entry.getKey()) {
                    case "xiangqi" -> XIANGQI_COLOR;
                    case "flightchess" -> FLIGHTCHESS_COLOR;
                    default -> CHESS_COLOR;
                };
                renderShape(poseStack, consumer, entry.getValue(), camPos, color);
            }

            for (WorkArea area : workAreas) {
                renderCenterLine(poseStack, consumer, area, camPos, CENTER_COLOR);
            }

            multiBufferSource.endBatch(RenderType.lines());
        }
    }

    private static boolean isWearingChessPiece(Player player) {
        ItemStack helmet = player.getInventory().getArmor(3);
        return helmet.getItem() instanceof ChessPieceItem;
    }

    private static List<WorkArea> getNearbyWorkAreas() {
        List<WorkArea> areas = new ArrayList<>();
        if (mc.level == null || mc.player == null) return areas;

        BlockPos playerPos = mc.player.blockPosition();
        int renderDistance = 16;

        for (int x = -renderDistance; x <= renderDistance; x++) {
            for (int y = -renderDistance; y <= renderDistance; y++) {
                for (int z = -renderDistance; z <= renderDistance; z++) {
                    BlockPos pos = playerPos.offset(x, y, z);
                    var state = mc.level.getBlockState(pos);

                    String boardType = null;
                    if (state.is(ModBlocks.MINI_CHESSBOARD_CHESS.get())) boardType = "chess";
                    else if (state.is(ModBlocks.MINI_CHESSBOARD_XIANGQI.get())) boardType = "xiangqi";
                    else if (state.is(ModBlocks.MINI_CHESSBOARD_FLIGHTCHESS.get())) boardType = "flightchess";

                    if (boardType != null && mc.level.getBlockEntity(pos) instanceof MiniChessboardBlockEntity board) {
                        areas.add(new WorkArea(board.getWorkAreaMin(), board.getWorkAreaMax(), boardType));
                    }
                }
            }
        }
        return areas;
    }

    private static VoxelShape convertSelectionToVoxelShape(Set<BlockPos> selectedBlocks) {
        VoxelShape combinedShape = Shapes.empty();
        for (BlockPos pos : selectedBlocks) {
            VoxelShape blockShape = Shapes.block();
            blockShape = blockShape.move(pos.getX(), pos.getY(), pos.getZ());
            combinedShape = Shapes.or(combinedShape, blockShape);
        }
        return combinedShape;
    }

    private static void renderShape(PoseStack poseStack, VertexConsumer consumer, Set<BlockPos> blocks,
                                    Vec3 cameraPosition, Vector4f color) {
        poseStack.pushPose();
        VoxelShape shape = convertSelectionToVoxelShape(blocks);
        var pose = poseStack.last();

        shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            bufferLine(pose, consumer,
                    (float)(x1 - cameraPosition.x), (float)(y1 - cameraPosition.y), (float)(z1 - cameraPosition.z),
                    (float)(x2 - cameraPosition.x), (float)(y2 - cameraPosition.y), (float)(z2 - cameraPosition.z),
                    color);
        });
        poseStack.popPose();
    }

    private static void renderCenterLine(PoseStack poseStack, VertexConsumer consumer,
                                         WorkArea area, Vec3 cameraPosition, Vector4f color) {
        int width = area.max.getX() - area.min.getX() + 1;
        int depth = area.max.getZ() - area.min.getZ() + 1;
        float centerX = area.min.getX() + width / 2.0f;
        float centerZ = area.min.getZ() + depth / 2.0f;
        float y1 = area.min.getY();
        float y2 = area.min.getY() + 1.0f;

        poseStack.pushPose();
        var pose = poseStack.last();

        bufferLine(pose, consumer,
                (float)(centerX - cameraPosition.x), (float)(y1 - cameraPosition.y), (float)(centerZ - cameraPosition.z),
                (float)(centerX - cameraPosition.x), (float)(y2 - cameraPosition.y), (float)(centerZ - cameraPosition.z),
                color);

        poseStack.popPose();
    }

    private static void bufferLine(PoseStack.Pose pose, VertexConsumer consumer,
                                   float x1, float y1, float z1, float x2, float y2, float z2,
                                   Vector4f color) {
        consumer.addVertex(pose.pose(), x1, y1, z1)
                .setColor(color.x(), color.y(), color.z(), color.w())
                .setLight(15728880)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
        consumer.addVertex(pose.pose(), x2, y2, z2)
                .setColor(color.x(), color.y(), color.z(), color.w())
                .setLight(15728880)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}