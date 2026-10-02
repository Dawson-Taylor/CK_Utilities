package com.codekanic.ckutilities.common.registration;

import com.codekanic.ckutilities.CKUtilities;
import com.codekanic.ckutilities.common.inventory.ChargerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CKUMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, CKUtilities.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ChargerMenu>> CHARGER =
            MENUS.register("charger", () -> IMenuTypeExtension.create((id, inventory, data) -> new ChargerMenu(id, inventory)));

    private CKUMenus() {
    }

    public static void init(IEventBus bus) {
        MENUS.register(bus);
    }
}
