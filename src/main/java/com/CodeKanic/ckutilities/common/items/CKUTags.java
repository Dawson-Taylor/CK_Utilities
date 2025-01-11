package com.CodeKanic.ckutilities.common.items;

import com.CodeKanic.ckutilities.CKUtilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static net.minecraft.tags.TagEntry.tag;

public class CKUTags {

    public static class Blocks {


        public static final TagKey<Block> MINEABLE_WITH_DRILL = tag("mineable/drill");


        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registries.BLOCK, CKUtilities.modLoc(name));
        }

    }
}
