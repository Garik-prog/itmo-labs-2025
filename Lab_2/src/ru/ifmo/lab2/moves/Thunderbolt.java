package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class Thunderbolt extends SpecialMove {
    public Thunderbolt() {
        super(Type.ELECTRIC, 90, 1.0);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        if (Math.random() <= 0.1) {
            Effect.paralyze(pokemon);
        }
    }

    @Override
    protected String describe() {
        return "Thunderbolt. Может вызвать паралич";
    }
}
