package dev.Herrschergeist.artificial_solaris.generated;

import dev.Herrschergeist.artificial_solaris.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static dev.Herrschergeist.artificial_solaris.artificial_solaris.MOD_ID;

/**
 * Generates item tags
 * Currently we don't have custom item tags, but this is here for future expansion
 */
public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Example:
        // tag(ItemTags.YOUR_TAG)
        //     .add(ModItems.YOUR_ITEM.get());
        tag(ItemTags.create(ResourceLocation.withDefaultNamespace("enchantable/sword")))
                .add(ModItems.LUNARIS_REAPER.get());
    }
}