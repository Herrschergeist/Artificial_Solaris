package dev.Herrschergeist.artificial_solaris.event;

import dev.Herrschergeist.artificial_solaris.item.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class LunarisReaperHeadDropEvent {

    private static final double BASE_HEAD_DROP_CHANCE = 0.10;
    private static final double LOOTING_BONUS = 0.05;

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        LivingEntity target = event.getEntity();

        if (!(event.getSource().getEntity() instanceof Player player)) return;

        ItemStack weapon = player.getMainHandItem();
        if (!weapon.is(ModItems.LUNARIS_REAPER.get())) return;

        int lootingLevel = net.minecraft.world.item.enchantment.EnchantmentHelper
                .getItemEnchantmentLevel(
                        player.level().registryAccess()
                                .registryOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                                .getHolderOrThrow(net.minecraft.world.item.enchantment.Enchantments.LOOTING),
                        weapon
                );

        double chance = BASE_HEAD_DROP_CHANCE + (lootingLevel * LOOTING_BONUS);

        if (target.level().random.nextDouble() > chance) return;

        ItemStack head = getHeadForMob(target);
        if (head.isEmpty()) return;

        target.spawnAtLocation(head);
    }

    private ItemStack getHeadForMob(LivingEntity entity) {
        if (entity instanceof Zombie)         return new ItemStack(Items.ZOMBIE_HEAD);
        if (entity instanceof Skeleton)       return new ItemStack(Items.SKELETON_SKULL);
        if (entity instanceof WitherSkeleton) return new ItemStack(Items.WITHER_SKELETON_SKULL);
        if (entity instanceof Creeper)        return new ItemStack(Items.CREEPER_HEAD);
        if (entity instanceof Piglin)         return new ItemStack(Items.PIGLIN_HEAD);
        if (entity instanceof Player p)       return new ItemStack(Items.PLAYER_HEAD);
        return ItemStack.EMPTY;
    }
}