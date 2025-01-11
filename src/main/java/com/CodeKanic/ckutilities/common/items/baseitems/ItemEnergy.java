package com.CodeKanic.ckutilities.common.items.baseitems;

import com.CodeKanic.ckutilities.common.items.CKUItems;
import com.CodeKanic.ckutilities.common.items.utils.CustomEnergyStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;

public abstract class ItemEnergy extends ItemBase {
    public final int maxPower;
    public final int transfer;

    public ItemEnergy(int maxPower, int transfer) {
        super(CKUItems.defaultProps().stacksTo(1));
        this.maxPower = maxPower;
        this.transfer = transfer;
    }
    public ItemEnergy(Properties props, int maxPower, int transfer) {
        super(props);
        this.maxPower = maxPower;
        this.transfer = transfer;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if(storage != null) {
            int energy = storage.getEnergyStored();
            NumberFormat format = NumberFormat.getInstance();
            tooltip.add(Component.translatable("ckutilities.festored", format.format(energy), format.format(storage.getMaxEnergyStored()))
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack itemStack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (storage != null) {
            return Math.round((13.0F / storage.getMaxEnergyStored() * storage.getEnergyStored()));
        }
        return 0;
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        int defaultColor = super.getBarColor(stack);
        if (FMLEnvironment.dist.isClient()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return defaultColor;
            float[] color = getWheelColor(mc.player.level().getGameTime() % 256);
            return Mth.color(color[0] / 255F, color[1] / 255F, color[2] / 255F);
        }
        return defaultColor;
    }

    public static float[] getWheelColor(float pos) {
        if (pos < 85.0f) {
            return new float[]{pos * 3.0F, 255.0f - pos * 3.0f, 0.0f};
        }
        if (pos < 170.0f) {
            return new float[]{255.0f - (pos -= 85.0f) * 3.0f, 0.0f, pos * 3.0f};
        }
        return new float[]{0.0f, (pos -= 170.0f) * 3.0f, 255.0f - pos * 3.0f};
    }

    public void setEnergy(ItemStack stack, int energy) {
        Optional.ofNullable(stack.getCapability(Capabilities.EnergyStorage.ITEM)).ifPresent(cap -> {
            if (cap instanceof CustomEnergyStorage) {
                ((CustomEnergyStorage) cap).setEnergyStored(energy);
            }
        });
    }

    @Deprecated
    public int receiveEnergyInternal(ItemStack stack, int maxReceive, boolean simulate) {
        return Optional.ofNullable(stack.getCapability(Capabilities.EnergyStorage.ITEM))
                .map(cap -> cap.receiveEnergy(maxReceive, simulate))
                .orElse(0);
    }

//    public int extractEnergyInternal(ItemStack stack, int maxExtract, boolean simulate) {
//        return Optional.ofNullable(stack.getCapability(Capabilities.EnergyStorage.ITEM))
//                .map(cap -> cap instanceof EnergyStorage
//                        ? cap.extractEnergy(maxExtract, simulate)
//                        : 0)
//                .orElse(0);
//    }

    @Deprecated
    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
        return Optional.ofNullable(stack.getCapability(Capabilities.EnergyStorage.ITEM))
                .map(cap -> cap.receiveEnergy(maxReceive, simulate))
                .orElse(0);
    }

    public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
        return Optional.ofNullable(stack.getCapability(Capabilities.EnergyStorage.ITEM))
                .map(cap -> cap.extractEnergy(maxExtract, simulate))
                .orElse(0);
    }

    public int getEnergyStored(ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(Capabilities.EnergyStorage.ITEM))
                .map(IEnergyStorage::getEnergyStored)
                .orElse(0);
    }

    public int getMaxEnergyStored(ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(Capabilities.EnergyStorage.ITEM))
                .map(IEnergyStorage::getMaxEnergyStored)
                .orElse(0);
    }
}

