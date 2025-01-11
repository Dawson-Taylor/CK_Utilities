package com.CodeKanic.ckutilities.common.items.utils;


import com.CodeKanic.ckutilities.common.items.CKUItems;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static net.minecraft.tags.BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
import static net.minecraft.tags.BlockTags.INCORRECT_FOR_NETHERITE_TOOL;

public enum ToolTier implements Tier {

    COPPER_ALLOY(INCORRECT_FOR_DIAMOND_TOOL, 1000, 10.0F, 4.0F, 18, () -> Ingredient.of(CKUItems.COPPER_ALLOY_INGOT.get()));
    //COPPER_ALLOY_DRILL(INCORRECT_FOR_NETHERITE_TOOL, 2561, 16.0F, 5.0F, 25, () -> Ingredient.of(CKUItems.COPPER_ALLOY_INGOT.get()));


    private final TagKey<Block> incorrectBlocksForDrops;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ToolTier(TagKey<Block> incorrectBlocksForDrops, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
