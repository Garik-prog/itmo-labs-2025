package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class WillOWisp extends StatusMove {
    public WillOWisp() {
        super(Type.FIRE, 0, 0.85);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Effect.burn(pokemon);
    }

    @Override
    protected String describe() {
        return "Will-O-Wisp. Обжигает противника";
    }
}
