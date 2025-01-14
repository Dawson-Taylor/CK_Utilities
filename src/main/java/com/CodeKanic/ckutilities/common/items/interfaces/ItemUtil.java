package com.CodeKanic.ckutilities.common.items.interfaces;

import com.CodeKanic.ckutilities.common.items.datacomponents.CKUDataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ItemUtil {

    static boolean isEnabled(ItemStack stack) {
        return stack.getOrDefault(CKUDataComponents.ENABLED, false);
    }

    static void changeEnabled(Player player, InteractionHand hand) {
        changeEnabled(player.getItemInHand(hand));
    }

    static void changeEnabled(ItemStack stack) {
        boolean isEnabled = isEnabled(stack);
        stack.set(CKUDataComponents.ENABLED, !isEnabled);
    }
}
