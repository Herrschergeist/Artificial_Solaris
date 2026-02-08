package dev.Herrschergeist.artificial_solaris.block.custom;

import dev.Herrschergeist.artificial_solaris.block.entity.SolarPanelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ProtostarBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(4, 4, 4, 12, 12, 12);
    private static final int RADIUS = 7; // 7 blocks radius

    public ProtostarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Apply artificial sunlight to solar panels in range
        applyArtificialSunlight(level, pos);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide) {
            // Schedule regular ticks
            level.scheduleTick(pos, this, 20); // Every second
        }
    }

    private void applyArtificialSunlight(ServerLevel level, BlockPos protostarPos) {
        // Search for solar panels below in 7x7 horizontal area
        BlockPos.betweenClosed(
                protostarPos.offset(-RADIUS, -RADIUS, -RADIUS),
                protostarPos.offset(RADIUS, 0, RADIUS)
        ).forEach(pos -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SolarPanelBlockEntity solarPanel) {
                // Mark this panel as receiving artificial sunlight
                solarPanel.setArtificialSunlight(true);
            }
        });

        // Schedule next tick
        level.scheduleTick(protostarPos, this, 20);
    }
}