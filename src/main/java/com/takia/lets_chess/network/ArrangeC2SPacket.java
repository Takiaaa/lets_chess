package com.takia.lets_chess.network;

import com.takia.lets_chess.Letschess;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ArrangeC2SPacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<ArrangeC2SPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("lets_chess", "arrange"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ArrangeC2SPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, ArrangeC2SPacket::pos,
                    ArrangeC2SPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ArrangeC2SPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            Letschess.LOGGER.info("[Packet] 收到摆放数据包: pos={}, player={}", packet.pos, player.getName().getString());
            ChessboardOperationHandler.handleArrange(player, packet.pos);
        });
    }
}