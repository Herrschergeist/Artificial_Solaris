package dev.Herrschergeist.artificial_solaris.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DragonForgeRecipe implements Recipe<RecipeInput> {

    // Each ingredient now has a count (how many are required)
    public record CountedIngredient(Ingredient ingredient, int count) {
        public static final Codec<CountedIngredient> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(CountedIngredient::ingredient),
                        Codec.INT.optionalFieldOf("count", 1).forGetter(CountedIngredient::count)
                ).apply(instance, CountedIngredient::new)
        );

        // Check if a given stack matches this ingredient with required count
        public boolean test(ItemStack stack) {
            return ingredient.test(stack) && stack.getCount() >= count;
        }
    }

    private final List<CountedIngredient> inputs; // Up to 3 input slots
    private final ItemStack output;
    private final int energyCost;
    private final int waterCost;
    private final int processingTime;

    public DragonForgeRecipe(List<CountedIngredient> inputs, ItemStack output,
                             int energyCost, int waterCost, int processingTime) {
        this.inputs = inputs;
        this.output = output;
        this.energyCost = energyCost;
        this.waterCost = waterCost;
        this.processingTime = processingTime;
    }

    public List<CountedIngredient> getInputs() {
        return inputs;
    }

    // Get ingredient for a specific slot (0, 1, 2)
    public CountedIngredient getInputForSlot(int slot) {
        if (slot < inputs.size()) {
            return inputs.get(slot);
        }
        return null; // Slot not required
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getWaterCost() {
        return waterCost;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @Override
    public boolean matches(@NotNull RecipeInput input, @NotNull Level level) {
        if (level.isClientSide()) {
            return false;
        }
        // Check all required input slots
        for (int i = 0; i < inputs.size(); i++) {
            if (!inputs.get(i).test(input.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput input, HolderLookup.@NotNull Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return output;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.DRAGON_FORGE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipes.DRAGON_FORGE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<DragonForgeRecipe> {

        private static final MapCodec<DragonForgeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        CountedIngredient.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.inputs),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                        Codec.INT.fieldOf("energy").forGetter(recipe -> recipe.energyCost),
                        Codec.INT.fieldOf("water").forGetter(recipe -> recipe.waterCost),
                        Codec.INT.fieldOf("time").forGetter(recipe -> recipe.processingTime)
                ).apply(instance, DragonForgeRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, DragonForgeRecipe> STREAM_CODEC =
                StreamCodec.of(
                        Serializer::toNetwork,
                        Serializer::fromNetwork
                );

        @Override
        public @NotNull MapCodec<DragonForgeRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, DragonForgeRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static DragonForgeRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            int size = buffer.readInt();
            List<CountedIngredient> inputs = new java.util.ArrayList<>();
            for (int i = 0; i < size; i++) {
                Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
                int count = buffer.readInt();
                inputs.add(new CountedIngredient(ingredient, count));
            }
            ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
            int energy = buffer.readInt();
            int water = buffer.readInt();
            int time = buffer.readInt();
            return new DragonForgeRecipe(inputs, output, energy, water, time);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, DragonForgeRecipe recipe) {
            buffer.writeInt(recipe.inputs.size());
            for (CountedIngredient ci : recipe.inputs) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ci.ingredient());
                buffer.writeInt(ci.count());
            }
            ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
            buffer.writeInt(recipe.energyCost);
            buffer.writeInt(recipe.waterCost);
            buffer.writeInt(recipe.processingTime);
        }
    }
}