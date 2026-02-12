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

    // Slot indices (7 total)
    public static final int BLAZE_POWDER_SLOT = 0;
    public static final int BUCKET_SLOT       = 1;
    public static final int DRAGON_HEAD_SLOT  = 2;
    public static final int INPUT_LEFT_SLOT   = 3; // (54, 9)
    public static final int INPUT_MID_SLOT    = 4; // (80, 9)
    public static final int INPUT_RIGHT_SLOT  = 5; // (106, 9)
    public static final int OUTPUT_SLOT       = 6;

    private static final int CONTAINER_SLOTS = 7;

    // Server constructor
    public DragonForgeMenu(int windowId, Inventory playerInventory,
                           DragonForgeBlockEntity be, ContainerData data) {
        super(ModMenuTypes.DRAGON_FORGE.get(), windowId);
        this.blockEntity = be;
        this.data = data;
        this.inventory = be != null ? be.getInventory() : new ItemStackHandler(CONTAINER_SLOTS);

        addDataSlots(data);

        // Blaze Powder slot
        this.addSlot(new SlotItemHandler(inventory, BLAZE_POWDER_SLOT, 152, 8) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.BLAZE_POWDER);
            }
        });

        // Water Bucket slot
        this.addSlot(new SlotItemHandler(inventory, BUCKET_SLOT, 46, 50) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.WATER_BUCKET);
            }
        });

        // Dragon Head slot
        this.addSlot(new SlotItemHandler(inventory, DRAGON_HEAD_SLOT, 115, 34) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.DRAGON_HEAD);
            }
            @Override
            public int getMaxStackSize(@NotNull ItemStack stack) {
                return 1;
            }
        });

        // Input slot LEFT (54, 9)
        this.addSlot(new SlotItemHandler(inventory, INPUT_LEFT_SLOT, 54, 9));

        // Input slot MID (80, 9)
        this.addSlot(new SlotItemHandler(inventory, INPUT_MID_SLOT, 80, 9));

        // Input slot RIGHT (106, 9)
        this.addSlot(new SlotItemHandler(inventory, INPUT_RIGHT_SLOT, 106, 9));

        // Output slot - no placement allowed
        this.addSlot(new SlotItemHandler(inventory, OUTPUT_SLOT, 80, 59) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });

        // Player inventory and hotbar
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    // Client constructor
    public DragonForgeMenu(int windowId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(windowId, playerInventory, null, new SimpleContainerData(6));
    }

    // ContainerData getters for GUI rendering
    public int getEnergy()      { return data.get(0); }
    public int getMaxEnergy()   { return data.get(1); }
    public int getWater()       { return data.get(2); }
    public int getMaxWater()    { return data.get(3); }
    public int getProgress()    { return data.get(4); }
    public int getMaxProgress() { return data.get(5); }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            // From container slots to player inventory
            if (index < CONTAINER_SLOTS) {
                if (!this.moveItemStackTo(slotStack, CONTAINER_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            // From player inventory to container
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
                    // Try to fill input slots left to right
                    if (!this.moveItemStackTo(slotStack, INPUT_LEFT_SLOT, OUTPUT_SLOT, false))
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
        if (blockEntity == null) return true;
        return blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) == blockEntity &&
                player.distanceToSqr(
                        blockEntity.getBlockPos().getX() + 0.5,
                        blockEntity.getBlockPos().getY() + 0.5,
                        blockEntity.getBlockPos().getZ() + 0.5) <= 64;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory,
                        col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }
}