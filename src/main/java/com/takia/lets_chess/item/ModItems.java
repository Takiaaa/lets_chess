package com.takia.lets_chess.item;

import com.takia.lets_chess.Letschess;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Letschess.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}