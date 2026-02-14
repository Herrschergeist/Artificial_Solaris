package dev.Herrschergeist.artificial_solaris.block.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.block.menu.SolarisRestraintMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class SolarisRestraintScreen extends AbstractContainerScreen<SolarisRestraintMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(artificial_solaris.MOD_ID,
                    "textures/gui/container/energy_gui.png");

    private static final int ENERGY_X      = 7;
    private static final int ENERGY_Y      = 23;
    private static final int ENERGY_WIDTH  = 164;
    private static final int ENERGY_HEIGHT = 36;

    public SolarisRestraintScreen(SolarisRestraintMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth  = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Background texture
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        // Energy bar fill (left to right)
        int filledWidth = (ENERGY_WIDTH * menu.getEnergyPercent()) / 100;
        if (filledWidth > 0) {
            graphics.blit(TEXTURE,
                    x + ENERGY_X,
                    y + ENERGY_Y,
                    0, 167,
                    filledWidth, ENERGY_HEIGHT,
                    256, 256);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Title centered at top
        int titleX = (imageWidth - font.width(title)) / 2;
        graphics.drawString(font, title, titleX, 6, 0x404040, false);

        // Player inventory label
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);

        // Tooltip on energy bar hover
        if (isHovering(ENERGY_X, ENERGY_Y, ENERGY_WIDTH, ENERGY_HEIGHT, mouseX, mouseY)) {
            int currentGen = menu.getCurrentGeneration();
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.literal("Energy: "
                    + formatFE(menu.getEnergy())
                    + " / "
                    + formatFE(menu.getMaxEnergy())
                    + " FE"));
            if (currentGen > 0) {
                tooltip.add(Component.literal("+" + formatFE(currentGen) + " FE/t")
                        .withStyle(style -> style.withColor(0x55AA55)));
            } else {
                tooltip.add(Component.literal("+0 FE/t")
                        .withStyle(style -> style.withColor(0x888888)));
            }
            graphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
        }
    }

    private static String formatFE(long fe) {
        if (fe >= 1_000_000_000) return String.format("%.1fG", fe / 1_000_000_000.0);
        if (fe >= 1_000_000)     return String.format("%.1fM", fe / 1_000_000.0);
        if (fe >= 1_000)         return String.format("%.1fk", fe / 1_000.0);
        return String.valueOf(fe);
    }
}