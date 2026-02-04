package dev.Herrschergeist.artificial_solaris.registry;

import dev.Herrschergeist.artificial_solaris.artificial_solaris;
import dev.Herrschergeist.artificial_solaris.block.menu.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, artificial_solaris.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<SolarPanelMenu>> SOLAR_PANEL_MENU =
            MENU_TYPES.register("solar_panel_menu",
                    () -> IMenuTypeExtension.create(SolarPanelMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<CopperPhotonIrradiatorMenu>> COPPER_PHOTON_IRRADIATOR =
            MENU_TYPES.register("copper_photon_irradiator",
                    () -> IMenuTypeExtension.create(CopperPhotonIrradiatorMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<IronPhotonIrradiatorMenu>> IRON_PHOTON_IRRADIATOR =
            MENU_TYPES.register("iron_photon_irradiator",
                    () -> IMenuTypeExtension.create(IronPhotonIrradiatorMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<GoldPhotonIrradiatorMenu>> GOLD_PHOTON_IRRADIATOR =
            MENU_TYPES.register("gold_photon_irradiator",
                    () -> IMenuTypeExtension.create(GoldPhotonIrradiatorMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<DiamondPhotonIrradiatorMenu>> DIAMOND_PHOTON_IRRADIATOR =
            MENU_TYPES.register("diamond_photon_irradiator",
                    () -> IMenuTypeExtension.create(DiamondPhotonIrradiatorMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<NetheritePhotonIrradiatorMenu>> NETHERITE_PHOTON_IRRADIATOR =
            MENU_TYPES.register("netherite_photon_irradiator",
                    () -> IMenuTypeExtension.create(NetheritePhotonIrradiatorMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<WitheringPhotonIrradiatorMenu>> WITHERING_PHOTON_IRRADIATOR =
            MENU_TYPES.register("withering_photon_irradiator",
                    () -> IMenuTypeExtension.create(WitheringPhotonIrradiatorMenu::new));
}