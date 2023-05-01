package com.person98.coinflip.Commands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.person98.coinflip.CoinFlip;
import com.person98.coinflip.util.Chat;
import com.person98.coinflip.util.CoinEntry;
import com.person98.coinflip.util.CoinManager;
import com.person98.coinflip.util.InventoryManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
public class CoinFlipCommand implements CommandExecutor, TabCompleter {
    private CoinManager coins = CoinFlip.getInstance().getCoins();

    private InventoryManager menu = CoinFlip.getInstance().getMenuManager();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("coinflip")) {
            Player p = (Player)sender;
            DecimalFormat df = new DecimalFormat("#,###");
            if (args.length == 0) {
                p.openInventory(CoinFlip.getInstance().getMenu());
                return false;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("toggle")) {
                    if (!CoinFlip.getInstance().getBroadcast().inEntry(p)) {
                        CoinFlip.getInstance().getBroadcast().createEntry(p);
                        p.sendMessage(Chat.color(CoinFlip.getInstance().getBroadcast().toString(p)));
                        return false;
                    }
                    CoinFlip.getInstance().getBroadcast().removeEntry(p);
                    p.sendMessage(Chat.color(CoinFlip.getInstance().getBroadcast().toString(p)));
                    return false;
                }
                if (!args[0].equalsIgnoreCase("cancel")) {
                    p.sendMessage(Chat.color(CoinFlip.getInstance().getConfig().getString("Messages.CanceledHelp")));
                    return false;
                }
                if (this.coins.inEntry(p)) {
                    double amount = ((CoinEntry)this.coins.getEntry().get(p)).getAmount();
                    CoinFlip.getEconomy().depositPlayer(p.getName(), amount);
                    this.coins.removeEntry(p);
                    this.menu.updateInv();
                    p.sendMessage(Chat.color(CoinFlip.getInstance().getConfig().getString("Messages.ReceivedMoney").replaceAll("%amount%", df.format(amount))));
                    p.sendMessage(Chat.color(CoinFlip.getInstance().getConfig().getString("Messages.Canceled")));
                    return false;
                }
                p.sendMessage(Chat.color(CoinFlip.getInstance().getConfig().getString("Messages.NotInBet")));
            }
            if (args.length == 2)
                try {
                    double amount = Double.parseDouble(args[0]);
                    boolean side = this.coins.getBooleanConverted(args[1]);
                    if (this.coins.getEntry().size() < this.menu.getSize())
                        if (!this.coins.inEntry(p)) {
                            if (CoinFlip.getEconomy().getBalance((OfflinePlayer)p) >= amount) {
                                if (amount >= CoinFlip.getInstance().getConfig().getInt("minAmount")) {
                                    CoinFlip.getEconomy().withdrawPlayer(p.getName(), amount);
                                    p.sendMessage(Chat.color(CoinFlip.getInstance().getConfig().getString("Messages.LostMoney").replaceAll("%amount%", df.format(amount))));
                                    p.sendMessage(Chat.color(CoinFlip.getInstance().getConfig().getString("Messages.Entered").replaceAll("%amount%", df.format(amount))));
                                    if (!CoinFlip.getInstance().getStats().inEntry(p))
                                        CoinFlip.getInstance().getStats().createEntry(p);
                                    this.coins.createEntry(p, amount, side);
                                    this.menu.updateInv();
                                    return false;
                                }
                                p.sendMessage(Chat.color(CoinFlip.getInstance().getConfig().getString("Messages.NotEnoughEnterMoney")));
                            } else {
                                p.sendMessage(Chat.color(CoinFlip.getInstance().getConfig().getString("Messages.NotEnoughMoney")));
                            }
                        } else {
                            p.sendMessage(Chat.color(CoinFlip.getInstance().getConfig().getString("Messages.AlreadyInBet")));
                        }
                } catch (NumberFormatException e) {
                    p.sendMessage(Chat.color("&e&l[!] &eUsage: &6&n/coinflip <$amount> <heads/tails>"));
                }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("coinflip")) {
            if (args.length == 1) {
                List<String> subcommands = Arrays.asList("toggle", "cancel");
                List<String> completions = new ArrayList<>();

                for (String subcommand : subcommands) {
                    if (subcommand.startsWith(args[0].toLowerCase())) {
                        completions.add(subcommand);
                    }
                }
                return completions;
            } else if (args.length == 2) {
                List<String> sides = Arrays.asList("heads", "tails");
                List<String> completions = new ArrayList<>();

                for (String side : sides) {
                    if (side.startsWith(args[1].toLowerCase())) {
                        completions.add(side);
                    }
                }
                return completions;
            }
        }
        return null;
    }

}