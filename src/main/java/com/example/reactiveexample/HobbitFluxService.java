package com.example.reactiveexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class HobbitFluxService {
    private List<Line>  lines;
    private AtomicInteger counter = new AtomicInteger();
    private Flux<Line> linesFlux;
    private ConnectableFlux<Line> lineConnectableFlux;
    private ConnectableFlux<Line> mongoConnectableFlux;


    @Autowired
    LineRepository lineRepository;

    public void loadFile(Path path) throws IOException {
        List<String> strLines = Files.readAllLines(path);
        AtomicInteger index = new AtomicInteger(1);
        this.lines = strLines.stream().map(line -> new Line(index.getAndIncrement(), line)).collect(Collectors.toList());
        this.linesFlux = Flux.fromStream(lines.stream());
        //copy to 2nd flux and make it connectable
        this.lineConnectableFlux = Flux.from(Flux.fromStream(lines.stream())).delayElements(Duration.ofSeconds(1)).replay(3);
        //start the connectable
        this.lineConnectableFlux.connect();
//        same as lineConnectableFlux.autoConnect(1); but need to return autoConnect since it creates new Flux
//        this.lineConnectableFlux.autoConnect();

        //3rd - save to mongo
        lineRepository.saveAll(Flux.fromStream(lines.stream())).subscribe(); //we must call subscribe since it is lazy


//        mongoConnectableFlux = Flux.from(lineRepository.findAll()).publish(); publish replaced by replay
        mongoConnectableFlux = Flux.from(lineRepository.findAll()).delayElements(Duration.ofSeconds(1)).replay(10);
        mongoConnectableFlux.connect();
    }


    public Flux<Line> getHobbit() {
        return linesFlux;
    }

    public ConnectableFlux<Line> getLineConnectableFlux() {
        return lineConnectableFlux;
    }

    public Flux<Line> getLinesFromMongo() {
        return lineRepository.findAll(Sort.by(Sort.Order.asc("Id")));
    }

    public Flux<Line> getLinesFromMongo(int from, int to, int delay) {
        return lineRepository.findByIdBetween(from, to).delayElements(Duration.ofMillis(delay));
    }
    public Mono<Line> saveLine(int id, String text) {
        return lineRepository.save(new Line(id, text));
    }

    public Mono<Line> getLinesById(int id) {
        return lineRepository.findById(id);
    }

    public ConnectableFlux<Line> getMongoConnectableFlux() {
        return mongoConnectableFlux;
    }

    public Flux<Tuple2<Integer, Line>> getTuppleLines() {
        Flux<Integer> range = Flux.range(1, lines.size());
        Flux<Tuple2<Integer, Line>> zip = Flux.zip(range, getHobbit());
        zip.doOnNext(line-> counter.addAndGet(line.getT2().getText().length()));
        //or         return Flux.range(1, lines.size()).zipWith(getHobbit());
        return zip;
    }

    public int countLetters(){
        return counter.get();
    }


}
