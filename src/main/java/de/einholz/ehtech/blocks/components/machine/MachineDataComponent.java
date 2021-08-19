package de.einholz.ehtech.blocks.components.machine;

import java.util.List;
import java.util.Optional;

import com.google.gson.JsonParseException;

import de.alberteinholz.ehmooshroom.MooshroomLib;
import io.github.cottonmc.component.data.DataProviderComponent;
import io.github.cottonmc.component.data.api.DataElement;
import io.github.cottonmc.component.data.api.Unit;
import io.github.cottonmc.component.data.api.UnitManager;
import io.github.cottonmc.component.data.impl.SimpleDataElement;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class MachineDataComponent implements DataProviderComponent {
    private SimpleDataElement activationState = new SimpleDataElement(ActivationState.values()[0].name());
    private SimpleDataElement efficiency = new SimpleDataElement(String.valueOf(1.0));
    public SimpleDataElement progress = new SimpleDataElement().withBar(0.0, 0.0, 100.0, UnitManager.PERCENT);
    private SimpleDataElement recipe = new SimpleDataElement((Text) null);
    //in percent per tick * fuelSpeed
    private SimpleDataElement speed = new SimpleDataElement(String.valueOf(1.0));

    @Override
    public void provideData(List<DataElement> data) {
        data.add(activationState);
        data.add(efficiency);
        data.add(progress);
        data.add(recipe);
        data.add(speed);
    }

    @Override
    public DataElement getElementFor(Unit unit) {
        return progress.getBarUnit().equals(unit) ? progress : null;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        if (!tag.contains("MachineData", NbtType.COMPOUND)) return;
        CompoundTag fromTag = tag.getCompound("MachineData");
        if (fromTag.contains("ActivationState", NbtType.STRING)) setActivationState(fromTag.getString("ActivationState"));
        if (fromTag.contains("Efficiency", NbtType.NUMBER)) setEfficiency(fromTag.getDouble("Efficiency"));
        if (fromTag.contains("ProgressCurrent", NbtType.NUMBER)) setProgress(fromTag.getDouble("ProgressCurrent"));
        if (fromTag.contains("Recipe", NbtType.STRING)) setRecipeById(new Identifier(fromTag.getString("Recipe")));
        if (fromTag.contains("Speed", NbtType.NUMBER)) setSpeed(fromTag.getDouble("Speed"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag toTag = new CompoundTag();
        if (getActivationState() != ActivationState.values()[0]) toTag.putString("ActivationState", String.valueOf(getActivationState()));
        if (getEfficiency() != 1.0) toTag.putDouble("Efficiency", getEfficiency());
        if (progress.getBarCurrent() > 0.0) toTag.putDouble("ProgressCurrent", progress.getBarCurrent());
        if (recipe.hasLabel()) toTag.putString("Recipe", recipe.getLabel().asString());
        if (getSpeed() != 1.0) toTag.putDouble("Speed", getSpeed());
        if (!toTag.isEmpty()) tag.put("MachineData", toTag);
        return tag;
    }

    public ActivationState getActivationState() {
        return ActivationState.valueOf(activationState.getLabel().getString());
    }

    private void setActivationState(String str) {
        if (!ActivationState.isValid(str)) MooshroomLib.LOGGER.smallBug(new JsonParseException("activation state " + str + "isn't valid! Setting the activation state will be skipped."));
        activationState.withLabel(str);
    }

    public void nextActivationState() {
        setActivationState(String.valueOf(getActivationState().next(1)));
    }
    
    public double getEfficiency() {
        return Double.valueOf(efficiency.getLabel().getString());
    }

    public void setEfficiency(double value) {
        efficiency.withLabel(String.valueOf(value));
    }

    public void addProgress(double value) {
        setProgress(progress.getBarCurrent() + value);
    }

    public void resetProgress() {
        setProgress(progress.getBarMinimum());
    }

    private void setProgress(double value) {
        value = value > progress.getBarMaximum() ? progress.getBarMaximum() : value < progress.getBarMinimum() ? progress.getBarMinimum() : value;
        progress.withBar(progress.getBarMinimum(), value, progress.getBarMaximum(), progress.getBarUnit());
    }

    public void setRecipe(Recipe<Inventory> recipe) {
        if (recipe != null) setRecipeById(recipe.getId());
        else resetRecipe();
    }

    private void setRecipeById(Identifier id) {
        recipe.withLabel(id.toString());
    }

    @SuppressWarnings("unchecked")
    public Recipe<Inventory> getRecipe(World world) {
        Optional<Recipe<Inventory>> optional = (Optional<Recipe<Inventory>>) world.getRecipeManager().get(new Identifier(recipe.getLabel().asString()));
        return optional.isPresent() ? optional.get() : null;
    }

    public void resetRecipe() {
        recipe.withLabel((Text) null);
    }

    public double getSpeed() {
        return Double.valueOf(speed.getLabel().getString());
    }

    public void setSpeed(double value) {
        speed.withLabel(String.valueOf(value));
    }
    
    public static enum ActivationState {
        ALWAYS_ON,
        REDSTONE_ON,
        REDSTONE_OFF,
        ALWAYS_OFF;

        public ActivationState next(int amount) {
            return values()[(ordinal() + amount) % values().length];
        }

        public static boolean isValid(String str) {
            try {
                toActivationState(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        //XXX: is this needed?
        @Deprecated
        public static String toString(ActivationState state) {
            return state.name();
        }

        public static ActivationState toActivationState(String string) {
            return valueOf(string);
        }
    }
}
