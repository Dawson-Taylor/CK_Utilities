package com.codekanic.ckutilities.common.menu;

import com.codekanic.ckutilities.common.items.custom.DrillUpgradeItem;
import com.codekanic.ckutilities.common.items.datacomponents.CKUDataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.List;

/**
 * Four upgrade slots stored on the copper alloy drill. Items move in and out; nothing is consumed.
 * Fortune and silk touch cannot be inserted together. A filled slot must be emptied before a different item goes in.
 */
public class DrillUpgradeMenu extends AbstractContainerMenu {
    public static final int UPGRADE_SLOTS = 4;
    public static final int SLOT_HAMMER = 0;
    public static final int SLOT_EFFICIENCY = 1;
    public static final int SLOT_FORTUNE = 2;
    public static final int SLOT_SILK = 3;

    /** Item position of the first upgrade slot. The screen draws the 18x18 widget one pixel up and left. */
    public static final int UPGRADE_X = 27;
    public static final int UPGRADE_Y = 20;
    public static final int UPGRADE_STEP = 52;
    public static final int PLAYER_X = 36;
    public static final int PLAYER_INV_Y = 63;
    public static final int HOTBAR_Y = 121;

    public static int upgradeX(int index) {
        return UPGRADE_X + index * UPGRADE_STEP;
    }

    private static final int PLAYER_START = UPGRADE_SLOTS;
    private static final int HOTBAR_START = PLAYER_START + 27;
    private static final int HOTBAR_END = HOTBAR_START + 9;

    private final Player player;
    private final ItemStack drill;
    private final UpgradeContainer upgrades;
    private boolean loading;

    /** Client menu. Slot contents arrive from the server. */
    public DrillUpgradeMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ItemStack.EMPTY);
    }

    public DrillUpgradeMenu(int containerId, Inventory playerInventory, ItemStack drill) {
        super(CKUMenus.DRILL_UPGRADES.get(), containerId);
        this.player = playerInventory.player;
        this.drill = drill;
        this.upgrades = new UpgradeContainer();
        this.load();
        if (!this.player.level().isClientSide()) {
            this.save();
        }

        this.addUpgradeSlot(SLOT_HAMMER, DrillUpgradeItem.Kind.HAMMER);
        this.addUpgradeSlot(SLOT_EFFICIENCY, DrillUpgradeItem.Kind.EFFICIENCY);
        UpgradeSlot fortune = this.addUpgradeSlot(SLOT_FORTUNE, DrillUpgradeItem.Kind.FORTUNE);
        UpgradeSlot silk = this.addUpgradeSlot(SLOT_SILK, DrillUpgradeItem.Kind.SILK_TOUCH);
        fortune.blockedBy = silk;
        silk.blockedBy = fortune;

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, PLAYER_X + column * 18, PLAYER_INV_Y + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(playerInventory, column, PLAYER_X + column * 18, HOTBAR_Y));
        }

    }

    private UpgradeSlot addUpgradeSlot(int index, DrillUpgradeItem.Kind kind) {
        UpgradeSlot slot = new UpgradeSlot(this.upgrades, index, upgradeX(index), UPGRADE_Y, kind);
        this.addSlot(slot);
        return slot;
    }

    private void load() {
        this.loading = true;
        ItemContainerContents contents = this.drill.getOrDefault(CKUDataComponents.DRILL_UPGRADES.get(), ItemContainerContents.EMPTY);
        net.minecraft.core.NonNullList<ItemStack> list = net.minecraft.core.NonNullList.withSize(UPGRADE_SLOTS, ItemStack.EMPTY);
        contents.copyInto(list);
        for (int i = 0; i < UPGRADE_SLOTS; i++) {
            this.upgrades.setItem(i, list.get(i));
        }
        this.loading = false;
    }

    private void save() {
        if (this.loading || this.player.level().isClientSide() || this.drill.isEmpty()) {
            return;
        }
        boolean any = false;
        for (int i = 0; i < UPGRADE_SLOTS; i++) {
            if (!this.upgrades.getItem(i).isEmpty()) {
                any = true;
                break;
            }
        }
        if (!any) {
            this.drill.remove(CKUDataComponents.DRILL_UPGRADES.get());
        } else {
            this.drill.set(CKUDataComponents.DRILL_UPGRADES.get(), ItemContainerContents.fromItems(List.of(
                    this.upgrades.getItem(SLOT_HAMMER),
                    this.upgrades.getItem(SLOT_EFFICIENCY),
                    this.upgrades.getItem(SLOT_FORTUNE),
                    this.upgrades.getItem(SLOT_SILK)
            )));
        }
        DrillUpgradeItem.applyStored(this.player.level(), this.drill, this.upgrades);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.drill.isEmpty()) {
            return true;
        }
        if (player.getMainHandItem() == this.drill || player.getOffhandItem() == this.drill || this.getCarried() == this.drill) {
            return true;
        }
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i) == this.drill) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < UPGRADE_SLOTS) {
            if (!this.moveItemStackTo(stack, PLAYER_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(stack, 0, UPGRADE_SLOTS, false)) {
            if (index < HOTBAR_START) {
                if (!this.moveItemStackTo(stack, HOTBAR_START, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, PLAYER_START, HOTBAR_START, false)) {
                return ItemStack.EMPTY;
            }
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return original;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.save();
    }

    private final class UpgradeContainer extends SimpleContainer {
        private UpgradeContainer() {
            super(UPGRADE_SLOTS);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            DrillUpgradeMenu.this.save();
        }
    }

    private static final class UpgradeSlot extends Slot {
        private final DrillUpgradeItem.Kind kind;
        private UpgradeSlot blockedBy;

        private UpgradeSlot(UpgradeContainer container, int index, int x, int y, DrillUpgradeItem.Kind kind) {
            super(container, index, x, y);
            this.kind = kind;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            if (!(stack.getItem() instanceof DrillUpgradeItem upgrade) || upgrade.kind() != this.kind) {
                return false;
            }
            if (this.hasItem() && !ItemStack.isSameItem(this.getItem(), stack)) {
                return false;
            }
            return this.blockedBy == null || !this.blockedBy.hasItem();
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int getMaxStackSize(ItemStack stack) {
            return 1;
        }
    }
}
