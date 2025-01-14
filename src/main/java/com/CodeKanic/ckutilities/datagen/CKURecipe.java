package com.CodeKanic.ckutilities.datagen;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.items.CKUItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CKURecipe extends RecipeProvider implements IConditionBuilder {
    public CKURecipe(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, CKUtilities.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // Resources
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_INGOT.get())
                .pattern("   ")
                .pattern("CIC")
                .pattern("   ")
                .define('I', Items.IRON_INGOT)
                .define('C', Items.COPPER_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COPPER_INGOT))
                .save(recipeOutput);

        //Tools
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_AXE.get())
                .pattern("CC ")
                .pattern("CS ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(CKUItems.COPPER_ALLOY_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_PICKAXE.get())
                .pattern("CCC")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(CKUItems.COPPER_ALLOY_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_SHOVEL.get())
                .pattern(" C ")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(CKUItems.COPPER_ALLOY_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_SWORD.get())
                .pattern(" C ")
                .pattern(" C ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(CKUItems.COPPER_ALLOY_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_DRILL.get())
                .pattern(" SP")
                .pattern("TBP")
                .pattern("KT ")
                .define('S', CKUItems.COPPER_ALLOY_SHOVEL)
                .define('P', CKUItems.COPPER_ALLOY_PICKAXE)
                .define('B', CKUItems.BATTERY)
                .define('T', Items.SMOOTH_STONE)
                .define('K', Items.STICK)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(CKUItems.COPPER_ALLOY_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CKUItems.COPPER_ALLOY_HAMMER.get())
                .pattern("CCC")
                .pattern("CSC")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(CKUItems.COPPER_ALLOY_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CKUItems.BATTERY.get())
                .pattern(" D ")
                .pattern("ICI")
                .pattern("WWW")
                .define('I', Items.IRON_INGOT)
                .define('C', CKUItems.COPPER_ALLOY_INGOT)
                .define('W', Items.BLACK_WOOL)
                .define('D', Items.DIAMOND)
                .group("ckutilities")
                .unlockedBy("has_copper_alloy_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(CKUItems.COPPER_ALLOY_INGOT))
                .save(recipeOutput);
    }
}
