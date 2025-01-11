package com.CodeKanic.ckutilities.common.items;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.items.custom.HammerItem;
import com.CodeKanic.ckutilities.common.items.utils.ToolTier;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CKUItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CKUtilities.MODID);

    public static final DeferredItem<Item> COPPER_ALLOY_INGOT = ITEMS.register("copper_alloy_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<PickaxeItem> COPPER_ALLOY_PICKAXE = ITEMS.register("copper_alloy_pickaxe", props -> new PickaxeItem(ToolTier.COPPER_ALLOY, new Item.Properties()));
    public static final DeferredItem<AxeItem> COPPER_ALLOY_AXE = ITEMS.register("copper_alloy_axe", props -> new AxeItem(ToolTier.COPPER_ALLOY, new Item.Properties()));
    public static final DeferredItem<ShovelItem> COPPER_ALLOY_SHOVEL = ITEMS.register("copper_alloy_shovel", props -> new ShovelItem( ToolTier.COPPER_ALLOY, new Item.Properties()));
    public static final DeferredItem<SwordItem> COPPER_ALLOY_SWORD = ITEMS.register("copper_alloy_sword", props -> new SwordItem(ToolTier.COPPER_ALLOY, new Item.Properties()));

    public static final DeferredItem<HammerItem> COPPER_ALLOY_HAMMER = ITEMS.register("copper_alloy_hammer",
            () -> new HammerItem(ToolTier.COPPER_ALLOY, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ToolTier.COPPER_ALLOY, 7F, -3.5f))));





    private static Supplier<Item> basicItem() {
        return () -> new Item(defaultProps());
    }
    public static Item.Properties defaultProps() {
        return new Item.Properties();
    }

    public static Item.Properties defaultNonStacking() {
        return defaultProps().stacksTo(1);
    }



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
