package dev.Herrschergeist.artificial_solaris.registry;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import dev.Herrschergeist.artificial_solaris.block.entity.PhotonIrradiatorBlockEntity;
import dev.Herrschergeist.artificial_solaris.block.entity.SolarPanelBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, artificial_solaris.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarPanelBlockEntity>> SOLAR_PANEL =
            BLOCK_ENTITIES.register("solar_panel", () ->
                    BlockEntityType.Builder.of(
                            SolarPanelBlockEntity::new,
                            ModBlocks.COPPER_SOLAR_PANEL.get(),
                            ModBlocks.IRON_SOLAR_PANEL.get(),
                            ModBlocks.GOLD_SOLAR_PANEL.get(),
                            ModBlocks.DIAMOND_SOLAR_PANEL.get(),
                            ModBlocks.NETHERITE_SOLAR_PANEL.get(),
                            ModBlocks.WITHERING_SOLAR_PANEL.get()
                    ).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PhotonIrradiatorBlockEntity>> PHOTON_IRRADIATOR =
            BLOCK_ENTITIES.register("photon_irradiator", () ->
                    BlockEntityType.Builder.of(
                            PhotonIrradiatorBlockEntity::new,
                            ModBlocks.COPPER_PHOTON_IRRADIATOR.get(),
                            ModBlocks.IRON_PHOTON_IRRADIATOR.get(),
                            ModBlocks.GOLD_PHOTON_IRRADIATOR.get(),
                            ModBlocks.DIAMOND_PHOTON_IRRADIATOR.get(),
                            ModBlocks.NETHERITE_PHOTON_IRRADIATOR.get(),
                            ModBlocks.WITHERING_PHOTON_IRRADIATOR.get()
                    ).build(null)
            );
}
