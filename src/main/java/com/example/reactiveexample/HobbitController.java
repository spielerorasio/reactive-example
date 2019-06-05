package com.example.reactiveexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class HobbitController {
    @Autowired
    HobbitFluxService hobbitFluxService;

    //http://localhost:8080/items
    @GetMapping(value = "items", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Line> items(){
        return hobbitFluxService.getHobbit();
    }

    //http://localhost:8080/items/2/100/300
    @GetMapping(value = "items/{from}/{to}/{delay}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Line> items(@PathVariable int from, @PathVariable int to, @PathVariable int delay ){
        return hobbitFluxService.getHobbit().delayElements(Duration.ofMillis(delay)).skip(from).take(to-from).log();
    }

    //http://localhost:8080/items/live
    @GetMapping(value = "items/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Line> itemsLive(){
        return hobbitFluxService.getLineConnectableFlux();
    }


    //http://localhost:8080/mongo/items
    @GetMapping(value = "mongo/items", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Line> mongoItems(){
        return hobbitFluxService.getLinesFromMongo();
    }

    //http://localhost:8080/mongo/items/2/100/300
    @GetMapping(value = "mongo/items/{from}/{to}/{delay}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Line> mongoItems(@PathVariable int from, @PathVariable int to, @PathVariable int delay ){
        return hobbitFluxService.getLinesFromMongo(from, to, delay);
    }

    //http://localhost:8080/mongo/items/live
    @GetMapping(value = "mongo/items/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Line> mongoItemsLive(){
        return hobbitFluxService.getMongoConnectableFlux();
    }

    //http://localhost:8080/mongo/items/2/100/300
    @GetMapping(value = "mongo/item/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Line> mongoItem(@PathVariable int id){
        return hobbitFluxService.getLinesById(id);
    }



    //http://localhost:8080/mongo/item/create/11111/hellohellohello

    //should be @PostMapping
    @GetMapping(value = "mongo/item/create/{id}/{text}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Line> mongoItems(@PathVariable int id, @PathVariable String text ){
        return hobbitFluxService.saveLine(id, text);
    }


}
