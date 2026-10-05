package com.codekanic.ckutilities.common.registration;

import com.codekanic.ckutilities.CKUtilities;
import com.codekanic.ckutilities.common.blocks.CKUBlocks;
import com.codekanic.ckutilities.common.blocks.entity.ChargerBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CKUBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CKUtilities.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ChargerBlockEntity>> CHARGER =
            BLOCK_ENTITIES.register("charger", () -> new BlockEntityType<>(ChargerBlockEntity::new, CKUBlocks.CHARGER.get()));

    private CKUBlockEntities() {
    }

    public static void init(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
