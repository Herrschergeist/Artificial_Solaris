package dev.Herrschergeist.artificial_solaris;

import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import dev.Herrschergeist.artificial_solaris.block.screen.*;
import dev.Herrschergeist.artificial_solaris.event.LunarisReaperHeadDropEvent;
import dev.Herrschergeist.artificial_solaris.event.WrenchEventHandler;
import dev.Herrschergeist.artificial_solaris.item.ModCreativeModeTabs;
import dev.Herrschergeist.artificial_solaris.item.ModItems;
import dev.Herrschergeist.artificial_solaris.recipe.ModRecipes;
import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
import dev.Herrschergeist.artificial_solaris.registry.ModEffects;
import dev.Herrschergeist.artificial_solaris.registry.ModMenuTypes;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;



@Mod(artificial_solaris.MOD_ID)
public class artificial_solaris {
    public static final String MOD_ID = "artificial_solaris";
    public static final Logger LOGGER = LogUtils.getLogger();

    public artificial_solaris(IEventBus modEventBus) {

        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_MODE_TAB.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModRecipes.SERIALIZERS.register(modEventBus);
        ModRecipes.TYPES.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
        NeoForge.EVENT_BUS.register(new LunarisReaperHeadDropEvent());
        NeoForge.EVENT_BUS.register(new WrenchEventHandler());

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
        // Photon Irradiator: accepts energy from all sides
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.PHOTON_IRRADIATOR.get(),
                (blockEntity, direction) -> blockEntity.getEnergyStorage()
        );
        // Solaris Restraint: outputs energy from all sides
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.SOLARIS_RESTRAINT.get(),
                (blockEntity, direction) -> blockEntity.getEnergyStorage()
        );
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents  {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemBlockRenderTypes.setRenderLayer(
                        ModBlocks.EXCITED_COPPER_CHAIN.get(),
                        RenderType.cutout()
                );
                ItemBlockRenderTypes.setRenderLayer(
                        ModBlocks.EXCITED_IRON_CHAIN.get(),
                        RenderType.cutout()
                );
                ItemBlockRenderTypes.setRenderLayer(
                        ModBlocks.EXCITED_GOLD_CHAIN.get(),
                        RenderType.cutout()
                );
                ItemBlockRenderTypes.setRenderLayer(
                        ModBlocks.EXCITED_NETHERITE_CHAIN.get(),
                        RenderType.cutout()
                );

                ItemBlockRenderTypes.setRenderLayer(
                        ModBlocks.DRAGON_FORGE.get(),
                        RenderType.cutout()
                );
            });
        }

            @SubscribeEvent
            public static void registerScreens (RegisterMenuScreensEvent event){
                event.register(ModMenuTypes.DRAGON_FORGE.get(), DragonForgeScreen::new);
                event.register(ModMenuTypes.COPPER_PHOTON_IRRADIATOR.get(), CopperPhotonIrradiatorScreen::new);
                event.register(ModMenuTypes.IRON_PHOTON_IRRADIATOR.get(), IronPhotonIrradiatorScreen::new);
                event.register(ModMenuTypes.GOLD_PHOTON_IRRADIATOR.get(), GoldPhotonIrradiatorScreen::new);
                event.register(ModMenuTypes.DIAMOND_PHOTON_IRRADIATOR.get(), DiamondPhotonIrradiatorScreen::new);
                event.register(ModMenuTypes.NETHERITE_PHOTON_IRRADIATOR.get(), NetheritePhotonIrradiatorScreen::new);
                event.register(ModMenuTypes.WITHERING_PHOTON_IRRADIATOR.get(), WitheringPhotonIrradiatorScreen::new);
            }
        }
    }