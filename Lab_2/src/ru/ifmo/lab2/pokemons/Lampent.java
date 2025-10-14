package ru.ifmo.lab2.pokemons;

import ru.ifmo.lab2.moves.*;
import ru.ifmo.se.pokemon.*;

public class Lampent extends Litwick {
    public Lampent(String name, int level) {
        super(name, level);
        setStats(60, 40, 60, 95, 60, 55);
        setType(Type.GHOST, Type.FIRE);
        setMove(new Confide(), new DoubleTeam(), new Astonish());
    }
}
