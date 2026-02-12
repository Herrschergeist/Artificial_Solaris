package dev.Herrschergeist.artificial_solaris.block.custom;

import dev.Herrschergeist.artificial_solaris.block.entity.RedDwarfBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RedDwarfBlock extends Block implements EntityBlock {

    public RedDwarfBlock(Properties properties) {
        super(properties);
    }

    // Creates the BlockEntity for this block
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RedDwarfBlockEntity(pos, state);
    }

    // Returns the ticker for this block entity (handles the tick logic)
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Only tick on server side to avoid double-processing
        if (level.isClientSide()) {
            return null;
        }

        // Return the ticker method from RedDwarfBlockEntity
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof RedDwarfBlockEntity redDwarf) {
                RedDwarfBlockEntity.tick(lvl, pos, st, redDwarf);
            }
        };
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        // Clean up: if Red Dwarf was part of multiblock, notify nearby solar panels
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RedDwarfBlockEntity redDwarf) {
                // Reset any solar panels that were receiving boost
                // (they will naturally lose the boost on next tick)
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}

