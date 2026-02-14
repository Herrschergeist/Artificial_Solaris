package dev.Herrschergeist.artificial_solaris.block.entity;

import dev.Herrschergeist.artificial_solaris.block.ModBlocks;
import dev.Herrschergeist.artificial_solaris.block.menu.HeatEaterMenu;
import dev.Herrschergeist.artificial_solaris.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class HeatEaterBlockEntity extends BlockEntity implements MenuProvider {

    // ─── Константы источников тепла ───────────────────────────────────────────
    public static final int FE_AMBIENT    = 5;
    public static final int FE_PROTOSTAR  = 20;
    public static final int FE_RED_DWARF  = 100;
    public static final int FE_MULTIBLOCK = 2000;

    public static final int RADIUS_PROTOSTAR = 7;
    public static final int RADIUS_RED_DWARF = 9;

    public static final int BUFFER_CAPACITY = 500_000;
    public static final int MAX_TRANSFER    = 10_000;

    private static final int SCAN_INTERVAL = 20;

    // ─── Источник тепла ───────────────────────────────────────────────────────
    public enum HeatSource {
        NONE      (FE_AMBIENT,    "heat_eater.source.ambient"),
        PROTOSTAR (FE_PROTOSTAR,  "heat_eater.source.protostar"),
        RED_DWARF (FE_RED_DWARF,  "heat_eater.source.red_dwarf"),
        MULTIBLOCK(FE_MULTIBLOCK, "heat_eater.source.yellow_dwarf");

        public final int fePerTick;
        public final String translationKey;

        HeatSource(int fePerTick, String translationKey) {
            this.fePerTick = fePerTick;
            this.translationKey = translationKey;
        }

        public static HeatSource fromOrdinal(int ordinal) {
            HeatSource[] values = values();
            return (ordinal >= 0 && ordinal < values.length) ? values[ordinal] : NONE;
        }
    }

    // ─── Именованный подкласс EnergyStorage ──────────────────────────────────
    // Именованный (не анонимный) — иначе не получить доступ к protected `energy`
    // из метода внешнего класса. onContentsChanged() отсутствует в NeoForge 1.21.1,
    // поэтому setChanged() вызываем явно там, где меняем энергию.
    public static final class HeatEaterEnergyStorage extends EnergyStorage {
        public HeatEaterEnergyStorage() {
            super(BUFFER_CAPACITY, MAX_TRANSFER, MAX_TRANSFER);
        }

        /** Прямая запись при загрузке NBT — минует canReceive/canExtract логику. */
        public void setEnergyDirect(int amount) {
            this.energy = Math.max(0, Math.min(amount, this.capacity));
        }
    }

    private final HeatEaterEnergyStorage energyStorage = new HeatEaterEnergyStorage();

    private void setStoredEnergy(int amount) {
        energyStorage.setEnergyDirect(amount);
    }

    // ─── Состояние ────────────────────────────────────────────────────────────
    private HeatSource currentHeatSource = HeatSource.NONE;
    private boolean    isPartOfMultiblock = false;
    private int        scanTimer          = 0;

    // ─── Конструктор ──────────────────────────────────────────────────────────
    public HeatEaterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HEAT_EATER_BE.get(), pos, state);
    }

    // ─── Тик ──────────────────────────────────────────────────────────────────
    public static void tick(Level level, BlockPos pos, BlockState state, HeatEaterBlockEntity be) {
        if (level.isClientSide()) return;

        // Сканировать источники раз в SCAN_INTERVAL тиков
        if (++be.scanTimer >= SCAN_INTERVAL) {
            be.scanTimer = 0;
            be.updateHeatSource(level, pos);
        }

        // Генерировать энергию каждый тик
        int feToAdd = be.currentHeatSource.fePerTick;
        int space   = be.energyStorage.getMaxEnergyStored() - be.energyStorage.getEnergyStored();
        if (space > 0) {
            be.energyStorage.receiveEnergy(Math.min(feToAdd, space), false);
        }

        // Пушить энергию соседям
        be.distributeEnergy(level, pos);
    }

    // ─── Логика источников ────────────────────────────────────────────────────

    private void updateHeatSource(Level level, BlockPos pos) {
        HeatSource newSource;

        if (isPartOfMultiblock) {
            newSource = HeatSource.MULTIBLOCK;
        } else if (hasBlockNearby(level, pos, ModBlocks.RED_DWARF.get(), RADIUS_RED_DWARF)) {
            newSource = HeatSource.RED_DWARF;
        } else if (hasBlockNearby(level, pos, ModBlocks.PROTOSTAR.get(), RADIUS_PROTOSTAR)) {
            newSource = HeatSource.PROTOSTAR;
        } else {
            newSource = HeatSource.NONE;
        }

        if (currentHeatSource != newSource) {
            currentHeatSource = newSource;
            setChanged();
        }
    }

    /**
     * Ищет блок заданного типа в кубическом радиусе. Досрочный выход при первом совпадении.
     */
    private static boolean hasBlockNearby(Level level, BlockPos center, Block target, int radius) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    mutable.setWithOffset(center, dx, dy, dz);
                    if (level.getBlockState(mutable).is(target)) return true;
                }
            }
        }
        return false;
    }

    // ─── Раздача энергии ──────────────────────────────────────────────────────

    private void distributeEnergy(Level level, BlockPos pos) {
        if (energyStorage.getEnergyStored() == 0) return;

        for (Direction dir : Direction.values()) {
            if (energyStorage.getEnergyStored() == 0) break;

            IEnergyStorage neighbor = level.getCapability(
                    Capabilities.EnergyStorage.BLOCK,
                    pos.relative(dir),
                    dir.getOpposite());

            if (neighbor != null && neighbor.canReceive()) {
                int toSend   = Math.min(energyStorage.getEnergyStored(), MAX_TRANSFER);
                int accepted = neighbor.receiveEnergy(toSend, false);
                if (accepted > 0) {
                    energyStorage.extractEnergy(accepted, false);
                }
            }
        }
    }

    // ─── API для мультиблока ──────────────────────────────────────────────────

    /**
     * Вызывается мультиблоком при формировании или разрушении.
     * @param active true — блок вошёл в мультиблок, false — вышел.
     */
    public void setMultiblockMember(boolean active) {
        if (this.isPartOfMultiblock == active) return;
        this.isPartOfMultiblock = active;
        // Форсируем пересчёт источника на следующем тике
        scanTimer = SCAN_INTERVAL;
        setChanged();
    }

    // ─── Геттеры ──────────────────────────────────────────────────────────────

    public HeatEaterEnergyStorage getEnergyStorage() { return energyStorage; }
    public HeatSource    getCurrentHeatSource() { return currentHeatSource; }
    public int           getEnergyStored()      { return energyStorage.getEnergyStored(); }
    public int           getMaxEnergy()         { return energyStorage.getMaxEnergyStored(); }
    public boolean       isPartOfMultiblock()   { return isPartOfMultiblock; }

    /** 0–100, для прогресс-бара GUI */
    public int getEnergyPercent() {
        int max = energyStorage.getMaxEnergyStored();
        return max == 0 ? 0 : energyStorage.getEnergyStored() * 100 / max;
    }

    // ─── NBT ──────────────────────────────────────────────────────────────────

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Energy",       energyStorage.getEnergyStored());
        tag.putInt("HeatSource",   currentHeatSource.ordinal());
        tag.putBoolean("InMultiblock", isPartOfMultiblock);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        setStoredEnergy(tag.getInt("Energy"));
        currentHeatSource = HeatSource.fromOrdinal(tag.getInt("HeatSource"));
        isPartOfMultiblock = tag.getBoolean("InMultiblock");
    }
    // ─── ContainerData для синхронизации с GUI ────────────────────────────────
    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> (int) (energyStorage.getEnergyStored() & 0xFFFFFFFFL);
                case 1 -> (int) ((long) energyStorage.getEnergyStored() >> 32);
                case 2 -> energyStorage.getMaxEnergyStored();
                case 3 -> currentHeatSource.ordinal();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) { /* клиент не пишет */ }

        @Override
        public int getCount() { return 4; }
    };

    public ContainerData getContainerData() { return containerData; }

    // ─── MenuProvider ─────────────────────────────────────────────────────────

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.artificial_solaris.heat_eater");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new HeatEaterMenu(containerId, playerInventory, this, containerData);
    }

}