package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class Rest extends StatusMove {
    public Rest() {
        super(Type.PSYCHIC, 0, 0.1);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        // Создаем эффект сна, который длится 2 хода
        Effect sleepEffect = new Effect().turns(2).condition(Status.SLEEP);
        pokemon.addEffect(sleepEffect);

        pokemon.setMod(Stat.HP, - (int) pokemon.getStat(Stat.HP));
    }

    @Override
    protected String describe() {
        return "Rest. Засыпает на 2 хода, полностью восстанавливает здоровье";
    }
}
