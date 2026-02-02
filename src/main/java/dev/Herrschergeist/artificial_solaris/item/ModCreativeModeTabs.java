package dev.Herrschergeist.artificial_solaris.item;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, artificial_solaris.MOD_ID);

    public static final Supplier<CreativeModeTab> ARTIFICIAL_SOLARIS =
            CREATIVE_MODE_TAB.register("artificial_solaris",
                    () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WRENCH.get()))
                            .title(Component.translatable("creativetab.artificial_solaris.artificial_solaris"))
                            .displayItems((itemDisplayParameters, output) -> {
                                output.accept(ModItems.WRENCH);

                                output.accept(ModBlocks.COPPER_SOLAR_PANEL);
                                output.accept(ModBlocks.IRON_SOLAR_PANEL);
                                output.accept(ModBlocks.GOLD_SOLAR_PANEL);
                                output.accept(ModBlocks.DIAMOND_SOLAR_PANEL);
                                output.accept(ModBlocks.NETHERITE_SOLAR_PANEL);
                                output.accept(ModBlocks.WITHERING_SOLAR_PANEL);

                                output.accept(ModItems.SOLCUPRUM_INGOT);
                                output.accept(ModItems.SOLARIS_ROSE_INGOT);
                                output.accept(ModItems.MIDAS_GOLD_INGOT);
                                output.accept(ModItems.SOLARIS_CAUGHT_GEM);
                                output.accept(ModItems.NOX_CAUGHT_GEM);
                                output.accept(ModItems.MYTHRIL_INGOT);
                                output.accept(ModItems.PURE_STAR);
                            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
