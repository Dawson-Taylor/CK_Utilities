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
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public class BatteryItem extends ItemEnergy {

    /** Stacks that already spent their transfer budget during this level's current game tick. */
    private static final Set<ItemStack> CHARGED_THIS_TICK = Collections.newSetFromMap(new IdentityHashMap<>());
    private static WeakReference<Level> chargedLevel = new WeakReference<>(null);
    private static long chargedGameTime = Long.MIN_VALUE;

    public BatteryItem(int maxPower, int transfer) {
        super(maxPower, transfer);
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return ItemUtil.isEnabled(stack);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, Level world, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
        // 1.21.1 ticks the main inventory and the offhand. The same stack must only charge once.
        if (world.isClientSide || !(entity instanceof Player player) || !ItemUtil.isEnabled(stack)) {
            return;
        }
        if (!claimChargeTick(world, stack)) {
            return;
        }
        chargeInventory(stack, player);
    }

    /**
     * Moves at most this battery's transfer rate, in total, into other single-item stacks.
     * Skips this stack, empty stacks, stacked items, and slots with no energy capability.
     */
    public void chargeInventory(ItemStack battery, Player player) {
        if (!ItemUtil.isEnabled(battery)) {
            return;
        }
        int remaining = this.transfer;
        if (remaining <= 0) {
            return;
        }
        net.minecraft.world.entity.player.Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot == battery || slot.isEmpty() || slot.getCount() != 1) {
                continue;
            }
            IEnergyStorage cap = slot.getCapability(Capabilities.EnergyStorage.ITEM);
            if (cap == null) {
                continue;
            }
            int extractable = this.extractEnergy(battery, remaining, true);
            if (extractable <= 0) {
                break;
            }
            int received = cap.receiveEnergy(extractable, false);
            if (received > 0) {
                this.extractEnergy(battery, received, false);
                remaining -= received;
            }
        }
    }

    /**
     * @return true the first time this stack is allowed to charge during the current level tick
     */
    public boolean claimChargeTick(Level level, ItemStack stack) {
        long time = level.getGameTime();
        if (chargedLevel.get() != level || chargedGameTime != time) {
            CHARGED_THIS_TICK.clear();
            chargedLevel = new WeakReference<>(level);
            chargedGameTime = time;
        }
        return CHARGED_THIS_TICK.add(stack);
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
