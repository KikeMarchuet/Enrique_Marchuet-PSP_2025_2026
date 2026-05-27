package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class EnviarEmailAPI_interactiu {

    public static void main(String[] args) throws Exception {

        String apiKey = "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";

        Scanner kb = new Scanner(System.in);
        String desti = "";
        String assumpte = "";
        String missatge = "";

        String opcio = "";
        while (!opcio.equals("s")) {

            System.out.print("\nCorreu de destí: ");
            desti = kb.nextLine();
            System.out.print("Assumpte del missatge: ");
            assumpte = kb.nextLine();
            System.out.print("Contingut del missatge: ");
            missatge = kb.nextLine();

            System.out.println("\n= = = RESUM DEL CORREU = = =");
            System.out.println("De: " + "kike.marchuet@gmail.com");
            System.out.println("Para: " + desti);
            System.out.println("Assumpte: " + assumpte);
            System.out.println("Missatge: " + missatge);
            System.out.println("= = = = = = = = = = = = = = =\n");
            System.out.print("Vols enviar el correu? (s/n): ");
            opcio = kb.nextLine();
        }

        String json = """
                {
                  "personalizations": [{
                    "to": [{"email": "%s"}]
                  }],
                  "from": {"email": "kike.marchuet@gmail.com"},
                  "subject": "%s",
                  "content": [{
                    "type": "text/plain",
                    "value": "%s"
                  }]
                }
                """.formatted(desti, assumpte, missatge);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.sendgrid.com/v3/mail/send"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("\nCodi resposta: " + response.statusCode());

        if (response.statusCode() == 202) {
            System.out.println("Email enviat amb èxit!!");
        }
    }
}