package dev.herrschergeist.artificialsolaris.item.custom;

import dev.herrschergeist.artificialsolaris.registry.ModTags;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class WrenchItem extends Item {

    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.isShiftKeyDown()) return InteractionResult.PASS;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (level.isClientSide || !state.is(ModTags.WRENCH_PICKUP)) {
            return InteractionResult.PASS;
        }

        boolean removed = level.destroyBlock(pos, false);
        if (!removed) return InteractionResult.FAIL;

        ItemStack drop = state.getBlock().asItem().getDefaultInstance();
        if (!drop.isEmpty()) {
            if (!player.getInventory().add(drop)) {
                player.drop(drop, false);
            }
        }

        return InteractionResult.SUCCESS;
    }
}