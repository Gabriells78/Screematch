package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTraducao (@JsonAlias(value = "responseData") DadosResposta dadosResposta){
}
