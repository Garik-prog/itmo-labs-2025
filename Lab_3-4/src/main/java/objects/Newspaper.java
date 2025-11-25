package objects;

import java.util.Objects;

public record Newspaper(int pages, String name, String text) {
    public void read() {
        System.out.println(STR."Reading newspaper '\{name}'");

        try {
            for (int i = 1; i <= pages; i++) {
                System.out.println(STR."\tReading page[\{i}]");
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(STR."Text { \{text.substring(0, 30).concat("...")} } has been read!");
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pages, text);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Newspaper(int pages1, String name1, String text1))) return false;
        return this.name.equals(name1) && this.pages == pages1 && this.text.equals(text1);
    }


    @Override
    public String toString() {
        return STR."""
 \{getClass()} name: \{name}
 pages: \{pages}\s
 text:
 \{text}""";
    }
}
