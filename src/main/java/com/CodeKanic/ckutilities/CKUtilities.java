package com.CodeKanic.ckutilities;

import com.CodeKanic.ckutilities.common.items.CKUItems;
import com.CodeKanic.ckutilities.common.items.CreativeModTab;
import com.CodeKanic.ckutilities.common.items.datacomponents.CKUDataComponents;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CKUtilities.MODID)
public class CKUtilities
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "ckutilities";


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CKUtilities(IEventBus modEventBus, ModContainer modContainer)
    {

        modEventBus.addListener(this::commonSetup);


        NeoForge.EVENT_BUS.register(this);

        CKUDataComponents.register(modEventBus);
        CreativeModTab.register(modEventBus);
        CKUItems.init(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }
    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
