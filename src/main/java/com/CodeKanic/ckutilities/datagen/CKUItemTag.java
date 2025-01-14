package com.CodeKanic.ckutilities.datagen;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.items.CKUItems;
import com.CodeKanic.ckutilities.common.items.CKUTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CKUItemTag extends ItemTagsProvider {
    public static final TagKey<Item> INGOT_COPPER_ALLOY = forgeTag();

    public CKUItemTag(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), CKUtilities.MODID, helper);
    }

    private static TagKey<Item> forgeTag() {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(CKUtilities.MODID, "ingots/copper_alloy"));
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

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
        tag(CKUTags.Items.DRILL)
                .add(CKUItems.COPPER_ALLOY_DRILL.get());
        tag(CKUTags.Items.BATTERY)
                .add(CKUItems.BATTERY.get());


    }
}
