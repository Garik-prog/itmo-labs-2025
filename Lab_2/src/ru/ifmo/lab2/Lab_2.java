package ru.ifmo.lab2;

import ru.ifmo.lab2.pokemons.*;
import ru.ifmo.se.pokemon.*;


public class Lab_2 {
    public static void main(String[] args) {
        Battle b = new Battle();

        b.addAlly(new Raticate( "->", 20));
        b.addAlly(new Rattata(  "->", 1));
        b.addAlly(new Jirachi(  "->", 1));

        b.addFoe(new Litwick(   "->", 1));
        b.addFoe(new Lampent(   "->", 41));
        b.addFoe(new Chandelure("->", 41));

        b.go();
    }
}