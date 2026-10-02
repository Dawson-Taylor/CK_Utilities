package com.codekanic.ckutilities.common.blocks;

import com.codekanic.ckutilities.CKUtilities;
import com.codekanic.ckutilities.common.items.CKUItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CKUBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CKUtilities.MODID);

    public static final DeferredBlock<Block> COPPER_ALLOY_BLOCK = BLOCKS.registerBlock(
            "copper_alloy_block",
            Block::new,
            properties -> properties.strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.METAL));

    static {
        CKUItems.ITEMS.registerSimpleBlockItem(COPPER_ALLOY_BLOCK);
    }

    public static void init(IEventBus evt) {
        BLOCKS.register(evt);
    }
}
