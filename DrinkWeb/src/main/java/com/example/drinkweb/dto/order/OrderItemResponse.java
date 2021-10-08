package com.example.drinkweb.dto.order;

import com.example.drinkweb.dto.perfume.PerfumeResponse;
import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id;
    private Long amount;
    private Long quantity;
    private PerfumeResponse perfume;
}
