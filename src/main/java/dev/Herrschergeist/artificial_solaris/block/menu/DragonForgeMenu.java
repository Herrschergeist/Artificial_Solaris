package dev.Herrschergeist.artificial_solaris.block.menu;

import dev.Herrschergeist.artificial_solaris.block.entity.DragonForgeBlockEntity;
import dev.Herrschergeist.artificial_solaris.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class DragonForgeMenu extends AbstractContainerMenu {

    private final DragonForgeBlockEntity blockEntity;
    private final ContainerData data;
    private final ItemStackHandler inventory;

    // Slot indices
    private static final int BLAZE_POWDER_SLOT = 0;
    private static final int BUCKET_SLOT = 1;
    private static final int DRAGON_HEAD_SLOT = 2;
    private static final int INPUT_SLOT = 3;
    private static final int OUTPUT_SLOT = 4;

    // Server constructor
    public DragonForgeMenu(int windowId, Inventory playerInventory, DragonForgeBlockEntity be, ContainerData data) {
        super(ModMenuTypes.DRAGON_FORGE.get(), windowId);
        this.blockEntity = be;
        this.data = data;
        this.inventory = be != null ? be.getInventory() : new ItemStackHandler(5);

        addDataSlots(data);

        // Blaze Powder slot (151, 7)
        this.addSlot(new SlotItemHandler(inventory, BLAZE_POWDER_SLOT, 152, 8) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.BLAZE_POWDER);
            }
        });

        // Bucket slot (45, 49)
        this.addSlot(new SlotItemHandler(inventory, BUCKET_SLOT, 46, 50) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.WATER_BUCKET);
            }
        });

        // Dragon Head slot (114, 33)
        this.addSlot(new SlotItemHandler(inventory, DRAGON_HEAD_SLOT, 115, 34) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.DRAGON_HEAD);
            }
        });

        // Input slot (79, 8)
        this.addSlot(new SlotItemHandler(inventory, INPUT_SLOT, 80, 9));

        // Output slot (79, 58) - output only
        this.addSlot(new SlotItemHandler(inventory, OUTPUT_SLOT, 80, 59) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });

        // Player inventory
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    // Client constructor
    public DragonForgeMenu(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, null, new SimpleContainerData(6));
    }

    // Data indices: 0=energy, 1=maxEnergy, 2=water, 3=maxWater, 4=progress, 5=maxProgress
    public int getEnergy() { return data.get(0); }
    public int getMaxEnergy() { return data.get(1); }
    public int getWater() { return data.get(2); }
    public int getMaxWater() { return data.get(3); }
    public int getProgress() { return data.get(4); }
    public int getMaxProgress() { return data.get(5); }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            // From container to player
            if (index < 5) {
                if (!this.moveItemStackTo(slotStack, 5, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            // From player to container
            else {
                if (slotStack.is(Items.BLAZE_POWDER)) {
                    if (!this.moveItemStackTo(slotStack, BLAZE_POWDER_SLOT, BLAZE_POWDER_SLOT + 1, false))
                        return ItemStack.EMPTY;
                } else if (slotStack.is(Items.WATER_BUCKET)) {
                    if (!this.moveItemStackTo(slotStack, BUCKET_SLOT, BUCKET_SLOT + 1, false))
                        return ItemStack.EMPTY;
                } else if (slotStack.is(Items.DRAGON_HEAD)) {
                    if (!this.moveItemStackTo(slotStack, DRAGON_HEAD_SLOT, DRAGON_HEAD_SLOT + 1, false))
                        return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(slotStack, INPUT_SLOT, INPUT_SLOT + 1, false))
                        return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (blockEntity == null) return true; // Allow on client
        return blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) == blockEntity &&
                player.distanceToSqr(blockEntity.getBlockPos().getX() + 0.5,
                        blockEntity.getBlockPos().getY() + 0.5,
                        blockEntity.getBlockPos().getZ() + 0.5) <= 64;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }
}