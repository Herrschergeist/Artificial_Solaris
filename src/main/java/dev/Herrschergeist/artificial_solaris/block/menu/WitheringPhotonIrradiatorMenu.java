package dev.Herrschergeist.artificial_solaris.block.menu;

import dev.Herrschergeist.artificial_solaris.block.entity.PhotonIrradiatorBlockEntity;
import dev.Herrschergeist.artificial_solaris.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class WitheringPhotonIrradiatorMenu extends BasePhotonIrradiatorMenu {

    private static final int CRAFT_SLOTS = 6;
    private static final int INPUT_Y = 9;
    private static final int OUTPUT_Y = 59;

    public WitheringPhotonIrradiatorMenu(int windowId, Inventory playerInventory, PhotonIrradiatorBlockEntity be) {
        super(ModMenuTypes.WITHERING_PHOTON_IRRADIATOR.get(), windowId, playerInventory, be, CRAFT_SLOTS);
    }

    public WitheringPhotonIrradiatorMenu(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(ModMenuTypes.WITHERING_PHOTON_IRRADIATOR.get(), windowId, playerInventory, CRAFT_SLOTS);
    }

    @Override
    protected int getInputSlotX(int slotIndex) {
        // Exact X positions for each of 6 slots
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
    protected int getInputSlotY() {
        return INPUT_Y;
    }

    @Override
    protected int getOutputSlotY() {
        return OUTPUT_Y;
    }
}