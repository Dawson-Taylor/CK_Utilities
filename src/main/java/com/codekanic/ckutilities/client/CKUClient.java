package com.codekanic.ckutilities.client;

import com.codekanic.ckutilities.CKUtilities;
import com.codekanic.ckutilities.common.menu.CKUMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = CKUtilities.MODID, value = Dist.CLIENT)
public final class CKUClient {
    private CKUClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(CKUMenus.DRILL_UPGRADES.get(), DrillUpgradeScreen::new);
        event.register(CKUMenus.CHARGER.get(), ChargerScreen::new);
    }
}
