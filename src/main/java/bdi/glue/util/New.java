package bdi.glue.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class New {
    private New() {
    }

    public static <T> ArrayList<T> arrayList() {
        return new ArrayList<T>();
    }

    public static <T> ArrayList<T> arrayList(Collection<? extends T> elements) {
        return new ArrayList<T>(elements);
    }

    public static <K, V> HashMap<K, V> hashMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> HashMap<K, V> hashMap(K k1, V v1) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        return map;
    }

    public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> HashMap<K, V> hashMap(Map<? extends K, ? extends V> values) {
        return new HashMap<K, V>(values);
    }

    public static <T> LinkedList<T> linkedList() {
        return new LinkedList<T>();
    }

    public static <T> ConcurrentLinkedQueue<T> concurrentLinkedQueue() {
        return new ConcurrentLinkedQueue<T>();
    }

    public static <K, V> ConcurrentHashMap<K, V> concurrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }

    public static <T> CopyOnWriteArrayList<T> copyOnWriteArrayList() {
        return new CopyOnWriteArrayList<T>();
    }

    public static <T> HashSet<T> hashSet(Iterable<? extends T> values) {
        HashSet<T> set = new HashSet<T>();
        for (T value : values) {
            set.add(value);
        }
        return set;
    }

    public static <T> Stack<T> stack() {
        return new Stack<T>();
    }

    public static <T> LinkedHashSet<T> linkedHashSet() {
        return new LinkedHashSet<T>();
    }
}
