package dev.Herrschergeist.artificial_solaris.generated;

import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import dev.Herrschergeist.artificial_solaris.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

import static dev.Herrschergeist.artificial_solaris.artificial_solaris.MOD_ID;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // ═══════════════════════════════════════════════════════
        // SOLAR PANELS - Upgrading pattern
        // ═══════════════════════════════════════════════════════

        // Copper Solar Panel (base tier)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COPPER_SOLAR_PANEL.get())
                .pattern("AAA")
                .pattern("CRC")
                .pattern("CCC")
                .define('A', Items.AMETHYST_SHARD)
                .define('C', Items.COPPER_INGOT)
                .define('R', Items.REDSTONE_BLOCK)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .save(recipeOutput);

        // Iron Solar Panel (upgrade from copper)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.IRON_SOLAR_PANEL.get())
                .pattern("ISI")
                .pattern("IRI")
                .pattern("IBI")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE_BLOCK)
                .define('B', Items.IRON_BLOCK)
                .define('S', ModBlocks.COPPER_SOLAR_PANEL.get())
                .unlockedBy("has_copper_panel", has(ModBlocks.COPPER_SOLAR_PANEL.get()))
                .save(recipeOutput);

        // Gold Solar Panel (upgrade from iron)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GOLD_SOLAR_PANEL.get())
                .pattern("GSG")
                .pattern("GRG")
                .pattern("BGB")
                .define('G', Items.GOLD_INGOT)
                .define('R', Items.REDSTONE_BLOCK)
                .define('B', Items.GOLD_BLOCK)
                .define('S', ModBlocks.IRON_SOLAR_PANEL.get())
                .unlockedBy("has_iron_panel", has(ModBlocks.IRON_SOLAR_PANEL.get()))
                .save(recipeOutput);

        // Diamond Solar Panel (upgrade from gold)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DIAMOND_SOLAR_PANEL.get())
                .pattern("BSB")
                .pattern("DRD")
                .pattern("DBD")
                .define('B', Items.DIAMOND_BLOCK)
                .define('R', Items.REDSTONE_BLOCK)
                .define('D', Items.DIAMOND)
                .define('S', ModBlocks.GOLD_SOLAR_PANEL.get())
                .unlockedBy("has_gold_panel", has(ModBlocks.GOLD_SOLAR_PANEL.get()))
                .save(recipeOutput);

        // Netherite Solar Panel (upgrade from diamond)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.NETHERITE_SOLAR_PANEL.get())
                .pattern("NSN")
                .pattern("NRN")
                .pattern("NNN")
                .define('N', Items.NETHERITE_INGOT)
                .define('S', ModBlocks.DIAMOND_SOLAR_PANEL.get())
                .define('R', Items.REDSTONE_BLOCK)
                .unlockedBy("has_diamond_panel", has(ModBlocks.DIAMOND_SOLAR_PANEL.get()))
                .save(recipeOutput);

        // Withering Solar Panel (upgrade from netherite)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WITHERING_SOLAR_PANEL.get())
                .pattern("BNB")
                .pattern("NSN")
                .pattern("BNB")
                .define('B', Items.NETHERITE_BLOCK)
                .define('S', ModBlocks.NETHERITE_SOLAR_PANEL.get())
                .define('N', Items.NETHER_STAR)
                .unlockedBy("has_netherite_panel", has(ModBlocks.NETHERITE_SOLAR_PANEL.get()))
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(recipeOutput);

        // ═══════════════════════════════════════════════════════
        // PHOTON IRRADIATORS - Upgrading pattern
        // ═══════════════════════════════════════════════════════

        // Copper Photon Irradiator (base tier)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COPPER_PHOTON_IRRADIATOR.get())
                .pattern("CCC")
                .pattern("CAC")
                .pattern("CCC")
                .define('C', Items.COPPER_INGOT)
                .define('A', Items.AMETHYST_BLOCK)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .save(recipeOutput);

        // Iron Photon Irradiator (upgrade from copper)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.IRON_PHOTON_IRRADIATOR.get())
                .pattern("III")
                .pattern("IPI")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('P', ModBlocks.COPPER_PHOTON_IRRADIATOR.get())
                .unlockedBy("has_copper_irradiator", has(ModBlocks.COPPER_PHOTON_IRRADIATOR.get()))
                .save(recipeOutput);

        // Gold Photon Irradiator (upgrade from iron)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GOLD_PHOTON_IRRADIATOR.get())
                .pattern("GGG")
                .pattern("GPG")
                .pattern("GGG")
                .define('G', Items.GOLD_INGOT)
                .define('P', ModBlocks.IRON_PHOTON_IRRADIATOR.get())
                .unlockedBy("has_iron_irradiator", has(ModBlocks.IRON_PHOTON_IRRADIATOR.get()))
                .save(recipeOutput);

        // Diamond Photon Irradiator (upgrade from gold)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DIAMOND_PHOTON_IRRADIATOR.get())
                .pattern("DDD")
                .pattern("DPD")
                .pattern("DDD")
                .define('D', Items.DIAMOND)
                .define('P', ModBlocks.GOLD_PHOTON_IRRADIATOR.get())
                .unlockedBy("has_gold_irradiator", has(ModBlocks.GOLD_PHOTON_IRRADIATOR.get()))
                .save(recipeOutput);

        // Netherite Photon Irradiator (upgrade from diamond)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.NETHERITE_PHOTON_IRRADIATOR.get())
                .pattern("NNN")
                .pattern("NPN")
                .pattern("NNN")
                .define('N', Items.NETHERITE_INGOT)
                .define('P', ModBlocks.DIAMOND_PHOTON_IRRADIATOR.get())
                .unlockedBy("has_diamond_irradiator", has(ModBlocks.DIAMOND_PHOTON_IRRADIATOR.get()))
                .save(recipeOutput);

        // Withering Photon Irradiator (upgrade from netherite)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WITHERING_PHOTON_IRRADIATOR.get())
                .pattern("BSB")
                .pattern("SPS")
                .pattern("BSB")
                .define('B', Items.NETHERITE_BLOCK)
                .define('S', Items.NETHER_STAR)
                .define('P', ModBlocks.NETHERITE_PHOTON_IRRADIATOR.get())
                .unlockedBy("has_netherite_irradiator", has(ModBlocks.NETHERITE_PHOTON_IRRADIATOR.get()))
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(recipeOutput);

        // ═══════════════════════════════════════════════════════
        // EXCITED MATERIALS - Blocks and compact/uncompact
        // ═══════════════════════════════════════════════════════

        // Excited Copper
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.EXCITED_COPPER_BLOCK.get())
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ModItems.EXCITED_COPPER_INGOT.get())
                .unlockedBy("has_excited_copper", has(ModItems.EXCITED_COPPER_INGOT.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EXCITED_COPPER_INGOT.get(), 9)
                .requires(ModBlocks.EXCITED_COPPER_BLOCK.get())
                .unlockedBy("has_excited_copper_block", has(ModBlocks.EXCITED_COPPER_BLOCK.get()))
                .save(recipeOutput, "excited_copper_ingot_from_block");  // ← ВАЖНО: уникальное имя!

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EXCITED_COPPER_INGOT.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.EXCITED_COPPER_NUGGET.get())
                .unlockedBy("has_excited_copper_nugget", has(ModItems.EXCITED_COPPER_NUGGET.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EXCITED_COPPER_NUGGET.get(), 9)
                .requires(ModItems.EXCITED_COPPER_INGOT.get())
                .unlockedBy("has_excited_copper_ingot", has(ModItems.EXCITED_COPPER_INGOT.get()))
                .save(recipeOutput, "excited_copper_nugget_from_ingot");  // ← ВАЖНО: уникальное имя!

        // Excited Iron
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.EXCITED_IRON_BLOCK.get())
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ModItems.EXCITED_IRON_INGOT.get())
                .unlockedBy("has_excited_iron", has(ModItems.EXCITED_IRON_INGOT.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EXCITED_IRON_INGOT.get(), 9)
                .requires(ModBlocks.EXCITED_IRON_BLOCK.get())
                .unlockedBy("has_excited_iron_block", has(ModBlocks.EXCITED_IRON_BLOCK.get()))
                .save(recipeOutput, "excited_iron_ingot_from_block");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EXCITED_IRON_INGOT.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.EXCITED_IRON_NUGGET.get())
                .unlockedBy("has_excited_iron_nugget", has(ModItems.EXCITED_IRON_NUGGET.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EXCITED_IRON_NUGGET.get(), 9)
                .requires(ModItems.EXCITED_IRON_INGOT.get())
                .unlockedBy("has_excited_iron_ingot", has(ModItems.EXCITED_IRON_INGOT.get()))
                .save(recipeOutput, "excited_iron_nugget_from_ingot");

        // Excited Gold
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.EXCITED_GOLD_BLOCK.get())
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ModItems.EXCITED_GOLD_INGOT.get())
                .unlockedBy("has_excited_gold", has(ModItems.EXCITED_GOLD_INGOT.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EXCITED_GOLD_INGOT.get(), 9)
                .requires(ModBlocks.EXCITED_GOLD_BLOCK.get())
                .unlockedBy("has_excited_gold_block", has(ModBlocks.EXCITED_GOLD_BLOCK.get()))
                .save(recipeOutput, "excited_gold_ingot_from_block");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EXCITED_GOLD_INGOT.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.EXCITED_GOLD_NUGGET.get())
                .unlockedBy("has_excited_gold_nugget", has(ModItems.EXCITED_GOLD_NUGGET.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EXCITED_GOLD_NUGGET.get(), 9)
                .requires(ModItems.EXCITED_GOLD_INGOT.get())
                .unlockedBy("has_excited_gold_ingot", has(ModItems.EXCITED_GOLD_INGOT.get()))
                .save(recipeOutput, "excited_gold_nugget_from_ingot");

        // Excited Netherite
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.EXCITED_NETHERITE_BLOCK.get())
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ModItems.EXCITED_NETHERITE_INGOT.get())
                .unlockedBy("has_excited_netherite", has(ModItems.EXCITED_NETHERITE_INGOT.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EXCITED_NETHERITE_INGOT.get(), 9)
                .requires(ModBlocks.EXCITED_NETHERITE_BLOCK.get())
                .unlockedBy("has_excited_netherite_block", has(ModBlocks.EXCITED_NETHERITE_BLOCK.get()))
                .save(recipeOutput, "excited_netherite_ingot_from_block");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.EXCITED_NETHERITE_INGOT.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.EXCITED_NETHERITE_NUGGET.get())
                .unlockedBy("has_excited_netherite_nugget", has(ModItems.EXCITED_NETHERITE_NUGGET.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EXCITED_NETHERITE_NUGGET.get(), 9)
                .requires(ModItems.EXCITED_NETHERITE_INGOT.get())
                .unlockedBy("has_excited_netherite_ingot", has(ModItems.EXCITED_NETHERITE_INGOT.get()))
                .save(recipeOutput, "excited_netherite_nugget_from_ingot");

        // ═══════════════════════════════════════════════════════
        // EXCITED CHAINS
        // ═══════════════════════════════════════════════════════

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.EXCITED_COPPER_CHAIN.get())
                .pattern("N")
                .pattern("I")
                .pattern("N")
                .define('N', ModItems.EXCITED_COPPER_NUGGET.get())
                .define('I', ModItems.EXCITED_COPPER_INGOT.get())
                .unlockedBy("has_excited_copper", has(ModItems.EXCITED_COPPER_INGOT.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.EXCITED_IRON_CHAIN.get())
                .pattern("N")
                .pattern("I")
                .pattern("N")
                .define('N', ModItems.EXCITED_IRON_NUGGET.get())
                .define('I', ModItems.EXCITED_IRON_INGOT.get())
                .unlockedBy("has_excited_iron", has(ModItems.EXCITED_IRON_INGOT.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.EXCITED_GOLD_CHAIN.get())
                .pattern("N")
                .pattern("I")
                .pattern("N")
                .define('N', ModItems.EXCITED_GOLD_NUGGET.get())
                .define('I', ModItems.EXCITED_GOLD_INGOT.get())
                .unlockedBy("has_excited_gold", has(ModItems.EXCITED_GOLD_INGOT.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.EXCITED_NETHERITE_CHAIN.get())
                .pattern("N")
                .pattern("I")
                .pattern("N")
                .define('N', ModItems.EXCITED_NETHERITE_NUGGET.get())
                .define('I', ModItems.EXCITED_NETHERITE_INGOT.get())
                .unlockedBy("has_excited_netherite", has(ModItems.EXCITED_NETHERITE_INGOT.get()))
                .save(recipeOutput);

        // ═══════════════════════════════════════════════════════
        // DRAGON FORGE
        // ═══════════════════════════════════════════════════════

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DRAGON_FORGE.get())
                .pattern("CCC")
                .pattern("GBI")
                .pattern("III")
                .define('C', ModBlocks.EXCITED_COPPER_BLOCK)
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('B', Items.BLAZE_ROD)
                .define('I', ModItems.EXCITED_COPPER_INGOT)
                .unlockedBy("has_blaze_rod", has(Items.BLAZE_ROD))
                .unlockedBy("has_excited_copper", has(ModItems.EXCITED_COPPER_INGOT))
                .save(recipeOutput);

        // ═══════════════════════════════════════════════════════
        // STELLAR CAGES
        // ═══════════════════════════════════════════════════════

        // Stellar Cage Tier 1
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STELLAR_CAGE_TIER1.get())
                .pattern("ICI")
                .pattern("CEC")
                .pattern("ICI")
                .define('I', ModItems.EXCITED_IRON_INGOT)
                .define('C', ModBlocks.EXCITED_IRON_CHAIN)
                .define('E', Items.ENDER_PEARL)
                .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
                .save(recipeOutput);

        // Stellar Cage Tier 2 (upgrade from tier 1)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STELLAR_CAGE_TIER2.get())
                .pattern("DND")
                .pattern("NTN")
                .pattern("DND")
                .define('D', Items.DIAMOND)
                .define('N', Items.NETHERITE_INGOT)
                .define('T', ModItems.STELLAR_CAGE_TIER1.get())
                .unlockedBy("has_tier1_cage", has(ModItems.STELLAR_CAGE_TIER1.get()))
                .unlockedBy("has_netherite", has(Items.NETHERITE_INGOT))
                .save(recipeOutput);

        // ═══════════════════════════════════════════════════════
        // WRENCH
        // ═══════════════════════════════════════════════════════

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.WRENCH.get())
                .pattern("I I")
                .pattern(" I ")
                .pattern(" I ")
                .define('I', ModItems.EXCITED_COPPER_INGOT)
                .unlockedBy("has_excited_copper", has(ModItems.EXCITED_COPPER_INGOT))
                .save(recipeOutput);

    }
}