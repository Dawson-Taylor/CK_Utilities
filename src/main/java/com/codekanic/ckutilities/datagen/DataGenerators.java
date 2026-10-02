package com.codekanic.ckutilities.datagen;

import com.codekanic.ckutilities.CKUtilities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = CKUtilities.MODID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        event.createProvider(CKURecipe.Runner::new);
        event.createProvider((PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) -> new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(CKULootTables::new, LootContextParamSets.BLOCK)),
                lookup));
        event.createProvider(CKUBlockTag::new);
        event.createProvider(CKUItemTag::new);
        event.createProvider(CKUDataMap::new);
    }

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        event.createProvider(CKUModelProvider::new);
        event.createProvider(output -> new CKULanguageModel(output, "en_us"));
    }
}
