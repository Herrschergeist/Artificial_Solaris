package dev.Herrschergeist.artificial_solaris;

import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import dev.Herrschergeist.artificial_solaris.item.ModCreativeModeTabs;
import dev.Herrschergeist.artificial_solaris.item.ModItems;
import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
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
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod(artificial_solaris.MOD_ID)
public class artificial_solaris {
    public static final String MOD_ID = "artificial_solaris";
    public static final Logger LOGGER = LogUtils.getLogger();

    public artificial_solaris(IEventBus modEventBus) {

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_MODE_TAB.register(modEventBus);

        modEventBus.addListener(this::registerCapabilities);

    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {

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