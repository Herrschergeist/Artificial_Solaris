package dev.Herrschergeist.artificial_solaris.event;

import dev.Herrschergeist.artificial_solaris.api.IWrenchable;
import dev.Herrschergeist.artificial_solaris.item.custom.WrenchItem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static dev.Herrschergeist.artificial_solaris.artificial_solaris.MOD_ID;

public class WrenchEventHandler {

    private static final TagKey<Item> WRENCHES = ItemTags.create(
            ResourceLocation.fromNamespaceAndPath("c", "tools/wrench")
    );

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        // Server side only
        if (level.isClientSide()) return;

        // Must be shift+click
        if (!player.isShiftKeyDown()) return;

        ItemStack stack = event.getItemStack();

        // Must be holding a wrench (any mod)
        if (!stack.is(WRENCHES)) return;

        // Skip our own wrench — it's handled by WrenchItem.useOn already
        if (stack.getItem() instanceof WrenchItem) return;

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        // Only act on our own IWrenchable blocks
        if (!(state.getBlock() instanceof IWrenchable wrenchable)) return;

        boolean success = wrenchable.onWrench(state, level, pos, player);
        if (success) {
            level.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.3f, 1.8f);
            event.setCanceled(true);
            player.swing(event.getHand());
        }
    }
}