package com.codekanic.ckutilities.client;

import com.codekanic.ckutilities.common.menu.DrillUpgradeMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * Labels sit beside four slots. The window is the vanilla dispenser GUI, stretched with its own gray row
 * so the slots and the player inventory still use Minecraft's widgets.
 */
public class DrillUpgradeScreen extends AbstractContainerScreen<DrillUpgradeMenu> {
    private static final Identifier DISPENSER = Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");
    private static final int BODY_TOP = 16;
    private static final int BODY_HEIGHT = 96;
    private static final int INVENTORY_TOP = BODY_TOP + BODY_HEIGHT;
    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = INVENTORY_TOP + 83;

    private static final Component[] SLOT_LABELS = {
            Component.translatable("container.ckutilities.drill_upgrades.hammer"),
            Component.translatable("container.ckutilities.drill_upgrades.efficiency"),
            Component.translatable("container.ckutilities.drill_upgrades.fortune"),
            Component.translatable("container.ckutilities.drill_upgrades.silk_touch")
    };

    public DrillUpgradeScreen(DrillUpgradeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, IMAGE_WIDTH, IMAGE_HEIGHT);
        this.inventoryLabelY = 101;
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
            graphics.blit(RenderPipelines.GUI_TEXTURED, DISPENSER, left + 7, top + 19 + i * 22, 61.0F, 16.0F, 18, 18, 256, 256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        for (int i = 0; i < SLOT_LABELS.length; i++) {
            graphics.text(this.font, SLOT_LABELS[i], 32, 24 + i * 22, -12566464, false);
        }
    }
}
