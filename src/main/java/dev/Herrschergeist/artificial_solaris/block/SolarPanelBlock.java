package dev.Herrschergeist.artificial_solaris.block;

import dev.Herrschergeist.artificial_solaris.block.entity.SolarPanelBlockEntity;
import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
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

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof SolarPanelBlockEntity solarPanel) {
            player.openMenu(solarPanel, pos);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SolarPanelBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        }

        if (type == ModBlockEntities.SOLAR_PANEL.get()) {
            return (level1, pos, state1, blockEntity) -> {
                if (blockEntity instanceof SolarPanelBlockEntity solarPanel) {
                    SolarPanelBlockEntity.tick(level1, pos, state1, solarPanel);
                }
            };
        }

        return null;
    }
}