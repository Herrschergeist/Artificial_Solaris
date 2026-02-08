package dev.Herrschergeist.artificial_solaris.item.custom;

import dev.Herrschergeist.artificial_solaris.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Stellar Cage - A tool for capturing and transporting mobs
 *
 * Tier 1: Captures all mobs except bosses
 * Tier 2: Captures all mobs including bosses
 *
 * This item works with ALL entities (vanilla and modded) without any special handling!
 * The Entity NBT system automatically serializes/deserializes all mob data.
 */
public class StellarCageItem extends Item {

    private final int tier; // 1 or 2

    /**
     * Constructor for Stellar Cage
     * @param tier The tier of the cage (1 = no bosses, 2 = all mobs)
     * @param properties Item properties
     */
    public StellarCageItem(int tier, Properties properties) {
        super(properties.stacksTo(1)); // Cannot stack - each cage can hold only one mob
        this.tier = tier;
    }

    // ========================================
    // MAIN INTERACTION METHODS
    // ========================================

    /**
     * Called when right-clicking on a block
     * This is used to RELEASE the captured mob
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;

        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();
        Level world = context.getLevel();
        ItemStack stack = context.getItemInHand();

        // Try to release the mob
        if (!release(player, pos, facing, world, stack)) {
            return InteractionResult.FAIL;
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Called when right-clicking on a living entity
     * This is used to CAPTURE the mob
     */
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player,
                                                  LivingEntity target, InteractionHand hand) {
        // Try to capture the mob
        if (!capture(stack, player, target, hand)) {
            return InteractionResult.FAIL;
        }

        // Visual feedback
        player.swing(hand);

        return InteractionResult.SUCCESS;
    }

    // ========================================
    // CAPTURE LOGIC
    // ========================================

    /**
     * Captures a living entity and stores it in the item
     *
     * @param stack The Stellar Cage item
     * @param player The player using the item
     * @param target The entity to capture
     * @return true if capture was successful
     */
    public boolean capture(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        // Only run on server side
        if (target.level().isClientSide) return false;

        // ===== VALIDATION CHECKS =====

        // Cannot capture players
        if (target instanceof Player) {
            player.displayClientMessage(
                    Component.translatable("message.artificial_solaris.stellar_cage.cannot_capture_player")
                            .withStyle(ChatFormatting.RED),
                    true
            );
            return false;
        }

        // Cannot capture dead mobs
        if (!target.isAlive()) {
            player.displayClientMessage(
                    Component.translatable("message.artificial_solaris.stellar_cage.cannot_capture_dead")
                            .withStyle(ChatFormatting.RED),
                    true
            );
            return false;
        }

        // Check if cage already contains an entity
        if (containsEntity(stack)) {
            player.displayClientMessage(
                    Component.translatable("message.artificial_solaris.stellar_cage.already_occupied")
                            .withStyle(ChatFormatting.GOLD),
                    true
            );
            return false;
        }

        // Tier 1 cannot capture bosses
        if (tier == 1 && isBoss(target)) {
            player.displayClientMessage(
                    Component.translatable("message.artificial_solaris.stellar_cage.tier1_no_boss")
                            .withStyle(ChatFormatting.RED),
                    true
            );
            player.displayClientMessage(
                    Component.translatable("message.artificial_solaris.stellar_cage.use_tier2")
                            .withStyle(ChatFormatting.GRAY),
                    true
            );
            return false;
        }

        // Check blacklist (optional - can be configured via datapacks)
        if (isBlacklisted(target.getType())) {
            player.displayClientMessage(
                    Component.translatable("message.artificial_solaris.stellar_cage.blacklisted")
                            .withStyle(ChatFormatting.RED),
                    true
            );
            return false;
        }

        // ===== CAPTURE THE MOB =====

        // Create a new NBT compound to store ALL entity data
        CompoundTag nbt = new CompoundTag();

        // Store the entity type ID (e.g., "minecraft:zombie", "twilightforest:lich")
        // This works for vanilla AND modded entities automatically!
        nbt.putString("entity", EntityType.getKey(target.getType()).toString());

        // ===== THE MAGIC LINE =====
        // This method automatically saves ALL entity data to NBT:
        // - Health, armor, effects
        // - Custom names, NBT tags
        // - AI state, breeding cooldowns
        // - Inventory contents (for mobs that have inventories)
        // - EVERYTHING the entity needs to be restored exactly as it was!
        //
        // This works for ANY entity (vanilla or modded) without special handling!
        target.saveWithoutId(nbt);

        // Store the NBT data in the item using DataComponents (Minecraft 1.21+)
        stack.set(ModDataComponents.STELLAR_CAGE_DATA.get(), nbt);
        // Update the item in player's hand to sync with client
        player.setItemInHand(hand, stack);

        // Remove the entity from the world
        target.remove(Entity.RemovalReason.DISCARDED);

        // ===== FEEDBACK =====

        // Play capture sound
        target.level().playSound(null, target.blockPosition(),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.2f);

        // Spawn capture particles (client side only)
        if (target.level().isClientSide) {
            spawnCaptureParticles(target);
        }

        // Success message
        player.displayClientMessage(
                Component.translatable("message.artificial_solaris.stellar_cage.captured", target.getName())
                        .withStyle(ChatFormatting.GREEN),
                true
        );

        return true;
    }

    // ========================================
    // RELEASE LOGIC
    // ========================================

    /**
     * Releases the captured entity back into the world
     *
     * @param player The player using the item
     * @param pos The position of the block that was clicked
     * @param facing The face of the block that was clicked
     * @param world The world
     * @param stack The Stellar Cage item
     * @return true if release was successful
     */
    public boolean release(Player player, BlockPos pos, Direction facing,
                           Level world, ItemStack stack) {
        // Only run on server side
        if (world.isClientSide) return false;

        // Check if cage contains an entity
        if (!containsEntity(stack)) {
            player.displayClientMessage(
                    Component.translatable("message.artificial_solaris.stellar_cage.empty")
                            .withStyle(ChatFormatting.GRAY),
                    true
            );
            return false;
        }

        // ===== LOAD THE ENTITY =====

        // Get the stored NBT data
        CompoundTag nbt = stack.get(ModDataComponents.STELLAR_CAGE_DATA.get());

        if (nbt == null) {
            return false;
        }

        // Get the entity type ID
        String entityId = nbt.getString("entity");

        // Convert string ID to EntityType
        // This works for vanilla and modded entities automatically!
        EntityType<?> type = EntityType.byString(entityId).orElse(null);

        if (type == null) {
            player.displayClientMessage(
                    Component.translatable("message.artificial_solaris.stellar_cage.error_unknown_type")
                            .withStyle(ChatFormatting.RED),
                    true
            );
            return false;
        }

        // Create a new entity instance of the correct type
        Entity entity = type.create(world);

        if (entity == null) {
            player.displayClientMessage(
                    Component.translatable("message.artificial_solaris.stellar_cage.error_create_failed")
                            .withStyle(ChatFormatting.RED),
                    true
            );
            return false;
        }

        // ===== THE MAGIC LINE =====
        // This method automatically restores ALL entity data from NBT:
        // - Health, armor, effects
        // - Custom names, NBT tags
        // - AI state, breeding cooldowns
        // - Inventory contents
        // - EVERYTHING is restored exactly as it was when captured!
        entity.load(nbt);

        // ===== SPAWN THE ENTITY =====

        // Calculate spawn position (on top of the clicked block face)
        BlockPos spawnPos = pos.relative(facing);

        // Set the entity's position and rotation
        entity.absMoveTo(
                spawnPos.getX() + 0.5,  // Center X
                spawnPos.getY(),         // Y position
                spawnPos.getZ() + 0.5,  // Center Z
                0,                       // Yaw rotation
                0                        // Pitch rotation
        );

        // Clear the cage's stored data
        stack.remove(ModDataComponents.STELLAR_CAGE_DATA.get());

        // Add the entity to the world
        world.addFreshEntity(entity);

        // ===== FEEDBACK =====

        // Play release sound
        world.playSound(null, spawnPos, SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0f, 0.8f);

        // Spawn release particles (client side only)
        if (world.isClientSide) {
            spawnReleaseParticles(entity);
        }

        // Success message
        player.displayClientMessage(
                Component.translatable("message.artificial_solaris.stellar_cage.released", entity.getName())
                        .withStyle(ChatFormatting.GREEN),
                true
        );

        return true;
    }

    // ========================================
    // HELPER METHODS
    // ========================================

    /**
     * Checks if the cage contains a captured entity
     */
    public boolean containsEntity(ItemStack stack) {
        return stack.has(ModDataComponents.STELLAR_CAGE_DATA.get());
    }

    /**
     * Gets the entity type ID from the cage
     * @return Entity ID string (e.g., "minecraft:zombie") or empty string if empty
     */
    public String getEntityId(ItemStack stack) {
        if (!containsEntity(stack)) return "";
        CompoundTag nbt = stack.get(ModDataComponents.STELLAR_CAGE_DATA.get());
        if (nbt == null) return "";
        return nbt.getString("entity");
    }

    /**
     * Checks if an entity is a boss
     * Uses the Forge Tags system - automatically includes vanilla and modded bosses!
     */
    public boolean isBoss(LivingEntity entity) {
        return entity.getType().is(Tags.EntityTypes.BOSSES);
    }

    /**
     * Checks if an entity type is blacklisted
     * Can be configured via datapacks by adding entities to the tag:
     * data/artificial_solaris/tags/entity_types/stellar_cage_blacklist.json
     */
    public boolean isBlacklisted(EntityType<?> type) {
        // TODO: Create artificial_solaris tag for blacklisting
        // For now, just return false
        return false;

        // Example implementation:
        // return type.is(ModTags.STELLAR_CAGE_BLACKLIST);
    }

    // ========================================
    // VISUAL EFFECTS
    // ========================================

    /**
     * Spawns particles when capturing an entity
     * Only called on client side
     */
    private void spawnCaptureParticles(LivingEntity entity) {
        Level level = entity.level();

        // Spawn portal particles around the entity
        for (int i = 0; i < 20; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * entity.getBbWidth();
            double offsetY = level.random.nextDouble() * entity.getBbHeight();
            double offsetZ = (level.random.nextDouble() - 0.5) * entity.getBbWidth();

            level.addParticle(
                    net.minecraft.core.particles.ParticleTypes.PORTAL,
                    entity.getX() + offsetX,
                    entity.getY() + offsetY,
                    entity.getZ() + offsetZ,
                    0, 0.1, 0
            );
        }

        // TODO: Add custom particles for artificial_solaris
    }

    /**
     * Spawns particles when releasing an entity
     * Only called on client side
     */
    private void spawnReleaseParticles(Entity entity) {
        Level level = entity.level();

        // Spawn portal particles around the spawn location
        for (int i = 0; i < 20; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 1.0;
            double offsetY = level.random.nextDouble() * 2.0;
            double offsetZ = (level.random.nextDouble() - 0.5) * 1.0;

            level.addParticle(
                    net.minecraft.core.particles.ParticleTypes.PORTAL,
                    entity.getX() + offsetX,
                    entity.getY() + offsetY,
                    entity.getZ() + offsetZ,
                    0, -0.1, 0
            );
        }

        // TODO: Add custom particles for artificial_solaris
    }

    // ========================================
    // TOOLTIP AND DISPLAY
    // ========================================

    /**
     * Adds information to the item's tooltip
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        if (containsEntity(stack)) {
            // Get stored entity data
            CompoundTag nbt = stack.get(ModDataComponents.STELLAR_CAGE_DATA.get());
            if (nbt != null) {
                String entityId = nbt.getString("entity");

                // Display captured entity info
                tooltip.add(Component.translatable("tooltip.artificial_solaris.stellar_cage.captured")
                        .withStyle(ChatFormatting.GOLD)
                        .append(Component.literal(": " + entityId)
                                .withStyle(ChatFormatting.YELLOW)));

                // Display health if available
                if (nbt.contains("Health")) {
                    double health = nbt.getDouble("Health");
                    tooltip.add(Component.translatable("tooltip.artificial_solaris.stellar_cage.health",
                                    String.format("%.1f", health))
                            .withStyle(ChatFormatting.GREEN));
                }

                // Display custom name if entity has one
                if (nbt.contains("CustomName")) {
                    String customName = nbt.getString("CustomName");
                    tooltip.add(Component.translatable("tooltip.artificial_solaris.stellar_cage.name", customName)
                            .withStyle(ChatFormatting.AQUA));
                }
            }

        } else {
            // Empty cage
            tooltip.add(Component.translatable("tooltip.artificial_solaris.stellar_cage.empty")
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.artificial_solaris.stellar_cage.how_to_capture")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }

        // Display tier information
        if (tier == 1) {
            tooltip.add(Component.translatable("tooltip.artificial_solaris.stellar_cage.tier1_limit")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("tooltip.artificial_solaris.stellar_cage.tier2_all")
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    /**
     * Makes the item glint (enchanted effect) when it contains an entity
     */
    @Override
    public boolean isFoil(ItemStack stack) {
        return containsEntity(stack);
    }

}