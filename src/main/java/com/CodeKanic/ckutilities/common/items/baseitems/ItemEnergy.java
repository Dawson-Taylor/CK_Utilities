package com.CodeKanic.ckutilities.common.items.baseitems;

import com.CodeKanic.ckutilities.common.items.CKUItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.text.NumberFormat;
import java.util.function.Consumer;

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

    protected EnergyHandler energy(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return ItemAccess.forStack(stack).getCapability(Capabilities.Energy.ITEM);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        EnergyHandler storage = energy(stack);
        if (storage != null) {
            NumberFormat format = NumberFormat.getInstance();
            tooltip.accept(Component.translatable("misc.ckutilities.festored", format.format(storage.getAmountAsInt()), format.format(storage.getCapacityAsInt()))
                    .withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        EnergyHandler storage = energy(stack);
        if (storage != null && storage.getCapacityAsInt() > 0) {
            return Math.round(13.0F / storage.getCapacityAsInt() * storage.getAmountAsInt());
        }
        return 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int defaultColor = super.getBarColor(stack);
        if (FMLEnvironment.getDist().isClient()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null) {
                return defaultColor;
            }
            float[] color = getWheelColor(minecraft.player.level().getOverworldClockTime() % 256);
            return ARGB.colorFromFloat(1.0F, color[0] / 255F, color[1] / 255F, color[2] / 255F);
        }
        return defaultColor;
    }

    public static float[] getWheelColor(float pos) {
        if (pos < 85.0F) {
            return new float[]{pos * 3.0F, 255.0F - pos * 3.0F, 0.0F};
        }
        if (pos < 170.0F) {
            return new float[]{255.0F - (pos -= 85.0F) * 3.0F, 0.0F, pos * 3.0F};
        }
        return new float[]{0.0F, (pos -= 170.0F) * 3.0F, 255.0F - pos * 3.0F};
    }

    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
        return transfer(stack, maxReceive, simulate, true);
    }

    public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
        return transfer(stack, maxExtract, simulate, false);
    }

    private int transfer(ItemStack stack, int amount, boolean simulate, boolean insert) {
        EnergyHandler storage = energy(stack);
        if (storage == null || amount <= 0) {
            return 0;
        }
        try (Transaction transaction = Transaction.open(null)) {
            int moved = insert ? storage.insert(amount, transaction) : storage.extract(amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return moved;
        }
    }

    public int getEnergyStored(ItemStack stack) {
        EnergyHandler storage = energy(stack);
        return storage == null ? 0 : storage.getAmountAsInt();
    }

    public int getMaxEnergyStored(ItemStack stack) {
        EnergyHandler storage = energy(stack);
        return storage == null ? 0 : storage.getCapacityAsInt();
    }
}
