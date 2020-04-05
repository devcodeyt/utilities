package me.devcode.utilities.player;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.devcode.utilities.rank.Rank;

@Getter
@Setter
@AllArgsConstructor

public class CustomPlayer {

    private UUID uuid;
    private String name;
    private Rank rank;
    private int coins;
    private long timeStamp;
    private boolean nick;

    public boolean hasMinRank(Rank rank) {

        return this.rank.getPriority() >= rank.getPriority();
    }

    public boolean hasRank(Rank rank) {

        return this.rank == rank;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public boolean removeCoins(int coins) {
        if (this.coins - coins < 0)
            return false;
        this.coins -= coins;
        return true;
    }


}
