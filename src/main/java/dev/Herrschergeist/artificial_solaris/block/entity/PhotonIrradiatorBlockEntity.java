package dev.Herrschergeist.artificial_solaris.block.entity;

import dev.Herrschergeist.artificial_solaris.block.custom.PhotonIrradiatorBlock;
import dev.Herrschergeist.artificial_solaris.block.menu.*;
import dev.Herrschergeist.artificial_solaris.recipe.IrradiationRecipe;
import dev.Herrschergeist.artificial_solaris.recipe.ModRecipes;
import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.MenuProvider;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhotonIrradiatorBlockEntity extends BlockEntity implements MenuProvider {

    // --- Parameters from block ---
    private int craftSlots;
    private int capacity;
    private int maxIO;
    private float speedMultiplier;

    // --- Energy buffer ---
    private CustomEnergyStorage energyStorage;

    // --- Processing slots: input[0..n-1], output[n..2n-1] ---
    private SimpleContainer craftInventory;

    // --- Progress per slot ---
    private int[] progress;       // current progress in ticks
    private int[] totalProgress;  // total time needed in ticks

    // --- Constructor ---
    public PhotonIrradiatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PHOTON_IRRADIATOR.get(), pos, state);
        initFromBlock(state);
    }

    private void initFromBlock(BlockState state) {
        if (state.getBlock() instanceof PhotonIrradiatorBlock block) {
            this.craftSlots = block.getCraftSlots();
            this.capacity   = block.getCapacity();
            this.maxIO      = block.getMaxIO();
            this.speedMultiplier = block.getSpeedMultiplier();
        } else {
            // fallback
            this.craftSlots = 1;
            this.capacity   = 10000;
            this.maxIO      = 1000;
            this.speedMultiplier = 1.0f;
        }
        this.energyStorage   = new CustomEnergyStorage(capacity, maxIO, maxIO);
        this.craftInventory  = new SimpleContainer(craftSlots * 2);
        this.progress        = new int[craftSlots];
        this.totalProgress   = new int[craftSlots];
    }

    // --- Tick ---
    public static void tick(Level level, BlockPos pos, BlockState state, PhotonIrradiatorBlockEntity be) {
        if (level.isClientSide) return;

        for (int i = 0; i < be.craftSlots; i++) {
            be.processSlot(i);
        }
    }

    private void processSlot(int index) {
        int inputIndex  = index;
        int outputIndex = craftSlots + index;

        ItemStack input  = craftInventory.getItem(inputIndex);
        ItemStack output = craftInventory.getItem(outputIndex);

        if (input.isEmpty()) {
            progress[index] = 0;
            totalProgress[index] = 0;
            return;
        }

        // Find recipe
        IrradiationRecipe recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.IRRADIATION_TYPE.get(), new SingleRecipeInput(input), level)
                .map(net.minecraft.world.item.crafting.RecipeHolder::value)
                .orElse(null);

        if (recipe == null) {
            progress[index] = 0;
            totalProgress[index] = 0;
            setChanged();
            return;
        }

        // Check if output slot is compatible
        ItemStack recipeOutput = recipe.getResult();
        if (!output.isEmpty() && !ItemStack.isSameItemSameComponents(output, recipeOutput)) {
            progress[index] = 0;
            totalProgress[index] = 0;
            return;
        }
        if (output.getCount() >= output.getMaxStackSize()) {
            return;
        }

        // Calculate actual processing time based on block speed
        int baseTime = recipe.getProcessingTime();
        int actualTime = Math.max(1, (int)(baseTime / speedMultiplier));
        totalProgress[index] = actualTime;

        // Calculate energy per tick
        int totalEnergy = recipe.getEnergyCost();
        int energyPerTick = Math.max(1, totalEnergy / actualTime);

        // Consume energy and progress
        if (energyStorage.getEnergyStored() >= energyPerTick) {
            energyStorage.extractEnergy(energyPerTick, false);
            progress[index]++;
            setChanged();
        } else {
            // Not enough energy - don't progress
            return;
        }

        // Finished?
        if (progress[index] >= totalProgress[index]) {
            // Take input
            input.shrink(1);
            craftInventory.setItem(inputIndex, input);

            // Put output
            if (output.isEmpty()) {
                craftInventory.setItem(outputIndex, recipeOutput.copy());
            } else {
                output.grow(1);
            }

            progress[index] = 0;
            totalProgress[index] = 0;
            setChanged();
        }
    }

    // --- NBT save / load ---
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putInt("Energy", energyStorage.getEnergyStored());

        // Save inventory slots
        ListTag items = new ListTag();
        for (int i = 0; i < craftInventory.getContainerSize(); i++) {
            CompoundTag slotTag = new CompoundTag();
            slotTag.putByte("Slot", (byte) i);
            ItemStack stack = craftInventory.getItem(i);
            if (!stack.isEmpty()) {
                slotTag.put("item", stack.save(registries));
            }
            items.add(slotTag);
        }
        tag.put("Items", items);

        // Progress
        tag.putIntArray("Progress",      progress);
        tag.putIntArray("TotalProgress", totalProgress);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        // Init from block (in case not yet done)
        initFromBlock(getBlockState());

        energyStorage.setEnergy(tag.getInt("Energy"));

        if (tag.contains("Items", Tag.TAG_LIST)) {
            ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
            for (int i = 0; i < items.size(); i++) {
                CompoundTag slotTag = items.getCompound(i);
                int slot = slotTag.getByte("Slot") & 0xFF;
                if (slot < craftInventory.getContainerSize()) {
                    ItemStack stack = ItemStack.parseOptional(registries, slotTag.getCompound("item"));
                    craftInventory.setItem(slot, stack);
                }
            }
        }

        if (tag.contains("Progress")) {
            int[] saved = tag.getIntArray("Progress");
            System.arraycopy(saved, 0, progress, 0, Math.min(saved.length, progress.length));
        }
        if (tag.contains("TotalProgress")) {
            int[] saved = tag.getIntArray("TotalProgress");
            System.arraycopy(saved, 0, totalProgress, 0, Math.min(saved.length, totalProgress.length));
        }
    }

    // --- MenuProvider ---
    @Override
    public Component getDisplayName() {
        return Component.translatable("container.artificial_solaris.photon_irradiator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return switch (craftSlots) {
            case 1 -> new CopperPhotonIrradiatorMenu(windowId, playerInventory, this);
            case 2 -> new IronPhotonIrradiatorMenu(windowId, playerInventory, this);
            case 3 -> new GoldPhotonIrradiatorMenu(windowId, playerInventory, this);
            case 4 -> new DiamondPhotonIrradiatorMenu(windowId, playerInventory, this);
            case 5 -> new NetheritePhotonIrradiatorMenu(windowId, playerInventory, this);
            case 6 -> new WitheringPhotonIrradiatorMenu(windowId, playerInventory, this);
            default -> new CopperPhotonIrradiatorMenu(windowId, playerInventory, this);
        };
    }

    // --- Getters for Menu / Screen ---
    public SimpleContainer getCraftInventory()  { return craftInventory; }
    public int getCraftSlots()                  { return craftSlots; }
    public int getProgress(int slot)            { return progress[slot]; }
    public int getTotalProgress(int slot)       { return totalProgress[slot]; }
    public IEnergyStorage getEnergyStorage()    { return energyStorage; }

    // --- CustomEnergyStorage ---
    private static class CustomEnergyStorage extends EnergyStorage {
        public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
            super(capacity, maxReceive, maxExtract);
        }
        public void setEnergy(int energy) {
            this.energy = Math.min(energy, capacity);
        }
    }
}
