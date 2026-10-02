package com.codekanic.ckutilities.datagen;

import com.codekanic.ckutilities.CKUtilities;
import com.codekanic.ckutilities.common.blocks.CKUBlocks;
import com.codekanic.ckutilities.common.items.CKUItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class CKUModelProvider extends ModelProvider {
    public CKUModelProvider(PackOutput output) {
        super(output, CKUtilities.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(CKUBlocks.COPPER_ALLOY_BLOCK.get());
        blockModels.createTrivialCube(CKUBlocks.CHARGER.get());

        itemModels.generateFlatItem(CKUItems.COPPER_ALLOY_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.TINY_COAL.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(CKUItems.COPPER_ALLOY_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CKUItems.COPPER_ALLOY_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CKUItems.COPPER_ALLOY_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CKUItems.COPPER_ALLOY_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CKUItems.COPPER_ALLOY_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CKUItems.COPPER_ALLOY_DRILL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CKUItems.BATTERY.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModels.generateFlatItem(CKUItems.UPGRADE_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.HAMMER_UPGRADE_3X3.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.HAMMER_UPGRADE_5X5.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.HAMMER_UPGRADE_9X9.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.EFFICIENCY_UPGRADE_1.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.EFFICIENCY_UPGRADE_2.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.EFFICIENCY_UPGRADE_3.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.EFFICIENCY_UPGRADE_4.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.EFFICIENCY_UPGRADE_5.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.FORTUNE_UPGRADE_1.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.FORTUNE_UPGRADE_2.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.FORTUNE_UPGRADE_3.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CKUItems.SILK_TOUCH_UPGRADE.get(), ModelTemplates.FLAT_ITEM);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return CKUBlocks.BLOCKS.getEntries().stream().map(holder -> holder);
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return CKUItems.ITEMS.getEntries().stream().map(holder -> holder);
    }
}
