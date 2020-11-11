package com.online.book.util;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

/**
 *
 */
public class Maps<K, V> {

	public static <K, V> Maps<K, V> newMaps() {
		return new Maps<>();
	}

	public static <K, V> Maps<K, V> newMaps(Map<K, V> map) {
		return new Maps<>(map);
	}

	private Map<K, V> map = newHashMap();

	public Maps() {
		this.map = newHashMap();
	}

	public Maps(Map<K, V> map) {
		this.map = newHashMap(map);
	}

	public Maps<K, V> putAll(Map<K, V> map) {
		this.map.putAll(map);
		return this;
	}

	public Maps<K, V> put(K key, V value) {
		this.map.put(key, value);
		return this;
	}

	public Maps<K, V> putIfNotNull(K key, V value) {
		if (value instanceof String) {
			if (StringUtils.isNotBlank((String) value)) {
				this.map.put(key, value);
			}
		} else {
			if (value != null) {
				this.map.put(key, value);
			}
		}

		return this;
	}

	public Maps<K, V> remove(K key) {
		this.map.remove(key);
		return this;
	}

	public Maps<K, V> clear() {
		this.map.clear();
		return this;
	}

	public V get(K key) {
		return map.get(key);
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Map<K, V> map() {
		return this.map;
	}

	public ImmutableMap<K, V> immutableMap() {
		return ImmutableMap.copyOf(map);
	}
}
