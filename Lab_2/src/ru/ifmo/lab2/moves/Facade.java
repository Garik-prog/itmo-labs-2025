package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class Facade extends PhysicalMove {
    public Facade() {
        super(Type.DARK, 70, 1.0);
    }

    @Override
    protected void applyOppDamage(Pokemon def, double damage) {
        Status userStatus = def.getCondition();

        if (userStatus == Status.BURN || userStatus == Status.POISON || userStatus == Status.PARALYZE) {
            super.applyOppDamage(def, damage * 2);
        } else {
            super.applyOppDamage(def, damage);
        }
    }

    @Override
    protected String describe() {
        return "Facade. Двойной урон, если противник отравлен, парализован или обожжён";
    }

}
