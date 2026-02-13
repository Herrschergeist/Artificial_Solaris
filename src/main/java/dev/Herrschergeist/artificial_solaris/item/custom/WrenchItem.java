package dev.Herrschergeist.artificial_solaris.item.custom;

import dev.Herrschergeist.artificial_solaris.util.WrenchUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WrenchItem extends Item {

    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Player player = ctx.getPlayer();
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();

        if (player == null) return InteractionResult.PASS;

        // Only act on shift+right-click
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        BlockState state = level.getBlockState(pos);

        boolean success = WrenchUtil.tryWrench(level, pos, state, player);

        if (success) {
            // Play a satisfying click sound
            level.playSound(
                    player,
                    pos,
                    SoundEvents.ANVIL_PLACE,
                    SoundSource.BLOCKS,
                    0.3f,
                    1.8f
            );
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }


}