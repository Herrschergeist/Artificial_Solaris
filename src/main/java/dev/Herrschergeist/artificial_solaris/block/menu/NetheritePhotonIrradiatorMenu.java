package dev.Herrschergeist.artificial_solaris.block.menu;

import dev.Herrschergeist.artificial_solaris.block.entity.PhotonIrradiatorBlockEntity;
import dev.Herrschergeist.artificial_solaris.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class NetheritePhotonIrradiatorMenu extends BasePhotonIrradiatorMenu {

    private static final int CRAFT_SLOTS = 5;
    private static final int INPUT_Y = 9;
    private static final int OUTPUT_Y = 59;

    public NetheritePhotonIrradiatorMenu(int windowId, Inventory playerInventory, PhotonIrradiatorBlockEntity be) {
        super(ModMenuTypes.NETHERITE_PHOTON_IRRADIATOR.get(), windowId, playerInventory, be, CRAFT_SLOTS);
    }

    public NetheritePhotonIrradiatorMenu(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(ModMenuTypes.NETHERITE_PHOTON_IRRADIATOR.get(), windowId, playerInventory, CRAFT_SLOTS);
    }

    @Override
    protected int getInputSlotX(int slotIndex) {
        // Exact X positions for each of 5 slots
        return switch (slotIndex) {
            case 0 -> 25;
            case 1 -> 52;
            case 2 -> 80;
            case 3 -> 107;
            case 4 -> 135;
            default -> 25;
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