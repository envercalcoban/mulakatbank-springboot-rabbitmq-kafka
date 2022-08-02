package com.mulakatbank.dto;

import com.mulakatbank.model.City;
import com.mulakatbank.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseAccountRequest {

    @NotBlank(message = "Customer id must not be null")//@NotNull anotain kullanimi entity kullanilmadi
    private String customerId;

    @NotNull//@NotNull anotain kullanimi entity kullanilmadi
    @Min(0)//para oldugu icin
    private Double balance;

    @NotNull(message = "Currency must not be null")//@NotNull anotain kullanimi entity kullanilmadi
    private Currency currency;

    @NotNull(message = "City must not be null")//@NotNull anotain kullanimi entity kullanilmadi
    private City city;
}
