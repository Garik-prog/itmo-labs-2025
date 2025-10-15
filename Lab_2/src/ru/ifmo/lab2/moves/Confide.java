package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class Confide extends SpecialMove {
    public Confide() {
        super(Type.NORMAL, 0, 1.0);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        super.applyOppEffects(pokemon);
        pokemon.setMod(Stat.SPECIAL_ATTACK, -1);
    }

    @Override
    protected String describe() {
        return "Confide. Понижает специальную атаку противника на 1";
    }
}