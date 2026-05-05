package server;

import common.models.Flat;
import common.models.View;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionManager {
    private LinkedHashMap<String, Flat> collection = new LinkedHashMap<>();
    private final Date initializationDate = new Date();
    private final Deque<String> history = new ArrayDeque<>(11);

    public LinkedHashMap<String, Flat> getCollection() { return collection; }
    public void setCollection(LinkedHashMap<String, Flat> collection) { this.collection = collection; }
    public Date getInitializationDate() { return initializationDate; }

    public int getNextId() {
        return collection.values().stream()
                       .mapToInt(Flat::getId)
                       .max()
                       .orElse(0) + 1;
    }

    public boolean containsKey(String key) { return collection.containsKey(key); }
    public Flat get(String key) { return collection.get(key); }
    public void insert(String key, Flat flat) { collection.put(key, flat); }
    public void updateByKey(String key, Flat flat) { collection.put(key, flat); }
    public void removeByKey(String key) { collection.remove(key); }
    public void clear() { collection.clear(); }

    public String getKeyById(int id) {
        return collection.entrySet().stream()
                .filter(e -> e.getValue().getId() == id)
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    public void removeLower(Flat flat) {
        collection.entrySet().removeIf(e -> e.getValue().compareTo(flat) < 0);
    }

    public boolean replaceIfLower(String key, Flat newFlat) {
        Flat old = collection.get(key);
        if (old == null) return false;
        if (newFlat.compareTo(old) < 0) {
            collection.put(key, newFlat);
            return true;
        }
        return false;
    }

    public Optional<Flat> minByCreationDate() {
        return collection.values().stream()
                .min(Comparator.comparing(Flat::getCreationDate));
    }

    public long countByView(View view) {
        return collection.values().stream()
                .filter(f -> f.getView() == view)
                .count();
    }

    public List<Flat> getSortedByNaturalOrder() {
        return collection.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public void addHistory(String cmd) {
        if (history.size() == 11) history.pollFirst();
        history.addLast(cmd);
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }
}