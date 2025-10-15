package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class ZenHeadbutt extends PhysicalMove {
    public ZenHeadbutt() {
        super(Type.PSYCHIC, 80, 0.9);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        if (Math.random() <= 0.2) {
            Effect.flinch(pokemon);
        }
    }

    @Override
    protected String describe() {
        return "ZenHeadbutt. Может заставить противника вздрогнуть";
    }
}
