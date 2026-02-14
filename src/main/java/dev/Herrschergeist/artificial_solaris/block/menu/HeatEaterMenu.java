package dev.Herrschergeist.artificial_solaris.block.menu;

import dev.Herrschergeist.artificial_solaris.block.entity.HeatEaterBlockEntity;
import dev.Herrschergeist.artificial_solaris.block.entity.HeatEaterBlockEntity.HeatSource;
import dev.Herrschergeist.artificial_solaris.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class HeatEaterMenu extends AbstractContainerMenu {

    private final HeatEaterBlockEntity blockEntity;
    private final ContainerData data;

    // ContainerData: 4 int-слота
    // [0] energy lo-word, [1] energy hi-word, [2] max energy, [3] heatSource ordinal
    private static final int DATA_ENERGY_LO  = 0;
    private static final int DATA_ENERGY_HI  = 1;
    private static final int DATA_MAX_ENERGY = 2;
    private static final int DATA_SOURCE     = 3;
    private static final int DATA_SIZE       = 4;

    // ─── Клиентский конструктор (вызывается через IMenuTypeExtension) ─────────
    public HeatEaterMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, null, new SimpleContainerData(DATA_SIZE));
    }

    // ─── Серверный конструктор ────────────────────────────────────────────────
    public HeatEaterMenu(int containerId, Inventory playerInventory,
                         HeatEaterBlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.HEAT_EATER_MENU.get(), containerId);

        this.blockEntity = blockEntity;
        this.data = data;
        addDataSlots(data);

        // Инвентарь игрока: 3 ряда × 9 слотов
        // Позиции соответствуют сетке в текстуре (под полоской энергии y=34..130)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory,
                        col + row * 9 + 9,   // индекс в инвентаре (9..35)
                        8 + col * 18,         // x
                        84 + row * 18));      // y: начинаем с 40 (под полоской 34px + отступ)
            }
        }

        // Хотбар: 9 слотов
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory,
                    col,             // индекс 0..8
                    8 + col * 18,
                    142));         // y: 40 + 3*18 + 2 = 98
        }
    }

    // ─── Синхронизированные данные ────────────────────────────────────────────

    /** Запасённая энергия (собирается из двух int). */
    public long getEnergy() {
        long lo = Integer.toUnsignedLong(data.get(DATA_ENERGY_LO));
        long hi = Integer.toUnsignedLong(data.get(DATA_ENERGY_HI));
        return lo | (hi << 32);
    }

    public int getMaxEnergy() {
        return data.get(DATA_MAX_ENERGY);
    }

    public HeatSource getHeatSource() {
        return HeatSource.fromOrdinal(data.get(DATA_SOURCE));
    }

    /** 0–100 для индикатора */
    public int getEnergyPercent() {
        int max = getMaxEnergy();
        if (max == 0) return 0;
        return (int) (getEnergy() * 100L / max);
    }

    // ─── Обязательные методы ──────────────────────────────────────────────────

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Нет блочных слотов — shift+click не делает ничего
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        if (blockEntity == null) return true; // клиентская сторона
        return blockEntity.getLevel() != null
                && player.distanceToSqr(
                blockEntity.getBlockPos().getX() + 0.5,
                blockEntity.getBlockPos().getY() + 0.5,
                blockEntity.getBlockPos().getZ() + 0.5) <= 64.0;
    }
}