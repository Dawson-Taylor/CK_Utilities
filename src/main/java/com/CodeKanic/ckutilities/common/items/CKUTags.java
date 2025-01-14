package com.CodeKanic.ckutilities.common.items;

import com.CodeKanic.ckutilities.CKUtilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CKUTags {

    public static class Blocks {


        public static final TagKey<Block> MINEABLE_WITH_DRILL = tag();


        private static TagKey<Block> tag() {
            return TagKey.create(Registries.BLOCK, CKUtilities.modLoc("mineable/drill"));
        }

    }

    public static class Items {
        public static final TagKey<Item> DRILL = tag("drill");
        public static final TagKey<Item> BATTERY = tag("battery");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, CKUtilities.modLoc(name));
        }
    }
}
