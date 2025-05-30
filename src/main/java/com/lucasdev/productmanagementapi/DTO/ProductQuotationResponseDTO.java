package com.lucasdev.productmanagementapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuotationResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long productId;
    private String productName;
    private BigDecimal originalPriceBRL; //price in real R$
    private BigDecimal convertedPrice;
    private String currencyCode; //the code that was converted... USD, EUR
}