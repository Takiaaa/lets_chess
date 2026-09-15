package com.takia.lets_chess.compat.create;

import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.takia.lets_chess.Letschess;
import com.takia.lets_chess.block.ModBlocks;

import net.neoforged.bus.api.IEventBus;

public class CreateCompat {
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(Letschess.MODID);

    private static RegistryEntry<DisplaySource, DiceDisplaySource> DICE_FACE;

    public static void register(IEventBus eventBus) {
        REGISTRATE.registerEventListeners(eventBus);

        DICE_FACE = REGISTRATE.displaySource("dice_face", DiceDisplaySource::new)
                .register();
    }

    public static void associateBlocks() {
        DisplaySource.BY_BLOCK.add(ModBlocks.DICE_WHITE.get(), DICE_FACE.get());
        DisplaySource.BY_BLOCK.add(ModBlocks.DICE_BLACK.get(), DICE_FACE.get());
    }
}