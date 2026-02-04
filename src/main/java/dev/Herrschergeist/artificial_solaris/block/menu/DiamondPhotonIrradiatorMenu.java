package dev.Herrschergeist.artificial_solaris.block.menu;

import dev.Herrschergeist.artificial_solaris.block.entity.PhotonIrradiatorBlockEntity;
import dev.Herrschergeist.artificial_solaris.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class DiamondPhotonIrradiatorMenu extends BasePhotonIrradiatorMenu {

    private static final int CRAFT_SLOTS = 4;
    private static final int INPUT_Y = 9;
    private static final int OUTPUT_Y = 59;

    public DiamondPhotonIrradiatorMenu(int windowId, Inventory playerInventory, PhotonIrradiatorBlockEntity be) {
        super(ModMenuTypes.DIAMOND_PHOTON_IRRADIATOR.get(), windowId, playerInventory, be, CRAFT_SLOTS);
    }

    public DiamondPhotonIrradiatorMenu(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(ModMenuTypes.DIAMOND_PHOTON_IRRADIATOR.get(), windowId, playerInventory, CRAFT_SLOTS);
    }

    @Override
    protected int getInputSlotX(int slotIndex) {
        // Exact X positions for each of 4 slots
        return switch (slotIndex) {
            case 0 -> 35;
            case 1 -> 70;
            case 2 -> 107;
            case 3 -> 142;
            default -> 35;
        };
    }

    @Override
    protected int getInputSlotY() {
        return INPUT_Y;
    }

    @Override
    protected int getOutputSlotY() {
        return OUTPUT_Y;
    }
}