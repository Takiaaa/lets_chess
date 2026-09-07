package com.takia.lets_chess.gui;

import com.takia.lets_chess.Letschess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.flag.FeatureFlags;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(Registries.MENU, Letschess.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<MiniChessboardMenu>> MINI_CHESSBOARD_MENU =
        MENUS.register("mini_chessboard", () -> new MenuType<MiniChessboardMenu>(
            (IContainerFactory<MiniChessboardMenu>) (id, inv, buf) -> {
                BlockPos pos = buf.readBlockPos();
                return new MiniChessboardMenu(id, inv, pos);
            },
            FeatureFlags.VANILLA_SET
        ));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}