package ru.ifmo.lab2.moves;

import ru.ifmo.se.pokemon.*;

public final class DreamEater extends SpecialMove {
    public DreamEater() {
        super(Type.PSYCHIC, 100, 1.0);
    }

    @Override
    protected boolean checkAccuracy(Pokemon attacked, Pokemon defensed) {
        super.checkAccuracy(attacked, defensed);
        return defensed.getCondition() == Status.SLEEP;
    }

    @Override
    protected String describe() {
        return "DreamEater. Атака пройдёт, только если противник спит";
    }
}
