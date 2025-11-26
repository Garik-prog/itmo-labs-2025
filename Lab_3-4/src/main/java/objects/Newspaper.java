package objects;

import java.util.Objects;

public record Newspaper(int pages, String name, String text) {
    public void read()  {
        System.out.println("Reading newspaper '" + name + "'");

        try {
            for (int i = 1; i <= pages; i++) {
                System.out.println("\tReading page[" + i + "]");
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Text { " + text.substring(0, Math.min(30, text.length())).concat("...") + " } has been read!");
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pages, text);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Newspaper other = (Newspaper) obj;
        return this.pages == other.pages &&
                Objects.equals(this.name, other.name) &&
                Objects.equals(this.text, other.text);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " name: " + name + "\n" +
                " pages: " + pages + " \n" +
                " text:\n" + text;
    }
}