package ru.ifmo.lab2.pokemons;

import ru.ifmo.lab2.moves.*;
import ru.ifmo.se.pokemon.Type;

public final class Raticate extends Rattata {
    public Raticate(String name, int level) {
        super(name, level);
        setStats(55, 81, 60, 50, 70, 97);
        setType(Type.NORMAL);
        setMove(new Rest(), new Crunch(), new Bite(), new SwordsDance());
    }
}
