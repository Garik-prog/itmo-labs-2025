package ru.ifmo.lab2.pokemons;

import ru.ifmo.lab2.moves.*;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Rattata extends Pokemon {
    public Rattata(String name, int level) {
        super(name, level);
        setStats(30, 56, 35, 25, 35, 72);
        setType(Type.NORMAL);
        setMove(new Rest(), new Crunch(), new Bite());
    }
}
