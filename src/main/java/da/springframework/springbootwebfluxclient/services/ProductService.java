package da.springframework.springbootwebfluxclient.services;

import da.springframework.springbootwebfluxclient.model.ProductDTO;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Flux<ProductDTO> findAll();

    Mono<ProductDTO> findById(String id);

    Mono<ProductDTO> save(ProductDTO productDTO);

    Mono<ProductDTO> update(ProductDTO productDTO, String id);

    Mono<Void> delete(String id);

    Mono<ProductDTO> uploadPhoto(FilePart filePart, String id);
}
