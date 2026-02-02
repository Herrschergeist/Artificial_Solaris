package dev.herrschergeist.artificialsolaris;

import dev.herrschergeist.artificialsolaris.block.ModBlocks;
import dev.herrschergeist.artificialsolaris.item.ModCreativeModeTabs;
import dev.herrschergeist.artificialsolaris.item.ModItems;
import dev.herrschergeist.artificialsolaris.registry.ModBlockEntities;
import net.minecraft.core.Direction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

import static net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion.MOD_ID;


@Mod(artificialsolaris.MOD_ID)
public class artificialsolaris {
    public static final String MOD_ID = "artificialsolaris";
    public static final Logger LOGGER = LogUtils.getLogger();

    public artificialsolaris(IEventBus modEventBus) {
        LOGGER.info("Artificial Solaris mod initializing...");

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        modEventBus.addListener(this::registerCapabilities);

        LOGGER.info("Artificial Solaris mod initialized successfully!");
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        LOGGER.info("Registering Solar Panel energy capabilities...");

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.SOLAR_PANEL.get(),
                (blockEntity, direction) -> {
                    if (direction == Direction.DOWN || direction == null) {
                        return blockEntity.getEnergyStorage();
                    }
                    return null;
                }
        );

        LOGGER.info("Solar Panel capabilities registered!");
    }


    private void commonSetup(final FMLCommonSetupEvent event) {

    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}