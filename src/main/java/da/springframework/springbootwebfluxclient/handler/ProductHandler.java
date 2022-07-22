package da.springframework.springbootwebfluxclient.handler;

import da.springframework.springbootwebfluxclient.model.ProductDTO;
import da.springframework.springbootwebfluxclient.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@RequiredArgsConstructor
@Component
public class ProductHandler {

    private final ProductService productService;

    public Mono<ServerResponse> listProducts(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(productService.findAll(), ProductDTO.class);
    }

    public Mono<ServerResponse> productDetail(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");

        return errorHandler(productService.findById(id).flatMap(productDTO -> ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(fromValue(productDTO))
                .switchIfEmpty(ServerResponse.notFound().build()))
        );
    }

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {

        Mono<ProductDTO> productDTOMono = serverRequest.bodyToMono(ProductDTO.class);

        return productDTOMono.flatMap(productDTO -> {
                    if (productDTO.getCreationDate() == null) {
                        productDTO.setCreationDate(new Date());
                    }

                    return productService.save(productDTO);
                }).flatMap(productDTO -> ServerResponse.created(URI.create("/api/client/".concat(productDTO.getId())))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(productDTO)) //same as .body(fromValue(productDTO))
                .onErrorResume(throwable -> {
                    WebClientResponseException responseException = (WebClientResponseException) throwable;

                    if (responseException.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return ServerResponse.badRequest()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(responseException.getResponseBodyAsString());
                    }

                    return Mono.error(responseException);
                });
    }

    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");
        Mono<ProductDTO> productDTOMono = serverRequest.bodyToMono(ProductDTO.class);

        return errorHandler(productDTOMono.flatMap(productDTO -> productService.update(productDTO, id))
                .flatMap(productDTO -> ServerResponse.created(URI.create("/api/client/".concat(productDTO.getId())))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(productDTO))
        );
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");

        return errorHandler(productService.delete(id).then(ServerResponse.noContent().build()));
    }

    public Mono<ServerResponse> uploadPhoto(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");

        return errorHandler(serverRequest.multipartData().map(stringPartMultiValueMap -> stringPartMultiValueMap.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(filePart -> productService.uploadPhoto(filePart, id))
                .flatMap(productDTO -> ServerResponse.created(URI.create("/api/client/".concat(id)))
                        .contentType(APPLICATION_JSON)
                        .bodyValue(productDTO))
        );
    }

    private Mono<ServerResponse> errorHandler(Mono<ServerResponse> serverResponseMono) {

        return serverResponseMono.onErrorResume(throwable -> {
            WebClientResponseException responseException = (WebClientResponseException) throwable;

            if (responseException.getStatusCode() == HttpStatus.NOT_FOUND) {
                Map<String, Object> body = new HashMap<>();
                body.put("error", "No existe el producto: ".concat(responseException.getMessage()));
                body.put("timestamp", new Date());
                body.put("status", responseException.getStatusCode().value());

                return ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(body);
            }

            return Mono.error(responseException);
        });
    }
}
