package com.kjeldsen.player.persistence.cache;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Crunchify.com
 * How to Create a Simple In Memory Cache in Java (Lightweight Cache)
 */
public abstract class InMemoryCacheStore<K, T> {
    private final long timeToLive;

    // LRUMap: A Map implementation with a fixed maximum size which removes the least recently used entry if an entry is added when full.
    // The least recently used algorithm works on the get and put operations only.
    // Iteration of any kind, including setting the value by iteration, does not change the order.
    // Queries such as containsKey and containsValue or access via views also do not change the order.
    private final LRUMap<K, CacheObject<T>> store;

    protected static class CacheObject<V> {

        // currentTimeMillis(): Returns the current time in milliseconds.
        // Note that while the unit of time of the return value is a millisecond,
        // the granularity of the value depends on the underlying operating system and may be larger.
        // For example, many operating systems measure time in units of tens of milliseconds.
        public long lastAccessed = System.currentTimeMillis();
        public V value;

        protected CacheObject(V value) {
            this.value = value;
        }
    }

    public InMemoryCacheStore(long timeToLive, final long timerInterval, int maxItems) {
        this.timeToLive = timeToLive * 1000;
        store = new LRUMap<>(maxItems);
        if (this.timeToLive > 0 && timerInterval > 0) {
            Thread t = new Thread(() -> {
                while (true) {
                    try {

                        // Thread: A thread is a thread of execution in a program.
                        // The Java Virtual Machine allows an application to have multiple threads of execution running concurrently.
                        Thread.sleep(timerInterval * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    cleanup();
                }
            });
            // setDaemon(): Marks this thread as either a daemon thread or a user thread.
            // The Java Virtual Machine exits when the only threads running are all daemon threads.
            // This method must be invoked before the thread is started.
            t.setDaemon(true);

            // start(): Causes this thread to begin execution; the Java Virtual Machine calls the run method of this thread.
            // The result is that two threads are running concurrently:
            // the current thread (which returns from the call to the start method) and the other thread (which executes its run method).
            t.start();
        }
    }

    public void put(K key, T value) {
        synchronized (store) {

            // put(): Puts a key-value mapping into this map.
            store.put(key, new CacheObject<>(value));
        }
    }

    public T get(K key) {
        synchronized (store) {
            CacheObject<T> c = store.get(key);
            if (c == null)
                return null;
            else {
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }

    public void remove(K key) {
        synchronized (store) {
            store.remove(key);
        }
    }

    public List<T> getAll() {
        synchronized (store) {
            List<T> values = new ArrayList<>();
            for (MapIterator<K, CacheObject<T>> it = store.mapIterator(); it.hasNext(); ) {
                it.next();
                values.add(it.getValue().value);
            }
            return values;
        }
    }

    public int size() {
        synchronized (store) {
            return store.size();
        }
    }

    public void cleanup() {
        // System: The System class contains several useful class fields and methods.
        // It cannot be instantiated. Among the facilities provided by the System class are standard input, standard output,
        // and error output streams; access to externally defined properties and environment variables;
        // a means of loading files and libraries; and a utility method for quickly copying a portion of an array.
        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey;
        synchronized (store) {
            MapIterator<K, CacheObject<T>> itr = store.mapIterator();
            // ArrayList: Constructs an empty list with the specified initial capacity.
            // size(): Gets the size of the map.
            deleteKey = new ArrayList<>((store.size() / 2) + 1);
            while (itr.hasNext()) {
                K key = itr.next();
                CacheObject<T> c = itr.getValue();
                if (c != null && (now > (timeToLive + c.lastAccessed))) {

                    // add(): Appends the specified element to the end of this list.
                    deleteKey.add(key);
                }
            }
        }
        for (K key : deleteKey) {
            synchronized (store) {
                // remove(): Removes the specified mapping from this map.
                store.remove(key);
            }
            // yield(): A hint to the scheduler that the current thread is willing to
            // yield its current use of a processor.
            // The scheduler is free to ignore this hint.
            Thread.yield();
        }
    }

    public void clear() {
        synchronized (store) {
            store.clear();
        }
    }

    public Page<T> find(Predicate<T> filter, Pageable pageable, Comparator<T> sort) {
        synchronized (store) {
            List<T> values = getAll().stream()
                .sorted(sort)
                .filter(filter)
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
            return PageableExecutionUtils.getPage(values, pageable, values::size);
        }
    }
}
