package br.com.alura.screenmatch.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ConsultaMyMemory {

    public static String obterTraducao(String text) {
        ObjectMapper mapper = new ObjectMapper();

        ConsumoAPI consumo = new ConsumoAPI();

        String texto = UriEncoder.encode(text);
        String langpair = UriEncoder.encode("en|pt-br");

        String url = "https://api.mymemory.translated.net/get?q="+ texto +"&langpair="+langpair;

        String json = consumo.ObterDados(url);

        DadosTraducao traducao;
        try{
            traducao= mapper.readValue(json,DadosTraducao.class);
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
return traducao.dadosResposta().textoTraduzido();
    }
}