package dev.Herrschergeist.artificial_solaris.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class StellarBurnEffect extends MobEffect {

    public StellarBurnEffect() {
        super(
                MobEffectCategory.HARMFUL, // Harmful effect
                0xFF6600 // Orange-red color for the effect
        );
    }

    // Apply damage every second (20 ticks)
    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        // Apply every 20 ticks (1 second)
        return tickCount % 20 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            // Damage scales with amplifier level
            // Level 0: 2 HP/sec
            // Level 1: 4 HP/sec
            // Level 2: 6 HP/sec
            // Formula: 2 + (amplifier * 2)
            float damage = 2.0f + (amplifier * 2.0f);
            entity.hurt(entity.level().damageSources().onFire(), damage);
        }
        return true;
    }

    // Custom method to get slowness percentage based on effect level
    public static double getSlownessModifier(int amplifier) {
        // Level 0: 10% slowness (-0.10)
        // Level 1: 20% slowness (-0.20)
        // Level 2: 30% slowness (-0.30)
        // Formula: -0.10 * (amplifier + 1)
        return -0.10 * (amplifier + 1);
    }
}

