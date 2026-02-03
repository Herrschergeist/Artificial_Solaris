package dev.Herrschergeist.artificial_solaris.block.screen;

import dev.Herrschergeist.artificial_solaris.block.custom.SolarPanelBlock;
import dev.Herrschergeist.artificial_solaris.block.menu.SolarPanelMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SolarPanelScreen extends AbstractContainerScreen<SolarPanelMenu> {
    private static final ResourceLocation INVENTORY_LOCATION =
            ResourceLocation.withDefaultNamespace("textures/gui/container/inventory.png");

    private static final int ENERGY_PANEL_HEIGHT = 60;
    private static final int GAP = 4;

    public SolarPanelScreen(SolarPanelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;

        // Height = Energy Panel + Gap + Inventory Slots
        this.imageHeight = ENERGY_PANEL_HEIGHT + GAP + 90;

        this.inventoryLabelY = 10000; // Hide label
        this.titleLabelY = 10000; // Hide standard title
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // 1. Energy panel
        renderEnergyPanel(guiGraphics, x, y);

        // 2. Inventory
        int inventoryY = y + ENERGY_PANEL_HEIGHT + GAP;

        // Only Slots
        // Coordinates in Texture: (0, 80) - beginning of slots, Pixel Height 80
        guiGraphics.blit(INVENTORY_LOCATION, x, inventoryY, 0, 80, this.imageWidth, 80);
    }

    private void renderEnergyPanel(GuiGraphics guiGraphics, int x, int y) {
        // Panel Background (hell-gray)
        guiGraphics.fill(x, y, x + this.imageWidth, y + ENERGY_PANEL_HEIGHT, 0xFFC6C6C6);

        // Dark Vignette
        guiGraphics.fill(x, y, x + this.imageWidth, y + 1, 0xFF373737); // Up
        guiGraphics.fill(x, y + ENERGY_PANEL_HEIGHT - 1, x + this.imageWidth, y + ENERGY_PANEL_HEIGHT, 0xFF373737); // Down
        guiGraphics.fill(x, y, x + 1, y + ENERGY_PANEL_HEIGHT, 0xFF373737); // Left
        guiGraphics.fill(x + this.imageWidth - 1, y, x + this.imageWidth, y + ENERGY_PANEL_HEIGHT, 0xFF373737); // Right

        // Hell Inner Vignette
        guiGraphics.fill(x + 1, y + 1, x + this.imageWidth - 1, y + 2, 0xFFFFFFFF);
        guiGraphics.fill(x + 1, y + 1, x + 2, y + ENERGY_PANEL_HEIGHT - 1, 0xFFFFFFFF);

        // Panel Name + Type
        renderPanelTitle(guiGraphics, x, y);

        // Energy Bar
        renderHorizontalEnergyBar(guiGraphics, x, y);
    }

    private void renderPanelTitle(GuiGraphics guiGraphics, int x, int y) {
        // Type Panel from Block Name
        String panelType = getPanelType();
        Component fullTitle = Component.literal("Solar Panel ").append(Component.literal("(" + panelType + ")").withStyle(style -> style.withColor(getPanelColor())));

        int titleWidth = this.font.width(fullTitle);
        int titleX = x + (this.imageWidth - titleWidth) / 2;

        guiGraphics.drawString(this.font, fullTitle, titleX, y + 6, 0x404040, false);
    }

    private String getPanelType() {
        String blockId = menu.getBlockEntity().getBlockState().getBlock().toString();

        if (blockId.contains("copper")) return "Copper";
        if (blockId.contains("iron")) return "Iron";
        if (blockId.contains("gold")) return "Gold";
        if (blockId.contains("diamond")) return "Diamond";
        if (blockId.contains("netherite")) return "Netherite";
        if (blockId.contains("withering")) return "Withering";

        return "Unknown";
    }

    private int getPanelColor() {
        String type = getPanelType();

        return switch (type) {
            case "Copper" -> 0xFF8B4513;      // Brown
            case "Iron" -> 0xFFD8D8D8;        // Hell-Gray
            case "Gold" -> 0xFFFFD700;        // Gold
            case "Diamond" -> 0xFF00FFFF;     // Turquoise
            case "Netherite" -> 0xFF654321;   // Dark-Brown
            case "Withering" -> 0xFF8B008B;   // Dark-Violet
            default -> 0xFF808080;            // Gray
        };
    }

    private void renderHorizontalEnergyBar(GuiGraphics guiGraphics, int x, int y) {
        int energy = menu.getEnergy();
        int maxEnergy = menu.getMaxEnergy();

        if (maxEnergy <= 0) return;

        // Bar Pos
        int barX = x + 8;
        int barY = y + 20;
        int barWidth = this.imageWidth - 16;
        int barHeight = 16;

        // Bar Background (Dark-Gray)
        guiGraphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF373737);

        // Black Vignette
        guiGraphics.fill(barX, barY, barX + barWidth, barY + 1, 0xFF000000);
        guiGraphics.fill(barX, barY + barHeight - 1, barX + barWidth, barY + barHeight, 0xFF000000);
        guiGraphics.fill(barX, barY, barX + 1, barY + barHeight, 0xFF000000);
        guiGraphics.fill(barX + barWidth - 1, barY, barX + barWidth, barY + barHeight, 0xFF000000);

        // Filling with Gradient
        int filledWidth = (int) ((energy / (float) maxEnergy) * (barWidth - 4));

        if (filledWidth > 0) {
            float percentage = energy / (float) maxEnergy;

            // Gradient
            for (int i = 0; i < filledWidth; i++) {
                float localPercentage = i / (float) filledWidth;
                int color = getGradientColor(localPercentage, percentage);
                guiGraphics.fill(barX + 2 + i, barY + 2, barX + 3 + i, barY + barHeight - 2, color);
            }
        }

        // Energy Text in the middle of the Bar
        String energyText = formatEnergy(energy) + " / " + formatEnergy(maxEnergy) + " FE";
        int textWidth = this.font.width(energyText);
        int textX = barX + (barWidth - textWidth) / 2;
        int textY = barY + (barHeight - this.font.lineHeight) / 2;

        guiGraphics.drawString(this.font, energyText, textX + 1, textY + 1, 0xFF000000, false);
        guiGraphics.drawString(this.font, energyText, textX, textY, 0xFFFFFFFF, false);

        // Energy Generation under the Bar
        SolarPanelBlock block = (SolarPanelBlock) menu.getBlockEntity().getBlockState().getBlock();
        String generation = "⚡ +" + formatEnergy(block.getEnergyPerTick()) + " FE/t";
        int genWidth = this.font.width(generation);
        int genX = barX + (barWidth - genWidth) / 2;
        int genY = barY + barHeight + 3;

        guiGraphics.drawString(this.font, generation, genX, genY, 0xFF55AA55, false);
    }

    private int getGradientColor(float position, float fillPercentage) {
        if (fillPercentage < 0.25f) {
            // Red
            int red = 180 + (int)(75 * position);
            return 0xFF000000 | (red << 16) | (0 << 8) | 0;
        } else if (fillPercentage < 0.5f) {
            // Red -> Orange
            int red = 255;
            int green = (int)(165 * position);
            return 0xFF000000 | (red << 16) | (green << 8) | 0;
        } else if (fillPercentage < 0.75f) {
            // Orange -> Yellow
            int red = 255;
            int green = 165 + (int)(90 * position);
            return 0xFF000000 | (red << 16) | (green << 8) | 0;
        } else {
            // Yellow -> Green
            int red = 255 - (int)(155 * position);
            int green = 255;
            return 0xFF000000 | (red << 16) | (green << 8) | 0;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    private String formatEnergy(int energy) {
        if (energy >= 1_000_000_000) {
            return String.format("%.1fG", energy / 1_000_000_000.0);
        } else if (energy >= 1_000_000) {
            return String.format("%.1fM", energy / 1_000_000.0);
        } else if (energy >= 1_000) {
            return String.format("%.1fk", energy / 1_000.0);
        }
        return String.valueOf(energy);
    }
}