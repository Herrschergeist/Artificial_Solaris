package dev.Herrschergeist.artificial_solaris.block.custom;

import dev.Herrschergeist.artificial_solaris.api.IWrenchable;
import dev.Herrschergeist.artificial_solaris.block.entity.PhotonIrradiatorBlockEntity;
import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
import dev.Herrschergeist.artificial_solaris.util.WrenchUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PhotonIrradiatorBlock extends Block implements EntityBlock, IWrenchable {

    private final int craftSlots;
    private final int capacity;
    private final int maxIO;
    private final float speedMultiplier;

    public PhotonIrradiatorBlock(Properties properties, int craftSlots, int capacity, int maxIO, float speedMultiplier) {
        super(properties);
        this.craftSlots = craftSlots;
        this.capacity = capacity;
        this.maxIO = maxIO;
        this.speedMultiplier = speedMultiplier;
    }

    public int getCraftSlots() { return craftSlots; }
    public int getCapacity() { return capacity; }
    public int getMaxIO() { return maxIO; }
    public float getSpeedMultiplier() { return speedMultiplier; }

    @Override
    public boolean onWrench(BlockState state, Level level, BlockPos pos, Player player) {
        return WrenchUtil.pickUpBlock(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PhotonIrradiatorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        if (type == ModBlockEntities.PHOTON_IRRADIATOR.get()) {
            return (lvl, pos, st, be) -> {
                if (be instanceof PhotonIrradiatorBlockEntity irradiator) {
                    PhotonIrradiatorBlockEntity.tick(lvl, pos, st, irradiator);
                }
            };
        }
        return null;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof PhotonIrradiatorBlockEntity irradiator) {
                serverPlayer.openMenu(irradiator);
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
}