package dev.Herrschergeist.artificial_solaris.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class IrradiationRecipe implements Recipe<RecipeInput> {

    private final Ingredient ingredient;
    private final ItemStack result;
    private final int energyCost;
    private final int processingTime; // Base time in ticks

    public IrradiationRecipe(Ingredient ingredient, ItemStack result, int energyCost, int processingTime) {
        this.ingredient = ingredient;
        this.result = result;
        this.energyCost = energyCost;
        this.processingTime = processingTime;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        if (input.isEmpty()) return false;
        return ingredient.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(ingredient);
        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.IRRADIATION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.IRRADIATION_TYPE.get();
    }

    // Serializer
    public static class Serializer implements RecipeSerializer<IrradiationRecipe> {

        private static final MapCodec<IrradiationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.INT.fieldOf("energy").forGetter(recipe -> recipe.energyCost),
                        Codec.INT.optionalFieldOf("processing_time", 100).forGetter(recipe -> recipe.processingTime)
                ).apply(instance, IrradiationRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, IrradiationRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC,
                        recipe -> recipe.ingredient,
                        ItemStack.STREAM_CODEC,
                        recipe -> recipe.result,
                        net.minecraft.network.codec.ByteBufCodecs.VAR_INT,
                        recipe -> recipe.energyCost,
                        net.minecraft.network.codec.ByteBufCodecs.VAR_INT,
                        recipe -> recipe.processingTime,
                        IrradiationRecipe::new
                );

        @Override
        public MapCodec<IrradiationRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IrradiationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}