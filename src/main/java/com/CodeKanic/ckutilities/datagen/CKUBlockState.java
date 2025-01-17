package com.CodeKanic.ckutilities.datagen;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.blocks.CKUBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class CKUBlockState extends BlockStateProvider {
    public CKUBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CKUtilities.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(CKUBlocks.COPPER_ALLOY_BLOCK);


    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

}
