package com.CodeKanic.ckutilities.common.items;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.items.baseitems.ItemEnergy;
import com.CodeKanic.ckutilities.common.items.custom.BatteryItem;
import com.CodeKanic.ckutilities.common.items.custom.DrillItem;
import com.CodeKanic.ckutilities.common.items.custom.HammerItem;
import com.CodeKanic.ckutilities.common.items.datacomponents.CKUDataComponents;
import com.CodeKanic.ckutilities.common.items.utils.ToolTier;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.spongepowered.include.com.google.common.collect.ImmutableSet;

import java.util.Set;
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

    public static final DeferredItem<DrillItem> COPPER_ALLOY_DRILL = ITEMS.register("copper_alloy_drill", DrillItem::new);
    public static final DeferredItem<BatteryItem> BATTERY = ITEMS.register("battery", () -> new BatteryItem(200000, 1000));


//    public static final Set<DeferredItem<? extends Item>> TOOLS = ImmutableSet.of(
//            // All in one tools
//            BATTERY
//    );




    public static Item.Properties defaultProps() {
        return new Item.Properties();
    }

    public static void init(IEventBus evt) {
        ITEMS.register(evt);
        evt.addListener(CKUItems::registerCapabilities);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (DeferredHolder<Item, ? extends Item> holder : ITEMS.getEntries()) {
            if (holder.get() instanceof ItemEnergy energyItem) {
                event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, context) ->
                                new ComponentEnergyStorage(stack, CKUDataComponents.ENERGY_STORAGE.get(), energyItem.maxPower, energyItem.transfer),
                        energyItem
                );
            }
        }
    }
}
