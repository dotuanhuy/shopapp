package com.project.shopapp.dtos.responses;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}
