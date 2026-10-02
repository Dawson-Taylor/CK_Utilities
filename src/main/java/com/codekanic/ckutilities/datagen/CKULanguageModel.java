package com.codekanic.ckutilities.datagen;

import com.codekanic.ckutilities.CKUtilities;
import com.codekanic.ckutilities.common.blocks.CKUBlocks;
import com.codekanic.ckutilities.common.items.CKUItems;
import net.minecraft.data.PackOutput;

public class CKULanguageModel extends net.neoforged.neoforge.common.data.LanguageProvider {
    public CKULanguageModel(PackOutput output, String locale) {
        super(output, CKUtilities.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.ckutilities", "CK Utilities");

        add(CKUItems.COPPER_ALLOY_INGOT.get(), "Copper Alloy Ingot");
        add(CKUBlocks.COPPER_ALLOY_BLOCK.get(), "Copper Alloy Block");
        add(CKUItems.TINY_COAL.get(), "Tiny Coal");

        add(CKUItems.COPPER_ALLOY_PICKAXE.get(), "Copper Alloy Pickaxe");
        add(CKUItems.COPPER_ALLOY_SHOVEL.get(), "Copper Alloy Shovel");
        add(CKUItems.COPPER_ALLOY_AXE.get(), "Copper Alloy Axe");
        add(CKUItems.COPPER_ALLOY_HAMMER.get(), "Copper Alloy Hammer");
        add(CKUItems.COPPER_ALLOY_SWORD.get(), "Copper Alloy Sword");
        add(CKUItems.COPPER_ALLOY_DRILL.get(), "Copper Alloy Drill");
        add(CKUItems.BATTERY.get(), "Battery");

        add(CKUItems.UPGRADE_TEMPLATE.get(), "Upgrade Template");
        add(CKUItems.HAMMER_UPGRADE_3X3.get(), "3x3 Hammer Upgrade");
        add(CKUItems.HAMMER_UPGRADE_5X5.get(), "5x5 Hammer Upgrade");
        add(CKUItems.HAMMER_UPGRADE_9X9.get(), "9x9 Hammer Upgrade");
        add(CKUItems.EFFICIENCY_UPGRADE_1.get(), "Efficiency Upgrade I");
        add(CKUItems.EFFICIENCY_UPGRADE_2.get(), "Efficiency Upgrade II");
        add(CKUItems.EFFICIENCY_UPGRADE_3.get(), "Efficiency Upgrade III");
        add(CKUItems.EFFICIENCY_UPGRADE_4.get(), "Efficiency Upgrade IV");
        add(CKUItems.EFFICIENCY_UPGRADE_5.get(), "Efficiency Upgrade V");
        add(CKUItems.FORTUNE_UPGRADE_1.get(), "Fortune Upgrade I");
        add(CKUItems.FORTUNE_UPGRADE_2.get(), "Fortune Upgrade II");
        add(CKUItems.FORTUNE_UPGRADE_3.get(), "Fortune Upgrade III");
        add(CKUItems.SILK_TOUCH_UPGRADE.get(), "Silk Touch Upgrade");

        add("misc.ckutilities.festored", "Forge Energy: %s / %s");
        add("misc.ckutilities.fenone", "Needs a charge");
        add("tooltip.ckutilities.battery.discharge", "Charging items in inventory");
        add("tooltip.ckutilities.battery.noDischarge", "Not charging items in inventory");
        add("tooltip.ckutilities.battery.changeMode", "Sneak + right click to toggle inventory charging");
        add("ckutilities.enabled", "Enabled");
        add("ckutilities.disabled", "Disabled");
        add("tooltip.ckutilities.drill.hammer", "Hammer area: %sx%s");
        add("tooltip.ckutilities.upgrade.apply", "Sneak and use with the copper alloy drill in the other hand");
        add("tooltip.ckutilities.upgrade.applied", "Applied %s");
        add("tooltip.ckutilities.upgrade.rejected", "That upgrade is already applied, or a stronger one is");
        add("tooltip.ckutilities.upgrade.no_drill", "Hold the copper alloy drill in your other hand");
        add("tooltip.ckutilities.upgrade.silk", "Replaces Fortune on the drill");
        add("tooltip.ckutilities.upgrade.fortune", "Replaces Silk Touch on the drill");
    }
}
