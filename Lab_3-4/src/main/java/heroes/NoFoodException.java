package heroes;

public class NoFoodException extends Exception {
    public NoFoodException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "No food to eat.";
    }
}
