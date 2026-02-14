package dev.Herrschergeist.artificial_solaris.item;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import dev.Herrschergeist.artificial_solaris.item.custom.SolarPanelBlockItem;
import dev.Herrschergeist.artificial_solaris.item.custom.StellarCageItem;
import dev.Herrschergeist.artificial_solaris.item.custom.WrenchItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(artificial_solaris.MOD_ID);

    // ─── Solar Panel Items ──────────────────────────────
    public static final DeferredItem<SolarPanelBlockItem> COPPER_SOLAR_PANEL =
            ITEMS.register("copper_solar_panel",
                    () -> new SolarPanelBlockItem(ModBlocks.COPPER_SOLAR_PANEL.get(),
                            new Item.Properties()));

    public static final DeferredItem<SolarPanelBlockItem> IRON_SOLAR_PANEL =
            ITEMS.register("iron_solar_panel",
                    () -> new SolarPanelBlockItem(ModBlocks.IRON_SOLAR_PANEL.get(),
                            new Item.Properties()));

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


    // ─── Photon Irradiator Items ──────────────────────────────
    public static final DeferredItem<BlockItem> COPPER_PHOTON_IRRADIATOR =
            ITEMS.register("copper_photon_irradiator",
                    () -> new BlockItem(ModBlocks.COPPER_PHOTON_IRRADIATOR.get(),
                            new Item.Properties()));

    public static final DeferredItem<BlockItem> IRON_PHOTON_IRRADIATOR =
            ITEMS.register("iron_photon_irradiator",
                    () -> new BlockItem(ModBlocks.IRON_PHOTON_IRRADIATOR.get(),
                            new Item.Properties()));

    public static final DeferredItem<BlockItem> GOLD_PHOTON_IRRADIATOR =
            ITEMS.register("gold_photon_irradiator",
                    () -> new BlockItem(ModBlocks.GOLD_PHOTON_IRRADIATOR.get(),
                            new Item.Properties()
                                    .rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<BlockItem> DIAMOND_PHOTON_IRRADIATOR =
            ITEMS.register("diamond_photon_irradiator",
                    () -> new BlockItem(ModBlocks.DIAMOND_PHOTON_IRRADIATOR.get(),
                            new Item.Properties()
                                    .rarity(Rarity.RARE)));

    public static final DeferredItem<BlockItem> NETHERITE_PHOTON_IRRADIATOR =
            ITEMS.register("netherite_photon_irradiator",
                    () -> new BlockItem(ModBlocks.NETHERITE_PHOTON_IRRADIATOR.get(),
                            new Item.Properties()
                                    .rarity(Rarity.RARE)));

    public static final DeferredItem<BlockItem> WITHERING_PHOTON_IRRADIATOR =
            ITEMS.register("withering_photon_irradiator",
                    () -> new BlockItem(ModBlocks.WITHERING_PHOTON_IRRADIATOR.get(),
                            new Item.Properties()
                                    .rarity(Rarity.EPIC)));


    // ─── Dragon Forge Item ──────────────────────────────
    public static final DeferredItem<BlockItem> DRAGON_FORGE =
            ITEMS.register("dragon_forge",
                    () -> new BlockItem(ModBlocks.DRAGON_FORGE.get(),
                            new Item.Properties()
                                    .rarity(Rarity.EPIC)));


    // ─── Star Items ──────────────────────────────
    public static final DeferredItem<Item> RESURRECTED_LOGIC =
            ITEMS.register("resurrected_logic",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GREED_MATRIX =
            ITEMS.register("greed_matrix",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<BlockItem> SOLARIS_RESTRAINT =
            ITEMS.register("solaris_restraint",
                    () -> new BlockItem(ModBlocks.SOLARIS_RESTRAINT.get(),
                            new Item.Properties()
                                    .rarity(Rarity.RARE)));

    public static final DeferredItem<BlockItem> HEAT_EATER =
            ITEMS.register("heat_eater",
                    () -> new BlockItem(ModBlocks.HEAT_EATER.get(),
                            new Item.Properties()
                                    .rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<BlockItem> PROTOSTAR =
            ITEMS.register("protostar",
                    () -> new BlockItem(ModBlocks.PROTOSTAR.get(),
                            new Item.Properties()
                                    .rarity(Rarity.EPIC)){
                        @Override
                        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                            if (Screen.hasShiftDown()) {
                                tooltipComponents.add(Component.translatable("tooltip.artificial_solaris.protostar"));
                            } else {
                                tooltipComponents.add(Component.translatable("tooltip.artificial_solaris.shift_down"));
                                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                            }
                            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                            if (Screen.hasAltDown()) {
                                tooltipComponents.add(Component.translatable("tooltip.artificial_solaris.protostar_description"));
                            } else {
                                tooltipComponents.add(Component.translatable("tooltip.artificial_solaris.alt_down"));
                                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                            }
                            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                        }
                    });

    public static final DeferredItem<Item> RED_DWARF =
            ITEMS.register("red_dwarf",
                    () -> new BlockItem(ModBlocks.RED_DWARF.get(),
                            new Item.Properties()
                                    .rarity(Rarity.EPIC)){
                        @Override
                        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                            if (Screen.hasShiftDown()) {
                                tooltipComponents.add(Component.translatable("tooltip.artificial_solaris.red_dwarf"));
                            } else {
                                tooltipComponents.add(Component.translatable("tooltip.artificial_solaris.shift_down"));
                                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                            }
                            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                            if (Screen.hasAltDown()) {
                                tooltipComponents.add(Component.translatable("tooltip.artificial_solaris.red_dwarf_description"));
                            } else {
                                tooltipComponents.add(Component.translatable("tooltip.artificial_solaris.alt_down"));
                                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                            }
                            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                        }
                    });

    /**
     * Stellar Cage Tier 1 - Cannot capture boss mobs
     */
    public static final DeferredItem<Item> STELLAR_CAGE_TIER1 = ITEMS.register(
            "stellar_cage_tier1",
            () -> new StellarCageItem(1, new Item.Properties())
    );

    /**
     * Stellar Cage Tier 2 - Can capture all mobs including bosses
     */
    public static final DeferredItem<Item> STELLAR_CAGE_TIER2 = ITEMS.register(
            "stellar_cage_tier2",
            () -> new StellarCageItem(2, new Item.Properties())
    );

    // ─── Tool Item ──────────────────────────────
    public static final DeferredItem<Item> WRENCH =
            ITEMS.register("wrench",
                    () -> new WrenchItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SwordItem> LUNARIS_REAPER =
            ITEMS.register("lunaris_reaper",
                    () -> new SwordItem(Tiers.NETHERITE,
                            new Item.Properties()
                                    .attributes(SwordItem.createAttributes(Tiers.NETHERITE, 3, -2.4f))
                                    .rarity(Rarity.RARE)
                    ) {
                        @Override
                        public void appendHoverText(ItemStack stack, TooltipContext context,
                                                    List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                            tooltipComponents.add(Component.translatable("tooltip.artificial_solaris.lunaris_reaper.head_drop"));
                        }
                    });

    // ─── Resource Items ──────────────────────────────
    public static final DeferredItem<Item> EXCITED_COPPER_INGOT =
            ITEMS.register("excited_copper_ingot",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EXCITED_COPPER_NUGGET =
            ITEMS.register("excited_copper_nugget",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EXCITED_IRON_INGOT =
            ITEMS.register("excited_iron_ingot",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EXCITED_IRON_NUGGET =
            ITEMS.register("excited_iron_nugget",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EXCITED_GOLD_INGOT =
            ITEMS.register("excited_gold_ingot",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EXCITED_GOLD_NUGGET =
            ITEMS.register("excited_gold_nugget",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EXCITED_NETHERITE_INGOT =
            ITEMS.register("excited_netherite_ingot",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> EXCITED_NETHERITE_NUGGET =
            ITEMS.register("excited_netherite_nugget",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LUNARIS_SHARD =
            ITEMS.register("lunaris_shard",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SOLARIS_CAUGHT_GEM =
            ITEMS.register("solaris_caught_gem",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> NOX_CAUGHT_GEM =
            ITEMS.register("nox_caught_gem",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> PURE_STAR =
            ITEMS.register("pure_star",
                    () -> new Item(new Item.Properties()){
                        @Override
                        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                            if (Screen.hasShiftDown()) {
                                tooltipComponents.add(Component.translatable("tooltip.artificial_solaris.pure_star"));
                            } else {
                                tooltipComponents.add(Component.translatable("tooltip.artificial_solaris.shift_down"));
                                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                            }
                            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                        }
                    });

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

