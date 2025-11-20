package objects;

public enum BriocheType {
    CINNAMON,
    VANILLA,
    CHOCOLATE,
    CARDAMOM,
    GOLDEN_CRUST,
    SWEDISH,
    HONEY_GLAZED,
    CREAM_FILLED;

    @Override
    public String toString() {
        return switch (this) {
            case CINNAMON -> "Cinnamon";
            case VANILLA -> "Vanilla";
            case CHOCOLATE -> "Chocolate";
            case CARDAMOM -> "Cardamom";
            case GOLDEN_CRUST -> "Golden Crust";
            case SWEDISH -> "Swedish";
            case HONEY_GLAZED -> "Honey Glazed";
            case CREAM_FILLED -> "Cream Filled";
        };
    }
}