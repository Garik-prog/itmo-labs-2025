package ru.ifmo.lab2.pokemons;

import ru.ifmo.lab2.moves.*;
import ru.ifmo.se.pokemon.*;

public final class Chandelure extends Lampent {
    public Chandelure(String name, int level) {
        super(name, level);
        setStats(60, 55, 90, 145, 90, 80);
        setType(Type.GHOST, Type.FIRE);
        setMove(new Confide(), new DoubleTeam(), new Astonish(), new WillOWisp());
    }
}
