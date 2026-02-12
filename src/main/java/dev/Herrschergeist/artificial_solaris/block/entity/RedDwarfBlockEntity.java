package dev.Herrschergeist.artificial_solaris.block.entity;

import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import dev.Herrschergeist.artificial_solaris.registry.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class RedDwarfBlockEntity extends BlockEntity {

    private static final int BURN_RADIUS = 9;
    private static final int SUNLIGHT_RADIUS = 9;
    private static final int STELLAR_BURN_LEVEL = 0;

    private int tickCounter = 0;
    private boolean isPartOfMultiblock = false;

    public RedDwarfBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.RED_DWARF.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RedDwarfBlockEntity blockEntity) {
        blockEntity.tickCounter++;

        // Only apply burn effect if NOT part of multiblock
        if (!blockEntity.isPartOfMultiblock) {
            // Apply stellar burn effect every second (20 ticks)
            if (blockEntity.tickCounter % 20 == 0) {
                blockEntity.applyBurnEffect(level, pos);
            }

            // Melt snow every 20 ticks
            if (blockEntity.tickCounter % 20 == 0) {
                blockEntity.meltSnow(level, pos);
            }
        }

        // These run every second
        if (blockEntity.tickCounter % 20 == 0) {
            blockEntity.isPartOfMultiblock = blockEntity.checkMultiblockStructure(level, pos);
            blockEntity.applyArtificialSunlight(level, pos);
        }
    }

    // Applies artificial sunlight to solar panels in range
    private void applyArtificialSunlight(Level level, BlockPos redDwarfPos) {
        // Search for solar panels in radius
        BlockPos.betweenClosed(
                redDwarfPos.offset(-SUNLIGHT_RADIUS, -SUNLIGHT_RADIUS, -SUNLIGHT_RADIUS),
                redDwarfPos.offset(SUNLIGHT_RADIUS, SUNLIGHT_RADIUS, SUNLIGHT_RADIUS)
        ).forEach(pos -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SolarPanelBlockEntity solarPanel) {
                // Mark this panel as receiving artificial sunlight
                solarPanel.setArtificialSunlight(true);

                // If this Red Dwarf is part of multiblock, boost energy by 20%
                if (isPartOfMultiblock) {
                    solarPanel.setMultiblockBoost(true);
                }
            }
        });
    }

    // Applies stellar burn effect to entities in range
    private void applyBurnEffect(Level level, BlockPos pos) {
        AABB searchBox = new AABB(pos).inflate(BURN_RADIUS);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, searchBox);

        for (LivingEntity entity : entities) {
            entity.addEffect(new MobEffectInstance(
                    ModEffects.STELLAR_BURN,
                    100,
                    STELLAR_BURN_LEVEL,
                    false,
                    true,
                    true
            ));
        }
    }

    // Melts snow blocks in a small radius around the Red Dwarf
    private void meltSnow(Level level, BlockPos pos) {
        // Check 3x3 area around the block
        for (BlockPos checkPos : BlockPos.betweenClosed(
                pos.offset(-1, -1, -1),
                pos.offset(1, 1, 1))) {
            BlockState state = level.getBlockState(checkPos);

            // Melt snow layers and snow blocks
            if (state.is(Blocks.SNOW) || state.is(Blocks.SNOW_BLOCK)) {
                level.removeBlock(checkPos, false);
            }

            // Melt ice
            if (state.is(Blocks.ICE)) {
                level.setBlockAndUpdate(checkPos, Blocks.WATER.defaultBlockState());
            }
        }
    }

    // Checks if this Red Dwarf is part of a valid multiblock structure
    private boolean checkMultiblockStructure(Level level, BlockPos center) {
        // Structure: Restraint - Chain - RedDwarf - Chain - Restraint
        // Distance: Restraint at ±2, Chain at ±1

        // Check North: Chain at +1, Restraint at +2
        BlockPos northChain = center.north();
        BlockPos northRestraint = center.north(2);

        // Check South: Chain at -1, Restraint at -2
        BlockPos southChain = center.south();
        BlockPos southRestraint = center.south(2);

        // Check East: Chain at +1, Restraint at +2
        BlockPos eastChain = center.east();
        BlockPos eastRestraint = center.east(2);

        // Check West: Chain at -1, Restraint at -2
        BlockPos westChain = center.west();
        BlockPos westRestraint = center.west(2);

        // Validate chains (excited copper chain)
        boolean hasNorthChain = level.getBlockState(northChain).is(ModBlocks.EXCITED_NETHERITE_CHAIN.get());
        boolean hasSouthChain = level.getBlockState(southChain).is(ModBlocks.EXCITED_NETHERITE_CHAIN.get());
        boolean hasEastChain = level.getBlockState(eastChain).is(ModBlocks.EXCITED_NETHERITE_CHAIN.get());
        boolean hasWestChain = level.getBlockState(westChain).is(ModBlocks.EXCITED_NETHERITE_CHAIN.get());

        // Validate restraints
        boolean hasNorthRestraint = level.getBlockEntity(northRestraint) instanceof SolarisRestraintBlockEntity;
        boolean hasSouthRestraint = level.getBlockEntity(southRestraint) instanceof SolarisRestraintBlockEntity;
        boolean hasEastRestraint = level.getBlockEntity(eastRestraint) instanceof SolarisRestraintBlockEntity;
        boolean hasWestRestraint = level.getBlockEntity(westRestraint) instanceof SolarisRestraintBlockEntity;

        return hasNorthChain && hasSouthChain && hasEastChain && hasWestChain &&
                hasNorthRestraint && hasSouthRestraint && hasEastRestraint && hasWestRestraint;
    }

    // Getter for multiblock status (used by solar panels and restraints)
    public boolean isPartOfMultiblock() {
        return isPartOfMultiblock;
    }

    // Get energy generation rate for Restraints based on star type
    public int getRestraintEnergyGeneration() {
        // Red Dwarf: 20 FE/tick per Restraint
        return 20;
    }

    // Get star type identifier for future expansion
    public String getStarType() {
        return "red_dwarf";
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("IsMultiblock", isPartOfMultiblock);
        tag.putInt("TickCounter", tickCounter);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isPartOfMultiblock = tag.getBoolean("IsMultiblock");
        tickCounter = tag.getInt("TickCounter");
    }
}
