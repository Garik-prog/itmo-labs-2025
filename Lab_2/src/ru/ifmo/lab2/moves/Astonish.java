package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class Astonish extends PhysicalMove {

    public Astonish() {
        super(Type.GHOST, 30, 1.0);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        if (Math.random() <= 0.3) {
            Effect.flinch(pokemon);
        }
    }

    @Override
    protected String describe() {
        return "Astonish. Может заставить противника вздрогнуть";
    }
}
