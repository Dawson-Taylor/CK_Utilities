package com.codekanic.ckutilities.common.items.custom;

import com.codekanic.ckutilities.common.items.datacomponents.CKUDataComponents;
import com.codekanic.ckutilities.common.menu.DrillUpgradeMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/**
 * An upgrade item that is inserted into the copper alloy drill's screen. It is not consumed.
 */
public class DrillUpgradeItem extends Item {
    public enum Kind {
        HAMMER,
        EFFICIENCY,
        FORTUNE,
        SILK_TOUCH
    }

    private final Kind kind;
    /** Hammer radius (1, 2, or 4) or enchantment level. Silk touch is always 1. */
    private final int tier;

    public DrillUpgradeItem(Properties properties, Kind kind, int tier) {
        super(properties.stacksTo(64));
        this.kind = kind;
        this.tier = tier;
    }

    public Kind kind() {
        return this.kind;
    }

    public int tier() {
        return this.tier;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable("tooltip.ckutilities.upgrade.insert").withStyle(ChatFormatting.GOLD));
        if (this.kind == Kind.SILK_TOUCH) {
            tooltip.accept(Component.translatable("tooltip.ckutilities.upgrade.silk").withStyle(ChatFormatting.GRAY));
        } else if (this.kind == Kind.FORTUNE) {
            tooltip.accept(Component.translatable("tooltip.ckutilities.upgrade.fortune").withStyle(ChatFormatting.GRAY));
        }
    }

    /**
     * Writes hammer size and the enchantment-equivalent effects from whatever is currently stored.
     * An empty slot turns that upgrade off. Silk touch wins if both it and fortune are present.
     */
    public static void applyStored(Level level, ItemStack drill, Container upgrades) {
        ItemStack hammer = upgrades.getItem(DrillUpgradeMenu.SLOT_HAMMER);
        ItemStack efficiency = upgrades.getItem(DrillUpgradeMenu.SLOT_EFFICIENCY);
        ItemStack fortune = upgrades.getItem(DrillUpgradeMenu.SLOT_FORTUNE);
        ItemStack silk = upgrades.getItem(DrillUpgradeMenu.SLOT_SILK);

        int radius = tierOf(hammer, Kind.HAMMER);
        if (radius > 0) {
            drill.set(CKUDataComponents.HAMMER_RADIUS.get(), radius);
        } else {
            drill.remove(CKUDataComponents.HAMMER_RADIUS.get());
        }

        if (!drill.has(DataComponents.ENCHANTMENTS)) {
            drill.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        }
        int efficiencyLevel = tierOf(efficiency, Kind.EFFICIENCY);
        int fortuneLevel = tierOf(fortune, Kind.FORTUNE);
        int silkLevel = tierOf(silk, Kind.SILK_TOUCH);
        if (silkLevel > 0) {
            fortuneLevel = 0;
        }
        Holder<Enchantment> efficiencyEnchantment = enchantment(level, Enchantments.EFFICIENCY);
        Holder<Enchantment> fortuneEnchantment = enchantment(level, Enchantments.FORTUNE);
        Holder<Enchantment> silkEnchantment = enchantment(level, Enchantments.SILK_TOUCH);
        int appliedFortune = fortuneLevel;
        EnchantmentHelper.updateEnchantments(drill, mutable -> {
            mutable.set(efficiencyEnchantment, efficiencyLevel);
            mutable.set(fortuneEnchantment, appliedFortune);
            mutable.set(silkEnchantment, silkLevel);
        });
    }

    private static int tierOf(ItemStack stack, Kind kind) {
        if (stack.getItem() instanceof DrillUpgradeItem upgrade && upgrade.kind == kind) {
            return upgrade.tier;
        }
        return 0;
    }

    private static Holder<Enchantment> enchantment(Level level, ResourceKey<Enchantment> key) {
        return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
    }
}
