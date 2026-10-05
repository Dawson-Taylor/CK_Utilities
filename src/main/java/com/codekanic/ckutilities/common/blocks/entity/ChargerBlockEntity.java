package com.codekanic.ckutilities.common.blocks.entity;

import com.codekanic.ckutilities.common.items.baseitems.ItemEnergy;
import com.codekanic.ckutilities.common.items.custom.BatteryItem;
import com.codekanic.ckutilities.common.items.interfaces.ItemUtil;
import com.codekanic.ckutilities.common.inventory.ChargerMenu;
import com.codekanic.ckutilities.common.registration.CKUBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil;

public class ChargerBlockEntity extends BaseContainerBlockEntity {
    public static final int SLOT_POWER = 0;
    public static final int SLOT_CHARGE_START = 1;
    public static final int CHARGE_SLOTS = 2;
    public static final int SLOT_COUNT = SLOT_CHARGE_START + CHARGE_SLOTS;

    private static final Component DEFAULT_NAME = Component.translatable("container.ckutilities.charger");

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    public ChargerBlockEntity(BlockPos pos, BlockState state) {
        super(CKUBlockEntities.CHARGER.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ChargerBlockEntity charger) {
        charger.distributeEnergy();
    }

    /**
     * Pulls FE from the power-slot battery into the charge slots.
     * The battery must be enabled (sneak-right-click) or it will not output.
     * One transfer budget, the battery's transfer rate, is shared by every charge slot each tick.
     */
    private void distributeEnergy() {
        ItemStack power = this.getItem(SLOT_POWER);
        if (!(power.getItem() instanceof BatteryItem) || !ItemUtil.isEnabled(power)) {
            return;
        }
        int budget = ((ItemEnergy) power.getItem()).transfer;
        if (budget <= 0) {
            return;
        }
        boolean movedAny = false;
        for (int slot = SLOT_CHARGE_START; slot < SLOT_COUNT && budget > 0; slot++) {
            ItemStack targetStack = this.getItem(slot);
            if (targetStack.isEmpty() || targetStack == power || targetStack.getCount() != 1) {
                continue;
            }
            EnergyHandler source = energyOf(this.getItem(SLOT_POWER));
            EnergyHandler target = energyOf(targetStack);
            if (source == null || target == null || source == target) {
                continue;
            }
            int moved = EnergyHandlerUtil.move(source, target, budget, null);
            if (moved > 0) {
                budget -= moved;
                movedAny = true;
            }
        }
        if (movedAny) {
            this.setChanged();
        }
    }

    public static boolean hasEnergy(ItemStack stack) {
        return energyOf(stack) != null;
    }

    private static EnergyHandler energyOf(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return ItemAccess.forStack(stack).getCapability(Capabilities.Energy.ITEM);
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == SLOT_POWER) {
            return stack.getItem() instanceof BatteryItem;
        }
        if (slot >= SLOT_CHARGE_START && slot < SLOT_COUNT) {
            return hasEnergy(stack) && stack != this.getItem(SLOT_POWER);
        }
        return false;
    }

    @Override
    protected Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (this.level != null && !this.level.isClientSide()) {
            Containers.dropContents(this.level, pos, this);
        }
        super.preRemoveSideEffects(pos, state);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ChargerMenu(containerId, inventory, this);
    }
}
