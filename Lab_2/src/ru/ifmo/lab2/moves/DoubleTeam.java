package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class DoubleTeam extends StatusMove {
    public DoubleTeam() {
        super(Type.NORMAL, 0, 1.0);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        super.applySelfEffects(pokemon);
        pokemon.setMod(Stat.EVASION, 1);
    }

    @Override
    protected String describe() {
        return "DoubleTeam. Создает копии самого себя (увеличивает шанс на уклонение от атак)";
    }

}
