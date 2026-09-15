package com.takia.lets_chess.compat.create;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.takia.lets_chess.Letschess;
import com.takia.lets_chess.block.ModBlocks;

import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class LetschessPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return Letschess.MODID;
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                RegisteredObjectsHelper::getKeyOrThrow);

        itemHelper.addToTag(AllCreatePonderTags.DISPLAY_SOURCES)
                .add(ModBlocks.DICE_WHITE.get())
                .add(ModBlocks.DICE_BLACK.get());
    }
}