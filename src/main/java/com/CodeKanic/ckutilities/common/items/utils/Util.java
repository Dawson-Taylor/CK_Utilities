package com.CodeKanic.ckutilities.common.items.utils;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public final class Util {

    public static double getReachDistance(Player player) {
        AttributeInstance attribute = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
        return attribute == null ? 4.5d : attribute.getValue();
    }
}
