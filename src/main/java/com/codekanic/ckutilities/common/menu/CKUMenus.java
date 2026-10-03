package com.codekanic.ckutilities.common.menu;

import com.codekanic.ckutilities.CKUtilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CKUMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, CKUtilities.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<DrillUpgradeMenu>> DRILL_UPGRADES = MENUS.register("drill_upgrades",
            () -> IMenuTypeExtension.create((containerId, inventory, extraData) -> new DrillUpgradeMenu(containerId, inventory)));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
