package da.springframework.springbootwebfluxclient.model;

import lombok.Data;

import java.util.Date;

@Data
public class ProductDTO {

    private String id;

    private String name;

    private Double price;

    private Date creationDate;

    private String photo;

    private CategoryDTO category;
}
