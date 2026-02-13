package dev.Herrschergeist.artificial_solaris.util;

import dev.Herrschergeist.artificial_solaris.api.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WrenchUtil {

    /**
     * Called by WrenchItem on shift+right-click.
     *
     * Priority:
     * 1. Block implements IWrenchable -> delegate to block's own logic
     * 2. Block has tag c:wrenchable   -> use our generic pickup logic
     * 3. Neither                      -> do nothing, return false
     */
    public static boolean tryWrench(Level level, BlockPos pos, BlockState state, Player player) {
        Block block = state.getBlock();

        // 1. Our own blocks (and any mod that implements IWrenchable)
        if (block instanceof IWrenchable wrenchable) {
            return wrenchable.onWrench(state, level, pos, player);
        }

        // 2. Other mods that use the common c:wrenchable tag
        if (isTaggedWrenchable(state)) {
            return pickUpBlock(level, pos, state, player);
        }

        return false;
    }

    /**
     * Generic block pickup: saves BlockEntity data, drops item, removes block.
     * Used both by IWrenchable blocks (via their onWrench) and tag-compatible blocks.
     */
    public static boolean pickUpBlock(Level level, BlockPos pos, BlockState state, Player player) {
        if (level.isClientSide()) return true;

        Block block = state.getBlock();
        BlockEntity blockEntity = level.getBlockEntity(pos);

        ItemStack drop = new ItemStack(block.asItem());

        // If the block has a BlockEntity, save its data into the ItemStack.
        // This preserves energy, fluid, inventory etc. via DataComponents.BLOCK_ENTITY_DATA.
        // Works automatically as long as the BlockEntity properly implements saveAdditional().
        if (blockEntity != null) {
            blockEntity.saveToItem(drop, level.registryAccess());
        }

        Block.popResource(level, pos, drop);
        level.removeBlock(pos, false);

        return true;
    }

    /**
     * Checks if the block is tagged as wrenchable by any mod.
     * Tag location: data/c/tags/blocks/wrenchable.json
     */
    private static boolean isTaggedWrenchable(BlockState state) {
        // c:wrenchable is the common tag used across mods (Create, Mekanism, etc.)
        return state.is(TagKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath("c", "wrenchable")
        ));
    }
}