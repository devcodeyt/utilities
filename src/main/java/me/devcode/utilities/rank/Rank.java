package me.devcode.utilities.rank;

import org.bukkit.ChatColor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Rank {

    ADMIN("§4Admin §7┃ §4", "§4Admin §7┃ §4", "01Admin", "Admin", ChatColor.DARK_RED, 10),
    DEVELOPER("§bDev §7┃ §b", "§bDeveloper §7┃ §b", "02Dev", "Developer", ChatColor.AQUA, 9),
    MODERATOR("§cMod §7┃ §c", "§cModerator §7┃ §c", "03Mod", "Moderator", ChatColor.RED, 8),
    CONTENT("§cContent §7┃ §c", "§cContent §7┃ §c", "04Content", "Content", ChatColor.RED, 7),
    SUPPORTER("§9Sup §7┃ §9", "§9Supporter §7┃ §9", "05Supporter", "Supporter", ChatColor.BLUE, 6),
    BUILDER("§aBuilder §7┃ §a", "§aBuilder §7┃ §a", "06Builder", "Builder", ChatColor.GREEN, 5),
    YOUTUBER("§5", "§5", "08Youtuber", "YouTuber", ChatColor.DARK_PURPLE, 4),
    PREMIUMPLUS("§e", "§e", "09PremiumPlus", "PremiumPlus", ChatColor.YELLOW, 3),
    PREMIUM("§6", "§6", "10Premium", "Premium", ChatColor.GOLD, 2),
    PLAYER("§3", "§3", "11Player", "Spieler", ChatColor.DARK_AQUA, 1);

    private String tabPrefix;
    private String chatPrefix;
    private String teamName;
    private String name;
    private ChatColor color;
    private int priority;

    public static Rank getRankByName(String name) {
        Rank[] ranks = values();
        int size = ranks.length;

        for(int i = 0; i < size; i++) {
            Rank index = ranks[i];
            if (index.getName().equalsIgnoreCase(name)) {
                return index;
            }
        }

        return Rank.PLAYER;
    }

}
