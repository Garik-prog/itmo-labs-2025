package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class Bite extends PhysicalMove {
    public Bite() {
        super(Type.DARK, 60, 1.0);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        if (Math.random() <= 0.3) {
            Effect.flinch(pokemon);
        }
    }

    @Override
    protected String describe() {
        return "Bite. Может заставить противника вздрогнуть";
    }
}
