package com.takia.lets_chess.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.takia.lets_chess.block.*;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class PlacementPreviewRenderer {

    private static final float RED = 0.0F;
    private static final float GREEN = 1.0F;
    private static final float BLUE = 0.8F;
    private static final float ALPHA = 0.4F;

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        ItemStack mainHand = player.getMainHandItem();
        if (!(mainHand.getItem() instanceof BlockItem blockItem)) return;

        Block block = blockItem.getBlock();
        if (!isChessPiece(block)) return;

        BlockHitResult hitResult = getPlayerPOVHitResult(mc.level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() != BlockHitResult.Type.BLOCK) return;

        BlockPos targetPos = hitResult.getBlockPos().relative(hitResult.getDirection());
        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();

        poseStack.pushPose();
        poseStack.translate(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);

        var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        for (AABB aabb : block.defaultBlockState().getShape(mc.level, targetPos).toAabbs()) {
            LevelRenderer.renderLineBox(
                    poseStack,
                    bufferSource.getBuffer(RenderType.lines()),
                    (float)(targetPos.getX() + aabb.minX),
                    (float)(targetPos.getY() + aabb.minY),
                    (float)(targetPos.getZ() + aabb.minZ),
                    (float)(targetPos.getX() + aabb.maxX),
                    (float)(targetPos.getY() + aabb.maxY),
                    (float)(targetPos.getZ() + aabb.maxZ),
                    0.0F, 1.0F, 0.8F, 0.4F
            );
        }

        poseStack.popPose();
    }

    private static boolean isChessPiece(Block block) {
        return block instanceof FlightchessBlock ||
                block instanceof XiangqiBlock ||
                block instanceof BadukBlock ||
                block instanceof ChessBlock ||
                block instanceof DiceBlock;
    }

    private static BlockHitResult getPlayerPOVHitResult(net.minecraft.world.level.Level level, Player player, ClipContext.Fluid fluidMode) {
        double reach = player.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.BLOCK_INTERACTION_RANGE);
        return level.clip(new ClipContext(
                player.getEyePosition(1.0F),
                player.getEyePosition(1.0F).add(player.getViewVector(1.0F).scale(reach)),
                ClipContext.Block.OUTLINE,
                fluidMode,
                player
        ));
    }
}