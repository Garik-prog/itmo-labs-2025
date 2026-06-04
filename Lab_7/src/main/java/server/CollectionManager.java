package server;

import common.models.Flat;
import common.models.View;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CollectionManager {
    private final Date initializationDate = new Date();
    private final Deque<String> history = new ArrayDeque<>(11);
    private final ReentrantLock lock = new ReentrantLock();
    private LinkedHashMap<String, Flat> collection = new LinkedHashMap<>();
    private DatabaseManager databaseManager;
    private UserManager userManager;

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager dm) {
        this.databaseManager = dm;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager um) {
        this.userManager = um;
    }

    public Date getInitializationDate() {
        return initializationDate;
    }

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
        try {
            collection = c;
        } finally {
            lock.unlock();
        }
    }

    public int getSize() {
        lock.lock();
        try {
            return collection.size();
        } finally {
            lock.unlock();
        }
    }

    public boolean containsKey(String key) {
        lock.lock();
        try {
            return collection.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    public Flat get(String key) {
        lock.lock();
        try {
            return collection.get(key);
        } finally {
            lock.unlock();
        }
    }

    public void insert(String key, Flat flat) {
        lock.lock();
        try {
            collection.put(key, flat);
        } finally {
            lock.unlock();
        }
    }

    public void updateByKey(String key, Flat flat) {
        lock.lock();
        try {
            collection.put(key, flat);
        } finally {
            lock.unlock();
        }
    }

    public void removeByKey(String key) {
        lock.lock();
        try {
            collection.remove(key);
        } finally {
            lock.unlock();
        }
    }

    public void clearForUser(String ownerLogin) {
        lock.lock();
        try {
            collection.entrySet().removeIf(e -> ownerLogin.equals(e.getValue().getOwnerLogin()));
        } finally {
            lock.unlock();
        }
    }

    public String getKeyById(int id) {
        lock.lock();
        try {
            for (Map.Entry<String, Flat> e : collection.entrySet()) {
                if (e.getValue().getId() == id) {
                    String key = e.getKey();
                    return key;
                }
            }

            return null;
        } finally {
            lock.unlock();
        }
    }

    public Optional<Flat> minByCreationDate() {
        lock.lock();
        try {
            boolean seen = false;
            Flat best = null;
            Comparator<Flat> comparator = Comparator.comparing(Flat::getCreationDate);

            for (Flat flat : collection.values()) {
                if (!seen || comparator.compare(flat, best) < 0) {
                    seen = true;
                    best = flat;
                }
            }

            if (seen) {
                return Optional.of(best);
            }

            return Optional.empty();
        } finally {
            lock.unlock();
        }
    }

    public long countByView(View view) {
        lock.lock();
        try {
            long count = 0L;
            for (Flat f : collection.values()) {
                if (f.getView() == view) {
                    count++;
                }
            }
            return count;
        } finally {
            lock.unlock();
        }
    }

    public List<Flat> getSortedByNaturalOrder() {
        lock.lock();
        try {
            List<Flat> list = new ArrayList<>();

            for (Flat flat : collection.values()) {
                list.add(flat);
            }

            list.sort(null);

            return list;
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
        try {
            return new ArrayList<>(history);
        } finally {
            lock.unlock();
        }
    }
}
