package de.einholz.ehtech.blocks.components.machine;

import java.util.Optional;

import de.einholz.ehmooshroom.container.component.util.BarComponent;
import de.einholz.ehtech.TechMod;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public interface MachineComponent extends BarComponent {
    public static final Identifier MACHINE_ID = TechMod.HELPER.makeId("machine");
    public static final ComponentKey<MachineComponent> MACHINE = ComponentRegistry.getOrCreate(MACHINE_ID, MachineComponent.class);
    //TODO: use cache!!!
    public static final BlockApiLookup<MachineComponent, Void> MACHINE_LOOKUP = BlockApiLookup.get(MACHINE_ID, MachineComponent.class, Void.class);

    //ALWAYS_ON = 0
    //REDSTONE_ON = 1
    //REDSTONE_OFF = 2
    //ALWAYS_OFF = 3
    short getActivationState();
    void setActivationState(short s);

    default void nextActivationState() {
        setActivationState((short) ((getActivationState() + 1) % 4));
    }
    
    float getEfficiency();
    void setEfficiency(float value);

    default void addProgress(float value) {
        setCur(getCur() + value);
    }

    default void resetProgress() {
        setCur(getMin());
    }

    void setRecipeById(Identifier id);
    boolean hasRecipe();
    Identifier getRecipeId();

    default void resetRecipe() {
        setRecipeById(null);
    }

    //XXX force AdvancedRecipe?
    default void setRecipe(Recipe<Inventory> recipe) {
        if (hasRecipe()) setRecipeById(getRecipeId());
        else resetRecipe();
    }

    @SuppressWarnings("unchecked")
    default Recipe<Inventory> getRecipe(World world) {
        Optional<Recipe<Inventory>> optional = (Optional<Recipe<Inventory>>) world.getRecipeManager().get(getRecipeId());
        return optional.isPresent() ? optional.get() : null;
    }

    float getSpeed();
    void setSpeed(float value);

    @Override
    default Identifier getId() {
        return MACHINE_ID;
    }

    @Override
    default float getBalance() {
        return 0.0F;
    }

    @Override
    default void setBalance(float bal) {}

    @Override
    default void writeNbt(NbtCompound nbt) {
        BarComponent.super.writeNbt(nbt);
        if (getActivationState() != 0) nbt.putShort("Activation_State", getActivationState());
        if (getEfficiency() != 1.0F) nbt.putFloat("Efficiency", getEfficiency());
        if (hasRecipe()) nbt.putString("Recipe", getRecipeId().toString());
        if (getSpeed() != 1.0F) nbt.putFloat("Speed", getSpeed());
    }

    @Override
    default void readNbt(NbtCompound nbt) {
        BarComponent.super.readNbt(nbt);
        if (nbt.contains("Activation_State", NbtType.NUMBER)) setActivationState(nbt.getShort("Activation_State"));
        if (nbt.contains("Efficiency", NbtType.NUMBER)) setEfficiency(nbt.getFloat("Efficiency"));
        if (nbt.contains("Recipe", NbtType.STRING)) setRecipeById(new Identifier(nbt.getString("Recipe")));
        if (nbt.contains("Speed", NbtType.NUMBER)) setSpeed(nbt.getFloat("Speed"));
    }
}
