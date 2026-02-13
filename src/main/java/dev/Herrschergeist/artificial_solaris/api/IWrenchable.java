package dev.Herrschergeist.artificial_solaris.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IWrenchable {

    boolean onWrench(BlockState state, Level level, BlockPos pos, Player player);
}