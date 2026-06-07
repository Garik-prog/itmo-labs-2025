package server;

import common.models.Flat;
import common.models.View;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class CollectionManager {
    private final Date initializationDate = new Date();
    private final Map<String, Deque<String>> userHistory = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private LinkedHashMap<String, Flat> collection = new LinkedHashMap<>();
    private DatabaseManager databaseManager;
    private UserManager userManager;

    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public void setDatabaseManager(DatabaseManager dm) { this.databaseManager = dm; }
    public UserManager getUserManager() { return userManager; }
    public void setUserManager(UserManager um) { this.userManager = um; }
    public Date getInitializationDate() { return initializationDate; }

    public LinkedHashMap<String, Flat> getCollectionSnapshot() {
        lock.lock();
        try { return new LinkedHashMap<>(collection); }
        finally { lock.unlock(); }
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
        try {
            Iterator<Map.Entry<String, Flat>> it = collection.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Flat> entry = it.next();
                if (ownerLogin.equals(entry.getValue().getOwnerLogin())) {
                    it.remove();
                }
            }
        } finally { lock.unlock(); }
    }

    public String getKeyById(int id) {
        lock.lock();
        try {
            for (Map.Entry<String, Flat> entry : collection.entrySet()) {
                if (entry.getValue().getId() == id) {
                    return entry.getKey();
                }
            }
            return null;
        } finally { lock.unlock(); }
    }

    public Optional<Flat> minByCreationDate() {
        lock.lock();
        try {
            Flat best = null;
            for (Flat flat : collection.values()) {
                if (best == null || flat.getCreationDate().before(best.getCreationDate())) {
                    best = flat;
                }
            }
            return Optional.ofNullable(best);
        } finally { lock.unlock(); }
    }

    public long countByView(View view) {
        lock.lock();
        try {
            long count = 0;
            for (Flat flat : collection.values()) {
                if (flat.getView() == view) count++;
            }
            return count;
        } finally { lock.unlock(); }
    }

    public List<Flat> getSortedByNaturalOrder() {
        lock.lock();
        try {
            List<Flat> list = new ArrayList<>(collection.values());
            Collections.sort(list);
            return list;
        } finally { lock.unlock(); }
    }

    public void addHistory(String login, String cmd) {
        if (login == null) return;
            lock.lock();
        try {
            Deque<String> history = userHistory.get(login);
            if (history == null) {
                history = new ArrayDeque<>(11);
                userHistory.put(login, history);
            }
            if (history.size() == 11) history.pollFirst();
            history.addLast(cmd);
        } finally { lock.unlock(); }
    }

    public List<String> getHistory(String login) {
        if (login == null) return new ArrayList<>();
        lock.lock();
        try {
            Deque<String> history = userHistory.get(login);
            if (history == null) return new ArrayList<>();
            return new ArrayList<>(history);
        } finally { lock.unlock(); }
    }
}