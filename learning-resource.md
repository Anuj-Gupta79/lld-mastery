Here are the exact 10 System Design questions that appeared in my last 15 interviews:

1. Design a Rate Limiter Every company asked this.
Know: Sliding window, Redis, race conditions, token bucket vs leaky bucket.

2. Design a Chat Application WhatsApp typing indicator alone can be a 45 min discussion.
Know: WebSockets, Redis Pub/Sub, message queues, offline delivery.

3. Design a URL Shortener Looks simple. Gets deep fast.
Know: Base62 encoding, collision handling, analytics, caching with Redis.

4. Design a Notification System
Know: Push vs pull, Kafka for async delivery, retry logic, user preferences.

5. Design a Payment System JP Morgan asked this. So did two others.
Know: Idempotency keys, Saga pattern, ACID vs eventual consistency.

6. Design an API Rate Limiter Different from 1. This one focuses on distributed systems.
Know: Token bucket, Redis INCR, Lua scripts, multi-server coordination.

7. Design a Video Streaming Platform
Know: CDN, chunked uploading, adaptive bitrate, storage at scale.

8. Design a Ride Hailing App
Know: Location tracking, matching algorithms, surge pricing logic, real-time updates.

9. Design an E-commerce Checkout System
Know: Inventory locking, flash sale handling, payment retries, order state machine.

10. Design a Search Autocomplete System
Know: Trie data structure, ranking by frequency, caching top results, latency under 100ms.