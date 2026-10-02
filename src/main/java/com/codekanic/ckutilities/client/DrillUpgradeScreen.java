package com.codekanic.ckutilities.client;

import com.codekanic.ckutilities.common.menu.DrillUpgradeMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;

/**
 * A small popup with four named upgrade slots in one row.
 * The player inventory is not part of this window. Click an upgrade on the hotbar to insert it,
 * and click a filled slot to send that upgrade back to the hotbar.
 */
public class DrillUpgradeScreen extends AbstractContainerScreen<DrillUpgradeMenu> {
    private static final Identifier DISPENSER = Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");
    private static final int IMAGE_WIDTH = 232;
    private static final int IMAGE_HEIGHT = 58;
    private static final int LABEL_Y = 39;
    private static final int LABEL_COLOR = -12566464;

    private static final Component[] SLOT_LABELS = {
            Component.translatable("container.ckutilities.drill_upgrades.hammer"),
            Component.translatable("container.ckutilities.drill_upgrades.efficiency"),
            Component.translatable("container.ckutilities.drill_upgrades.fortune"),
            Component.translatable("container.ckutilities.drill_upgrades.silk_touch")
    };

    public DrillUpgradeScreen(DrillUpgradeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    protected void init() {
        super.init();
        // Above the crosshair, clear of the hotbar, so this reads as a popup.
        this.topPos = Math.max(8, this.height / 2 - this.imageHeight - 12);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int left = this.leftPos;
        int top = this.topPos;
        this.blitSlice(graphics, left, top, this.imageWidth, 0, 16);
        int bodyBottom = top + this.imageHeight - 7;
        for (int y = top + 16; y < bodyBottom; y++) {
            this.blitSlice(graphics, left, y, this.imageWidth, 8, 1);
        }
        this.blitSlice(graphics, left, bodyBottom, this.imageWidth, 159, 7);

        for (int i = 0; i < DrillUpgradeMenu.UPGRADE_SLOTS; i++) {
            this.drawSlot(graphics, left + DrillUpgradeMenu.upgradeX(i), top + DrillUpgradeMenu.UPGRADE_Y);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, LABEL_COLOR, false);
        for (int i = 0; i < SLOT_LABELS.length; i++) {
            Component label = SLOT_LABELS[i];
            int center = DrillUpgradeMenu.upgradeX(i) + 8;
            graphics.text(this.font, label, center - this.font.width(label) / 2, LABEL_Y, LABEL_COLOR, false);
        }
    }

    /**
     * Left click on a filled slot returns it to the hotbar. Left click on a hotbar upgrade inserts it.
     * Other clicks are ignored so items are never picked up onto the cursor.
     */
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() != 0) {
            return true;
        }
        Slot upgrade = this.upgradeSlotAt(event.x(), event.y());
        if (upgrade != null) {
            if (upgrade.hasItem()) {
                this.slotClicked(upgrade, upgrade.index, 0, ContainerInput.QUICK_MOVE);
            }
            return true;
        }
        int hotbar = this.hotbarIndexAt(event.x(), event.y());
        if (hotbar >= 0) {
            Slot slot = this.menu.getSlot(DrillUpgradeMenu.HOTBAR_START + hotbar);
            if (slot.hasItem()) {
                this.slotClicked(slot, slot.index, 0, ContainerInput.QUICK_MOVE);
            }
        }
        return true;
    }

    private Slot upgradeSlotAt(double mouseX, double mouseY) {
        for (int i = 0; i < DrillUpgradeMenu.UPGRADE_SLOTS; i++) {
            Slot slot = this.menu.getSlot(i);
            if (this.isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY)) {
                return slot;
            }
        }
        return null;
    }

    /** Same layout as the HUD hotbar: 182 pixels wide, sitting on the bottom of the screen. */
    private int hotbarIndexAt(double mouseX, double mouseY) {
        if (mouseY < this.height - 22 || mouseY >= this.height) {
            return -1;
        }
        double local = mouseX - (this.width / 2 - 91);
        if (local < 0 || local >= 182) {
            return -1;
        }
        return (int) (local / 20);
    }

    /** One dispenser slot, drawn at the item position. Nothing is painted inside it. */
    private void drawSlot(GuiGraphicsExtractor graphics, int itemX, int itemY) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, DISPENSER, itemX - 1, itemY - 1, 61.0F, 16.0F, 18, 18, 256, 256);
    }

    /** Left and right bevels stay put. The gray middle is repeated so the window can be wider than the texture. */
    private void blitSlice(GuiGraphicsExtractor graphics, int x, int y, int width, int srcV, int srcH) {
        int cap = 4;
        graphics.blit(RenderPipelines.GUI_TEXTURED, DISPENSER, x, y, 0.0F, srcV, cap, srcH, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, DISPENSER, x + width - cap, y, 172.0F, srcV, cap, srcH, 256, 256);
        int filled = cap;
        int end = width - cap;
        while (filled < end) {
            int chunk = Math.min(168, end - filled);
            graphics.blit(RenderPipelines.GUI_TEXTURED, DISPENSER, x + filled, y, 4.0F, srcV, chunk, srcH, 256, 256);
            filled += chunk;
        }
    }
}
