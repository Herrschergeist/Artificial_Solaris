package dev.Herrschergeist.artificial_solaris.recipe;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, artificial_solaris.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, artificial_solaris.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<DragonForgeRecipe>> DRAGON_FORGE_TYPE =
            TYPES.register("dragon_forge", () -> new RecipeType<DragonForgeRecipe>() {
                @Override
                public String toString() {
                    return "dragon_forge";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DragonForgeRecipe>> DRAGON_FORGE_SERIALIZER =
            SERIALIZERS.register("dragon_forge", DragonForgeRecipe.Serializer::new);

    // Recipe Type
    public static final DeferredHolder<RecipeType<?>, RecipeType<IrradiationRecipe>> IRRADIATION_TYPE =
            TYPES.register("irradiation", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "irradiation";
                }
            });

    // Recipe Serializer
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<IrradiationRecipe>> IRRADIATION_SERIALIZER =
            SERIALIZERS.register("irradiation", IrradiationRecipe.Serializer::new);
}