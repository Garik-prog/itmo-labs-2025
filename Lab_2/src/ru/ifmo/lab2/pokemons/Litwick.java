package ru.ifmo.lab2.pokemons;

import ru.ifmo.lab2.moves.*;
import ru.ifmo.se.pokemon.*;

public class Litwick extends Pokemon {
    public Litwick(String name, int level) {
        super(name, level);
        setStats(210, 135, 115, 0, 0, 45);
        setType(Type.GHOST, Type.FIRE);
        setMove(new Confide(), new DoubleTeam());
    }
}
