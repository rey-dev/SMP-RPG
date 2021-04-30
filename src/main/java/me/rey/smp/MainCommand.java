package me.rey.smp;

import me.rey.utils.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !label.equalsIgnoreCase("cigive")) {
            return true;
        }

        if (!sender.isOp()) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(Text.format("SMP", "Usage: &e/cigive <id|help>"));
            return true;
        }

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(Text.format("SMP", "Your inventory is full!"));
            return true;
        }

        Optional<CustomItem> customItem = SMPRPG.getInstance().getCustomItemFromId(args[0].toUpperCase());
        if (customItem.isPresent()) {
            player.sendMessage(Text.format("SMP", "Success!"));
            player.getInventory().addItem(customItem.get().get());
        } else {
            sendHelp(sender);
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String id : SMPRPG.getInstance().getRegisteredCustomItems().keySet()) {
            stringBuilder.append(", ").append(id);
        }
        String list = stringBuilder.toString().trim().replaceFirst(", ", "");
        sender.sendMessage(Text.format("SMP", "Valid IDs: &e" + list));
    }

}
