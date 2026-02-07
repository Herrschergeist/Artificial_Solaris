package dev.Herrschergeist.artificial_solaris.event;

import dev.Herrschergeist.artificial_solaris.block.entity.DragonForgeBlockEntity;
import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static dev.Herrschergeist.artificial_solaris.artificial_solaris.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class CapabilityHandler {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Register fluid capability for Dragon Forge
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.DRAGON_FORGE.get(),
                (blockEntity, side) -> {
                    if (blockEntity instanceof DragonForgeBlockEntity dragonForge) {
                        return dragonForge.getFluidHandler(side);
                    }
                    return null;
                }
        );

        // Register energy capability for Dragon Forge
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.DRAGON_FORGE.get(),
                (blockEntity, side) -> {
                    if (blockEntity instanceof DragonForgeBlockEntity dragonForge) {
                        return dragonForge.getEnergyStorage(side);
                    }
                    return null;
                }
        );
    }
}