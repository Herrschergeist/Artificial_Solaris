package dev.Herrschergeist.artificial_solaris.generated;

import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

/**
 * Generates loot tables for blocks
 * Defines what items drop when blocks are broken
 */
public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // ═══════════════════════════════════════════════════════
        // SOLAR PANELS - drop themselves
        // ═══════════════════════════════════════════════════════
        dropSelf(ModBlocks.COPPER_SOLAR_PANEL.get());
        dropSelf(ModBlocks.IRON_SOLAR_PANEL.get());
        dropSelf(ModBlocks.GOLD_SOLAR_PANEL.get());
        dropSelf(ModBlocks.DIAMOND_SOLAR_PANEL.get());
        dropSelf(ModBlocks.NETHERITE_SOLAR_PANEL.get());
        dropSelf(ModBlocks.WITHERING_SOLAR_PANEL.get());

        // ═══════════════════════════════════════════════════════
        // PHOTON IRRADIATORS - drop themselves
        // ═══════════════════════════════════════════════════════
        dropSelf(ModBlocks.COPPER_PHOTON_IRRADIATOR.get());
        dropSelf(ModBlocks.IRON_PHOTON_IRRADIATOR.get());
        dropSelf(ModBlocks.GOLD_PHOTON_IRRADIATOR.get());
        dropSelf(ModBlocks.DIAMOND_PHOTON_IRRADIATOR.get());
        dropSelf(ModBlocks.NETHERITE_PHOTON_IRRADIATOR.get());
        dropSelf(ModBlocks.WITHERING_PHOTON_IRRADIATOR.get());

        // ═══════════════════════════════════════════════════════
        // DRAGON FORGE - drops itself
        // ═══════════════════════════════════════════════════════
        dropSelf(ModBlocks.DRAGON_FORGE.get());

        // ═══════════════════════════════════════════════════════
        // EXCITED BLOCKS - drop themselves
        // ═══════════════════════════════════════════════════════
        dropSelf(ModBlocks.EXCITED_COPPER_BLOCK.get());
        dropSelf(ModBlocks.EXCITED_IRON_BLOCK.get());
        dropSelf(ModBlocks.EXCITED_GOLD_BLOCK.get());
        dropSelf(ModBlocks.EXCITED_NETHERITE_BLOCK.get());
        dropSelf(ModBlocks.LUNARIS_BLOCK.get());

        // ═══════════════════════════════════════════════════════
        // EXCITED CHAINS - drop themselves
        // ═══════════════════════════════════════════════════════
        dropSelf(ModBlocks.EXCITED_COPPER_CHAIN.get());
        dropSelf(ModBlocks.EXCITED_IRON_CHAIN.get());
        dropSelf(ModBlocks.EXCITED_GOLD_CHAIN.get());
        dropSelf(ModBlocks.EXCITED_NETHERITE_CHAIN.get());

        // ═══════════════════════════════════════════════════════
        // STARS - drop themselves
        // ═══════════════════════════════════════════════════════
        dropSelf(ModBlocks.SOLARIS_RESTRAINT.get());
        dropSelf(ModBlocks.HEAT_EATER.get());
        dropSelf(ModBlocks.PROTOSTAR.get());
        dropSelf(ModBlocks.RED_DWARF.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(Holder::value)
                .toList();
    }
}