package da.springframework.springbootwebfluxclient.handler;

import da.springframework.springbootwebfluxclient.model.ProductDTO;
import da.springframework.springbootwebfluxclient.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@RequiredArgsConstructor
@Component
public class ProductHandler {

    private final ProductService productService;

    public Mono<ServerResponse> listProducts(ServerRequest serverRequest){
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(productService.findAll(), ProductDTO.class);
    }

    public Mono<ServerResponse> productDetail(ServerRequest serverRequest){

        String id = serverRequest.pathVariable("id");

        return productService.findById(id).flatMap(productDTO -> ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(fromValue(productDTO))
                .switchIfEmpty(ServerResponse.notFound().build())
        );
    }
}
