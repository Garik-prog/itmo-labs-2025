package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class Crunch extends PhysicalMove {
    public Crunch() {
        super(Type.DARK, 80, 1.0);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        super.applyOppEffects(pokemon);
        if (Math.random() <= 0.2) {
            pokemon.setMod(Stat.DEFENSE, -1);
        }
    }

    @Override
    protected String describe() {
        return "Crunch. Может понизить защиту цели на 1";
    }
}
