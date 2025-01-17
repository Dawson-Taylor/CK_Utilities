package com.CodeKanic.ckutilities.common.items;

import com.CodeKanic.ckutilities.common.blocks.CKUBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.CodeKanic.ckutilities.CKUtilities.MODID;

public class CreativeModTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_CKUTILITIES = CREATIVE_MODE_TABS.register(MODID, () -> CreativeModeTab.builder()
            .title(Component.literal("CK Utilities"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> new ItemStack(CKUItems.COPPER_ALLOY_HAMMER.get()))
            .displayItems((parameters, output) -> {
                CKUItems.ITEMS.getEntries().forEach(e -> {
                    Item item = e.get();
                    output.accept(item);
                });
                CKUBlocks.BLOCKS.getEntries().forEach(e -> {
                    Block block = e.get();
                    output.accept(block);
                });
//                Registration.ARMORS.getEntries().forEach(e -> {
//                    Item item = e.get();
//                    output.accept(item);
//                });

            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
