package dev.Herrschergeist.artificial_solaris.event;

import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import dev.Herrschergeist.artificial_solaris.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;

import static dev.Herrschergeist.artificial_solaris.artificial_solaris.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class AnvilCraftingHandler {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        // Check if it's a falling anvil (any type)
        if (!(event.getEntity() instanceof FallingBlockEntity fallingBlock)) return;

        BlockState anvilState = fallingBlock.getBlockState();
        // Check for any anvil
        if (!anvilState.is(net.minecraft.tags.BlockTags.ANVIL)) return;

        if (!fallingBlock.onGround()) return;

        Level level = fallingBlock.level();
        if (level.isClientSide) return;

        BlockPos anvilPos = fallingBlock.blockPosition();
        BlockPos belowPos = anvilPos.below();

        // Check if BEDROCK is below
        if (!level.getBlockState(belowPos).is(Blocks.BEDROCK)) return;

        // Find Pure Star items on the bedrock
        AABB searchBox = new AABB(belowPos).inflate(0.5);
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, searchBox,
                itemEntity -> itemEntity.getItem().is(ModItems.PURE_STAR.get()));

        if (items.isEmpty()) return;

        // CRAFT ALL Pure Stars!
        int totalCrafted = 0;

        for (ItemEntity pureStarItem : items) {
            ItemStack pureStarStack = pureStarItem.getItem();
            int count = pureStarStack.getCount();

            // Convert all Pure Stars in this stack
            totalCrafted += count;
            pureStarItem.discard(); // Remove the Pure Star entity
        }

        if (totalCrafted == 0) return;

        // Drop Protostar items (one stack or multiple if needed)
        ItemStack protostarStack = new ItemStack(ModBlocks.PROTOSTAR.get(), totalCrafted);

        // Split into multiple stacks if more than max stack size
        while (protostarStack.getCount() > 0) {
            int dropCount = Math.min(protostarStack.getCount(), protostarStack.getMaxStackSize());
            ItemStack toDrop = new ItemStack(ModBlocks.PROTOSTAR.get(), dropCount);

            ItemEntity droppedProtostar = new ItemEntity(
                    level,
                    belowPos.getX() + 0.5,
                    belowPos.getY() + 1.0,
                    belowPos.getZ() + 0.5,
                    toDrop
            );
            droppedProtostar.setDefaultPickUpDelay();
            level.addFreshEntity(droppedProtostar);

            protostarStack.shrink(dropCount);
        }

        // Keep anvil - place it back
        level.setBlock(anvilPos, anvilState, 3);

        // Effects
        level.playSound(null, belowPos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.playSound(null, belowPos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.5F);

        // Remove falling block entity
        fallingBlock.discard();
    }
}