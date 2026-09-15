package com.takia.lets_chess.compat.create;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.NumericSingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.takia.lets_chess.block.DiceBlock;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.state.BlockState;

public class DiceDisplaySource extends NumericSingleLineDisplaySource {

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockState state = context.level().getBlockState(context.getSourcePos());
        if (!state.hasProperty(DiceBlock.FACE)) {
            return Component.literal("0");
        }
        int face = state.getValue(DiceBlock.FACE);
        return Component.literal(String.valueOf(face));
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}