package de.alberteinholz.ehtech.blocks.components.machine;

import java.util.List;

import io.github.cottonmc.component.data.DataProviderComponent;
import io.github.cottonmc.component.data.api.DataElement;
import io.github.cottonmc.component.data.api.Unit;
import io.github.cottonmc.component.data.api.UnitManager;
import io.github.cottonmc.component.data.impl.SimpleDataElement;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;

public class HeatDataComponent implements DataProviderComponent {
    public SimpleDataElement heat = new SimpleDataElement();

    //1773.15
    public HeatDataComponent(double max) {
        heat.withBar(273.15, 273.15, max, UnitManager.KELVIN);
    }

    @Override
    public void provideData(List<DataElement> data) {
        data.add(heat);
    }

    @Override
    public DataElement getElementFor(Unit unit) {
        return heat.getBarUnit().equals(unit) ? heat : null;
    }

    public void addHeat(double value) {
        setHeat(heat.getBarCurrent() + value);
    }

    public void decreaseHeat() {
        setHeat(heat.getBarCurrent() - 0.1);
    }

    private void setHeat(double value) {
        value = value > heat.getBarMaximum() ? heat.getBarMaximum() : value < heat.getBarMinimum() ? heat.getBarMinimum() : value;
        heat.withBar(heat.getBarMinimum(), value, heat.getBarMaximum(), heat.getBarUnit());
    }

    @Override
    public void fromTag(CompoundTag tag) {
        if (tag.contains("Heat", NbtType.NUMBER)) setHeat(tag.getDouble("Heat"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        if (heat.getBarCurrent() > 273.15) tag.putDouble("Heat", heat.getBarCurrent());
        return tag;
    }
}