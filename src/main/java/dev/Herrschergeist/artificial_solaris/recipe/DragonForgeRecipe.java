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

public class DragonForgeRecipe implements Recipe<RecipeInput> {

    private final Ingredient input;
    private final ItemStack output;
    private final int energyCost;
    private final int waterCost;
    private final int processingTime;

    public DragonForgeRecipe(Ingredient input, ItemStack output, int energyCost, int waterCost, int processingTime) {
        this.input = input;
        this.output = output;
        this.energyCost = energyCost;
        this.waterCost = waterCost;
        this.processingTime = processingTime;
    }

    public Ingredient getInput() {
        return input;
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
        return this.input.test(input.getItem(0));
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
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.input),
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
            Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
            int energy = buffer.readInt();
            int water = buffer.readInt();
            int time = buffer.readInt();
            return new DragonForgeRecipe(input, output, energy, water, time);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, DragonForgeRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
            buffer.writeInt(recipe.energyCost);
            buffer.writeInt(recipe.waterCost);
            buffer.writeInt(recipe.processingTime);
        }
    }
}