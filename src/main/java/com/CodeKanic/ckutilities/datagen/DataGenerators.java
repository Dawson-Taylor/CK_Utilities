package com.CodeKanic.ckutilities.datagen;


import com.CodeKanic.ckutilities.CKUtilities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = CKUtilities.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();


        generator.addProvider(event.includeServer(), new CKURecipe(packOutput, event.getLookupProvider()));
//        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
//                List.of(new LootTableProvider.SubProviderEntry(CKULootTables::new, LootContextParamSets.BLOCK)), event.getLookupProvider()));
        CKUBlockTag blockTags = new CKUBlockTag(packOutput, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);

        CKUItemTag itemTags = new CKUItemTag(packOutput, lookupProvider, blockTags, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), itemTags);
        CKUDataMap dataMaps = new CKUDataMap(packOutput, lookupProvider);
        generator.addProvider(event.includeServer(), dataMaps);


        generator.addProvider(event.includeClient(), new CKUBlockState(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new CKUItemModel(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new CKULanguageModel(packOutput, "en_us"));

    }


}
