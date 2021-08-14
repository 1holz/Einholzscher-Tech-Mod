package de.alberteinholz.ehtech.blocks.guis.guis.machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import de.alberteinholz.ehmooshroom.container.component.data.ConfigDataComponent.ConfigBehavior;
import de.alberteinholz.ehmooshroom.container.component.energy.AdvancedCapacitorComponent;
import de.alberteinholz.ehmooshroom.container.component.item.AdvancedInventoryComponent;
import de.alberteinholz.ehmooshroom.registry.RegistryHelper;
import de.alberteinholz.ehtech.TechMod;
import de.alberteinholz.ehtech.blocks.blockentities.containers.machines.MachineBlockEntity;
import de.alberteinholz.ehtech.blocks.components.machine.MachineDataComponent;
import de.alberteinholz.ehtech.blocks.guis.guis.ContainerGui;
import de.alberteinholz.ehtech.blocks.guis.widgets.Button;
import io.github.cottonmc.component.UniversalComponents;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import nerdhub.cardinal.components.api.component.BlockComponentProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class MachineConfigGui extends ContainerGui {
    private final Supplier<ConfigEntry> CONFIG_SUPPLIER = ConfigEntry::new;
    protected WLabel down, up, north, south, west, east;
    protected WListPanel<Identifier, ConfigEntry> configPanel;
    protected List<Identifier> configIds;
    protected BiConsumer<Identifier, ConfigEntry> configBuilder;
    /*FIXME: check this class for content that can be removed
    protected WLabel item;
    protected WLabel fluid;
    protected WLabel power;
    */
    protected Map<Integer, ConfigButton> configButtons;
    protected Button cancel;

    protected MachineConfigGui(ScreenHandlerType<SyncedGuiDescription> type, int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
        super(type, syncId, playerInv, buf);
    }

    @SuppressWarnings("unchecked")
    public static MachineConfigGui init(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        return init(new MachineConfigGui((ScreenHandlerType<SyncedGuiDescription>) RegistryHelper.getEntry(TechMod.HELPER.makeId("machine_config")).screenHandlerType, syncId, playerInventory, buf));
    }

    public static MachineConfigGui init(MachineConfigGui gui) {
        gui.down = new WLabel(new TranslatableText("block.ehtech.machine_config.down"));
        gui.up = new WLabel(new TranslatableText("block.ehtech.machine_config.up"));
        gui.north = new WLabel(new TranslatableText("block.ehtech.machine_config.north"));
        gui.south = new WLabel(new TranslatableText("block.ehtech.machine_config.south"));
        gui.west = new WLabel(new TranslatableText("block.ehtech.machine_config.west"));
        gui.east = new WLabel(new TranslatableText("block.ehtech.machine_config.east"));
        gui.configIds = gui.getConfigComp().getIds();
        gui.configBuilder = (id, entry) -> entry.build(id);
        //XXX: WHY DOES ConfigEntry::gui.new NOT WORK INSTEAD OF gui.CONFIG_SUPPLIER??? THIS REALLY SHOULD WORK!!!!!
        gui.configPanel = new WListPanel<>(gui.configIds, gui.CONFIG_SUPPLIER, gui.configBuilder);
        gui.configButtons = new HashMap<Integer, ConfigButton>();
        gui.cancel = (Button) new Button().setLabel(new LiteralText("X"));
        return (MachineConfigGui) ContainerGui.init(gui);
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        /*
        item = new WLabel(new TranslatableText("block.ehtech.machine_config.item"));
        fluid = new WLabel(new TranslatableText("block.ehtech.machine_config.fluid"));
        power = new WLabel(new TranslatableText("block.ehtech.machine_config.power"));
        for (Identifier id : ConfigType.values()) for (Direction dir : Direction.values()) for (ConfigBehavior behavior : ConfigBehavior.values()) {
            ConfigButton button = new ConfigButton(id, dir, behavior);
            buttonIds.add(button);
            button.id = buttonIds.indexOf(button);
            configButtons.put(button.id, button);
            if (getConfigComp().getConfig(id, behavior, dir) == null) button.setEnabled(false);
            else button.setOnClick(getDefaultOnButtonClick(button));
        }
        */
        cancel.tooltips.add("tooltip.ehtech.cancel_button");
        buttonIds.add(cancel);
        cancel.setOnClick(getDefaultOnButtonClick(cancel));
    }

    @Override
    protected void drawDefault() {
        super.drawDefault();
        ((WGridPanel) rootPanel).add(down, 2, 1, 1, 1);
        ((WGridPanel) rootPanel).add(up, 3, 1, 1, 1);
        ((WGridPanel) rootPanel).add(north, 4, 1, 1, 1);
        ((WGridPanel) rootPanel).add(south, 5, 1, 1, 1);
        ((WGridPanel) rootPanel).add(west, 6, 1, 1, 1);
        ((WGridPanel) rootPanel).add(east, 7, 1, 1, 1);
        /*
        ((WGridPanel) rootPanel).add(item, 0, 4, 4, 2);
        ((WGridPanel) rootPanel).add(fluid, 0, 6, 4, 2);
        ((WGridPanel) rootPanel).add(power, 0, 8, 4, 2);
        configButtons.forEach((id, button) -> {
            ((WGridPanel) rootPanel).add(button, button.dir.ordinal() * 2 + 4 + (int) Math.floor((double) button.behavoir.ordinal() / 2.0), button.TYPE.ordinal() * 2 + 4 + (button.behavoir.ordinal() + 1) % 2);
        });
        */
        ((WGridPanel) rootPanel).add(configPanel, 0, 2, 9, 5);
        ((WGridPanel) rootPanel).add(cancel, 9, 5, 1, 1);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (configButtons.containsKey(id)) {
            ConfigButton button = configButtons.get(id);
            if (button.isEnabled()) {
                getConfigComp().changeConfig(button.id, button.behavior, button.dir);
                return true;
            }
        } else if (id == buttonIds.indexOf(cancel)) {
            if (!world.isClient) player.openHandledScreen((MachineBlockEntity) world.getBlockEntity(pos));
            return true;
        }
        return false;
    }
    
    protected MachineDataComponent getMachineDataComp() {
        return (MachineDataComponent) getDataComp().getComp(TechMod.HELPER.makeId("data_machine"));
    }

    protected AdvancedCapacitorComponent getCapacitorComp() {
        return (AdvancedCapacitorComponent) BlockComponentProvider.get(world.getBlockState(pos)).getComponent(world, pos, UniversalComponents.CAPACITOR_COMPONENT, null);
    }

    protected AdvancedInventoryComponent getMachineInvComp() {
        return (AdvancedInventoryComponent) getInvComp().getComp(TechMod.HELPER.makeId("inventory_machine"));
    }

    protected class ConfigEntry extends WGridPanel {
        public Identifier id;
        //TODO: is this needed?
        public List<ConfigButton> buttons = new ArrayList<>();

        public ConfigEntry() {
            grid = 9;
        }

        public void build(Identifier id) {
            this.id = id;
            add(new WLabel(new TranslatableText("block." + id.getNamespace() + ".machine_config." + id.getPath())), 0, 0, 4, 2);
            for (Direction dir : Direction.values()) for (ConfigBehavior behavior : ConfigBehavior.values()) {
                ConfigButton button = new ConfigButton(id, dir, behavior);
                buttonIds.add(button);
                //FIXME: delete: button.id = buttonIds.indexOf(button);
                configButtons.put(buttonIds.indexOf(button), button);
                if (!(getConfigComp().isAvailable(id, behavior, dir))) button.setEnabled(false);
                else button.setOnClick(getDefaultOnButtonClick(button));
                add(button, button.dir.ordinal() * 2 + 4 + (int) Math.floor((double) button.behavior.ordinal() / 2.0), (button.behavior.ordinal() + 1) % 2);
            }
            
        }
    }

    protected class ConfigButton extends Button {
        public final Identifier id;
        public final Direction dir;
        public final ConfigBehavior behavior;

        @SuppressWarnings("unchecked")
        public ConfigButton(Identifier id, Direction dir, ConfigBehavior behavior) {
            this.id = id;
            this.dir = dir;
            this.behavior = behavior;
            setSize(8, 8);
            resizeability = false;
            if (!isEnabled()) return;
            Supplier<?>[] suppliers = {
                () -> {
                    return behavior.name().toLowerCase();
                }, () -> {
                    return dir.getName();
                }, () -> {
                    return String.valueOf(getConfigComp().allowsConfig(id, behavior, dir));
                }, () -> {
                    return id.toString();
                }
            };
            advancedTooltips.put("tooltip.ehtech.config_button", (Supplier<Object>[]) suppliers);
        }

        @Override
        public void draw(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
            if (isEnabled()) withTint(getConfigComp().allowsConfig(id, behavior, dir) ? 0xFFFFFF00 : 0xFFFF0000);
            else advancedTooltips.remove("tooltip.ehtech.config_button");
            super.draw(matrices, x, y, mouseX, mouseY);
        }
    }
}