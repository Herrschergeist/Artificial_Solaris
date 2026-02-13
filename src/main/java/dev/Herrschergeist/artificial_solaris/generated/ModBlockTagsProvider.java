package dev.Herrschergeist.artificial_solaris.generated;

import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import dev.Herrschergeist.artificial_solaris.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static dev.Herrschergeist.artificial_solaris.artificial_solaris.MOD_ID;

/**
 * Generates block tags
 * Tags are used for grouping blocks (e.g., mineable with pickaxe)
 */
public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // ═══════════════════════════════════════════════════════
        // MINEABLE WITH PICKAXE - all metal blocks and machines
        // ═══════════════════════════════════════════════════════
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                // Solar Panels
                .add(ModBlocks.COPPER_SOLAR_PANEL.get())
                .add(ModBlocks.IRON_SOLAR_PANEL.get())
                .add(ModBlocks.GOLD_SOLAR_PANEL.get())
                .add(ModBlocks.DIAMOND_SOLAR_PANEL.get())
                .add(ModBlocks.NETHERITE_SOLAR_PANEL.get())
                .add(ModBlocks.WITHERING_SOLAR_PANEL.get())
                // Photon Irradiators
                .add(ModBlocks.COPPER_PHOTON_IRRADIATOR.get())
                .add(ModBlocks.IRON_PHOTON_IRRADIATOR.get())
                .add(ModBlocks.GOLD_PHOTON_IRRADIATOR.get())
                .add(ModBlocks.DIAMOND_PHOTON_IRRADIATOR.get())
                .add(ModBlocks.NETHERITE_PHOTON_IRRADIATOR.get())
                .add(ModBlocks.WITHERING_PHOTON_IRRADIATOR.get())
                // Dragon Forge
                .add(ModBlocks.DRAGON_FORGE.get())
                // Excited Blocks
                .add(ModBlocks.EXCITED_COPPER_BLOCK.get())
                .add(ModBlocks.EXCITED_IRON_BLOCK.get())
                .add(ModBlocks.EXCITED_GOLD_BLOCK.get())
                .add(ModBlocks.EXCITED_NETHERITE_BLOCK.get())
                .add(ModBlocks.LUNARIS_BLOCK.get())
                // Excited Chains
                .add(ModBlocks.EXCITED_COPPER_CHAIN.get())
                .add(ModBlocks.EXCITED_IRON_CHAIN.get())
                .add(ModBlocks.EXCITED_GOLD_CHAIN.get())
                .add(ModBlocks.EXCITED_NETHERITE_CHAIN.get())
                // Stars
                .add(ModBlocks.SOLARIS_RESTRAINT.get())
                .add(ModBlocks.PROTOSTAR.get())
                .add(ModBlocks.RED_DWARF.get());

        // ═══════════════════════════════════════════════════════
        // NEEDS STONE TOOL - copper and iron tier blocks
        // ═══════════════════════════════════════════════════════
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.COPPER_SOLAR_PANEL.get())
                .add(ModBlocks.IRON_SOLAR_PANEL.get())
                .add(ModBlocks.COPPER_PHOTON_IRRADIATOR.get())
                .add(ModBlocks.IRON_PHOTON_IRRADIATOR.get())
                .add(ModBlocks.EXCITED_COPPER_BLOCK.get())
                .add(ModBlocks.EXCITED_COPPER_CHAIN.get())
                .add(ModBlocks.EXCITED_IRON_BLOCK.get())
                .add(ModBlocks.EXCITED_IRON_CHAIN.get())
                .add(ModBlocks.LUNARIS_BLOCK.get());

        // ═══════════════════════════════════════════════════════
        // NEEDS IRON TOOL - gold and diamond tier blocks
        // ═══════════════════════════════════════════════════════
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.GOLD_SOLAR_PANEL.get())
                .add(ModBlocks.DIAMOND_SOLAR_PANEL.get())
                .add(ModBlocks.GOLD_PHOTON_IRRADIATOR.get())
                .add(ModBlocks.DIAMOND_PHOTON_IRRADIATOR.get())
                .add(ModBlocks.DRAGON_FORGE.get())
                .add(ModBlocks.EXCITED_GOLD_BLOCK.get())
                .add(ModBlocks.EXCITED_GOLD_CHAIN.get())
                .add(ModBlocks.PROTOSTAR.get());

        // ═══════════════════════════════════════════════════════
        // NEEDS DIAMOND TOOL - netherite and withering tier blocks
        // ═══════════════════════════════════════════════════════
        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.NETHERITE_SOLAR_PANEL.get())
                .add(ModBlocks.WITHERING_SOLAR_PANEL.get())
                .add(ModBlocks.NETHERITE_PHOTON_IRRADIATOR.get())
                .add(ModBlocks.WITHERING_PHOTON_IRRADIATOR.get())
                .add(ModBlocks.EXCITED_NETHERITE_BLOCK.get())
                .add(ModBlocks.EXCITED_NETHERITE_CHAIN.get())
                .add(ModBlocks.SOLARIS_RESTRAINT.get());

    }
}