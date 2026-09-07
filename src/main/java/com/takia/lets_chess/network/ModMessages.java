package com.takia.lets_chess.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ModMessages {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToServer(
                TakeBackC2SPacket.TYPE,
                TakeBackC2SPacket.STREAM_CODEC,
                TakeBackC2SPacket::handle
        );
        registrar.playToServer(
                ArrangeC2SPacket.TYPE,
                ArrangeC2SPacket.STREAM_CODEC,
                ArrangeC2SPacket::handle
        );
        registrar.playToClient(
                OperationResultS2CPacket.TYPE,
                OperationResultS2CPacket.STREAM_CODEC,
                OperationResultS2CPacket::handle
        );
    }
}