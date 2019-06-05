package com.example.reactiveexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class ReactiveExampleApplication {

	@Autowired
	HobbitFluxService hobbitFluxService;

	public static void main(String[] args)  {
		SpringApplication.run(ReactiveExampleApplication.class, args);

	}

	@EventListener
	public void isUp(ApplicationReadyEvent event) throws IOException, InterruptedException {
		Path path = Paths.get(new ClassPathResource("Hobbit.txt").getURI());

		hobbitFluxService.loadFile(path);


		/**
		 * Examples

		 Flux<Long> startFlux = Flux.interval(Duration.ofSeconds(1)).share().cache(2);

		 //		Flux firstFlux = Flux.from(startFlux);
		 startFlux.subscribe(out -> System.out.println("firstFlux value: " + out));
		 new CountDownLatch(1).await(5, TimeUnit.SECONDS);

		 //		Flux secondFlux = Flux.from(startFlux);
		 startFlux.subscribe(out -> System.out.println("secondFlux value: " + out));
		 new CountDownLatch(1).await(5, TimeUnit.SECONDS);


		 Path path = Paths.get(new ClassPathResource("Hobbit.txt").getURI());
		 System.out.println(path.toFile().exists());
		 hobbitFluxService.loadFile(path);
		 Flux<Line> hobbit = hobbitFluxService.getHobbit();
		 //		hobbit.subscribe(System.out::println);

		 Flux<Tuple2<Integer, Line>> tuppleLines = hobbitFluxService.getTuppleLines();

		 tuppleLines.buffer(10).subscribe(System.out::println);
		 System.out.println(hobbitFluxService.countLetters());

		 Flux<List<Integer>> buffer = Flux.range(1, 14).buffer(5);
		 buffer.subscribe(System.out::println);

		 Flux<List<Integer>> listFlux = Flux.range(1, 14).bufferUntil(i -> i < 7);
		 listFlux.subscribe(System.out::println);
		 System.out.println("====================================");

		 Flux<Integer> cacheFlux = Flux.range(1, 14).cache(4);
		 cacheFlux.subscribe(System.out::println);
		 cacheFlux.replay(4).subscribe(System.out::println);
		 */
	}

}
