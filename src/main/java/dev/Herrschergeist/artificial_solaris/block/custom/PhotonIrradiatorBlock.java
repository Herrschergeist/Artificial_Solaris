package dev.Herrschergeist.artificial_solaris.block.custom;

import dev.Herrschergeist.artificial_solaris.block.entity.PhotonIrradiatorBlockEntity;
import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
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

public class PhotonIrradiatorBlock extends Block implements EntityBlock {

    private final int craftSlots;
    private final int capacity;
    private final int maxIO;
    private final float speedMultiplier; // NEW: speed multiplier (1.0 = normal, 10.0 = 10x faster)

    public PhotonIrradiatorBlock(Properties properties, int craftSlots, int capacity, int maxIO, float speedMultiplier) {
        super(properties);
        this.craftSlots = craftSlots;
        this.capacity = capacity;
        this.maxIO = maxIO;
        this.speedMultiplier = speedMultiplier;
    }

    // ─── Tier parameter getters ───────────────────────────────
    public int getCraftSlots() {
        return craftSlots;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getMaxIO() {
        return maxIO;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    // ─── BlockEntity ──────────────────────────────────────────
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

    // ─── Open GUI on right-click ──────────────────────────────
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