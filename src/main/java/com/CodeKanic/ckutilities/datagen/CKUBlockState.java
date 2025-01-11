package com.CodeKanic.ckutilities.datagen;

import com.CodeKanic.ckutilities.CKUtilities;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CKUBlockState extends BlockStateProvider {
    public CKUBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CKUtilities.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {


    }

}
