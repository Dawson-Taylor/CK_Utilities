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

        add("misc.ckutilities.festored", "Forge Energy: %s / %s");
        add("misc.ckutilities.fenone", "Needs a charge");
        add("tooltip.ckutilities.battery.discharge", "Charging items in inventory");
        add("tooltip.ckutilities.battery.noDischarge", "Not charging items in inventory");
        add("tooltip.ckutilities.battery.changeMode", "Sneak + right click to toggle inventory charging");
        add("ckutilities.enabled", "Enabled");
        add("ckutilities.disabled", "Disabled");
    }
}
