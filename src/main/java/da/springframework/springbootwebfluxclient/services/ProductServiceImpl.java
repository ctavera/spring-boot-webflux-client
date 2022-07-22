package da.springframework.springbootwebfluxclient.services;

import da.springframework.springbootwebfluxclient.model.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private WebClient webClient;

    @Override
    public Flux<ProductDTO> findAll() {
        return webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(ProductDTO.class));
//                .retrieve().bodyToFlux(ProductDTO.class); // same as above
    }

    @Override
    public Mono<ProductDTO> findById(String id) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return webClient.get().uri("/{id}", params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(ProductDTO.class);
//                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(ProductDTO.class)); // same as above

    }

    @Override
    public Mono<ProductDTO> save(ProductDTO productDTO) {
        return null;
    }

    @Override
    public Mono<ProductDTO> update(ProductDTO productDTO, String id) {
        return null;
    }

    @Override
    public Mono<Void> delete(String id) {
        return null;
    }
}
