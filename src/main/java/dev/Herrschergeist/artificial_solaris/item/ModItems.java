package dev.Herrschergeist.artificial_solaris.item;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import dev.Herrschergeist.artificial_solaris.item.custom.SolarPanelBlockItem;
import dev.Herrschergeist.artificial_solaris.item.custom.WrenchItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(artificial_solaris.MOD_ID);

    public static final DeferredItem<SolarPanelBlockItem> COPPER_SOLAR_PANEL =
            ITEMS.register("copper_solar_panel",
                    () -> new SolarPanelBlockItem(ModBlocks.COPPER_SOLAR_PANEL.get(),
                            new Item.Properties()
                                    .rarity(Rarity.COMMON)));

    public static final DeferredItem<SolarPanelBlockItem> IRON_SOLAR_PANEL =
            ITEMS.register("iron_solar_panel",
                    () -> new SolarPanelBlockItem(ModBlocks.IRON_SOLAR_PANEL.get(),
                            new Item.Properties()
                                    .rarity(Rarity.COMMON)));

    public static final DeferredItem<SolarPanelBlockItem> GOLD_SOLAR_PANEL =
            ITEMS.register("gold_solar_panel",
                    () -> new SolarPanelBlockItem(ModBlocks.GOLD_SOLAR_PANEL.get(),
                            new Item.Properties()
                                    .rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<SolarPanelBlockItem> DIAMOND_SOLAR_PANEL =
            ITEMS.register("diamond_solar_panel",
                    () -> new SolarPanelBlockItem(ModBlocks.DIAMOND_SOLAR_PANEL.get(),
                            new Item.Properties()
                                    .rarity(Rarity.RARE)));

    public static final DeferredItem<SolarPanelBlockItem> NETHERITE_SOLAR_PANEL =
            ITEMS.register("netherite_solar_panel",
                    () -> new SolarPanelBlockItem(ModBlocks.NETHERITE_SOLAR_PANEL.get(),
                            new Item.Properties()
                                    .rarity(Rarity.RARE)));

    public static final DeferredItem<SolarPanelBlockItem> WITHERING_SOLAR_PANEL =
            ITEMS.register("withering_solar_panel",
                    () -> new SolarPanelBlockItem(ModBlocks.WITHERING_SOLAR_PANEL.get(),
                            new Item.Properties()
                                    .rarity(Rarity.EPIC)));



    public static final DeferredItem<Item> WRENCH =
            ITEMS.register("wrench",
                    () -> new WrenchItem(new Item.Properties().stacksTo(1)));


    public static final DeferredItem<Item> EXCITED_COPPER_INGOT =
            ITEMS.register("excited_copper_ingot",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EXCITED_IRON_INGOT =
            ITEMS.register("excited_iron_ingot",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EXCITED_GOLD_INGOT =
            ITEMS.register("excited_gold_ingot",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EXCITED_NETHERITE_INGOT =
            ITEMS.register("excited_netherite_ingot",
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

