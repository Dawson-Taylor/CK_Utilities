package com.CodeKanic.ckutilities.common.items.custom;

import com.CodeKanic.ckutilities.common.items.baseitems.ItemEnergy;
import com.CodeKanic.ckutilities.common.items.interfaces.ItemUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class BatteryItem extends ItemEnergy {


    public BatteryItem(int maxPower, int transfer) {
        super(maxPower, transfer);
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return ItemUtil.isEnabled(stack);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, Level world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isClientSide && entity instanceof Player player && ItemUtil.isEnabled(stack) && !isSelected) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack slot = player.getInventory().getItem(i);
                if (!slot.isEmpty() && slot.getCount() == 1) {
                    Optional<IEnergyStorage> energy = Optional.ofNullable(slot.getCapability(Capabilities.EnergyStorage.ITEM));
                    energy.ifPresent(cap -> {
                        int extractable = this.extractEnergy(stack, Integer.MAX_VALUE, true);
                        int received = cap.receiveEnergy(extractable, false);

                        if (received > 0) {
                            this.extractEnergy(stack, received, false);
                        }
                    });
                }
            }
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (!worldIn.isClientSide && player.isShiftKeyDown()) {
            ItemUtil.changeEnabled(player, hand);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return super.use(worldIn, player, hand);
    }


    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext playerIn, @Nonnull List<Component> list, @Nonnull TooltipFlag advanced) {
        super.appendHoverText(stack, playerIn, list, advanced);
        list.add(Component.translatable("tooltip.ckutilities.battery." + (ItemUtil.isEnabled(stack)
                ? "discharge"
                : "noDischarge")).withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable("tooltip.ckutilities.battery.changeMode").withStyle(ChatFormatting.GOLD));
    }
}
