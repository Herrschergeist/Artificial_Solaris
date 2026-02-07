package dev.Herrschergeist.artificial_solaris.block.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.block.menu.DragonForgeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DragonForgeScreen extends AbstractContainerScreen<DragonForgeMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(artificial_solaris.MOD_ID,
                    "textures/gui/container/dragon_forge_gui.png");

    // Energy bar
    private static final int ENERGY_BAR_X = 8;
    private static final int ENERGY_BAR_Y = 15;
    private static final int ENERGY_BAR_WIDTH = 14;
    private static final int ENERGY_BAR_HEIGHT = 51;

    // Water tank
    private static final int WATER_TANK_X = 28;
    private static final int WATER_TANK_Y = 17;
    private static final int WATER_TANK_WIDTH = 14;
    private static final int WATER_TANK_HEIGHT = 47;

    // Dragon breath animation
    private static final int BREATH_X = 95;
    private static final int BREATH_Y = 33;
    private static final int BREATH_WIDTH = 33;
    private static final int BREATH_HEIGHT = 28;

    public DragonForgeScreen(DragonForgeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 166;
        this.imageWidth = 176;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Render background
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        // Render energy bar
        renderEnergyBar(guiGraphics, x, y);

        // Render water tank
        renderWaterTank(guiGraphics, x, y);

        // Render dragon breath
        renderDragonBreath(guiGraphics, x, y);
    }

    private void renderEnergyBar(GuiGraphics guiGraphics, int x, int y) {
        int energy = menu.getEnergy();
        int maxEnergy = menu.getMaxEnergy();

        if (maxEnergy > 0) {
            int filled = (int) ((float) energy / maxEnergy * ENERGY_BAR_HEIGHT);

            if (filled > 0) {
                // Render filled portion (bottom to top)
                guiGraphics.blit(
                        TEXTURE,
                        x + ENERGY_BAR_X,
                        y + ENERGY_BAR_Y + (ENERGY_BAR_HEIGHT - filled),
                        176, // texture X (filled energy bar)
                        ENERGY_BAR_HEIGHT - filled, // texture Y
                        ENERGY_BAR_WIDTH,
                        filled,
                        256, 256
                );
            }
        }
    }

    private void renderWaterTank(GuiGraphics guiGraphics, int x, int y) {
        int water = menu.getWater();
        int maxWater = menu.getMaxWater();

        if (maxWater > 0) {
            int filled = (int) ((float) water / maxWater * WATER_TANK_HEIGHT);

            if (filled > 0) {
                // Render filled portion (bottom to top)
                guiGraphics.blit(
                        TEXTURE,
                        x + WATER_TANK_X,
                        y + WATER_TANK_Y + (WATER_TANK_HEIGHT - filled),
                        176, // texture X (filled water)
                        81 + (WATER_TANK_HEIGHT - filled), // texture Y
                        WATER_TANK_WIDTH,
                        filled,
                        256, 256
                );
            }
        }
    }

    private void renderDragonBreath(GuiGraphics guiGraphics, int x, int y) {
        int progress = menu.getProgress();
        int maxProgress = menu.getMaxProgress();

        if (maxProgress > 0 && progress > 0) {
            int filled = (int) ((float) progress / maxProgress * 33);  // 110-77=33 ширина

            if (filled > 0) {
                // Render breath animation (RIGHT to LEFT)
                guiGraphics.blit(
                        TEXTURE,
                        x + 77 + (33 - filled),
                        y + 27,
                        176 + (33 - filled),
                        52,
                        filled,
                        28,
                        256, 256
                );
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Energy bar tooltip
        if (isHovering(ENERGY_BAR_X, ENERGY_BAR_Y, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT, mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font,
                    Component.literal("Energy: " + formatEnergy(menu.getEnergy()) + " / " + formatEnergy(menu.getMaxEnergy())),
                    mouseX, mouseY);
        }

        // Water tank tooltip
        if (isHovering(WATER_TANK_X, WATER_TANK_Y, WATER_TANK_WIDTH, WATER_TANK_HEIGHT, mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font,
                    Component.literal("Water: " + menu.getWater() + " / " + menu.getMaxWater() + " mB"),
                    mouseX, mouseY);
        }
    }

    private boolean isHovering(int x, int y, int width, int height, int mouseX, int mouseY) {
        int leftPos = (this.width - this.imageWidth) / 2;
        int topPos = (this.height - this.imageHeight) / 2;
        return mouseX >= leftPos + x && mouseX < leftPos + x + width &&
                mouseY >= topPos + y && mouseY < topPos + y + height;
    }

    private String formatEnergy(int energy) {
        if (energy >= 1_000_000) {
            return String.format("%.1fM", energy / 1_000_000.0);
        } else if (energy >= 1_000) {
            return String.format("%.1fk", energy / 1_000.0);
        }
        return String.valueOf(energy);
    }
}