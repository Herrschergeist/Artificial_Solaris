package dev.Herrschergeist.artificial_solaris.generated;

import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import dev.Herrschergeist.artificial_solaris.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import static dev.Herrschergeist.artificial_solaris.artificial_solaris.MOD_ID;

/**
 * Generates item models for all items in the mod
 */
public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // ═══════════════════════════════════════════════════════
        // SOLAR PANELS - use parent block models
        // ═══════════════════════════════════════════════════════
        blockItem(ModBlocks.COPPER_SOLAR_PANEL);
        blockItem(ModBlocks.IRON_SOLAR_PANEL);
        blockItem(ModBlocks.GOLD_SOLAR_PANEL);
        blockItem(ModBlocks.DIAMOND_SOLAR_PANEL);
        blockItem(ModBlocks.NETHERITE_SOLAR_PANEL);
        blockItem(ModBlocks.WITHERING_SOLAR_PANEL);

        // ═══════════════════════════════════════════════════════
        // PHOTON IRRADIATORS - use parent block models
        // ═══════════════════════════════════════════════════════
        blockItem(ModBlocks.COPPER_PHOTON_IRRADIATOR);
        blockItem(ModBlocks.IRON_PHOTON_IRRADIATOR);
        blockItem(ModBlocks.GOLD_PHOTON_IRRADIATOR);
        blockItem(ModBlocks.DIAMOND_PHOTON_IRRADIATOR);
        blockItem(ModBlocks.NETHERITE_PHOTON_IRRADIATOR);
        blockItem(ModBlocks.WITHERING_PHOTON_IRRADIATOR);

        // ═══════════════════════════════════════════════════════
        // EXCITED CHAINS - special handling for chain items
        // Chains need "generated" parent, not the block model
        // ═══════════════════════════════════════════════════════
        chainItem(ModBlocks.EXCITED_COPPER_CHAIN);
        chainItem(ModBlocks.EXCITED_IRON_CHAIN);
        chainItem(ModBlocks.EXCITED_GOLD_CHAIN);
        chainItem(ModBlocks.EXCITED_NETHERITE_CHAIN);

        // ═══════════════════════════════════════════════════════
        // RESOURCE ITEMS - simple items with textures
        // ═══════════════════════════════════════════════════════
        basicItem(ModItems.EXCITED_COPPER_INGOT.get());
        basicItem(ModItems.EXCITED_COPPER_NUGGET.get());
        basicItem(ModItems.EXCITED_IRON_INGOT.get());
        basicItem(ModItems.EXCITED_IRON_NUGGET.get());
        basicItem(ModItems.EXCITED_GOLD_INGOT.get());
        basicItem(ModItems.EXCITED_GOLD_NUGGET.get());
        basicItem(ModItems.EXCITED_NETHERITE_INGOT.get());
        basicItem(ModItems.EXCITED_NETHERITE_NUGGET.get());

        // ═══════════════════════════════════════════════════════
        // GEMS AND SPECIAL ITEMS
        // ═══════════════════════════════════════════════════════
        basicItem(ModItems.SOLARIS_CAUGHT_GEM.get());
        basicItem(ModItems.NOX_CAUGHT_GEM.get());
        basicItem(ModItems.PURE_STAR.get());

        // ═══════════════════════════════════════════════════════
        // STELLAR CAGES
        // ═══════════════════════════════════════════════════════
        basicItem(ModItems.STELLAR_CAGE_TIER1.get());
        basicItem(ModItems.STELLAR_CAGE_TIER2.get());

        // ═══════════════════════════════════════════════════════
        // STARS
        // ═══════════════════════════════════════════════════════
        withExistingParent("protostar", modLoc("block/protostar"));

        // ═══════════════════════════════════════════════════════
        // TOOLS
        // ═══════════════════════════════════════════════════════
        withExistingParent(ModItems.WRENCH.getId().getPath(), mcLoc("item/handheld"))
                .texture("layer0", modLoc("item/" + ModItems.WRENCH.getId().getPath()));
    }

    /**
     * Helper method for block items that use the block's model
     */
    private void blockItem(DeferredBlock<?> block) {
        withExistingParent(block.getId().getPath(),
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/" + block.getId().getPath()));
    }

    /**
     * Helper method for chain items - chains use "generated" parent with texture
     */
    private void chainItem(DeferredBlock<?> block) {
        String name = block.getId().getPath();
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + name));
    }
}