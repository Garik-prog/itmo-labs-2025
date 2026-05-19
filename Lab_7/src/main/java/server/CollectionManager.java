package server;

import common.models.Flat;
import common.models.View;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CollectionManager {
    private LinkedHashMap<String, Flat> collection = new LinkedHashMap<>();
    private final Date initializationDate = new Date();
    private final Deque<String> history = new ArrayDeque<>(11);
    private final ReentrantLock lock = new ReentrantLock();
    private DatabaseManager databaseManager;
    private UserManager userManager;

    public void setDatabaseManager(DatabaseManager dm) { this.databaseManager = dm; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public void setUserManager(UserManager um) { this.userManager = um; }
    public UserManager getUserManager() { return userManager; }

    public Date getInitializationDate() { return initializationDate; }

    /** Returns a snapshot copy of the collection (safe for iteration outside the lock). */
    public LinkedHashMap<String, Flat> getCollectionSnapshot() {
        lock.lock();
        try {
            return new LinkedHashMap<>(collection);
        } finally {
            lock.unlock();
        }
    }

    public void setCollection(LinkedHashMap<String, Flat> c) {
        lock.lock();
        try { collection = c; }
        finally { lock.unlock(); }
    }

    public int getSize() {
        lock.lock();
        try { return collection.size(); }
        finally { lock.unlock(); }
    }

    public boolean containsKey(String key) {
        lock.lock();
        try { return collection.containsKey(key); }
        finally { lock.unlock(); }
    }

    public Flat get(String key) {
        lock.lock();
        try { return collection.get(key); }
        finally { lock.unlock(); }
    }

    public void insert(String key, Flat flat) {
        lock.lock();
        try { collection.put(key, flat); }
        finally { lock.unlock(); }
    }

    public void updateByKey(String key, Flat flat) {
        lock.lock();
        try { collection.put(key, flat); }
        finally { lock.unlock(); }
    }

    public void removeByKey(String key) {
        lock.lock();
        try { collection.remove(key); }
        finally { lock.unlock(); }
    }

    public void clearForUser(String ownerLogin) {
        lock.lock();
        try { collection.entrySet().removeIf(e -> ownerLogin.equals(e.getValue().getOwnerLogin())); }
        finally { lock.unlock(); }
    }

    public String getKeyById(int id) {
        lock.lock();
        try {
            return collection.entrySet().stream()
                    .filter(e -> e.getValue().getId() == id)
                    .map(Map.Entry::getKey)
                    .findFirst().orElse(null);
        } finally {
            lock.unlock();
        }
    }

    public Optional<Flat> minByCreationDate() {
        lock.lock();
        try {
            return collection.values().stream()
                    .min(Comparator.comparing(Flat::getCreationDate));
        } finally {
            lock.unlock();
        }
    }

    public long countByView(View view) {
        lock.lock();
        try {
            return collection.values().stream()
                    .filter(f -> f.getView() == view)
                    .count();
        } finally {
            lock.unlock();
        }
    }

    public List<Flat> getSortedByNaturalOrder() {
        lock.lock();
        try {
            return collection.values().stream().sorted().collect(Collectors.toList());
        } finally {
            lock.unlock();
        }
    }

    public void addHistory(String cmd) {
        lock.lock();
        try {
            if (history.size() == 11) history.pollFirst();
            history.addLast(cmd);
        } finally {
            lock.unlock();
        }
    }

    public List<String> getHistory() {
        lock.lock();
        try { return new ArrayList<>(history); }
        finally { lock.unlock(); }
    }
}
