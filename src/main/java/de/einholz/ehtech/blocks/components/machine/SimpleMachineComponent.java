package de.einholz.ehtech.blocks.components.machine;

import de.einholz.ehtech.blocks.blockentities.containers.machines.MachineBE;
import net.minecraft.util.Identifier;

public class SimpleMachineComponent implements MachineComponent {
    private short activationState = 0;
    private float efficiency = 1.0F;
    private final float min = 0.0F;
    private float cur = min;
    private final float max = 100.0F;
    private Identifier recipe = null;
    //in percent per tick * fuelSpeed
    private float speed = 1.0F;

    public SimpleMachineComponent(MachineBE<?> be) {}

    @Override
    public float getCur() {
        return cur;
    }

    @Override
    public float getMax() {
        return max;
    }

    @Override
    public float getMin() {
        return min;
    }

    @Override
    public void setCur(float cur) {
        this.cur = cur > getMax() ? getMax() : cur < getMin() ? getMin() : cur;
    }

    @Override
    public short getActivationState() {
        return activationState;
    }

    @Override
    public void setActivationState(short s) {
        activationState = s > 3 ? 0 : s;
    }
    
    @Override
    public float getEfficiency() {
        return efficiency;
    }

    @Override
    public void setEfficiency(float value) {
        efficiency = value;
    }

    @Override
    public void setRecipeById(Identifier id) {
        recipe = id;
    }

    @Override
    public boolean hasRecipe() {
        return recipe != null;
    }

    @Override
    public Identifier getRecipeId() {
        return recipe;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(float value) {
        speed = value;
    }
}
