package dev.Herrschergeist.artificial_solaris.block.entity;

import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class SolarisRestraintBlockEntity extends BlockEntity {

    private static final int CAPACITY = 10000;
    private static final int MAX_TRANSFER = 1000;

    private final CustomEnergyStorage energyStorage;
    private int tickCounter = 0;
    private boolean isPartOfMultiblock = false;
    private int energyGenerationRate = 0; // Dynamic based on star type

    public SolarisRestraintBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SOLARIS_RESTRAINT.get(), pos, blockState);
        this.energyStorage = new CustomEnergyStorage(CAPACITY, 0, MAX_TRANSFER);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SolarisRestraintBlockEntity blockEntity) {
        if (level.isClientSide) return;

        blockEntity.tickCounter++;

        // Check multiblock status and update energy generation rate every second
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
        // Search for star cores within 3 blocks radius
        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -3; dy <= 3; dy++) {
                for (int dz = -3; dz <= 3; dz++) {
                    BlockPos checkPos = pos.offset(dx, dy, dz);
                    BlockEntity be = level.getBlockEntity(checkPos);

                    // Check for Red Dwarf (and future stars)
                    if (be instanceof RedDwarfBlockEntity) {
                        return be;
                    }
                    // TODO: Add checks for other star types here:
                    // if (be instanceof YellowDwarfBlockEntity) return be;
                    // if (be instanceof WhiteStarBlockEntity) return be;
                    // if (be instanceof BlueGiantBlockEntity) return be;
                }
            }
        }
        return null;
    }

    private boolean isValidMultiblock(Level level, BlockPos pos, BlockEntity starEntity) {
        // Verify the star considers itself part of a multiblock
        if (starEntity instanceof RedDwarfBlockEntity redDwarf) {
            return redDwarf.isPartOfMultiblock();
        }

        // TODO: Add checks for other star types
        // if (starEntity instanceof YellowDwarfBlockEntity yellowDwarf) {
        //     return yellowDwarf.isPartOfMultiblock();
        // }

        return false;
    }

    private int getEnergyRateFromStar(BlockEntity starEntity) {
        // Get energy generation rate based on star type
        if (starEntity instanceof RedDwarfBlockEntity redDwarf) {
            return redDwarf.getRestraintEnergyGeneration(); // 20 FE/tick
        }

        // TODO: Future star types
        // if (starEntity instanceof YellowDwarfBlockEntity yellowDwarf) {
        //     return yellowDwarf.getRestraintEnergyGeneration(); // e.g., 50 FE/tick
        // }
        // if (starEntity instanceof WhiteStarBlockEntity whiteStar) {
        //     return whiteStar.getRestraintEnergyGeneration(); // e.g., 100 FE/tick
        // }
        // if (starEntity instanceof BlueGiantBlockEntity blueGiant) {
        //     return blueGiant.getRestraintEnergyGeneration(); // e.g., 200 FE/tick
        // }

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

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public int getEnergyGenerationRate() {
        return energyGenerationRate;
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
