package dev.herrschergeist.artificialsolaris.block.entity;

import dev.herrschergeist.artificialsolaris.artificialsolaris;
import dev.herrschergeist.artificialsolaris.block.SolarPanelBlock;
import dev.herrschergeist.artificialsolaris.registry.ModBlockEntities;
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

public class SolarPanelBlockEntity extends BlockEntity {
    private final CustomEnergyStorage energyStorage;
    private int tickCounter = 0;
    private boolean hasLoggedFirstTick = false;

    public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOLAR_PANEL.get(), pos, state);

        int capacity = 10000;
        int maxTransfer = 100;

        if (state.getBlock() instanceof SolarPanelBlock solarBlock) {
            capacity = solarBlock.getCapacity();
            maxTransfer = solarBlock.getEnergyPerTick() * 2;
        }

        // ИСПРАВЛЕНО: maxReceive должен быть maxTransfer, а не 0!
        this.energyStorage = new CustomEnergyStorage(capacity, maxTransfer, maxTransfer);
        artificialsolaris.LOGGER.info("SolarPanelBlockEntity created at {} with capacity {}", pos, capacity);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SolarPanelBlockEntity blockEntity) {
        if (level == null || level.isClientSide) return;

        blockEntity.tickCounter++;

        if (!blockEntity.hasLoggedFirstTick) {
            artificialsolaris.LOGGER.info("Solar Panel at {} started ticking!", pos);
            blockEntity.hasLoggedFirstTick = true;
        }

        // Диагностика каждые 100 тиков
        if (blockEntity.tickCounter % 100 == 0) {
            artificialsolaris.LOGGER.info("Solar Panel at {}: tick {}, energy: {}/{}",
                    pos, blockEntity.tickCounter,
                    blockEntity.energyStorage.getEnergyStored(),
                    blockEntity.energyStorage.getMaxEnergyStored());
        }

        // Генерация энергии: день + небо = полная энергия
        if (blockEntity.canGenerateEnergy(level, pos)) {
            int energyToGenerate = blockEntity.getEnergyGenerationRate(state);
            if (energyToGenerate > 0) {
                int received = blockEntity.energyStorage.receiveEnergy(energyToGenerate, false);
                if (blockEntity.tickCounter % 100 == 0 && received > 0) {
                    artificialsolaris.LOGGER.info("Generated {} FE", received);
                }
            }
        }

        // Отдача энергии вниз каждые 5 тиков
        if (blockEntity.tickCounter % 5 == 0) {
            blockEntity.pushEnergyToBottom(level, pos);
        }

        if (blockEntity.tickCounter % 20 == 0) {
            blockEntity.setChanged();
        }
    }

    private boolean canGenerateEnergy(Level level, BlockPos pos) {
        boolean isDay = level.isDay();
        boolean canSeeSky = level.canSeeSky(pos.above());
        boolean isRaining = level.isRaining();
        boolean isThundering = level.isThundering();

        boolean canGenerate = isDay && canSeeSky && !isRaining && !isThundering;

        // Логируем каждые 100 тиков
        if (tickCounter % 100 == 0) {
            artificialsolaris.LOGGER.info("Can generate check: isDay={}, canSeeSky={}, rain={}, thunder={}, result={}",
                    isDay, canSeeSky, isRaining, isThundering, canGenerate);
        }

        return canGenerate;
    }

    private int getEnergyGenerationRate(BlockState state) {
        // Просто возвращаем полную мощность, если условия подходящие
        if (state.getBlock() instanceof SolarPanelBlock solarBlock) {
            return solarBlock.getEnergyPerTick();
        }
        return 0;
    }

    private void pushEnergyToBottom(Level level, BlockPos pos) {
        BlockPos targetPos = pos.below();

        IEnergyStorage targetStorage = level.getCapability(
                Capabilities.EnergyStorage.BLOCK,
                targetPos,
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

                if (accepted > 0 && tickCounter % 20 == 0) {
                    artificialsolaris.LOGGER.info("Transferred {} FE to block below", accepted);
                }
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
    }
}
