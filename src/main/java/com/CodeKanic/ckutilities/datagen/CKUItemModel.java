package com.CodeKanic.ckutilities.datagen;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.items.CKUItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforgespi.locating.ForgeFeature;


public class CKUItemModel extends ItemModelProvider {
    public CKUItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CKUtilities.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        basicItem(CKUItems.COPPER_ALLOY_INGOT.get());


        handheldItem(CKUItems.COPPER_ALLOY_AXE);
        handheldItem(CKUItems.COPPER_ALLOY_PICKAXE);
        handheldItem(CKUItems.COPPER_ALLOY_SHOVEL);
        handheldItem(CKUItems.COPPER_ALLOY_SWORD);
        handheldItem(CKUItems.COPPER_ALLOY_HAMMER);

    }


    public void buttonItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/button_inventory"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(CKUtilities.MODID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void fenceItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(CKUtilities.MODID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void wallItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", ResourceLocation.fromNamespaceAndPath(CKUtilities.MODID,
                        "block/" + baseBlock.getId().getPath()));
    }

    private void handheldItem(DeferredItem<?> item) {
        withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(CKUtilities.MODID, "item/" + item.getId().getPath()));
    }
}
