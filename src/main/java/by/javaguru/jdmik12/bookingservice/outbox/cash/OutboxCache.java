package by.javaguru.jdmik12.bookingservice.outbox.cash;

import by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class OutboxCache {

    public static final Map<Long, OutboxStatus> cache = new ConcurrentHashMap<>();

    public static void put(Long key, OutboxStatus value) {
        cache.put(key, value);
        log.debug("Добавлено в кэш: {} = {}", key, value);
    }

    public static OutboxStatus get(Long key) {
        OutboxStatus value = cache.get(key);
        log.debug("Получено из кэша: {} = {}", key, value);
        return value;
    }

    public static Map<Long, OutboxStatus> getAll() {
        log.debug("Получены все данные из кэша ");
        return cache;
    }

    public static void remove(Long key) {
        cache.remove(key);
        log.debug("Удалено из кэша: {}", key);
    }

    public static void clear() {
        cache.clear();
        log.debug("Кэш очищен");
    }

    public static int size() {
        return cache.size();
    }

    public static boolean containsKey(Long key) {
        return cache.containsKey(key);
    }
}
