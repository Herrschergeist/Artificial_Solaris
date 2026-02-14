package dev.Herrschergeist.artificial_solaris.block.custom;

import dev.Herrschergeist.artificial_solaris.api.IWrenchable;
import dev.Herrschergeist.artificial_solaris.block.entity.SolarisRestraintBlockEntity;
import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
import dev.Herrschergeist.artificial_solaris.util.WrenchUtil;
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

public class SolarisRestraintBlock extends Block implements EntityBlock, IWrenchable {

    public SolarisRestraintBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onWrench(BlockState state, Level level, BlockPos pos, Player player) {
        return WrenchUtil.pickUpBlock(level, pos, state, player);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level,
                                               BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SolarisRestraintBlockEntity restraint) {
                player.openMenu(restraint, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SolarisRestraintBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }

        return blockEntityType == ModBlockEntities.SOLARIS_RESTRAINT.get()
                ? (BlockEntityTicker<T>) (BlockEntityTicker<SolarisRestraintBlockEntity>) SolarisRestraintBlockEntity::tick
                : null;
    }
}
