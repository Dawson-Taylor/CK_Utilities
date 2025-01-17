package com.CodeKanic.ckutilities.datagen;


import com.CodeKanic.ckutilities.common.blocks.CKUBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class CKULootTables extends BlockLootSubProvider {
    protected CKULootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(CKUBlocks.COPPER_ALLOY_BLOCK.get());


    }
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return CKUBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }


}
