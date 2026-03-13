package manager;

import models.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Менеджер для работы с файлами. Отвечает за загрузку коллекции из XML-файла
 * и сохранение коллекции в XML-файл.
 * Использует Scanner для чтения и BufferedOutputStream для записи.
 */
public class FileManager {

    /** Имя файла для загрузки/сохранения */
    private final String filename;

    /** Формат даты для XML (ISO 8601 с часовым поясом) */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    /**
     * Создаёт FileManager с заданным именем файла.
     *
     * @param filename имя файла
     */
    public FileManager(String filename) {
        this.filename = filename;
    }

    /**
     * Загружает коллекцию из XML-файла.
     * Использует простой парсинг строк (не XML-парсер).
     *
     * @return загруженная коллекция LinkedHashMap
     * @throws IOException если произошла ошибка чтения или отсутствуют обязательные поля
     */
    public LinkedHashMap<String, Flat> load() throws IOException {
        LinkedHashMap<String, Flat> map = new LinkedHashMap<>();
        File file = new File(filename);
        System.out.println("Загрузка из файла: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("Файл не существует, возвращаем пустую коллекцию");
            return map;
        }

        StringBuilder content = new StringBuilder();
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
        }

        String xml = content.toString();
        String[] flats = xml.split("</flat>");

        for (String flatPart : flats) {
            if (!flatPart.contains("<flat") || !flatPart.contains("key=\"")) continue;

            String key = extractSimple(flatPart, "key=\"", "\"");
            if (key == null) continue;

            Flat flat = new Flat();

            // id
            String idStr = extractSimple(flatPart, "<id>", "</id>");
            if (idStr != null) flat.setId(Integer.parseInt(idStr));

            // name
            flat.setName(extractSimple(flatPart, "<name>", "</name>"));

            // coordinates
            Coordinates coords = new Coordinates();
            String xStr = extractSimple(flatPart, "<x>", "</x>");
            String yStr = extractSimple(flatPart, "<y>", "</y>");
            if (xStr != null) coords.setX(Integer.parseInt(xStr));
            if (yStr != null) coords.setY(Long.parseLong(yStr));
            flat.setCoordinates(coords);

            // creationDate
            String dateStr = extractSimple(flatPart, "<creationDate>", "</creationDate>");
            if (dateStr != null) {
                try {
                    flat.setCreationDate(dateFormat.parse(dateStr));
                } catch (Exception e) {
                    flat.setCreationDate(new Date());
                }
            }

            // area — теперь обязательное поле
            String areaStr = extractSimple(flatPart, "<area>", "</area>");
            if (areaStr == null) {
                throw new IOException("Отсутствует обязательное поле area в XML для элемента с ключом " + key);
            }
            flat.setArea(Long.parseLong(areaStr));

            // numberOfRooms
            String roomsStr = extractSimple(flatPart, "<numberOfRooms>", "</numberOfRooms>");
            if (roomsStr != null) flat.setNumberOfRooms(Integer.parseInt(roomsStr));

            // furnish
            String furnishStr = extractSimple(flatPart, "<furnish>", "</furnish>");
            if (furnishStr != null) flat.setFurnish(Furnish.valueOf(furnishStr));

            // view
            String viewStr = extractSimple(flatPart, "<view>", "</view>");
            if (viewStr != null) flat.setView(View.valueOf(viewStr));

            // transport
            String transportStr = extractSimple(flatPart, "<transport>", "</transport>");
            if (transportStr != null) flat.setTransport(Transport.valueOf(transportStr));

            // house
            House house = new House();
            house.setName(extractSimple(flatPart, "<houseName>", "</houseName>"));
            String yearStr = extractSimple(flatPart, "<houseYear>", "</houseYear>");
            if (yearStr != null) house.setYear(Integer.parseInt(yearStr));
            String flatsStr = extractSimple(flatPart, "<houseFlatsOnFloor>", "</houseFlatsOnFloor>");
            if (flatsStr != null) house.setNumberOfFlatsOnFloor(Integer.parseInt(flatsStr));
            flat.setHouse(house);

            map.put(key, flat);
        }

        System.out.println("Загружено квартир: " + map.size());
        return map;
    }

    /**
     * Вспомогательный метод для извлечения содержимого между открывающим и закрывающим тегами.
     *
     * @param text текст для поиска
     * @param open открывающий тег (включая возможные кавычки для атрибутов)
     * @param close закрывающий тег
     * @return извлечённая строка или null, если теги не найдены
     */
    private String extractSimple(String text, String open, String close) {
        int start = text.indexOf(open);
        if (start == -1) return null;
        start += open.length();
        int end = text.indexOf(close, start);
        if (end == -1) return null;
        return text.substring(start, end).trim();
    }

    /**
     * Сохраняет коллекцию в XML-файл.
     * Использует BufferedOutputStream для записи.
     *
     * @param collection коллекция для сохранения
     * @throws IOException если произошла ошибка записи
     */
    public void save(LinkedHashMap<String, Flat> collection) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<flats>\n");

        for (Map.Entry<String, Flat> entry : collection.entrySet()) {
            Flat f = entry.getValue();
            sb.append("    <flat key=\"").append(entry.getKey()).append("\">\n");
            sb.append("        <id>").append(f.getId()).append("</id>\n");
            sb.append("        <name>").append(f.getName()).append("</name>\n");
            sb.append("        <x>").append(f.getCoordinates().getX()).append("</x>\n");
            sb.append("        <y>").append(f.getCoordinates().getY()).append("</y>\n");
            sb.append("        <creationDate>").append(dateFormat.format(f.getCreationDate())).append("</creationDate>\n");
            sb.append("        <area>").append(f.getArea()).append("</area>\n"); // всегда пишем
            sb.append("        <numberOfRooms>").append(f.getNumberOfRooms()).append("</numberOfRooms>\n");
            if (f.getFurnish() != null)
                sb.append("        <furnish>").append(f.getFurnish()).append("</furnish>\n");
            if (f.getView() != null)
                sb.append("        <view>").append(f.getView()).append("</view>\n");
            if (f.getTransport() != null)
                sb.append("        <transport>").append(f.getTransport()).append("</transport>\n");
            if (f.getHouse() != null) {
                sb.append("        <houseName>").append(f.getHouse().getName()).append("</houseName>\n");
                sb.append("        <houseYear>").append(f.getHouse().getYear()).append("</houseYear>\n");
                if (f.getHouse().getNumberOfFlatsOnFloor() != null)
                    sb.append("        <houseFlatsOnFloor>").append(f.getHouse().getNumberOfFlatsOnFloor()).append("</houseFlatsOnFloor>\n");
            }
            sb.append("    </flat>\n");
        }

        sb.append("</flats>");

        // Запись через BufferedOutputStream (требование задания)
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename))) {
            bos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}