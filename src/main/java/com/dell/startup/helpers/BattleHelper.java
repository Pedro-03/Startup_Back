package com.dell.startup.helpers;

import com.dell.startup.models.Battle;
import com.dell.startup.models.Startup;

import java.util.Random;

public class BattleHelper {

    public static Startup decideWinner(Battle battle) {
        Startup s1 = battle.getStartupA();
        Startup s2 = battle.getStartupB();

        int pointsA = s1.getPoints();
        int pointsB = s2.getPoints();

        if (pointsA > pointsB) return s1;
        if (pointsB > pointsA) return s2;

        Startup chosen = new Random().nextBoolean() ? s1 : s2;
        chosen.setPoints(chosen.getPoints() + 2);
        return chosen;
    }
}
