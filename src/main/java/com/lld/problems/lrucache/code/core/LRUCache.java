package com.lld.problems.lrucache.code.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

import com.lld.problems.lrucache.code.exceptions.InvalidCapacityException;
import com.lld.problems.lrucache.code.exceptions.InvalidKeyException;
import com.lld.problems.lrucache.code.models.Node;

public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> nodes;
    private final Node<K, V> head;
    private final Node<K, V> tail;
    private final ReentrantLock lock;

    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new InvalidCapacityException("[Error Cache Creation] No Cache initialization with <= 0 capacity");
        }

        this.capacity = capacity;
        this.lock = new ReentrantLock();
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        this.head.setNext(this.tail);
        this.tail.setPrev(this.head);
        this.nodes = new HashMap<>();
    }

    public V get(K key) {
        this.lock.lock();

        try {

            if (Objects.isNull(key)) {
                throw new InvalidKeyException("[Error Get] Null key is not allowed!");
            }

            if (this.nodes.containsKey(key)) {
                Node<K, V> node = this.nodes.get(key);
                detach(node);
                attachAtFront(node);
                return node.getValue();
            }

            return null;

        } finally {
            this.lock.unlock();
        }
    }

    public void put(K key, V value) {
        this.lock.lock();

        try {
            if (Objects.isNull(key)) {
                throw new InvalidKeyException("[Error Put] Null key is not allowed!");
            }

            if (this.nodes.containsKey(key)) {
                Node<K, V> node = this.nodes.get(key);
                node.setValue(value);
                detach(node);
                attachAtFront(node);

                return;
            }

            if (this.nodes.size() >= this.capacity) {
                evictLeastRecentlyUsed();
            }

            Node<K, V> node = new Node<>(key, value);

            this.nodes.put(key, node);
            attachAtFront(node);

        } finally {
            this.lock.unlock();
        }
    }

    public boolean remove(K key) {
        this.lock.lock();

        try {
            if (Objects.isNull(key)) {
                throw new InvalidKeyException("[Error Remove] Null key is not allowed!");
            }

            if (this.nodes.containsKey(key)) {
                Node<K, V> node = this.nodes.get(key);
                this.nodes.remove(key);
                detach(node);
                return true;
            }

            return false;

        } finally {
            this.lock.unlock();
        }
    }

    private void detach(Node<K, V> node) {
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        node.setNext(null);
        node.setPrev(null);
    }

    private void attachAtFront(Node<K, V> node) {
        this.head.getNext().setPrev(node);
        node.setNext(this.head.getNext());
        this.head.setNext(node);
        node.setPrev(head);
    }

    private void evictLeastRecentlyUsed() {
        Node<K, V> lruNode = this.tail.getPrev();
        this.nodes.remove(lruNode.getKey());
        detach(lruNode);
    }

}
