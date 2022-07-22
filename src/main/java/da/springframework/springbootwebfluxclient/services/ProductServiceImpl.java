package da.springframework.springbootwebfluxclient.services;

import da.springframework.springbootwebfluxclient.model.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final WebClient webClient;

    @Override
    public Flux<ProductDTO> findAll() {
        return webClient.get()
                .accept(APPLICATION_JSON)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(ProductDTO.class));
//                .retrieve().bodyToFlux(ProductDTO.class); // same as above
    }

    @Override
    public Mono<ProductDTO> findById(String id) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return webClient.get().uri("/{id}", params)
                .accept(APPLICATION_JSON)
                .retrieve().bodyToMono(ProductDTO.class);
//                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(ProductDTO.class)); // same as above

    }

    @Override
    public Mono<ProductDTO> save(ProductDTO productDTO) {
        return webClient.post()
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(fromValue(productDTO))
                .retrieve().bodyToMono(ProductDTO.class);
    }

    @Override
    public Mono<ProductDTO> update(ProductDTO productDTO, String id) {

        return webClient.put().uri("/{id}", Collections.singletonMap("id", id))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(fromValue(productDTO))
                .retrieve().bodyToMono(ProductDTO.class);
    }

    @Override
    public Mono<Void> delete(String id) {
        return webClient.delete().uri("/{id}", Collections.singletonMap("id", id))
                .retrieve().toBodilessEntity().then(); //exchange().then()
    }
}
