# L15: LRU Cache

## Requirements

The system should:

1. Create a cache with a fixed capacity > 0, set at creation.
2. Return the value for an existing key.
3. Return null for a missing key, without changing the cache.
4. Store a new key and its value.
5. Replace the value when an existing key is added.
6. Remove a key; return false if it was not there.
7. Count a read and any add as a use (key becomes most recently used).
8. Evict the least recently used key first when full and a new key is added.
9. Reject a null key.
10. Be correct under concurrent use.
11. Do get, put, remove in O(1).

## Design

- Node<K,V>: key, value, prev, next. Holds data only.
  Key is stored so eviction can delete the map entry.
- LRUCache<K,V>: capacity, Map<K, Node>, dummy head, dummy tail, ReentrantLock.
  Newest next to head, oldest next to tail.
- Dummy head/tail remove all empty-list and edge-node checks.
- No design pattern. Extension: EvictionPolicy interface (Strategy) for LFU/FIFO.

## Operations (inside the lock)

- get: map lookup, detach, attach at front, return value.
- put: existing key -> set value, move to front. New key -> evict if full, add to map, attach at front.
- evict: lru = tail.prev; map.remove(lru.key); detach(lru).
- remove: map.remove, detach.
- detach(X): X.prev.next = X.next; X.next.prev = X.prev.
- attachAtFront(X): X.next = head.next; head.next.prev = X; head.next = X; X.prev = head.

## Decisions

- Generic <K,V>.
- One lock per cache around whole methods: get also changes shared pointers.
- Pointer helpers kept private inside LRUCache.

## Hand trace (capacity 2)

| Step     | List (head -> tail) | Map keys |
|----------|---------------------|----------|
| put(A,1) | A                   | A        |
| put(B,2) | B, A                | A, B     |
| get(A)   | A, B                | A, B     |
| put(C,3) | C, A (B evicted)    | A, C     |
| get(B)   | C, A (returns null) | A, C     |
