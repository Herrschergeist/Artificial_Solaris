package dev.Herrschergeist.artificial_solaris.block.menu;

import dev.Herrschergeist.artificial_solaris.block.entity.PhotonIrradiatorBlockEntity;
import dev.Herrschergeist.artificial_solaris.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class CopperPhotonIrradiatorMenu extends BasePhotonIrradiatorMenu {

    private static final int CRAFT_SLOTS = 1;
    private static final int INPUT_SLOT_X = 80;
    private static final int INPUT_Y = 9;   // Was 17, moved down 6 pixels
    private static final int OUTPUT_Y = 59;  // Was 62, moved down 3 pixels

    // Server constructor
    public CopperPhotonIrradiatorMenu(int windowId, Inventory playerInventory, PhotonIrradiatorBlockEntity be) {
        super(ModMenuTypes.COPPER_PHOTON_IRRADIATOR.get(), windowId, playerInventory, be, CRAFT_SLOTS);
    }

    // Client constructor
    public CopperPhotonIrradiatorMenu(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(ModMenuTypes.COPPER_PHOTON_IRRADIATOR.get(), windowId, playerInventory, CRAFT_SLOTS);
    }

    @Override
    protected int getInputSlotX(int slotIndex) {
        return INPUT_SLOT_X;
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