package dev.Herrschergeist.artificial_solaris.block.entity;

import dev.Herrschergeist.artificial_solaris.block.custom.SolarPanelBlock;
import dev.Herrschergeist.artificial_solaris.block.menu.SolarPanelMenu;
import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class SolarPanelBlockEntity extends BlockEntity implements MenuProvider {

    private final CustomEnergyStorage energyStorage;
    private int tickCounter = 0;
    private boolean artificialSunlight = false;
    private int artificialSunlightTimer = 0;
    private boolean hasMultiblockBoost = false;
    private int currentGeneration = 0;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> energyStorage.getEnergyStored();
                case 1 -> energyStorage.getMaxEnergyStored();
                case 2 -> currentGeneration;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 2) {
                currentGeneration = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOLAR_PANEL.get(), pos, state);
        int capacity = 100000;
        int maxTransfer = 100000000;
        if (state.getBlock() instanceof SolarPanelBlock solarBlock) {
            capacity = solarBlock.getCapacity();
        }
        this.energyStorage = new CustomEnergyStorage(capacity, 0, maxTransfer);
    }

    public void setArtificialSunlight(boolean active) {
        this.artificialSunlight = active;
        this.artificialSunlightTimer = 40;
    }

    public void setMultiblockBoost(boolean boost) {
        this.hasMultiblockBoost = boost;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SolarPanelBlockEntity blockEntity) {
        if (level.isClientSide) return;
        blockEntity.tickCounter++;

        // Energy Generation
        if (blockEntity.canGenerateEnergy(level, pos)) {
            int energyToGenerate = blockEntity.getEnergyGenerationRate(state);
            if (energyToGenerate > 0) {
                blockEntity.energyStorage.addEnergy(energyToGenerate);
                blockEntity.currentGeneration = energyToGenerate;
            } else {
                blockEntity.currentGeneration = 0;
            }
        } else {
            blockEntity.currentGeneration = 0;
        }

        // Giving Energy down every 20 ticks
        if (blockEntity.tickCounter % 20 == 0) {
            blockEntity.pushEnergyToBottom(level, pos);
        }

        // Save every 40 ticks
        if (blockEntity.tickCounter % 40 == 0) {
            blockEntity.setChanged();
        }
    }

    private boolean canGenerateEnergy(Level level, BlockPos pos) {
        // Decrease artificial sunlight timer
        if (artificialSunlightTimer > 0) {
            artificialSunlightTimer--;
        } else {
            artificialSunlight = false;
            hasMultiblockBoost = false;
        }

        // Check artificial sunlight first
        if (artificialSunlight) {
            return true;
        }

        // Check natural sunlight
        return level.isDay()
                && level.canSeeSky(pos.above())
                && !level.isRaining()
                && !level.isThundering();
    }

    private int getEnergyGenerationRate(BlockState state) {
        if (state.getBlock() instanceof SolarPanelBlock solarBlock) {
            int baseEnergy = solarBlock.getEnergyPerTick();

            if (hasMultiblockBoost) {
                baseEnergy = (int) (baseEnergy * 1.2f);
            }

            return baseEnergy;
        }
        return 0;
    }

    private void pushEnergyToBottom(Level level, BlockPos pos) {
        IEnergyStorage targetStorage = level.getCapability(
                Capabilities.EnergyStorage.BLOCK,
                pos.below(),
                Direction.UP
        );

        if (targetStorage != null && targetStorage.canReceive()) {
            int energyToSend = Math.min(
                    energyStorage.getEnergyStored(),
                    energyStorage.getMaxExtract()
            );

            if (energyToSend > 0) {
                int accepted = targetStorage.receiveEnergy(energyToSend, false);
                energyStorage.extractEnergy(accepted, false);
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Energy", energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.setEnergy(tag.getInt("Energy"));
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public ContainerData getDataAccess() {
        return dataAccess;
    }

    @Override
    public Component getDisplayName() {
        // Use block's translation key for unique name per tier
        return Component.translatable("container." + getBlockState().getBlock()
                .getDescriptionId().replace("block.", ""));
    }
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SolarPanelMenu(containerId, playerInventory, this, this.dataAccess);
    }

    private static class CustomEnergyStorage extends EnergyStorage {
        public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
            super(capacity, maxReceive, maxExtract);
        }

        public void setEnergy(int energy) {
            this.energy = energy;
        }

        public int getMaxExtract() {
            return maxExtract;
        }

        public int addEnergy(int amount) {
            int energyReceived = Math.min(capacity - energy, amount);
            energy += energyReceived;
            return energyReceived;
        }
    }
}
