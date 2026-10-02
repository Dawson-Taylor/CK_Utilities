package com.CodeKanic.ckutilities;

import com.CodeKanic.ckutilities.common.blocks.CKUBlocks;
import com.CodeKanic.ckutilities.common.items.CKUItems;
import com.CodeKanic.ckutilities.common.items.CreativeModTab;
import com.CodeKanic.ckutilities.common.items.datacomponents.CKUDataComponents;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(CKUtilities.MODID)
public class CKUtilities {
    public static final String MODID = "ckutilities";

    public CKUtilities(IEventBus modEventBus, ModContainer modContainer) {
        CKUDataComponents.register(modEventBus);
        CreativeModTab.register(modEventBus);
        CKUItems.init(modEventBus);
        CKUBlocks.init(modEventBus);
    }

    public static Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
