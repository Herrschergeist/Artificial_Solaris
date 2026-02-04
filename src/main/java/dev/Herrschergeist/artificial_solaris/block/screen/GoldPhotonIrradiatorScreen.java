package dev.Herrschergeist.artificial_solaris.block.screen;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.block.menu.GoldPhotonIrradiatorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GoldPhotonIrradiatorScreen extends BasePhotonIrradiatorScreen<GoldPhotonIrradiatorMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(artificial_solaris.MOD_ID,
                    "textures/gui/container/gold_irradiator_gui.png");

    private static final int ENERGY_BAR_X = 8;
    private static final int ENERGY_BAR_Y = 14;
    private static final int ENERGY_BAR_HEIGHT = 51;

    private static final int ARROW_Y = 32;

    public GoldPhotonIrradiatorScreen(GoldPhotonIrradiatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    protected String getTierName() {
        return "Gold";
    }

    @Override
    protected int getTierColor() {
        return 0xFFFFD700; // Gold
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
            case 0 -> 44;
            case 1 -> 80;
            case 2 -> 116;
            default -> 44;
        };
    }

    @Override
    protected int getArrowY() {
        return ARROW_Y;
    }
}