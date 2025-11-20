import heroes.Boy;
import heroes.Carlson;
import heroes.NoFoodException;
import objects.*;


/*
    Только после того как вся каша до последней крупинки оказалась у Карлсона в тарелке, он приступил к еде, да так, что за ушами трещало.
    На несколько минут все звуки в кухне заглушило громкое чавканье, которое всегда раздается,
    когда кто-нибудь очень жадно уплетает кашу. Пока Карлсон заглатывал одну за другой булочки,
    Малыш тихо сидел и обдумывал, как ему поумнее предостеречь своего друга.
    "Может, правильнее всего просто дать Карлсону газету?
    Пусть сам все прочтет", -- решил он и после некоторого колебания придвинул газету к Карлсону.
*/
void main() {
    Food porridge = new Porridge("Вкуснейшая каша");

    porridge.addIngredient(new Grain(GrainType.WHEAT));
    porridge.addIngredient(new Grain(GrainType.BARLEY));
    porridge.addIngredient(new Grain(GrainType.OAT));

    Carlson carlson = new Carlson("Carlson", 27);
    Boy boy = new Boy("Little Boy", 11);

    try {
        carlson.startEating(porridge, 5);
        carlson.endEating(porridge);
    } catch (NoFoodException e) {
        System.out.println(e.getMessage());
    }

    Food[] brioches = new Food[]{
            new Brioche(BriocheType.CARDAMOM, new Grain(GrainType.WHEAT)),
            new Brioche(BriocheType.CHOCOLATE)
    };

    try {
        carlson.startEatingAll(4, brioches);
    } catch (NoFoodException e) {
        System.out.println(e.getMessage());
    }

    boy.think();

    try {
        carlson.endEatingAll(brioches);
    } catch (NoFoodException e) {
        System.out.println(e.getMessage());
    }

    Newspaper newspaper = new Newspaper(
            10,
            "Washington Post",
            "Lorem ipsum dolor sit amet, consectetur adipisicing elit. " +
                    "Atque consectetur consequatur consequuntur delectus dolorem ducimus error facilis fuga hic," +
                    " iusto laboriosam nostrum numquam perferendis praesentium quibusdam quis quod " +
                    "repellendus unde vero voluptates! Alias animi debitis eligendi hic" +
                    " minima nobis quia quis quisquam quos sequi! Ducimus iste iure quam quisquam reprehenderit."
    );

    boy.getNewspaper(newspaper);
    boy.think("thought: \"Maybe it would be better to just give Carlson the newspaper?\"\n" +
            " - The boy hesitated a bit, but decided to let him read it himself.");

    try {
        carlson.endEatingAll(brioches);
    } catch (NoFoodException e) {
        System.out.println(e.getMessage());
    }

    boy.giveNewspaper(carlson);
    carlson.readNewspaper();
}