package dev.herrschergeist.artificialsolaris.item;

import dev.herrschergeist.artificialsolaris.artificialsolaris;
import dev.herrschergeist.artificialsolaris.block.ModBlocks;
import dev.herrschergeist.artificialsolaris.item.custom.WrenchItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(artificialsolaris.MOD_ID);

    public static final DeferredItem<BlockItem> COPPER_SOLAR_PANEL =
            ITEMS.register("copper_solar_panel",
                    () -> new BlockItem(ModBlocks.COPPER_SOLAR_PANEL.get(),
                            new Item.Properties()));

    public static final DeferredItem<BlockItem> IRON_SOLAR_PANEL =
            ITEMS.register("iron_solar_panel",
                    () -> new BlockItem(ModBlocks.IRON_SOLAR_PANEL.get(),
                            new Item.Properties()));

    public static final DeferredItem<BlockItem> GOLD_SOLAR_PANEL =
            ITEMS.register("gold_solar_panel",
                    () -> new BlockItem(ModBlocks.GOLD_SOLAR_PANEL.get(),
                            new Item.Properties()));

    public static final DeferredItem<BlockItem> DIAMOND_SOLAR_PANEL =
            ITEMS.register("diamond_solar_panel",
                    () -> new BlockItem(ModBlocks.DIAMOND_SOLAR_PANEL.get(),
                            new Item.Properties()));

    public static final DeferredItem<BlockItem> NETHERITE_SOLAR_PANEL =
            ITEMS.register("netherite_solar_panel",
                    () -> new BlockItem(ModBlocks.NETHERITE_SOLAR_PANEL.get(),
                            new Item.Properties()));



    public static final DeferredItem<Item> WRENCH =
            ITEMS.register("wrench",
                    () -> new WrenchItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SOLCUPRUM_INGOT =
            ITEMS.register("solcuprum_ingot",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SOLARIS_ROSE_INGOT =
            ITEMS.register("solaris_rose_ingot",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MIDAS_GOLD_INGOT =
            ITEMS.register("midas_gold_ingot",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MYTHRIL_INGOT =
            ITEMS.register("mythril_ingot",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SOLARIS_CAUGHT_GEM =
            ITEMS.register("solaris_caught_gem",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> NOX_CAUGHT_GEM =
            ITEMS.register("nox_caught_gem",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PURE_STAR =
            ITEMS.register("pure_star",
                    () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

