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


    public void setEnergyStored(int energy) {
        this.energy = energy;
        this.dirty = true;
    }
}
