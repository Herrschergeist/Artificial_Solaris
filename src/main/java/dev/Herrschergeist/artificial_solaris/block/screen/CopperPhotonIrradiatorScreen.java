package dev.Herrschergeist.artificial_solaris.block.screen;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.block.menu.CopperPhotonIrradiatorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CopperPhotonIrradiatorScreen extends BasePhotonIrradiatorScreen<CopperPhotonIrradiatorMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(artificial_solaris.MOD_ID,
                    "textures/gui/container/copper_irradiator_gui.png");

    // Exact coordinates for Copper tier (1 slot, centered)
    private static final int ENERGY_BAR_X = 8;
    private static final int ENERGY_BAR_Y = 14;
    private static final int ENERGY_BAR_HEIGHT = 51;

    private static final int ARROW_X = 80;  // Same as slot X
    private static final int ARROW_Y = 32;  // Between input and output slots

    public CopperPhotonIrradiatorScreen(CopperPhotonIrradiatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    protected String getTierName() {
        return "Copper";
    }

    @Override
    protected int getTierColor() {
        return 0xFF8B4513; // Brown
    }

    @Override
    protected int getEnergyBarX() {
        return ENERGY_BAR_X;
    }

    @Override
    protected int getEnergyBarY() {
        return ENERGY_BAR_Y;
    }

    @Override
    protected int getEnergyBarHeight() {
        return ENERGY_BAR_HEIGHT;
    }

    @Override
    protected int getArrowX(int slotIndex) {
        return ARROW_X;
    }

    @Override
    protected int getArrowY() {
        return ARROW_Y;
    }
}