package com.CodeKanic.ckutilities.datagen;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.blocks.CKUBlocks;
import com.CodeKanic.ckutilities.common.items.CKUItems;
import com.CodeKanic.ckutilities.common.items.CreativeModTab;
import net.minecraft.data.PackOutput;

public class CKULanguageModel extends net.neoforged.neoforge.common.data.LanguageProvider {

    public CKULanguageModel(PackOutput output, String locale) {
        super(output, CKUtilities.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + CreativeModTab.TAB_CKUTILITIES, "CK Utilities");


        // Resources
        add(CKUItems.COPPER_ALLOY_INGOT.get(), "Copper Alloy Ingot");
        add(CKUBlocks.COPPER_ALLOY_BLOCK.get(), "Copper Alloy Block");
        add(CKUItems.TINY_COAL.get(), "Tiny Coal");

        //Items - Tools
        add(CKUItems.COPPER_ALLOY_PICKAXE.get(), "Copper Alloy Pickaxe");
        add(CKUItems.COPPER_ALLOY_SHOVEL.get(), "Copper Alloy Shovel");
        add(CKUItems.COPPER_ALLOY_AXE.get(), "Copper Alloy Axe");
        add(CKUItems.COPPER_ALLOY_HAMMER.get(), "Copper Alloy Hammer");
        add(CKUItems.COPPER_ALLOY_SWORD.get(), "Copper Alloy Sword");
        add(CKUItems.COPPER_ALLOY_DRILL.get(), "Copper Alloy Drill");
        add(CKUItems.BATTERY.get(), "Battery");

        //Misc
        add("misc.ckutilities.festored", "Forge Energy: %s / %s");
        add("misc.ckutilities.fenone", "Needs a charge");
        add("tooltip.ckutilities.battery.discharge", "Charging items in inventory");
        add("tooltip.ckutilities.battery.noDischarge", "Not charging items in inventory");
        add("tooltip.ckutilities.battery.changeMode", "Sneak + right click to toggle inventory charging");
        add("ckutilities.enabled", "Enabled");
        add("ckutilities.disabled", "Disabled");
    }
}
