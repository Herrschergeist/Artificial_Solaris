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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DragonForgeBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler inventory = new ItemStackHandler(5) {
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

    // Fluid handler that only accepts water
    private final IFluidHandler fluidHandler = new IFluidHandler() {
        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return new FluidStack(Fluids.WATER, waterStored);
        }

        @Override
        public int getTankCapacity(int tank) {
            return MAX_WATER;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return stack.getFluid() == Fluids.WATER;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.getFluid() != Fluids.WATER) {
                return 0; // Only accept water
            }

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
            return FluidStack.EMPTY; // Don't allow draining
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY; // Don't allow draining
        }
    };

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
        public void set(int index, int value) {
            // Client sync
        }

        @Override
        public int getCount() {
            return 6;
        }
    };

    public DragonForgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRAGON_FORGE.get(), pos, state);
    }

    // Expose capabilities
    public IFluidHandler getFluidHandler(Direction side) {
        // Only allow fluid input from the water tank side (right side relative to facing)
        Direction facing = getBlockState().getValue(DragonForgeBlock.FACING);
        Direction tankSide = facing.getClockWise(); // Right side

        if (side == tankSide) {
            return fluidHandler;
        }
        return null;
    }

    public IEnergyStorage getEnergyStorage(Direction side) {
        // Allow energy from all sides except water tank side and top
        Direction facing = getBlockState().getValue(DragonForgeBlock.FACING);
        Direction tankSide = facing.getClockWise();

        if (side != Direction.UP && side != tankSide) {
            return energyStorage;
        }
        return null;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DragonForgeBlockEntity be) {
        if (level.isClientSide) return;

        if (be.bucketProcessCooldown > 0) {
            be.bucketProcessCooldown--;
        }

        if (be.bucketProcessCooldown == 0) {
            be.processWaterBucket();
        }

        if (be.canProcess()) {
            if (!be.isCrafting) {
                be.isCrafting = true;  // Начинаем крафт
                be.progress = 0;
            }

            be.progress++;
            be.craftItem();

            if (be.currentRecipe != null && be.progress >= be.currentRecipe.getProcessingTime()) {
                be.progress = 0;
                be.isCrafting = false;  // Закончили крафт
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

                ItemStack emptyBucket = new ItemStack(Items.BUCKET);
                if (bucketStack.isEmpty()) {
                    inventory.setStackInSlot(1, emptyBucket);
                }

                bucketProcessCooldown = 20;
                setChanged();
            }
        }

    private boolean canProcess() {
        ItemStack blazePowder = inventory.getStackInSlot(0);
        ItemStack dragonHead = inventory.getStackInSlot(2);
        ItemStack input = inventory.getStackInSlot(3);
        ItemStack output = inventory.getStackInSlot(4);

        if (blazePowder.isEmpty() || dragonHead.isEmpty() || input.isEmpty()) {
            currentRecipe = null;
            isCrafting = false;
            return false;
        }

        // Find recipe only if not already crafting
        if (!isCrafting && (currentRecipe == null || !currentRecipe.getInput().test(input))) {
            currentRecipe = findRecipe(input);
            if (currentRecipe != null) {
                waterPerTick = currentRecipe.getWaterCost() / currentRecipe.getProcessingTime();
                energyPerTick = currentRecipe.getEnergyCost() / currentRecipe.getProcessingTime();
            }
        }

        if (currentRecipe == null) {
            isCrafting = false;
            return false;
        }

        // Check resources
        if (waterStored < waterPerTick || energyStorage.getEnergyStored() < energyPerTick) {
            return false;
        }

        // Check output space
        ItemStack result = currentRecipe.getOutput();
        if (output.isEmpty()) {
            return true;
        }

        return output.getItem() == result.getItem() &&
                output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private DragonForgeRecipe findRecipe(ItemStack input) {
        if (level == null) return null;

        RecipeManager recipeManager = level.getRecipeManager();

        for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
            if (holder.value() instanceof DragonForgeRecipe recipe) {
                if (recipe.getInput().test(input)) {
                    return recipe;
                }
            }
        }
        return null;
    }

    private void craftItem() {
        if (currentRecipe == null) return;

        // Consume resources PER TICK (except blaze powder)
        waterStored -= waterPerTick;
        energyStorage.extractEnergy(energyPerTick, false);

        // On completion - consume blaze powder ONCE
        if (progress >= currentRecipe.getProcessingTime() - 1) {
            // Consume blaze powder only at the end
            inventory.getStackInSlot(0).shrink(1);

            // Process input -> output
            ItemStack input = inventory.getStackInSlot(3);
            ItemStack output = inventory.getStackInSlot(4);
            ItemStack result = currentRecipe.getOutput().copy();

            if (output.isEmpty()) {
                inventory.setStackInSlot(4, result);
            } else {
                output.grow(result.getCount());
            }

            input.shrink(1);
            currentRecipe = null; // Reset recipe
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

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public int getWaterStored() {
        return waterStored;
    }

    public int getMaxWater() {
        return MAX_WATER;
    }

    public void drops() {
        if (level != null) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
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
    }
}