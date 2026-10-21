package com.lld.problems.lrucache.code.models;

public class Node<K, V> {
    private K key;
    private V value;
    private Node<K, V> prev;
    private Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
        this.prev = null;
        this.next = null;
    }

    public void setPrev(Node<K, V> prev) {
        this.prev = prev;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }

    public void setValue(V value){
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public Node<K, V> getNext() {
        return this.next;
    }

    public Node<K, V> getPrev() {
        return this.prev;
    }
}
