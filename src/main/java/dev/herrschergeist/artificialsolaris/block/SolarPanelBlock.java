package dev.herrschergeist.artificialsolaris.block;

import dev.herrschergeist.artificialsolaris.artificialsolaris;
import dev.herrschergeist.artificialsolaris.block.entity.SolarPanelBlockEntity;
import dev.herrschergeist.artificialsolaris.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SolarPanelBlock extends Block implements EntityBlock {
    private final int energyPerTick;
    private final int capacity;

    public SolarPanelBlock(Properties properties, int energyPerTick, int capacity) {
        super(properties);
        this.energyPerTick = energyPerTick;
        this.capacity = capacity;
    }

    public int getEnergyPerTick() {
        return energyPerTick;
    }

    public int getCapacity() {
        return capacity;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        artificialsolaris.LOGGER.info("Creating Solar Panel BlockEntity at {}", pos);
        return new SolarPanelBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        }

        if (type == ModBlockEntities.SOLAR_PANEL.get()) {
            artificialsolaris.LOGGER.info("getTicker: Creating ticker for Solar Panel");
            return (level1, pos, state1, blockEntity) -> {
                if (blockEntity instanceof SolarPanelBlockEntity solarPanel) {
                    SolarPanelBlockEntity.tick(level1, pos, state1, solarPanel);
                }
            };
        }

        return null;
    }
}
