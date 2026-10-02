package com.codekanic.ckutilities.common.inventory;

import com.codekanic.ckutilities.common.blocks.entity.ChargerBlockEntity;
import com.codekanic.ckutilities.common.items.custom.BatteryItem;
import com.codekanic.ckutilities.common.registration.CKUMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ChargerMenu extends AbstractContainerMenu {
    private static final int PLAYER_START = ChargerBlockEntity.SLOT_COUNT;
    private static final int HOTBAR_START = PLAYER_START + 27;

    private final Container charger;

    public ChargerMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(ChargerBlockEntity.SLOT_COUNT));
    }

    public ChargerMenu(int containerId, Inventory playerInventory, Container charger) {
        super(CKUMenus.CHARGER.get(), containerId);
        checkContainerSize(charger, ChargerBlockEntity.SLOT_COUNT);
        this.charger = charger;
        charger.startOpen(playerInventory.player);

        this.addSlot(new RestrictedSlot(charger, ChargerBlockEntity.SLOT_POWER, 44, 35));
        this.addSlot(new RestrictedSlot(charger, ChargerBlockEntity.SLOT_CHARGE_START, 98, 35));
        this.addSlot(new RestrictedSlot(charger, ChargerBlockEntity.SLOT_CHARGE_START + 1, 116, 35));
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.charger.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        clicked = stack.copy();
        if (index < ChargerBlockEntity.SLOT_COUNT) {
            if (!this.moveItemStackTo(stack, PLAYER_START, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveIntoCharger(stack) && !this.moveWithinPlayer(index, stack)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == clicked.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return clicked;
    }

    private boolean moveIntoCharger(ItemStack stack) {
        if (stack.getItem() instanceof BatteryItem && this.moveItemStackTo(stack, ChargerBlockEntity.SLOT_POWER, ChargerBlockEntity.SLOT_POWER + 1, false)) {
            return true;
        }
        return ChargerBlockEntity.hasEnergy(stack)
                && this.moveItemStackTo(stack, ChargerBlockEntity.SLOT_CHARGE_START, ChargerBlockEntity.SLOT_COUNT, false);
    }

    private boolean moveWithinPlayer(int index, ItemStack stack) {
        if (index < HOTBAR_START) {
            return this.moveItemStackTo(stack, HOTBAR_START, this.slots.size(), false);
        }
        return this.moveItemStackTo(stack, PLAYER_START, HOTBAR_START, false);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.charger.stopOpen(player);
    }

    private static final class RestrictedSlot extends Slot {
        private RestrictedSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return this.container.canPlaceItem(this.getSlotIndex(), stack);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
