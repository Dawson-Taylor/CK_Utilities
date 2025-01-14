package com.CodeKanic.ckutilities.common.items.datacomponents;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CKUDataComponents {

    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "ckutilities");


    public static final Supplier<DataComponentType<Integer>> ENERGY_STORAGE = REGISTRAR.register("energy", () ->
            DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> COORDINATES = register(
            builder -> builder.persistent(BlockPos.CODEC));

    public static final Supplier<DataComponentType<Boolean>> ENABLED = REGISTRAR.register("enabled", () ->
            DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build());

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return REGISTRAR.register("coordinates", () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        REGISTRAR.register(eventBus);
    }

}
