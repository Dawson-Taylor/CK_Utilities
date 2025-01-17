package com.CodeKanic.ckutilities.datagen;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.blocks.CKUBlocks;
import com.CodeKanic.ckutilities.common.items.CKUTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CKUBlockTag extends BlockTagsProvider {
    public CKUBlockTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CKUtilities.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(CKUTags.Blocks.MINEABLE_WITH_DRILL).addTags(
                BlockTags.MINEABLE_WITH_SHOVEL,
                BlockTags.MINEABLE_WITH_PICKAXE
        );

        tag(CKUTags.Blocks.MINEABLE_WITH_HAMMER).addTags(
                BlockTags.MINEABLE_WITH_SHOVEL,
                BlockTags.MINEABLE_WITH_PICKAXE
        );

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(CKUBlocks.COPPER_ALLOY_BLOCK.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(CKUBlocks.COPPER_ALLOY_BLOCK.get());


    }
}
