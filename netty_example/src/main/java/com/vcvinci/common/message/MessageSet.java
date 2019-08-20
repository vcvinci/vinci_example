package com.vcvinci.common.message;

import com.vcvinci.common.partition.TopicPartition;
import com.ucarinc.dove.util.AbstractIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 消息迭代器
 * @author vinci
 * @date 2018-12-12 08:48
 */
public class MessageSet<K,V> implements Iterable<Message<K, V>> {

	@SuppressWarnings("unchecked")
	public static final MessageSet<Object, Object> EMPTY = new MessageSet<>(Collections.EMPTY_MAP);

	private final Map<TopicPartition, List<Message<K, V>>> records;

	public MessageSet(Map<TopicPartition, List<Message<K, V>>> records) {
		this.records = records;
	}

	public List<Message<K, V>> records(TopicPartition partition) {
		List<Message<K, V>> recs = this.records.get(partition);
		if (recs == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(recs);
		}
	}

	public Iterable<Message<K, V>> records(String topic) {
		if (topic == null) {
			throw new IllegalArgumentException("Topic must be non-null.");
		}
		List<List<Message<K, V>>> recs = new ArrayList<>();
		for (Map.Entry<TopicPartition, List<Message<K, V>>> entry : records.entrySet()) {
			if (entry.getKey().getTopic().equals(topic)) {
				recs.add(entry.getValue());
			}
		}
		return new ConcatenatedIterable<>(recs);
	}

	public Set<TopicPartition> partitions() {
		return Collections.unmodifiableSet(records.keySet());
	}

	@Override
	public Iterator<Message<K, V>> iterator() {
		return new ConcatenatedIterable<>(records.values()).iterator();
	}

	public int count() {
		int count = 0;
		for (List<Message<K, V>> recs: this.records.values()) {
			count += recs.size();
		}
		return count;
	}

	private class ConcatenatedIterable<K, V> implements Iterable<Message<K, V>> {

		private final Iterable<? extends Iterable<Message<K, V>>> iterables;

		public ConcatenatedIterable(Iterable<? extends Iterable<Message<K, V>>> iterables) {
			this.iterables = iterables;
		}

		@Override
		public Iterator<Message<K, V>> iterator() {
			return new AbstractIterator<Message<K,V>>() {
				Iterator<? extends Iterable<Message<K, V>>> iters = iterables.iterator();
				Iterator<Message<K, V>> current;

				@Override
				public Message<K, V> makeNext() {
					while (current == null || !current.hasNext()) {
						if (iters.hasNext()) {
							current = iters.next().iterator();
						} else {
							return allDone();
						}
					}
					return current.next();
				}
			};
		}
	}

	public boolean isEmpty() {
		return records.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public static <K, V> MessageSet<K, V> empty() {
		return (MessageSet<K, V>) EMPTY;
	}
}
