package com.CodeKanic.ckutilities.common.items;

import com.CodeKanic.ckutilities.CKUtilities;
import com.CodeKanic.ckutilities.common.items.baseitems.ItemEnergy;
import com.CodeKanic.ckutilities.common.items.custom.BatteryItem;
import com.CodeKanic.ckutilities.common.items.custom.DrillItem;
import com.CodeKanic.ckutilities.common.items.custom.FuelItem;
import com.CodeKanic.ckutilities.common.items.custom.HammerItem;
import com.CodeKanic.ckutilities.common.items.datacomponents.CKUDataComponents;
import com.CodeKanic.ckutilities.common.items.utils.ToolTier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CKUItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CKUtilities.MODID);

    public static final DeferredItem<Item> COPPER_ALLOY_INGOT = ITEMS.registerSimpleItem("copper_alloy_ingot");
    public static final DeferredItem<Item> COPPER_ALLOY_PICKAXE = ITEMS.registerItem("copper_alloy_pickaxe",
            properties -> new Item(properties.pickaxe(ToolTier.COPPER_ALLOY, 0.5F, -2.4F)));
    public static final DeferredItem<AxeItem> COPPER_ALLOY_AXE = ITEMS.registerItem("copper_alloy_axe",
            properties -> new AxeItem(ToolTier.COPPER_ALLOY, 4.5F, -3.2F, properties));
    public static final DeferredItem<ShovelItem> COPPER_ALLOY_SHOVEL = ITEMS.registerItem("copper_alloy_shovel",
            properties -> new ShovelItem(ToolTier.COPPER_ALLOY, 1.0F, -2.2F, properties));
    public static final DeferredItem<Item> COPPER_ALLOY_SWORD = ITEMS.registerItem("copper_alloy_sword",
            properties -> new Item(properties.sword(ToolTier.COPPER_ALLOY, 2.0F, -2.4F)));
    public static final DeferredItem<HammerItem> COPPER_ALLOY_HAMMER = ITEMS.registerItem("copper_alloy_hammer",
            properties -> new HammerItem(properties.tool(
                    ToolTier.COPPER_ALLOY, CKUTags.Blocks.MINEABLE_WITH_HAMMER, 7.0F, -3.5F, 0.0F)));
    public static final DeferredItem<DrillItem> COPPER_ALLOY_DRILL = ITEMS.registerItem("copper_alloy_drill", DrillItem::new);
    public static final DeferredItem<BatteryItem> BATTERY = ITEMS.registerItem("battery",
            properties -> new BatteryItem(properties, 200000, 1000));
    public static final DeferredItem<Item> TINY_COAL = ITEMS.registerItem("tiny_coal",
            properties -> new FuelItem(properties, 200));

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
                event.registerItem(Capabilities.Energy.ITEM, (stack, access) -> {
                    if (access == null) {
                        return null;
                    }
                    return new ItemAccessEnergyHandler(access, CKUDataComponents.ENERGY_STORAGE.get(), energyItem.maxPower, energyItem.transfer);
                }, energyItem);
            }
        }
    }
}
