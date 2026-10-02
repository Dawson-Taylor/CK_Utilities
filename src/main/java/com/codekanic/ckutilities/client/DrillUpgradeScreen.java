package com.codekanic.ckutilities.client;

import com.codekanic.ckutilities.common.menu.DrillUpgradeMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * Four upgrade slots in one row. An empty slot shows an emblem; a filled slot shows the upgrade item.
 * The window is the vanilla dispenser GUI, with a short gray band so the player inventory still lines up.
 */
public class DrillUpgradeScreen extends AbstractContainerScreen<DrillUpgradeMenu> {
    private static final Identifier DISPENSER = Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");
    private static final int BODY_TOP = 16;
    private static final int INVENTORY_TOP = 50;
    private static final int BODY_HEIGHT = INVENTORY_TOP - BODY_TOP;
    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = INVENTORY_TOP + 83;

    private static final int METAL = 0xFFC6C6C6;
    private static final int METAL_DARK = 0xFF555555;
    private static final int WOOD = 0xFF8B5A2B;
    private static final int WOOD_DARK = 0xFF5C3A1E;
    private static final int SPEED = 0xFFFFDD55;
    private static final int GEM = 0xFF5CDBF0;
    private static final int GEM_LIGHT = 0xFFD6FBFF;
    private static final int GEM_DARK = 0xFF1A8FA3;
    private static final int SILK = 0xFFF7F7FF;
    private static final int SILK_EDGE = 0xFFB8B8D8;
    private static final int SILK_THREAD = 0xFFD0D0EA;

    public DrillUpgradeScreen(DrillUpgradeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, IMAGE_WIDTH, IMAGE_HEIGHT);
        this.inventoryLabelY = INVENTORY_TOP - 11;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int left = this.leftPos;
        int top = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, DISPENSER, left, top, 0.0F, 0.0F, this.imageWidth, BODY_TOP, 256, 256);
        for (int y = 0; y < BODY_HEIGHT; y++) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, DISPENSER, left, top + BODY_TOP + y, 0.0F, 8.0F, this.imageWidth, 1, 256, 256);
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, DISPENSER, left, top + INVENTORY_TOP, 0.0F, 83.0F, this.imageWidth, 83, 256, 256);

        for (int i = 0; i < DrillUpgradeMenu.UPGRADE_SLOTS; i++) {
            int x = left + DrillUpgradeMenu.upgradeX(i);
            int y = top + DrillUpgradeMenu.UPGRADE_Y;
            graphics.blit(RenderPipelines.GUI_TEXTURED, DISPENSER, x - 1, y - 1, 61.0F, 16.0F, 18, 18, 256, 256);
        }

        graphics.nextStratum();
        for (int i = 0; i < DrillUpgradeMenu.UPGRADE_SLOTS; i++) {
            if (this.menu.getSlot(i).hasItem()) {
                continue;
            }
            int x = left + DrillUpgradeMenu.upgradeX(i);
            int y = top + DrillUpgradeMenu.UPGRADE_Y;
            switch (i) {
                case DrillUpgradeMenu.SLOT_HAMMER -> this.drawHammer(graphics, x, y);
                case DrillUpgradeMenu.SLOT_EFFICIENCY -> this.drawPickaxe(graphics, x, y);
                case DrillUpgradeMenu.SLOT_FORTUNE -> this.drawGem(graphics, x, y);
                case DrillUpgradeMenu.SLOT_SILK -> this.drawSilk(graphics, x, y);
                default -> {
                }
            }
        }
    }

    /** A hammer head over a short handle. */
    private void drawHammer(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fill(x + 1, y + 3, x + 15, y + 7, METAL);
        graphics.fill(x + 1, y + 6, x + 15, y + 7, METAL_DARK);
        graphics.fill(x + 6, y + 3, x + 10, y + 5, 0xFF8B8B8B);
        graphics.fill(x + 7, y + 7, x + 9, y + 14, WOOD);
        graphics.fill(x + 8, y + 7, x + 9, y + 14, WOOD_DARK);
    }

    /** A pick head, a handle, and two speed marks. */
    private void drawPickaxe(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fill(x + 1, y + 2, x + 7, y + 4, METAL);
        graphics.fill(x + 2, y + 4, x + 6, y + 6, METAL);
        graphics.fill(x + 9, y + 2, x + 15, y + 4, METAL);
        graphics.fill(x + 10, y + 4, x + 14, y + 6, METAL);
        graphics.fill(x + 7, y + 5, x + 9, y + 14, WOOD);
        graphics.fill(x + 8, y + 5, x + 9, y + 14, WOOD_DARK);
        graphics.fill(x + 11, y + 9, x + 15, y + 10, SPEED);
        graphics.fill(x + 12, y + 12, x + 15, y + 13, SPEED);
    }

    /** A faceted gem. */
    private void drawGem(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fill(x + 7, y + 1, x + 9, y + 3, GEM);
        graphics.fill(x + 5, y + 3, x + 11, y + 5, GEM);
        graphics.fill(x + 3, y + 5, x + 13, y + 8, GEM);
        graphics.fill(x + 5, y + 8, x + 11, y + 11, GEM);
        graphics.fill(x + 7, y + 11, x + 9, y + 14, GEM);
        graphics.fill(x + 6, y + 4, x + 8, y + 6, GEM_LIGHT);
        graphics.fill(x + 8, y + 8, x + 11, y + 10, GEM_DARK);
    }

    /** A pale square with a woven thread. */
    private void drawSilk(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fill(x + 2, y + 2, x + 14, y + 14, SILK);
        graphics.fill(x + 2, y + 2, x + 14, y + 3, SILK_EDGE);
        graphics.fill(x + 2, y + 13, x + 14, y + 14, SILK_EDGE);
        graphics.fill(x + 2, y + 2, x + 3, y + 14, SILK_EDGE);
        graphics.fill(x + 13, y + 2, x + 14, y + 14, SILK_EDGE);
        graphics.fill(x + 4, y + 6, x + 12, y + 7, SILK_THREAD);
        graphics.fill(x + 4, y + 9, x + 12, y + 10, SILK_THREAD);
        graphics.fill(x + 6, y + 4, x + 7, y + 12, SILK_THREAD);
        graphics.fill(x + 9, y + 4, x + 10, y + 12, SILK_THREAD);
    }
}
