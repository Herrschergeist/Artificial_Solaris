package dev.Herrschergeist.artificial_solaris.block.entity;

import dev.Herrschergeist.artificial_solaris.block.custom.DragonForgeBlock;
import dev.Herrschergeist.artificial_solaris.block.menu.DragonForgeMenu;
import dev.Herrschergeist.artificial_solaris.recipe.DragonForgeRecipe;
import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DragonForgeBlockEntity extends BlockEntity implements MenuProvider {

    // Slot layout:
    // 0 - Blaze Powder (fuel)
    // 1 - Water Bucket
    // 2 - Dragon Head
    // 3 - Input slot LEFT  (54, 9)
    // 4 - Input slot MID   (80, 9)
    // 5 - Input slot RIGHT (106, 9)
    // 6 - Output slot
    private static final int BLAZE_POWDER_SLOT = 0;
    private static final int BUCKET_SLOT       = 1;
    private static final int DRAGON_HEAD_SLOT  = 2;
    private static final int INPUT_LEFT_SLOT   = 3;
    private static final int INPUT_MID_SLOT    = 4;
    private static final int INPUT_RIGHT_SLOT  = 5;
    private static final int OUTPUT_SLOT       = 6;

    private final ItemStackHandler inventory = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (slot == 2) { // Dragon head slot
                updateBlockState();
            }
        }
    };

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(10000000, 1000000, 0);
    private int waterStored = 0;
    private static final int MAX_WATER = 100000;

    private int progress = 0;
    private static final int MAX_PROGRESS = 200;

    private int bucketProcessCooldown = 0;

    private DragonForgeRecipe currentRecipe = null;
    private int waterPerTick = 0;
    private int energyPerTick = 0;
    private boolean isCrafting = false;

    // Fluid handler - only accepts water
    private final IFluidHandler fluidHandler = new IFluidHandler() {
        @Override
        public int getTanks() { return 1; }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return new FluidStack(Fluids.WATER, waterStored);
        }

        @Override
        public int getTankCapacity(int tank) { return MAX_WATER; }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return stack.getFluid() == Fluids.WATER;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.getFluid() != Fluids.WATER) return 0;
            int space = MAX_WATER - waterStored;
            int toFill = Math.min(space, resource.getAmount());
            if (action.execute() && toFill > 0) {
                waterStored += toFill;
                setChanged();
            }
            return toFill;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }
    };

    // Item handler exposed to pipes/hoppers with slot restrictions
    private final IItemHandler itemHandler = new IItemHandler() {
        @Override
        public int getSlots() {
            return inventory.getSlots();
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return inventory.getStackInSlot(slot);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            // Check slot validity - each slot only accepts specific items
            boolean allowed = switch (slot) {
                case BLAZE_POWDER_SLOT -> stack.is(Items.BLAZE_POWDER);
                case BUCKET_SLOT -> stack.is(Items.WATER_BUCKET);
                case DRAGON_HEAD_SLOT -> stack.is(Items.DRAGON_HEAD);
                case OUTPUT_SLOT -> false; // No insertion into output
                default -> // Input slots: reject blaze powder, dragon head, bucket
                        !stack.is(Items.BLAZE_POWDER) &&
                                !stack.is(Items.DRAGON_HEAD) &&
                                !stack.is(Items.WATER_BUCKET);
            };

            if (!allowed) return stack; // Reject item
            return inventory.insertItem(slot, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            // Only allow extraction from output slot
            if (slot == OUTPUT_SLOT) {
                return inventory.extractItem(slot, amount, simulate);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            // Dragon head slot - max 1
            if (slot == DRAGON_HEAD_SLOT) return 1;
            return inventory.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case BLAZE_POWDER_SLOT -> stack.is(Items.BLAZE_POWDER);
                case BUCKET_SLOT -> stack.is(Items.WATER_BUCKET);
                case DRAGON_HEAD_SLOT -> stack.is(Items.DRAGON_HEAD);
                case OUTPUT_SLOT -> false;
                default -> !stack.is(Items.BLAZE_POWDER) &&
                        !stack.is(Items.DRAGON_HEAD) &&
                        !stack.is(Items.WATER_BUCKET);
            };
        }
    };

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> energyStorage.getEnergyStored();
                case 1 -> energyStorage.getMaxEnergyStored();
                case 2 -> waterStored;
                case 3 -> MAX_WATER;
                case 4 -> progress;
                case 5 -> currentRecipe != null ? currentRecipe.getProcessingTime() : MAX_PROGRESS;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {}

        @Override
        public int getCount() { return 6; }
    };

    public DragonForgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRAGON_FORGE.get(), pos, state);
    }

    // Only allow fluid from the right side (water tank side)
    public IFluidHandler getFluidHandler(Direction side) {
        Direction facing = getBlockState().getValue(DragonForgeBlock.FACING);
        Direction tankSide = facing.getClockWise();
        if (side == tankSide) return fluidHandler;
        return null;
    }

    // Allow energy from all sides except top and water tank side
    public IEnergyStorage getEnergyStorage(Direction side) {
        Direction facing = getBlockState().getValue(DragonForgeBlock.FACING);
        Direction tankSide = facing.getClockWise();
        if (side != Direction.UP && side != tankSide) return energyStorage;
        return null;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DragonForgeBlockEntity be) {
        if (level.isClientSide) return;

        // Bucket cooldown
        if (be.bucketProcessCooldown > 0) {
            be.bucketProcessCooldown--;
        }
        if (be.bucketProcessCooldown == 0) {
            be.processWaterBucket();
        }

        if (be.canProcess()) {
            if (!be.isCrafting) {
                be.isCrafting = true;
                be.progress = 0;
            }

            be.progress++;
            be.craftItem();

            if (be.currentRecipe != null && be.progress >= be.currentRecipe.getProcessingTime()) {
                be.progress = 0;
                be.isCrafting = false;
            }
        } else {
            be.progress = 0;
            be.currentRecipe = null;
            be.isCrafting = false;
        }

        be.setChanged();
    }

    private void processWaterBucket() {
        ItemStack bucketStack = inventory.getStackInSlot(1);
        if (bucketStack.is(Items.WATER_BUCKET) && waterStored + 1000 <= MAX_WATER) {
            waterStored += 1000;
            bucketStack.shrink(1);
            if (bucketStack.isEmpty()) {
                inventory.setStackInSlot(1, new ItemStack(Items.BUCKET));
            }
            bucketProcessCooldown = 20;
            setChanged();
        }
    }

    private boolean canProcess() {
        ItemStack blazePowder = inventory.getStackInSlot(0);
        ItemStack dragonHead = inventory.getStackInSlot(2);

        // Blaze powder and dragon head are always required
        if (blazePowder.isEmpty() || dragonHead.isEmpty()) {
            currentRecipe = null;
            isCrafting = false;
            return false;
        }

        // At least one input slot must have an item
        boolean anyInput = !inventory.getStackInSlot(3).isEmpty() ||
                !inventory.getStackInSlot(4).isEmpty() ||
                !inventory.getStackInSlot(5).isEmpty();
        if (!anyInput) {
            currentRecipe = null;
            isCrafting = false;
            return false;
        }

        // Always check if current inputs still match, even while crafting
        if (currentRecipe == null || !matchesCurrentInputs()) {
            isCrafting = false;
            currentRecipe = findRecipe();
            if (currentRecipe != null) {
                waterPerTick = currentRecipe.getWaterCost() / currentRecipe.getProcessingTime();
                energyPerTick = currentRecipe.getEnergyCost() / currentRecipe.getProcessingTime();
            }
        }

        if (currentRecipe == null) {
            isCrafting = false;
            return false;
        }

        // Check if enough resources for full craft
        if (waterStored < currentRecipe.getWaterCost() ||
                energyStorage.getEnergyStored() < currentRecipe.getEnergyCost()) {
            return false;
        }

        // Check output space
        ItemStack output = inventory.getStackInSlot(6);
        ItemStack result = currentRecipe.getOutput();
        if (output.isEmpty()) return true;

        return output.getItem() == result.getItem() &&
                output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    // Check if current inputs still satisfy the active recipe (any order)
    private boolean matchesCurrentInputs() {
        if (currentRecipe == null) return false;
        return canSatisfyRecipe(currentRecipe);
    }

    // Try to match recipe ingredients against input slots in any order
    private boolean canSatisfyRecipe(DragonForgeRecipe recipe) {
        // Collect all non-empty input stacks
        List<ItemStack> available = new java.util.ArrayList<>();
        for (int i = 3; i <= 5; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) available.add(stack);
        }

        // Each required ingredient must be satisfied by a unique slot
        boolean[] used = new boolean[available.size()];

        for (DragonForgeRecipe.CountedIngredient ci : recipe.getInputs()) {
            boolean found = false;
            for (int j = 0; j < available.size(); j++) {
                if (!used[j] && ci.ingredient().test(available.get(j))
                        && available.get(j).getCount() >= ci.count()) {
                    used[j] = true;
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    // Find recipe that matches inputs in any order
    private DragonForgeRecipe findRecipe() {
        if (level == null) return null;

        RecipeManager recipeManager = level.getRecipeManager();
        for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
            if (holder.value() instanceof DragonForgeRecipe recipe) {
                if (canSatisfyRecipe(recipe)) return recipe;
            }
        }
        return null;
    }

    private void craftItem() {
        if (currentRecipe == null) return;

        // On completion - consume everything at once
        if (progress >= currentRecipe.getProcessingTime() - 1) {
            // Consume blaze powder
            inventory.getStackInSlot(0).shrink(1);

            // Consume water all at once
            waterStored -= currentRecipe.getWaterCost();
            if (waterStored < 0) waterStored = 0;

            // Consume energy all at once
            energyStorage.extractEnergyInternal(currentRecipe.getEnergyCost(), false);

            // Consume input items
            for (DragonForgeRecipe.CountedIngredient ci : currentRecipe.getInputs()) {
                for (int i = 3; i <= 5; i++) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (ci.ingredient().test(stack) && stack.getCount() >= ci.count()) {
                        stack.shrink(ci.count());
                        break;
                    }
                }
            }

            // Add result to output slot
            ItemStack output = inventory.getStackInSlot(6);
            ItemStack result = currentRecipe.getOutput().copy();
            if (output.isEmpty()) {
                inventory.setStackInSlot(6, result);
            } else {
                output.grow(result.getCount());
            }

            currentRecipe = null;
        }
    }

    private void updateBlockState() {
        if (level != null && !level.isClientSide) {
            ItemStack head = inventory.getStackInSlot(2);
            boolean hasHead = !head.isEmpty();
            BlockState currentState = getBlockState();
            if (currentState.getValue(DragonForgeBlock.HAS_HEAD) != hasHead) {
                level.setBlock(worldPosition, currentState.setValue(DragonForgeBlock.HAS_HEAD, hasHead), 3);
            }
        }
    }

    public ItemStackHandler getInventory() { return inventory; }
    public int getWaterStored() { return waterStored; }
    public int getMaxWater() { return MAX_WATER; }

    public void drops() {
        if (level != null) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, worldPosition.getX(),
                            worldPosition.getY(), worldPosition.getZ(), stack);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("Energy", energyStorage.getEnergyStored());
        tag.putInt("Water", waterStored);
        tag.putInt("Progress", progress);
        tag.putBoolean("Crafting", isCrafting);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        energyStorage.setEnergy(tag.getInt("Energy"));
        waterStored = tag.getInt("Water");
        progress = tag.getInt("Progress");
        isCrafting = tag.getBoolean("Crafting");
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.artificial_solaris.dragon_forge");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new DragonForgeMenu(windowId, playerInventory, this, data);
    }

    private static class CustomEnergyStorage extends EnergyStorage {
        public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
            super(capacity, maxReceive, maxExtract);
        }

        public void setEnergy(int energy) {
            this.energy = energy;
        }

        // Internal extraction ignores maxExtract limit
        public int extractEnergyInternal(int amount, boolean simulate) {
            int extracted = Math.min(this.energy, amount);
            if (!simulate) {
                this.energy -= extracted;
            }
            return extracted;
        }
    }
}