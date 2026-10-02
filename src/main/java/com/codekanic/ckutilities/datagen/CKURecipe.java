package com.codekanic.ckutilities.datagen;

import com.codekanic.ckutilities.common.blocks.CKUBlocks;
import com.codekanic.ckutilities.common.items.CKUItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CKURecipe extends RecipeProvider {
    public CKURecipe(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_INGOT.get())
                .pattern("   ")
                .pattern("CIC")
                .pattern("   ")
                .define('I', Items.IRON_INGOT)
                .define('C', Items.COPPER_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
        shaped(RecipeCategory.MISC, CKUBlocks.COPPER_ALLOY_BLOCK.get())
                .pattern("CCC")
                .pattern("CCC")
                .pattern("CCC")
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(output);
        shaped(RecipeCategory.MISC, Items.COAL)
                .pattern("CCC")
                .pattern("C C")
                .pattern("CCC")
                .define('C', CKUItems.TINY_COAL)
                .group("ckutilities")
                .unlockedBy("has_coal", has(Items.COAL))
                .save(output);
        shapeless(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_INGOT.get(), 9)
                .requires(CKUBlocks.COPPER_ALLOY_BLOCK)
                .unlockedBy("has_copper_alloy_block", has(CKUBlocks.COPPER_ALLOY_BLOCK))
                .save(output, "alloy_ingot");
        shapeless(RecipeCategory.MISC, CKUItems.TINY_COAL.get(), 8)
                .requires(Items.COAL)
                .unlockedBy("has_coal", has(Items.COAL))
                .save(output);

        shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_AXE.get())
                .pattern("CC ")
                .pattern("CS ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", has(CKUItems.COPPER_ALLOY_INGOT))
                .save(output);
        shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_PICKAXE.get())
                .pattern("CCC")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", has(CKUItems.COPPER_ALLOY_INGOT))
                .save(output);
        shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_SHOVEL.get())
                .pattern(" C ")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", has(CKUItems.COPPER_ALLOY_INGOT))
                .save(output);
        shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_SWORD.get())
                .pattern(" C ")
                .pattern(" C ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", has(CKUItems.COPPER_ALLOY_INGOT))
                .save(output);
        shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_DRILL.get())
                .pattern(" SD")
                .pattern("TBP")
                .pattern("KT ")
                .define('S', CKUItems.COPPER_ALLOY_SHOVEL)
                .define('P', CKUItems.COPPER_ALLOY_PICKAXE)
                .define('D', Items.DIAMOND_PICKAXE)
                .define('B', CKUItems.BATTERY)
                .define('T', Items.SMOOTH_STONE)
                .define('K', Items.STICK)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", has(CKUItems.COPPER_ALLOY_INGOT))
                .save(output);
        shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_HAMMER.get())
                .pattern("CCC")
                .pattern("CSC")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('C', CKUBlocks.COPPER_ALLOY_BLOCK)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", has(CKUItems.COPPER_ALLOY_INGOT))
                .save(output);
        shaped(RecipeCategory.MISC, CKUItems.BATTERY.get())
                .pattern(" D ")
                .pattern("ICI")
                .pattern("WWW")
                .define('I', Items.IRON_INGOT)
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .define('W', Items.WOOL.black())
                .define('D', Items.DIAMOND)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", has(CKUItems.COPPER_ALLOY_INGOT))
                .save(output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new CKURecipe(registries, output);
        }

        @Override
        public String getName() {
            return "CK Utilities Recipes";
        }
    }
}
