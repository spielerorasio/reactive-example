package com.example.reactiveexample;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface LineRepository extends ReactiveMongoRepository<Line, Integer> {
    Flux<Line> findByIdBetween(int from, int to);
}
