package com.codekanic.ckutilities.client;

import com.codekanic.ckutilities.CKUtilities;
import com.codekanic.ckutilities.common.inventory.ChargerMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * Two separate windows. The top one is the charger face (power slot and two charge slots).
 * The player inventory is its own window underneath, with a clear gap so the panels are not one GUI.
 */
public class ChargerScreen extends AbstractContainerScreen<ChargerMenu> {
    private static final Identifier TEXTURE = CKUtilities.modLoc("textures/gui/container/charger.png");
    private static final Identifier DISPENSER = Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");
    /** Bottom edge of the charger texture, used as the machine window's own cap. */
    private static final int CAP_V = 159;

    public ChargerScreen(ChargerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, ChargerMenu.IMAGE_WIDTH, ChargerMenu.IMAGE_HEIGHT);
        this.inventoryLabelX = ChargerMenu.INV_LABEL_X;
        this.inventoryLabelY = ChargerMenu.INV_LABEL_Y;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int left = this.leftPos;
        int top = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, left, top, 0.0F, 0.0F, ChargerMenu.MACHINE_PANEL_WIDTH, ChargerMenu.MACHINE_FACE_HEIGHT, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, left, top + ChargerMenu.MACHINE_FACE_HEIGHT, 0.0F, CAP_V, ChargerMenu.MACHINE_PANEL_WIDTH, ChargerMenu.MACHINE_CAP_HEIGHT, 256, 256);

        int inventoryLeft = left + ChargerMenu.INV_PANEL_X;
        int inventoryTop = top + ChargerMenu.INV_PANEL_Y;
        this.drawPanel(graphics, inventoryLeft, inventoryTop, ChargerMenu.INV_PANEL_WIDTH, ChargerMenu.INV_PANEL_HEIGHT);
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.drawSlot(graphics, left + ChargerMenu.PLAYER_X + column * 18, top + ChargerMenu.PLAYER_INV_Y + row * 18);
            }
        }
        for (int column = 0; column < 9; column++) {
            this.drawSlot(graphics, left + ChargerMenu.PLAYER_X + column * 18, top + ChargerMenu.HOTBAR_Y);
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
