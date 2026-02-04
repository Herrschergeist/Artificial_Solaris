package dev.Herrschergeist.artificial_solaris.block.screen;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.block.menu.WitheringPhotonIrradiatorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WitheringPhotonIrradiatorScreen extends BasePhotonIrradiatorScreen<WitheringPhotonIrradiatorMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(artificial_solaris.MOD_ID,
                    "textures/gui/container/withering_irradiator_gui.png");

    private static final int ENERGY_BAR_X = 8;
    private static final int ENERGY_BAR_Y = 14;
    private static final int ENERGY_BAR_HEIGHT = 51;

    private static final int ARROW_START_X = 35;
    private static final int ARROW_Y = 32;

    public WitheringPhotonIrradiatorScreen(WitheringPhotonIrradiatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    protected String getTierName() {
        return "Withering";
    }

    @Override
    protected int getTierColor() {
        return 0xFF8B008B; // Dark-Violet
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
        return switch (slotIndex) {
            case 0 -> 31;
            case 1 -> 56;
            case 2 -> 80;
            case 3 -> 104;
            case 4 -> 128;
            case 5 -> 152;
            default -> 31;
        };
    }

    @Override
    protected int getArrowY() {
        return ARROW_Y;
    }
}