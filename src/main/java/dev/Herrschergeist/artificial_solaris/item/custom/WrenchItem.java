package dev.Herrschergeist.artificial_solaris.item.custom;

import dev.Herrschergeist.artificial_solaris.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.List;

public class WrenchItem extends Item {

    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.PASS;

        BlockState state = level.getBlockState(pos);

        if (state.is(ModTags.Blocks.WRENCH_BLACKLIST)) {
            return InteractionResult.PASS;
        }

        if (!state.hasBlockEntity()) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity == null) return InteractionResult.FAIL;

                // Создаем ItemStack
                ItemStack drop = new ItemStack(state.getBlock());

                // Сохраняем NBT с ID
                CompoundTag tag = blockEntity.saveWithId(level.registryAccess());

                // Удаляем координаты
                tag.remove("x");
                tag.remove("y");
                tag.remove("z");

                // **КЛЮЧЕВОЕ ИЗМЕНЕНИЕ: очищаем инвентарь из NBT**
                tag.remove("Items");  // Стандартное поле для инвентаря
                tag.remove("Inventory"); // Если используется другое имя

                // Применяем NBT
                if (!tag.isEmpty()) {
                    drop.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tag));
                }

                // **Сначала дропаем предметы из блока**
                state.getBlock().playerWillDestroy(level, pos, state, player);

                // Удаляем блок из мира
                level.removeBlock(pos, false);

                // Звук
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);

                // Даём игроку
                if (!player.getInventory().add(drop)) {
                    player.drop(drop, false);
                }

                // Урон инструменту
                context.getItemInHand().hurtAndBreak(1, player, player.getEquipmentSlotForItem(context.getItemInHand()));
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("§7Shift + ПКМ для подбора блоков"));
        tooltipComponents.add(Component.literal("§7Сохраняет NBT данные"));
    }
}