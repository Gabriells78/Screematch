package br.com.alura.screenmatch.service;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ConsumoAPI {

    public String ObterDados(String endereco){

        //endereco= "http://www.omdbapi.com/?t=" + URLEncoder.encode(endereco, StandardCharsets.UTF_8) +"&apiKey=53de85b5";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endereco))
                .build();

        HttpResponse<String> response = null;
        try{
            response= client
                    .send(request,HttpResponse.BodyHandlers.ofString());
        } catch (IOException e){
            throw new RuntimeException(e);
        }catch (InterruptedException e ){
            throw new RuntimeException(e);
        }
        String json = response.body();
        return json;

    }

}
