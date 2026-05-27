package org.example;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EnviarEmailAPI {

    public static void main(String[] args) throws Exception {

        String apiKey = "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";
        String json = """
        {
          "personalizations": [{
            "to": [{"email": "kike@marchuet.es"}]
          }],
          "from": {"email": "kike.marchuet@gmail.com"},
          "subject": "Prova PSP",
          "content": [{
            "type": "text/plain",
            "value": "Hola, este es un exemple de envio de mail des de Java"
          }]
        }
        """;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.sendgrid.com/v3/mail/send"))
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Codi resposta: " + response.statusCode());
    }
}