package dev.Herrschergeist.artificial_solaris.block;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(artificial_solaris.MOD_ID);

    public static final DeferredBlock<Block> COPPER_SOLAR_PANEL = BLOCKS.register(
            "copper_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(2.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.COPPER),
                    40,
                    10000
            )
    );

    public static final DeferredBlock<Block> IRON_SOLAR_PANEL = BLOCKS.register(
            "iron_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(3.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL),
                    120,
                    40000
            )
    );

    public static final DeferredBlock<Block> GOLD_SOLAR_PANEL = BLOCKS.register(
            "gold_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(3.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL),
                    480,
                    100000
            )
    );

    public static final DeferredBlock<Block> DIAMOND_SOLAR_PANEL = BLOCKS.register(
            "diamond_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(4.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL),
                    1500,
                    500000
            )
    );

    public static final DeferredBlock<Block> NETHERITE_SOLAR_PANEL = BLOCKS.register(
            "netherite_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(5.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK),
                    5000,
                    1000000
            )
    );

    public static final DeferredBlock<Block> WITHERING_SOLAR_PANEL = BLOCKS.register(
            "withering_solar_panel",
            () -> new SolarPanelBlock(
                    BlockBehaviour.Properties.of()
                            .strength(5.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK),
                    50000,
                    10000000
            )
    );

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
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