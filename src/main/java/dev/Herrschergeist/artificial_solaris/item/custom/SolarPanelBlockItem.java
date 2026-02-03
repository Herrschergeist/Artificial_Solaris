package dev.Herrschergeist.artificial_solaris.item.custom;

import dev.Herrschergeist.artificial_solaris.block.custom.SolarPanelBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class SolarPanelBlockItem extends BlockItem {

    public SolarPanelBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (!(getBlock() instanceof SolarPanelBlock solarPanel)) return;

        // get energy from NBT
        int storedEnergy = 0;
        CustomData blockEntityData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (blockEntityData != null) {
            storedEnergy = blockEntityData.copyTag().getInt("Energy");
        }

        int capacity = solarPanel.getCapacity();
        int generation = solarPanel.getEnergyPerTick();

        // Empty filled for space
        tooltipComponents.add(Component.empty());

        // Energy
        tooltipComponents.add(Component.literal("⚡ Energy: ")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.literal(formatEnergy(storedEnergy) + " / " + formatEnergy(capacity))
                        .withStyle(ChatFormatting.YELLOW)));

        // FE/t
        tooltipComponents.add(Component.literal("☀ FE/t: ")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.literal(formatEnergy(generation) + " FE/t")
                        .withStyle(ChatFormatting.GREEN)));

        // Fill Percentage (optional)
        if (storedEnergy > 0) {
            int percentage = (int) ((storedEnergy / (double) capacity) * 100);
            tooltipComponents.add(Component.literal("├ Capacity: ")
                    .withStyle(ChatFormatting.DARK_GRAY)
                    .append(Component.literal(percentage + "%")
                            .withStyle(ChatFormatting.AQUA)));
        }
    }

    // Formating big numbers (100000 -> 100k)
    private String formatEnergy(int energy) {
        if (energy >= 1_000_000) {
            return String.format("%.1fM", energy / 1_000_000.0);
        } else if (energy >= 1_000) {
            return String.format("%.1fk", energy / 1_000.0);
        }
        return String.valueOf(energy);
    }
}