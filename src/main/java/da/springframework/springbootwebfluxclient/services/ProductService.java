package da.springframework.springbootwebfluxclient.services;

import da.springframework.springbootwebfluxclient.model.ProductDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Flux<ProductDTO> findAll();

    Mono<ProductDTO> findById(String id);

    Mono<ProductDTO> save(ProductDTO productDTO);

    Mono<ProductDTO> update(ProductDTO productDTO, String id);

    Mono<Void> delete(String id);
}
