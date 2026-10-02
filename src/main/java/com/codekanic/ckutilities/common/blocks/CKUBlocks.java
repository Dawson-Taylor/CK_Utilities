package com.codekanic.ckutilities.common.blocks;

import com.codekanic.ckutilities.CKUtilities;
import com.codekanic.ckutilities.common.items.CKUItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CKUBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CKUtilities.MODID);

    public static final DeferredBlock<Block> COPPER_ALLOY_BLOCK = BLOCKS.registerBlock(
            "copper_alloy_block",
            Block::new,
            properties -> properties.strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.METAL));
    public static final DeferredBlock<ChargerBlock> CHARGER = BLOCKS.registerBlock(
            "charger",
            ChargerBlock::new,
            properties -> properties.mapColor(MapColor.COLOR_ORANGE).strength(3.5F).requiresCorrectToolForDrops().sound(SoundType.METAL));

    static {
        CKUItems.ITEMS.registerSimpleBlockItem(COPPER_ALLOY_BLOCK);
        CKUItems.ITEMS.registerSimpleBlockItem(CHARGER);
    }

    public static void init(IEventBus evt) {
        BLOCKS.register(evt);
    }
}
