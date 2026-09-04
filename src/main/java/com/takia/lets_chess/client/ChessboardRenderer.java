package com.takia.lets_chess.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.takia.lets_chess.block.BadukTableBlock;
import com.takia.lets_chess.block.FlightchessTableBlock;
import com.takia.lets_chess.block.XiangqiTableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class ChessboardRenderer {

    private static final ResourceLocation XIANGQI_BOARD_TEXTURE =
            ResourceLocation.parse("lets_chess:block/chessboard_xiangqi");
    private static final ResourceLocation BADUK_BOARD_TEXTURE =
            ResourceLocation.parse("lets_chess:block/chessboard_baduk");
    private static final ResourceLocation FLIGHTCHESS_BOARD_TEXTURE =
            ResourceLocation.parse("lets_chess:block/chessboard_flightchess");

    private static final float BOARD_Y_OFFSET = 0.005F;
    private static final float BOARD_SIZE = 0.9375F;
    private static final int RENDER_DISTANCE = 16;

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }

        Level level = mc.level;
        PoseStack poseStack = event.getPoseStack();

        BlockPos playerPos = mc.player.blockPosition();

        for (int x = -RENDER_DISTANCE; x <= RENDER_DISTANCE; x++) {
            for (int y = -RENDER_DISTANCE; y <= RENDER_DISTANCE; y++) {
                for (int z = -RENDER_DISTANCE; z <= RENDER_DISTANCE; z++) {
                    BlockPos pos = playerPos.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);

                    if (state.getBlock() instanceof XiangqiTableBlock) {
                        poseStack.pushPose();
                        renderBoard(poseStack, pos, XIANGQI_BOARD_TEXTURE);
                        poseStack.popPose();
                    } else if (state.getBlock() instanceof BadukTableBlock) {
                        poseStack.pushPose();
                        renderBoard(poseStack, pos, BADUK_BOARD_TEXTURE);
                        poseStack.popPose();
                    } else if (state.getBlock() instanceof FlightchessTableBlock) {
                        poseStack.pushPose();
                        renderBoard(poseStack, pos, FLIGHTCHESS_BOARD_TEXTURE);
                        poseStack.popPose();
                    }
                }
            }
        }
    }

    private static void renderBoard(PoseStack poseStack, BlockPos pos, ResourceLocation texture) {
        poseStack.translate(pos.getX() + 0.5, pos.getY() + BOARD_Y_OFFSET, pos.getZ() + 0.5);
        poseStack.scale(BOARD_SIZE, 0.01F, BOARD_SIZE);
        renderBoardTexture(poseStack, texture);
    }

    private static void renderBoardTexture(PoseStack poseStack, ResourceLocation textureLocation) {
        try {
            Minecraft mc = Minecraft.getInstance();
            TextureAtlasSprite sprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(textureLocation);

            if (sprite != null) {
                renderQuadWithSprite(poseStack, sprite);
            }
        } catch (Exception e) {
            // 纹理加载失败，静默处理
        }
    }

    private static void renderQuadWithSprite(PoseStack poseStack, TextureAtlasSprite sprite) {
        Minecraft mc = Minecraft.getInstance();
        net.minecraft.client.renderer.MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = bufferSource.getBuffer(net.minecraft.client.renderer.RenderType.cutout());

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        PoseStack.Pose pose = poseStack.last();

        consumer.addVertex(pose.pose(), -0.5F, 0.0F, -0.5F)
               .setColor(1.0F, 1.0F, 1.0F, 1.0F)
               .setUv(u0, v1)
               .setUv1(0, 10)
               .setUv2(0, 240)
               .setNormal(0.0F, 1.0F, 0.0F);

        consumer.addVertex(pose.pose(), -0.5F, 0.0F, 0.5F)
               .setColor(1.0F, 1.0F, 1.0F, 1.0F)
               .setUv(u0, v0)
               .setUv1(0, 10)
               .setUv2(0, 240)
               .setNormal(0.0F, 1.0F, 0.0F);

        consumer.addVertex(pose.pose(), 0.5F, 0.0F, 0.5F)
               .setColor(1.0F, 1.0F, 1.0F, 1.0F)
               .setUv(u1, v0)
               .setUv1(0, 10)
               .setUv2(0, 240)
               .setNormal(0.0F, 1.0F, 0.0F);

        consumer.addVertex(pose.pose(), 0.5F, 0.0F, -0.5F)
               .setColor(1.0F, 1.0F, 1.0F, 1.0F)
               .setUv(u1, v1)
               .setUv1(0, 10)
               .setUv2(0, 240)
               .setNormal(0.0F, 1.0F, 0.0F);
    }
}