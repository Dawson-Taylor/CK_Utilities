package com.CodeKanic.ckutilities.common.items.utils;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {
    boolean dirty = false;

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        dirty = true;
        return super.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        dirty = true;
        return super.extractEnergy(maxExtract, simulate);
    }

    public int extractEnergyInternal(int maxExtract, boolean simulate) {
        int before = this.maxExtract;
        this.maxExtract = Integer.MAX_VALUE;

        int toReturn = this.extractEnergy(maxExtract, simulate);

        this.maxExtract = before;
        return toReturn;
    }

    public int receiveEnergyInternal(int maxReceive, boolean simulate) {
        int before = this.maxReceive;
        this.maxReceive = Integer.MAX_VALUE;

        int toReturn = this.receiveEnergy(maxReceive, simulate);

        this.maxReceive = before;
        return toReturn;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void clearDirty() {
        dirty = false;
    }

    public void readFromNBT(CompoundTag compound) {
        if (compound.contains("Energy"))
            this.setEnergyStored(compound.getInt("Energy"));
    }

    public void writeToNBT(CompoundTag compound) {
        compound.putInt("Energy", this.getEnergyStored());
    }

    public void setEnergyStored(int energy) {
        this.energy = energy;
        this.dirty = true;
    }
}
