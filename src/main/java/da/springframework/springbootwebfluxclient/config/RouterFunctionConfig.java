package da.springframework.springbootwebfluxclient.config;

import da.springframework.springbootwebfluxclient.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler) {
        return route(GET("/api/client"), productHandler::listProducts)
                .andRoute(GET("/api/client/{id}"), productHandler::productDetail)
                .andRoute(POST("/api/client"), productHandler::createProduct)
                .andRoute(PUT("/api/client/{id}"), productHandler::updateProduct)
                .andRoute(DELETE("/api/client/{id}"), productHandler::deleteProduct);
    }
}
