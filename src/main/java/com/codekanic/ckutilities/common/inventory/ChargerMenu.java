package com.codekanic.ckutilities.common.inventory;

import com.codekanic.ckutilities.common.blocks.entity.ChargerBlockEntity;
import com.codekanic.ckutilities.common.items.custom.BatteryItem;
import com.codekanic.ckutilities.common.menu.CKUMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Power slot plus two charge slots. The machine face and the player inventory are separate windows.
 * Slot positions include the empty gap between those windows. Energy transfer is unchanged.
 */
public class ChargerMenu extends AbstractContainerMenu {
    /** Charger face, including its own top frame. The bottom cap is drawn by the screen. */
    public static final int MACHINE_PANEL_WIDTH = 176;
    public static final int MACHINE_FACE_HEIGHT = 59;
    public static final int MACHINE_CAP_HEIGHT = 7;
    public static final int MACHINE_PANEL_HEIGHT = MACHINE_FACE_HEIGHT + MACHINE_CAP_HEIGHT;
    /** Empty space between the charger window and the inventory window. Not part of either texture. */
    public static final int PANEL_GAP = 12;
    /** Own bordered window for the normal 27+9 player inventory. */
    public static final int INV_PANEL_WIDTH = 176;
    public static final int INV_PANEL_HEIGHT = 101;
    public static final int INV_PANEL_X = (MACHINE_PANEL_WIDTH - INV_PANEL_WIDTH) / 2;
    public static final int INV_PANEL_Y = MACHINE_PANEL_HEIGHT + PANEL_GAP;

    public static final int IMAGE_WIDTH = MACHINE_PANEL_WIDTH;
    public static final int IMAGE_HEIGHT = INV_PANEL_Y + INV_PANEL_HEIGHT;

    public static final int POWER_X = 44;
    public static final int POWER_Y = 35;
    public static final int CHARGE_X = 98;
    public static final int CHARGE_Y = 35;
    public static final int CHARGE_STEP = 18;

    /** 8px inset inside the inventory panel, matching a vanilla inventory. */
    public static final int PLAYER_X = INV_PANEL_X + 8;
    public static final int PLAYER_INV_Y = INV_PANEL_Y + 18;
    public static final int HOTBAR_Y = INV_PANEL_Y + 76;
    public static final int INV_LABEL_X = INV_PANEL_X + 8;
    public static final int INV_LABEL_Y = INV_PANEL_Y + 6;

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

        this.addSlot(new RestrictedSlot(charger, ChargerBlockEntity.SLOT_POWER, POWER_X, POWER_Y));
        this.addSlot(new RestrictedSlot(charger, ChargerBlockEntity.SLOT_CHARGE_START, CHARGE_X, CHARGE_Y));
        this.addSlot(new RestrictedSlot(charger, ChargerBlockEntity.SLOT_CHARGE_START + 1, CHARGE_X + CHARGE_STEP, CHARGE_Y));
        this.addStandardInventorySlots(playerInventory, PLAYER_X, PLAYER_INV_Y);
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
