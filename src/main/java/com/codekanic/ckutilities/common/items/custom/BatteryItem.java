package com.codekanic.ckutilities.common.items.custom;

import com.codekanic.ckutilities.common.items.baseitems.ItemEnergy;
import com.codekanic.ckutilities.common.items.interfaces.ItemUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class BatteryItem extends ItemEnergy {
    public BatteryItem(Properties properties, int maxPower, int transfer) {
        super(properties.stacksTo(1), maxPower, transfer);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return ItemUtil.isEnabled(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        // The player inventory tick does not visit equipment. Offhand charging is handled separately.
        if (slot == EquipmentSlot.OFFHAND || !(entity instanceof Player player)) {
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
        EnergyHandler source = energy(battery);
        if (source == null) {
            return;
        }
        var inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
            ItemStack slotStack = inventory.getItem(i);
            if (slotStack == battery || slotStack.isEmpty() || slotStack.getCount() != 1) {
                continue;
            }
            EnergyHandler target = ItemAccess.forStack(slotStack).getCapability(Capabilities.Energy.ITEM);
            if (target == null) {
                continue;
            }
            remaining -= EnergyHandlerUtil.move(source, target, remaining, null);
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player.isShiftKeyDown()) {
            ItemUtil.changeEnabled(player, hand);
            return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable("tooltip.ckutilities.battery." + (ItemUtil.isEnabled(stack) ? "discharge" : "noDischarge"))
                .withStyle(ChatFormatting.GOLD));
        tooltip.accept(Component.translatable("tooltip.ckutilities.battery.changeMode").withStyle(ChatFormatting.GOLD));
    }
}
