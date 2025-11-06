package objects;

import java.util.Objects;

public record Newspaper(int pages, String name, String text) {
    public void read() {
        System.out.println("Reading newspaper '" + name + "'");

        try {
            for (int i = 1; i <= pages; i++) {
                System.out.println("\tReading page[" + i + "]");
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Text { " + text.substring(0, 30).concat("...") + " } has been read!");
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pages, text);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Newspaper other)) return false;
        if (obj.hashCode() != hashCode()) return false;
        return this.name.equals(other.name) && this.pages == other.pages && this.text.equals(other.text);
    }



    @Override
    public String toString() {
        return getClass() + " name: " + name + "\n pages: " + pages + " \n text:\n " + text;
    }
}
