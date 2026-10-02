package com.CodeKanic.ckutilities.common.items.custom;

import com.CodeKanic.ckutilities.common.items.baseitems.ItemEnergy;
import com.CodeKanic.ckutilities.common.items.interfaces.ItemUtil;
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
        // Charges other single stacks while held. Skip this stack so it cannot feed itself.
        if (!(entity instanceof Player player) || !ItemUtil.isEnabled(stack)) {
            return;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slotStack = player.getInventory().getItem(i);
            if (slotStack == stack || slotStack.isEmpty() || slotStack.getCount() != 1) {
                continue;
            }
            chargeSlot(stack, slotStack);
        }
    }

    private void chargeSlot(ItemStack battery, ItemStack slotStack) {
        EnergyHandler target = ItemAccess.forStack(slotStack).getCapability(Capabilities.Energy.ITEM);
        EnergyHandler source = energy(battery);
        EnergyHandlerUtil.move(source, target, Integer.MAX_VALUE, null);
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
