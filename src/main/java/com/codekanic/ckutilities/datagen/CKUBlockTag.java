package com.codekanic.ckutilities.datagen;

import com.codekanic.ckutilities.CKUtilities;
import com.codekanic.ckutilities.common.blocks.CKUBlocks;
import com.codekanic.ckutilities.common.items.CKUTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class CKUBlockTag extends BlockTagsProvider {
    public CKUBlockTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, CKUtilities.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CKUTags.Blocks.MINEABLE_WITH_DRILL).addTags(
                BlockTags.MINEABLE_WITH_SHOVEL,
                BlockTags.MINEABLE_WITH_PICKAXE
        );
        tag(CKUTags.Blocks.MINEABLE_WITH_HAMMER).addTags(
                BlockTags.MINEABLE_WITH_SHOVEL,
                BlockTags.MINEABLE_WITH_PICKAXE
        );
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(CKUBlocks.COPPER_ALLOY_BLOCK.getKey(), CKUBlocks.CHARGER.getKey());
        tag(BlockTags.NEEDS_IRON_TOOL).add(CKUBlocks.COPPER_ALLOY_BLOCK.getKey(), CKUBlocks.CHARGER.getKey());
    }
}
