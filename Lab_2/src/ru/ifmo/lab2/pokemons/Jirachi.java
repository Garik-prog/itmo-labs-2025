package ru.ifmo.lab2.pokemons;

import ru.ifmo.lab2.moves.*;
import ru.ifmo.se.pokemon.*;

public final class Jirachi extends Pokemon {
    public Jirachi(String name, int level) {
        super(name, level);
        setStats(100, 100, 100, 100, 100, 100);
        setType(Type.STEEL, Type.PSYCHIC);
        setMove(new Thunderbolt(), new Facade(), new ZenHeadbutt(), new DreamEater());
    }
}
