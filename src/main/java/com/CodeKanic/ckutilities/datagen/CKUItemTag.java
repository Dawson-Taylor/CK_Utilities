package com.CodeKanic.ckutilities.datagen;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.items.CKUItems;
import com.CodeKanic.ckutilities.common.items.resources.CopperAlloyIngot;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CKUItemTag extends ItemTagsProvider {
    public static final TagKey<Item> INGOT_COPPER_ALLOY = forgeTag("ingots/copper_alloy");

    public CKUItemTag(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), CKUtilities.MODID, helper);
    }

    private static TagKey<Item> forgeTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(CKUtilities.MODID, name));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(INGOT_COPPER_ALLOY)
                .add(CKUItems.COPPER_ALLOY_INGOT.get());
        tag(ItemTags.SWORDS)
                .add(CKUItems.COPPER_ALLOY_SWORD.get());
        tag(ItemTags.PICKAXES)
                .add(CKUItems.COPPER_ALLOY_HAMMER.get())
                .add(CKUItems.COPPER_ALLOY_PICKAXE.get());
        tag(ItemTags.SHOVELS)
                .add(CKUItems.COPPER_ALLOY_SHOVEL.get());
        tag(ItemTags.AXES)
                .add(CKUItems.COPPER_ALLOY_AXE.get());


    }
}
