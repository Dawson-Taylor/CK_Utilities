package com.codekanic.ckutilities.common.items;

import com.codekanic.ckutilities.CKUtilities;
import com.codekanic.ckutilities.common.items.baseitems.ItemEnergy;
import com.codekanic.ckutilities.common.items.custom.BatteryItem;
import com.codekanic.ckutilities.common.items.custom.DrillItem;
import com.codekanic.ckutilities.common.items.custom.DrillUpgradeItem;
import com.codekanic.ckutilities.common.items.custom.FuelItem;
import com.codekanic.ckutilities.common.items.custom.HammerItem;
import com.codekanic.ckutilities.common.items.datacomponents.CKUDataComponents;
import com.codekanic.ckutilities.common.items.utils.ToolTier;
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

    public static final DeferredItem<Item> UPGRADE_TEMPLATE = ITEMS.registerSimpleItem("upgrade_template");
    public static final DeferredItem<DrillUpgradeItem> HAMMER_UPGRADE_3X3 = registerUpgrade("hammer_upgrade_3x3", DrillUpgradeItem.Kind.HAMMER, 1);
    public static final DeferredItem<DrillUpgradeItem> HAMMER_UPGRADE_5X5 = registerUpgrade("hammer_upgrade_5x5", DrillUpgradeItem.Kind.HAMMER, 2);
    public static final DeferredItem<DrillUpgradeItem> HAMMER_UPGRADE_9X9 = registerUpgrade("hammer_upgrade_9x9", DrillUpgradeItem.Kind.HAMMER, 4);
    public static final DeferredItem<DrillUpgradeItem> EFFICIENCY_UPGRADE_1 = registerUpgrade("efficiency_upgrade_1", DrillUpgradeItem.Kind.EFFICIENCY, 1);
    public static final DeferredItem<DrillUpgradeItem> EFFICIENCY_UPGRADE_2 = registerUpgrade("efficiency_upgrade_2", DrillUpgradeItem.Kind.EFFICIENCY, 2);
    public static final DeferredItem<DrillUpgradeItem> EFFICIENCY_UPGRADE_3 = registerUpgrade("efficiency_upgrade_3", DrillUpgradeItem.Kind.EFFICIENCY, 3);
    public static final DeferredItem<DrillUpgradeItem> EFFICIENCY_UPGRADE_4 = registerUpgrade("efficiency_upgrade_4", DrillUpgradeItem.Kind.EFFICIENCY, 4);
    public static final DeferredItem<DrillUpgradeItem> EFFICIENCY_UPGRADE_5 = registerUpgrade("efficiency_upgrade_5", DrillUpgradeItem.Kind.EFFICIENCY, 5);
    public static final DeferredItem<DrillUpgradeItem> FORTUNE_UPGRADE_1 = registerUpgrade("fortune_upgrade_1", DrillUpgradeItem.Kind.FORTUNE, 1);
    public static final DeferredItem<DrillUpgradeItem> FORTUNE_UPGRADE_2 = registerUpgrade("fortune_upgrade_2", DrillUpgradeItem.Kind.FORTUNE, 2);
    public static final DeferredItem<DrillUpgradeItem> FORTUNE_UPGRADE_3 = registerUpgrade("fortune_upgrade_3", DrillUpgradeItem.Kind.FORTUNE, 3);
    public static final DeferredItem<DrillUpgradeItem> SILK_TOUCH_UPGRADE = registerUpgrade("silk_touch_upgrade", DrillUpgradeItem.Kind.SILK_TOUCH, 1);

    private static DeferredItem<DrillUpgradeItem> registerUpgrade(String name, DrillUpgradeItem.Kind kind, int tier) {
        return ITEMS.registerItem(name, properties -> new DrillUpgradeItem(properties, kind, tier));
    }

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
