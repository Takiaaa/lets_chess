package com.takia.lets_chess.network;

import com.takia.lets_chess.Letschess;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OperationResultS2CPacket(BlockPos pos, Component message) implements CustomPacketPayload {
    public static final Type<OperationResultS2CPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("lets_chess", "operation_result"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OperationResultS2CPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, OperationResultS2CPacket::pos,
                    ComponentSerialization.STREAM_CODEC, OperationResultS2CPacket::message,
                    OperationResultS2CPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OperationResultS2CPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof net.minecraft.client.player.LocalPlayer) {
                Letschess.LOGGER.info("[S2C] 收到操作结果: pos={}, message={}", packet.pos, packet.message.getString());
                net.minecraft.client.Minecraft.getInstance().execute(() -> {
                    var screen = net.minecraft.client.Minecraft.getInstance().screen;
                    if (screen instanceof com.takia.lets_chess.gui.MiniChessboardScreen chessScreen) {
                        chessScreen.setMessage(packet.message);
                        Letschess.LOGGER.info("[S2C] 已设置GUI消息");
                    } else {
                        Letschess.LOGGER.warn("[S2C] 当前屏幕不是MiniChessboardScreen: {}", screen != null ? screen.getClass().getSimpleName() : "null");
                    }
                });
            }
        });
    }
}