package dev.Herrschergeist.artificial_solaris.block.menu;

import dev.Herrschergeist.artificial_solaris.block.entity.PhotonIrradiatorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class BasePhotonIrradiatorMenu extends AbstractContainerMenu {

    protected final PhotonIrradiatorBlockEntity blockEntity;
    protected final SimpleContainer container;
    protected final ContainerData data;
    protected final int craftSlots;

    // Subclasses override these for exact positions
    protected abstract int getInputSlotX(int slotIndex);
    protected abstract int getInputSlotY();
    protected abstract int getOutputSlotY();

    // Server constructor
    protected BasePhotonIrradiatorMenu(MenuType<?> menuType, int windowId, Inventory playerInventory,
                                       PhotonIrradiatorBlockEntity be, int craftSlots) {
        super(menuType, windowId);
        this.blockEntity = be;
        this.container = be.getCraftInventory();
        this.craftSlots = craftSlots;
        this.data = new SimpleContainerData(2 + 2 * craftSlots);

        addSlots(playerInventory);
        addDataSlots(data);
    }

    // Client constructor
    protected BasePhotonIrradiatorMenu(MenuType<?> menuType, int windowId, Inventory playerInventory, int craftSlots) {
        super(menuType, windowId);
        this.blockEntity = null;
        this.container = new SimpleContainer(craftSlots * 2);
        this.craftSlots = craftSlots;
        this.data = new SimpleContainerData(2 + 2 * craftSlots);

        addSlots(playerInventory);
        addDataSlots(data);
    }

    private void addSlots(Inventory playerInventory) {
        int inputY = getInputSlotY();
        int outputY = getOutputSlotY();

        // Input slots
        for (int i = 0; i < craftSlots; i++) {
            int x = getInputSlotX(i);
            this.addSlot(new Slot(container, i, x, inputY));
        }

        // Output slots
        for (int i = 0; i < craftSlots; i++) {
            int x = getInputSlotX(i);
            this.addSlot(new Slot(container, craftSlots + i, x, outputY) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        }

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        8 + col * 18, 84 + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (blockEntity != null) {
            data.set(0, blockEntity.getEnergyStorage().getEnergyStored());
            data.set(1, blockEntity.getEnergyStorage().getMaxEnergyStored());

            for (int i = 0; i < craftSlots; i++) {
                data.set(2 + i, blockEntity.getProgress(i));
                data.set(2 + craftSlots + i, blockEntity.getTotalProgress(i));
            }
        }
    }

    public int getStoredEnergy() { return data.get(0); }
    public int getMaxEnergy() { return data.get(1); }
    public int getCraftSlots() { return craftSlots; }
    public int getProgress(int i) { return data.get(2 + i); }
    public int getTotalProgress(int i) { return data.get(2 + craftSlots + i); }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (blockEntity == null) return true;
        return blockEntity.getLevel() != null
                && !blockEntity.isRemoved()
                && player.distanceToSqr(blockEntity.getBlockPos().getX() + 0.5,
                blockEntity.getBlockPos().getY() + 0.5,
                blockEntity.getBlockPos().getZ() + 0.5) <= 64;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            int totalBlockSlots = 2 * craftSlots;

            if (index < totalBlockSlots) {
                if (!this.moveItemStackTo(stack, totalBlockSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stack, 0, craftSlots, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return result;
    }
}