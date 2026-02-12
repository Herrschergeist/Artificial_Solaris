package dev.Herrschergeist.artificial_solaris.generated;

import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import static dev.Herrschergeist.artificial_solaris.artificial_solaris.MOD_ID;

/**
 * Generates blockstates and block models
 */
public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // ═══════════════════════════════════════════════════════
        // EXCITED BLOCKS - simple cube blocks with all sides same
        // ═══════════════════════════════════════════════════════
        blockWithItem(ModBlocks.EXCITED_COPPER_BLOCK);
        blockWithItem(ModBlocks.EXCITED_IRON_BLOCK);
        blockWithItem(ModBlocks.EXCITED_GOLD_BLOCK);
        blockWithItem(ModBlocks.EXCITED_NETHERITE_BLOCK);
        blockWithItem(ModBlocks.LUNARIS_BLOCK);

        // ═══════════════════════════════════════════════════════
        // EXCITED CHAINS - directional blocks like vanilla chains
        // ═══════════════════════════════════════════════════════
        chainBlock(ModBlocks.EXCITED_COPPER_CHAIN);
        chainBlock(ModBlocks.EXCITED_IRON_CHAIN);
        chainBlock(ModBlocks.EXCITED_GOLD_CHAIN);
        chainBlock(ModBlocks.EXCITED_NETHERITE_CHAIN);

        // ═══════════════════════════════════════════════════════
        // SOLAR PANELS - simple blocks
        // ═══════════════════════════════════════════════════════
        simpleBlock(ModBlocks.COPPER_SOLAR_PANEL.get(),
                models().getExistingFile(modLoc("block/copper_solar_panel")));

        simpleBlock(ModBlocks.IRON_SOLAR_PANEL.get(),
                models().getExistingFile(modLoc("block/iron_solar_panel")));

        simpleBlock(ModBlocks.GOLD_SOLAR_PANEL.get(),
                models().getExistingFile(modLoc("block/gold_solar_panel")));

        simpleBlock(ModBlocks.DIAMOND_SOLAR_PANEL.get(),
                models().getExistingFile(modLoc("block/diamond_solar_panel")));

        simpleBlock(ModBlocks.NETHERITE_SOLAR_PANEL.get(),
                models().getExistingFile(modLoc("block/netherite_solar_panel")));

        simpleBlock(ModBlocks.WITHERING_SOLAR_PANEL.get(),
                models().getExistingFile(modLoc("block/withering_solar_panel")));

        // ═══════════════════════════════════════════════════════
        // PHOTON IRRADIATORS - simple blocks
        // ═══════════════════════════════════════════════════════
        blockWithItem(ModBlocks.COPPER_PHOTON_IRRADIATOR);
        blockWithItem(ModBlocks.IRON_PHOTON_IRRADIATOR);
        blockWithItem(ModBlocks.GOLD_PHOTON_IRRADIATOR);
        blockWithItem(ModBlocks.DIAMOND_PHOTON_IRRADIATOR);
        blockWithItem(ModBlocks.NETHERITE_PHOTON_IRRADIATOR);
        blockWithItem(ModBlocks.WITHERING_PHOTON_IRRADIATOR);


        // ═══════════════════════════════════════════════════════
        // STARS - simple glowing block
        // ═══════════════════════════════════════════════════════
        blockWithItem(ModBlocks.SOLARIS_RESTRAINT);
        simpleBlock(ModBlocks.PROTOSTAR.get(),
                models().getExistingFile(modLoc("block/protostar")));
        blockWithItem(ModBlocks.RED_DWARF);
    }

    /**
     * Helper method for simple blocks (creates both block model and item model)
     */
    private void blockWithItem(DeferredBlock<?> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    /**
     * Helper method for chain blocks (directional axis blocks)
     */
    private void chainBlock(DeferredBlock<ChainBlock> block) {
        String name = block.getId().getPath();
        ModelFile model = models().withExistingParent(name, mcLoc("block/chain"))
                .texture("all", modLoc("block/" + name))
                .texture("particle", modLoc("block/" + name))
                .renderType("cutout");

        getVariantBuilder(block.get())
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(model).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.X)
                .modelForState().modelFile(model).rotationX(90).rotationY(90).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(model).rotationX(90).addModel();
    }
}