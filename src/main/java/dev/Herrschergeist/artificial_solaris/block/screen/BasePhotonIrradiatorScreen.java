package dev.Herrschergeist.artificial_solaris.block.screen;

import dev.Herrschergeist.artificial_solaris.block.menu.BasePhotonIrradiatorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class BasePhotonIrradiatorScreen<T extends BasePhotonIrradiatorMenu> extends AbstractContainerScreen<T> {

    // Subclasses provide these
    protected abstract ResourceLocation getTexture();
    protected abstract String getTierName();
    protected abstract int getTierColor();
    protected abstract int getEnergyBarX();
    protected abstract int getEnergyBarY();
    protected abstract int getEnergyBarHeight();
    protected abstract int getArrowX(int slotIndex);
    protected abstract int getArrowY();

    // Common constants
    protected static final int ENERGY_BAR_WIDTH = 14;
    protected static final int ARROW_WIDTH = 16;
    protected static final int ARROW_HEIGHT = 22;

    public BasePhotonIrradiatorScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        // Draw background texture
        guiGraphics.blit(getTexture(), x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        // Draw energy bar
        renderEnergyBar(guiGraphics, x, y);

        // Draw progress arrows
        renderProgressArrows(guiGraphics, x, y);
    }

    private void renderEnergyBar(GuiGraphics guiGraphics, int x, int y) {
        int stored = menu.getStoredEnergy();
        int max = menu.getMaxEnergy();

        if (max <= 0) return;

        int barX = getEnergyBarX();
        int barY = getEnergyBarY();
        int barHeight = getEnergyBarHeight();

        // Calculate fill
        int fillHeight = (int) ((double) stored / max * barHeight);

        // Position of filled energy in texture (right side - red bar)
        int filledU = 176;
        int filledV = 0;

        // Draw from bottom up
        if (fillHeight > 0) {
            int sourceY = barHeight - fillHeight;

            guiGraphics.blit(
                    getTexture(),
                    x + barX, y + barY + sourceY,
                    filledU, filledV + sourceY,
                    ENERGY_BAR_WIDTH, fillHeight,
                    256, 256
            );
        }
    }

    private void renderProgressArrows(GuiGraphics guiGraphics, int x, int y) {
        int arrowY = getArrowY();

        // Position of filled arrow in texture (white arrow on right)
        int filledArrowU = 176;
        int filledArrowV = 54;

        int craftSlots = menu.getCraftSlots();

        for (int i = 0; i < craftSlots; i++) {
            int total = menu.getTotalProgress(i);
            int current = menu.getProgress(i);

            if (total > 0 && current > 0) {
                int arrowX = getArrowX(i);

                // Fill from top to bottom
                int fillHeight = (int) ((double) current / total * ARROW_HEIGHT);

                if (fillHeight > 0) {
                    guiGraphics.blit(
                            getTexture(),
                            x + arrowX, y + arrowY,
                            filledArrowU, filledArrowV,
                            ARROW_WIDTH, fillHeight,
                            256, 256
                    );
                }
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderTitleAboveGUI(guiGraphics);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        renderEnergyTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderTitleAboveGUI(GuiGraphics guiGraphics) {
        String baseTitle = "Photon Irradiator (";
        String tierName = getTierName();
        String closingBracket = ")";

        int baseTitleWidth = this.font.width(baseTitle);
        int tierNameWidth = this.font.width(tierName);
        int closingWidth = this.font.width(closingBracket);
        int totalWidth = baseTitleWidth + tierNameWidth + closingWidth;

        int startX = (this.width - totalWidth) / 2;
        int y = this.topPos - 12;

        guiGraphics.drawString(this.font, baseTitle, startX, y, 0x404040, false);
        guiGraphics.drawString(this.font, tierName, startX + baseTitleWidth, y, getTierColor(), false);
        guiGraphics.drawString(this.font, closingBracket, startX + baseTitleWidth + tierNameWidth, y, 0x404040, false);
    }

    private void renderEnergyTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int barX = this.leftPos + getEnergyBarX();
        int barY = this.topPos + getEnergyBarY();
        int barHeight = getEnergyBarHeight();

        if (mouseX >= barX && mouseX <= barX + ENERGY_BAR_WIDTH
                && mouseY >= barY && mouseY <= barY + barHeight) {
            int stored = menu.getStoredEnergy();
            int max = menu.getMaxEnergy();

            guiGraphics.renderTooltip(this.font,
                    Component.literal("Energy: " + formatEnergy(stored) + " / " + formatEnergy(max) + " FE"),
                    mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.playerInventoryTitle,
                this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
    }

    protected String formatEnergy(int energy) {
        if (energy >= 1_000_000_000) return String.format("%.1fG", energy / 1_000_000_000.0);
        if (energy >= 1_000_000) return String.format("%.1fM", energy / 1_000_000.0);
        if (energy >= 1_000) return String.format("%.1fk", energy / 1_000.0);
        return String.valueOf(energy);
    }
}