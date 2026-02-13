package dev.Herrschergeist.artificial_solaris.block.custom;

import dev.Herrschergeist.artificial_solaris.api.IWrenchable;
import dev.Herrschergeist.artificial_solaris.block.entity.DragonForgeBlockEntity;
import dev.Herrschergeist.artificial_solaris.util.WrenchUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DragonForgeBlock extends Block implements EntityBlock, IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_HEAD = BooleanProperty.create("has_head");
    private boolean wrenching = false;

    public DragonForgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HAS_HEAD, false));
    }

    @Override
    public boolean onWrench(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide()) return true;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof DragonForgeBlockEntity dragonForge)) return false;

        ItemStack drop = new ItemStack(this.asItem());
        dragonForge.saveToItem(drop, level.registryAccess());

        wrenching = true;
        level.removeBlock(pos, false);
        wrenching = false;

        Block.popResource(level, pos, drop);
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_HEAD);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case NORTH -> Block.box(0, 0, 3, 16, 16, 13);
            case SOUTH -> Block.box(0, 0, 3, 16, 16, 13);
            case WEST -> Block.box(3, 0, 0, 13, 16, 16);
            case EAST -> Block.box(3, 0, 0, 13, 16, 16);
            default -> Block.box(0, 0, 0, 16, 16, 16);
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DragonForgeBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, be) -> {
            if (be instanceof DragonForgeBlockEntity dragonForge) {
                DragonForgeBlockEntity.tick(lvl, pos, st, dragonForge);
            }
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof DragonForgeBlockEntity dragonForge) {
                player.openMenu(dragonForge, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!wrenching) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof DragonForgeBlockEntity dragonForge) {
                    dragonForge.drops();
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}