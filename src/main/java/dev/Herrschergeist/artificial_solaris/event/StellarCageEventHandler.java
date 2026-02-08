package dev.Herrschergeist.artificial_solaris.event;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.item.custom.StellarCageItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Event handler for Stellar Cage interactions
 * Handles shift-clicking on mobs with custom interactions (villagers, horses, etc.)
 */
@EventBusSubscriber(modid = artificial_solaris.MOD_ID)
public class StellarCageEventHandler {

    /**
     * Intercepts entity interactions to allow capturing mobs with shift-click
     * This runs BEFORE the mob's custom interaction (trading, mounting, etc.)
     */
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();

        // Only handle on server side
        if (player.level().isClientSide) return;

        // Check if player is holding a Stellar Cage
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);

        if (!(stack.getItem() instanceof StellarCageItem stellarCage)) {
            return; // Not holding a Stellar Cage
        }

        // Check if target is a living entity
        if (!(event.getTarget() instanceof LivingEntity target)) {
            return; // Not a living entity
        }

        // If player is sneaking (shift), capture the mob
        if (player.isShiftKeyDown()) {
            // Try to capture
            if (stellarCage.capture(stack, player, target, hand)) {
                // Success - cancel the event to prevent normal interaction
                event.setCanceled(true);
                player.swing(hand);
            }
        }
        // If not sneaking, let normal interaction happen (event continues)
    }
}