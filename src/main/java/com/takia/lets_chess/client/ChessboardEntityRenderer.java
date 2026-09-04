package com.takia.lets_chess.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import com.takia.lets_chess.entity.ChessboardEntity;

public class ChessboardEntityRenderer extends EntityRenderer<ChessboardEntity> {

    private static final ResourceLocation XIANGQI_TEXTURE =
            ResourceLocation.parse("lets_chess:block/chessboard_xiangqi");
    private static final ResourceLocation BADUK_TEXTURE =
            ResourceLocation.parse("lets_chess:block/chessboard_baduk");
    private static final ResourceLocation FLIGHTCHESS_TEXTURE =
            ResourceLocation.parse("lets_chess:block/chessboard_flightchess");

    public ChessboardEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ChessboardEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(ChessboardEntity entity, float entityYaw, float partialTicks,
                      PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0, 0.22, 0);
        poseStack.scale(19.0F, 0.1F, 19.0F);

        String boardType = entity.getBoardType();
        ResourceLocation texture;
        if (ChessboardEntity.TYPE_XIANGQI.equals(boardType)) {
            texture = XIANGQI_TEXTURE;
        } else if (ChessboardEntity.TYPE_BADUK.equals(boardType)) {
            texture = BADUK_TEXTURE;
        } else {
            texture = FLIGHTCHESS_TEXTURE;
        }

        try {
            Minecraft mc = Minecraft.getInstance();
            TextureAtlasSprite sprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);

            if (sprite != null) {
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.cutout());
                float u0 = sprite.getU0();
                float u1 = sprite.getU1();
                float v0 = sprite.getV0();
                float v1 = sprite.getV1();

                var pose = poseStack.last();

                // 顶点顺序：左上 → 左下 → 右下 → 右上（逆时针）
                // 对应纹理：(u0,v0) → (u0,v1) → (u1,v1) → (u1,v0)
                consumer.addVertex(pose.pose(), -0.5F, 0.0F, -0.5F)  // 左上 (西北)
                       .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                       .setUv(u0, v0)  // 纹理左上
                       .setUv1(0, 10)
                       .setUv2(0, 240)
                       .setNormal(0.0F, 1.0F, 0.0F);

                consumer.addVertex(pose.pose(), -0.5F, 0.0F, 0.5F)   // 左下 (西南)
                       .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                       .setUv(u0, v1)  // 纹理左下
                       .setUv1(0, 10)
                       .setUv2(0, 240)
                       .setNormal(0.0F, 1.0F, 0.0F);

                consumer.addVertex(pose.pose(), 0.5F, 0.0F, 0.5F)    // 右下 (东南)
                       .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                       .setUv(u1, v1)  // 纹理右下
                       .setUv1(0, 10)
                       .setUv2(0, 240)
                       .setNormal(0.0F, 1.0F, 0.0F);

                consumer.addVertex(pose.pose(), 0.5F, 0.0F, -0.5F)   // 右上 (东北)
                       .setColor(1.0F, 1.0F, 1.0F, 1.0F)
                       .setUv(u1, v0)  // 纹理右上
                       .setUv1(0, 10)
                       .setUv2(0, 240)
                       .setNormal(0.0F, 1.0F, 0.0F);
            }
        } catch (Exception e) {
            // 纹理加载失败时静默处理
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}