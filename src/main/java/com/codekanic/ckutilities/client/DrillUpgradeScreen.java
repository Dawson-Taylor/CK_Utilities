package com.codekanic.ckutilities.client;

import com.codekanic.ckutilities.common.menu.DrillUpgradeMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * Two separate windows. The top one is titled "Drill Upgrades" and holds the four named slots.
 * The player inventory is its own window underneath, with a clear gap so the panels are not one GUI.
 * Clicks and shift-clicks use the normal container slots.
 */
public class DrillUpgradeScreen extends AbstractContainerScreen<DrillUpgradeMenu> {
    private static final Identifier DISPENSER = Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");
    private static final int LABEL_Y = 39;
    private static final int LABEL_COLOR = -12566464;

    private static final Component[] SLOT_LABELS = {
            Component.translatable("container.ckutilities.drill_upgrades.hammer"),
            Component.translatable("container.ckutilities.drill_upgrades.efficiency"),
            Component.translatable("container.ckutilities.drill_upgrades.fortune"),
            Component.translatable("container.ckutilities.drill_upgrades.silk_touch")
    };

    public DrillUpgradeScreen(DrillUpgradeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, DrillUpgradeMenu.IMAGE_WIDTH, DrillUpgradeMenu.IMAGE_HEIGHT);
        this.inventoryLabelX = DrillUpgradeMenu.INV_LABEL_X;
        this.inventoryLabelY = DrillUpgradeMenu.INV_LABEL_Y;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int left = this.leftPos;
        int top = this.topPos;
        this.drawPanel(graphics, left, top, DrillUpgradeMenu.UPGRADE_PANEL_WIDTH, DrillUpgradeMenu.UPGRADE_PANEL_HEIGHT);
        int inventoryLeft = left + DrillUpgradeMenu.INV_PANEL_X;
        int inventoryTop = top + DrillUpgradeMenu.INV_PANEL_Y;
        this.drawPanel(graphics, inventoryLeft, inventoryTop, DrillUpgradeMenu.INV_PANEL_WIDTH, DrillUpgradeMenu.INV_PANEL_HEIGHT);

        for (int i = 0; i < DrillUpgradeMenu.UPGRADE_SLOTS; i++) {
            this.drawSlot(graphics, left + DrillUpgradeMenu.upgradeX(i), top + DrillUpgradeMenu.UPGRADE_Y);
        }
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.drawSlot(graphics, left + DrillUpgradeMenu.PLAYER_X + column * 18, top + DrillUpgradeMenu.PLAYER_INV_Y + row * 18);
            }
        }
        for (int column = 0; column < 9; column++) {
            this.drawSlot(graphics, left + DrillUpgradeMenu.PLAYER_X + column * 18, top + DrillUpgradeMenu.HOTBAR_Y);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        for (int i = 0; i < SLOT_LABELS.length; i++) {
            Component label = SLOT_LABELS[i];
            int center = DrillUpgradeMenu.upgradeX(i) + 8;
            graphics.text(this.font, label, center - this.font.width(label) / 2, LABEL_Y, LABEL_COLOR, false);
        }
    }

    /** One dispenser slot, drawn at the item position. Nothing is painted inside it. */
    private void drawSlot(GuiGraphicsExtractor graphics, int itemX, int itemY) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, DISPENSER, itemX - 1, itemY - 1, 61.0F, 16.0F, 18, 18, 256, 256);
    }

    /** A complete window: top cap, stretched body, bottom cap. Drawn on its own, not as part of the other panel. */
    private void drawPanel(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        this.blitSlice(graphics, x, y, width, 0, 16);
        int bodyBottom = y + height - 7;
        for (int row = y + 16; row < bodyBottom; row++) {
            this.blitSlice(graphics, x, row, width, 8, 1);
        }
        this.blitSlice(graphics, x, bodyBottom, width, 159, 7);
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
