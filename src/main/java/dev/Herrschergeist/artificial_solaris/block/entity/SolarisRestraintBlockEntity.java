package dev.Herrschergeist.artificial_solaris.block.entity;

import dev.Herrschergeist.artificial_solaris.block.menu.SolarisRestraintMenu;
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
import org.jetbrains.annotations.Nullable;

public class SolarisRestraintBlockEntity extends BlockEntity implements MenuProvider {

    private static final int CAPACITY = 10000;
    private static final int MAX_TRANSFER = 1000;

    private final CustomEnergyStorage energyStorage;
    private int tickCounter = 0;
    private boolean isPartOfMultiblock = false;
    private int energyGenerationRate = 0;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> energyStorage.getEnergyStored();
                case 1 -> energyStorage.getMaxEnergyStored();
                case 2 -> energyGenerationRate;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {}

        @Override
        public int getCount() { return 3; }
    };

    public SolarisRestraintBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SOLARIS_RESTRAINT.get(), pos, blockState);
        this.energyStorage = new CustomEnergyStorage(CAPACITY, 0, MAX_TRANSFER);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SolarisRestraintBlockEntity blockEntity) {
        if (level.isClientSide) return;

        blockEntity.tickCounter++;

        // Check multiblock status every second
        if (blockEntity.tickCounter % 20 == 0) {
            BlockEntity starEntity = blockEntity.findNearbyStarCore(level, pos);

            if (starEntity != null && blockEntity.isValidMultiblock(level, pos, starEntity)) {
                blockEntity.isPartOfMultiblock = true;
                blockEntity.energyGenerationRate = blockEntity.getEnergyRateFromStar(starEntity);
            } else {
                blockEntity.isPartOfMultiblock = false;
                blockEntity.energyGenerationRate = 0;
            }
        }

        // Generate energy only if part of multiblock
        if (blockEntity.isPartOfMultiblock && blockEntity.energyGenerationRate > 0) {
            blockEntity.energyStorage.addEnergy(blockEntity.energyGenerationRate);
        }

        // Push energy down every 20 ticks
        if (blockEntity.tickCounter % 20 == 0) {
            blockEntity.pushEnergyToBottom(level, pos);
        }

        // Save every 40 ticks
        if (blockEntity.tickCounter % 40 == 0) {
            blockEntity.setChanged();
        }
    }

    private BlockEntity findNearbyStarCore(Level level, BlockPos pos) {
        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -3; dy <= 3; dy++) {
                for (int dz = -3; dz <= 3; dz++) {
                    BlockPos checkPos = pos.offset(dx, dy, dz);
                    BlockEntity be = level.getBlockEntity(checkPos);
                    if (be instanceof RedDwarfBlockEntity) {
                        return be;
                    }
                    // TODO: Add checks for other star types here
                }
            }
        }
        return null;
    }

    private boolean isValidMultiblock(Level level, BlockPos pos, BlockEntity starEntity) {
        if (starEntity instanceof RedDwarfBlockEntity redDwarf) {
            return redDwarf.isPartOfMultiblock();
        }
        // TODO: Add checks for other star types
        return false;
    }

    private int getEnergyRateFromStar(BlockEntity starEntity) {
        if (starEntity instanceof RedDwarfBlockEntity redDwarf) {
            return redDwarf.getRestraintEnergyGeneration();
        }
        // TODO: Future star types
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

    public IEnergyStorage getEnergyStorage() { return energyStorage; }
    public int getEnergyGenerationRate() { return energyGenerationRate; }
    public ContainerData getDataAccess() { return dataAccess; }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.artificial_solaris.solaris_restraint");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new SolarisRestraintMenu(containerId, playerInventory, this, dataAccess);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Energy", energyStorage.getEnergyStored());
        tag.putInt("GenerationRate", energyGenerationRate);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.setEnergy(tag.getInt("Energy"));
        energyGenerationRate = tag.getInt("GenerationRate");
    }

    private static class CustomEnergyStorage extends EnergyStorage {
        public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
            super(capacity, maxReceive, maxExtract);
        }

        public void setEnergy(int energy) { this.energy = energy; }
        public int getMaxExtract() { return maxExtract; }

        public int addEnergy(int amount) {
            int energyReceived = Math.min(capacity - energy, amount);
            energy += energyReceived;
            return energyReceived;
        }
    }
}