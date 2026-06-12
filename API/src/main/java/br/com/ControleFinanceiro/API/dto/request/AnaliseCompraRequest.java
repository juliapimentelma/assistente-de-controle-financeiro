package br.com.ControleFinanceiro.API.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record AnaliseCompraRequest(
        @JsonProperty(required = true)
        @JsonPropertyDescription("O nome do item que o usuário deseja comprar")
        String item,

        @JsonProperty(required = true)
        @JsonPropertyDescription("O valor do item em Reais")
        Double valor
) {}
