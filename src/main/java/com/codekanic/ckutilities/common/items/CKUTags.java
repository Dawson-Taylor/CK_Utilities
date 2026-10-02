package com.codekanic.ckutilities.common.items;

import com.codekanic.ckutilities.CKUtilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CKUTags {
    public static class Blocks {
        public static final TagKey<Block> MINEABLE_WITH_DRILL = createTag("mineable/drill");
        public static final TagKey<Block> MINEABLE_WITH_HAMMER = createTag("mineable/hammer");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(CKUtilities.modLoc(name));
        }
    }

    public static class Items {
        public static final TagKey<Item> DRILL = tag("drill");
        public static final TagKey<Item> BATTERY = tag("battery");
        public static final TagKey<Item> COPPER_ALLOY_INGOTS = tag("ingots/copper_alloy");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, CKUtilities.modLoc(name));
        }
    }
}
