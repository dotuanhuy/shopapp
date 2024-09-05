package com.project.shopapp.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {
    @JsonProperty("image_url")
    @Size(min = 5, max = 200, message = "Name image be between 3 and 200 character")
    private String imageUrl;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product id must be > 0")
    private Long productId;
}
