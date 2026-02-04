package dev.Herrschergeist.artificial_solaris.block.menu;

import dev.Herrschergeist.artificial_solaris.block.entity.PhotonIrradiatorBlockEntity;
import dev.Herrschergeist.artificial_solaris.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class GoldPhotonIrradiatorMenu extends BasePhotonIrradiatorMenu {

    private static final int CRAFT_SLOTS = 3;
    private static final int INPUT_Y = 9;
    private static final int OUTPUT_Y = 59;

    public GoldPhotonIrradiatorMenu(int windowId, Inventory playerInventory, PhotonIrradiatorBlockEntity be) {
        super(ModMenuTypes.GOLD_PHOTON_IRRADIATOR.get(), windowId, playerInventory, be, CRAFT_SLOTS);
    }

    public GoldPhotonIrradiatorMenu(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(ModMenuTypes.GOLD_PHOTON_IRRADIATOR.get(), windowId, playerInventory, CRAFT_SLOTS);
    }

    @Override
    protected int getInputSlotX(int slotIndex) {
        // Exact X positions for each of 3 slots
        return switch (slotIndex) {
            case 0 -> 44;
            case 1 -> 80;
            case 2 -> 116;
            default -> 44;
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