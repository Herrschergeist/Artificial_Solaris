package dev.herrschergeist.artificialsolaris.registry;

import dev.herrschergeist.artificialsolaris.artificialsolaris;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Block> WRENCH_PICKUP = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("artificialsolaris", "wrench_pickup")
    );
}