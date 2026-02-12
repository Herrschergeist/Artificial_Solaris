package dev.Herrschergeist.artificial_solaris.effect;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.registry.ModEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid = artificial_solaris.MOD_ID)
public class StellarBurnAttributeModifier {

    private static final ResourceLocation STELLAR_BURN_SLOWNESS_ID =
            ResourceLocation.fromNamespaceAndPath(artificial_solaris.MOD_ID, "stellar_burn_slowness");

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() == ModEffects.STELLAR_BURN.get()) {
            int amplifier = event.getEffectInstance().getAmplifier();
            double slowness = StellarBurnEffect.getSlownessModifier(amplifier);

            // Add movement speed modifier
            var attribute = event.getEntity().getAttribute(Attributes.MOVEMENT_SPEED);
            if (attribute != null) {
                attribute.removeModifier(STELLAR_BURN_SLOWNESS_ID);
                attribute.addPermanentModifier(new AttributeModifier(
                        STELLAR_BURN_SLOWNESS_ID,
                        slowness,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ));
            }
        }
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        if (event.getEffect() == ModEffects.STELLAR_BURN.get()) {
            // Remove movement speed modifier
            var attribute = event.getEntity().getAttribute(Attributes.MOVEMENT_SPEED);
            if (attribute != null) {
                attribute.removeModifier(STELLAR_BURN_SLOWNESS_ID);
            }
        }
    }
}
