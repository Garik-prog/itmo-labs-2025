package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class SwordsDance extends StatusMove {
    public SwordsDance() {
        super(Type.NORMAL, 90, 1.0);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        pokemon.setMod(Stat.ATTACK, 2);
    }

    @Override
    protected String describe() {
        return "Swords Dance. Может повысить свою атаку на 2";
    }
}
