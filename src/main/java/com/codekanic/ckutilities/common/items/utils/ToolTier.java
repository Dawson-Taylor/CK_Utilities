package com.codekanic.ckutilities.common.items.utils;

import com.codekanic.ckutilities.common.items.CKUTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public final class ToolTier {
    public static final ToolMaterial COPPER_ALLOY = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            1000,
            8.0F,
            4.0F,
            18,
            CKUTags.Items.COPPER_ALLOY_INGOTS
    );
    public static final ToolMaterial COPPER_ALLOY_DRILL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            2561,
            8.5F,
            5.0F,
            25,
            CKUTags.Items.COPPER_ALLOY_INGOTS
    );

    private ToolTier() {
    }
}
