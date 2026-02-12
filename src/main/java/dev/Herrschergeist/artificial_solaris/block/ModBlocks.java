package dev.Herrschergeist.artificial_solaris.block;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.block.custom.*;
import dev.Herrschergeist.artificial_solaris.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(artificial_solaris.MOD_ID);

    // ─── Deco Blocks ──────────────────────────────
    public static final DeferredBlock<Block> EXCITED_COPPER_BLOCK = registerBlockWithItem(
            "excited_copper_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(2.0f)
                            .lightLevel(state -> 5)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL)
            )
    );

    public static final DeferredBlock<ChainBlock> EXCITED_COPPER_CHAIN = registerBlockWithItem(
            "excited_copper_chain",
            () -> new ChainBlock(
                    BlockBehaviour.Properties.of()
                            .strength(2.0f)
                            .lightLevel(state -> 5)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.CHAIN)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> EXCITED_IRON_BLOCK = registerBlockWithItem(
            "excited_iron_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(4.0f)
                            .lightLevel(state -> 5)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL)
            )
    );

    public static final DeferredBlock<ChainBlock> EXCITED_IRON_CHAIN = registerBlockWithItem(
            "excited_iron_chain",
            () -> new ChainBlock(
                    BlockBehaviour.Properties.of()
                            .strength(3.0f)
                            .lightLevel(state -> 5)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.CHAIN)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> EXCITED_GOLD_BLOCK = registerBlockWithItem(
            "excited_gold_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(2.0f)
                            .lightLevel(state -> 5)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL)
            )
    );

    public static final DeferredBlock<ChainBlock> EXCITED_GOLD_CHAIN = registerBlockWithItem(
            "excited_gold_chain",
            () -> new ChainBlock(
                    BlockBehaviour.Properties.of()
                            .strength(2.0f)
                            .lightLevel(state -> 5)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.CHAIN)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> EXCITED_NETHERITE_BLOCK = registerBlockWithItem(
            "excited_netherite_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(10.0f)
                            .lightLevel(state -> 5)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );

    public static final DeferredBlock<ChainBlock> EXCITED_NETHERITE_CHAIN = registerBlockWithItem(
            "excited_netherite_chain",
            () -> new ChainBlock(
                    BlockBehaviour.Properties.of()
                            .strength(7.0f)
                            .lightLevel(state -> 5)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.CHAIN)
                            .noOcclusion()
            )
    );


    public static final DeferredBlock<Block> LUNARIS_BLOCK = registerBlockWithItem(
            "lunaris_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(2.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.AMETHYST)
            )
    );

    // ─── Solar Panel Blocks ──────────────────────────────
    public static final DeferredBlock<Block> COPPER_SOLAR_PANEL = BLOCKS.register(
            "copper_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(3.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.COPPER),
                    16,
                    10000
            )
    );

    public static final DeferredBlock<Block> IRON_SOLAR_PANEL = BLOCKS.register(
            "iron_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(5.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL),
                    64,
                    100000
            )
    );

    public static final DeferredBlock<Block> GOLD_SOLAR_PANEL = BLOCKS.register(
            "gold_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(3.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL),
                    256,
                    500000
            )
    );

    public static final DeferredBlock<Block> DIAMOND_SOLAR_PANEL = BLOCKS.register(
            "diamond_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(5.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL),
                    1024,
                    1000000
            )
    );

    public static final DeferredBlock<Block> NETHERITE_SOLAR_PANEL = BLOCKS.register(
            "netherite_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(10.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK),
                    4096,
                    10000000
            )
    );

    public static final DeferredBlock<Block> WITHERING_SOLAR_PANEL = BLOCKS.register(
            "withering_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(12.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK),
                    16384,
                    100000000
            )
    );


    // ─── Photon Irradiator Blocks ──────────────────────────────
    public static final DeferredBlock<Block> COPPER_PHOTON_IRRADIATOR = BLOCKS.register(
            "copper_photon_irradiator",
            () -> new PhotonIrradiatorBlock(
                    BlockBehaviour.Properties.of()
                            .strength(2.5f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.COPPER),
                    1,
                    10000,
                    1000,
                    2.0f    // 1x speed
            )
    );

    public static final DeferredBlock<Block> IRON_PHOTON_IRRADIATOR = BLOCKS.register(
            "iron_photon_irradiator",
            () -> new PhotonIrradiatorBlock(
                    BlockBehaviour.Properties.of()
                            .strength(3.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL),
                    2,
                    50000,
                    5000,
                    3.0f    // 2x speed
            )
    );

    public static final DeferredBlock<Block> GOLD_PHOTON_IRRADIATOR = BLOCKS.register(
            "gold_photon_irradiator",
            () -> new PhotonIrradiatorBlock(
                    BlockBehaviour.Properties.of()
                            .strength(3.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL),
                    3,
                    200000,
                    20000,
                    5.0f    // 5x speed
            )
    );

    public static final DeferredBlock<Block> DIAMOND_PHOTON_IRRADIATOR = BLOCKS.register(
            "diamond_photon_irradiator",
            () -> new PhotonIrradiatorBlock(
                    BlockBehaviour.Properties.of()
                            .strength(4.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL),
                    4,
                    500000,
                    50000,
                    10.0f   // 10x speed
            )
    );

    public static final DeferredBlock<Block> NETHERITE_PHOTON_IRRADIATOR = BLOCKS.register(
            "netherite_photon_irradiator",
            () -> new PhotonIrradiatorBlock(
                    BlockBehaviour.Properties.of()
                            .strength(5.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK),
                    5,
                    2000000,
                    200000,
                    20.0f   // 20x speed
            )
    );

    public static final DeferredBlock<Block> WITHERING_PHOTON_IRRADIATOR = BLOCKS.register(
            "withering_photon_irradiator",
            () -> new PhotonIrradiatorBlock(
                    BlockBehaviour.Properties.of()
                            .strength(5.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK),
                    6,
                    10000000,
                    1000000,
                    50.0f   // 50x speed!
            )
    );


    // ─── Dragon Forge Block ──────────────────────────────
    public static final DeferredBlock<Block> DRAGON_FORGE = BLOCKS.register(
            "dragon_forge",
            () -> new DragonForgeBlock(
                    BlockBehaviour.Properties.of()
                            .strength(2.5f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.ANVIL)
                            .noOcclusion()
            )
    );


    // ─── Star Blocks ──────────────────────────────
    public static final DeferredBlock<Block> SOLARIS_RESTRAINT = BLOCKS.register("solaris_restraint",
            () -> new SolarisRestraintBlock(BlockBehaviour.Properties.of()
                    .strength(5.0f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)));


    public static final DeferredBlock<Block> PROTOSTAR = BLOCKS.register(
            "protostar",
            () -> new ProtostarBlock(
                    BlockBehaviour.Properties.of()
                            .strength(1.0f)
                            .lightLevel(state -> 10)
                            .sound(SoundType.GLASS)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> RED_DWARF = BLOCKS.register(
            "red_dwarf",
            () -> new RedDwarfBlock(
                    BlockBehaviour.Properties.of()
                            .strength(1.0f)
                            .lightLevel(state -> 10)
                            .sound(SoundType.GLASS)
                            .noOcclusion()
            )
    );

    // Additional method for automatic registration of BlockItem
    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}