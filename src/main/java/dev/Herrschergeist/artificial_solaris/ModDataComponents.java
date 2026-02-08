package dev.Herrschergeist.artificial_solaris;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers all data components for the mod
 * Data Components are used to store custom data on items in Minecraft 1.21+
 */
public class ModDataComponents {

    // Create a DeferredRegister for data components
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(artificial_solaris.MOD_ID);

    /**
     * Data component for storing captured entity data in Stellar Cage items
     * Stores the complete NBT data of the captured mob
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> STELLAR_CAGE_DATA =
            DATA_COMPONENTS.register("stellar_cage_data", () ->
                    DataComponentType.<CompoundTag>builder()
                            .persistent(CompoundTag.CODEC)
                            .networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
                            .cacheEncoding()
                            .build()
            );

    /**
     * Call this method in your main mod class to register data components
     * @param eventBus The mod event bus
     */
    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}