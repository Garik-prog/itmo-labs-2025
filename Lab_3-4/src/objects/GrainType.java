package objects;

public enum GrainType {
    OAT,
    BUCKWHEAT,
    RICE,
    BARLEY,
    WHEAT;

    @Override
    public String toString() {
        return switch (this) {
            case OAT -> "oat";
            case BUCKWHEAT -> "buckwheat";
            case RICE -> "rice";
            case BARLEY -> "barley";
            case WHEAT -> "wheat";
        };
    }
}