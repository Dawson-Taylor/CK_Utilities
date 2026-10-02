package com.CodeKanic.ckutilities.datagen;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.items.CKUItems;
import com.CodeKanic.ckutilities.common.items.CKUTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class CKUItemTag extends ItemTagsProvider {
    public CKUItemTag(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, CKUtilities.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CKUTags.Items.COPPER_ALLOY_INGOTS).add(CKUItems.COPPER_ALLOY_INGOT.getKey());
        tag(ItemTags.SWORDS).add(CKUItems.COPPER_ALLOY_SWORD.getKey());
        tag(ItemTags.PICKAXES)
                .add(CKUItems.COPPER_ALLOY_HAMMER.getKey())
                .add(CKUItems.COPPER_ALLOY_PICKAXE.getKey());
        tag(ItemTags.SHOVELS).add(CKUItems.COPPER_ALLOY_SHOVEL.getKey());
        tag(ItemTags.AXES).add(CKUItems.COPPER_ALLOY_AXE.getKey());
        tag(CKUTags.Items.DRILL).add(CKUItems.COPPER_ALLOY_DRILL.getKey());
        tag(CKUTags.Items.BATTERY).add(CKUItems.BATTERY.getKey());
        tag(ItemTags.COALS).add(CKUItems.TINY_COAL.getKey());
    }
}
