package org.example;

import java.io.*;
import java.nio.file.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.*;

public class ServidorWebFormulari {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
        server.createContext("/",
                ex -> {
                    String p = ex.getRequestURI().getPath();
                    if (p.equals("/")) {
                        try {
                            enviarFitxer(ex, "web/indexForm.html", "text/html", 200);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            enviar404(ex);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        server.createContext("/web",
                ex -> {
                    try {
                        enviarFitxer(ex, "web/info.html", "text/html", 200);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        server.createContext("/form",
                ex -> {
                    try {
                        enviarFitxer(ex, "web/formulari.html", "text/html", 200);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        server.createContext("/calc",
                ex -> {
                    try {
                        enviarFitxer(ex, "web/calcForm.html", "text/html", 200);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        server.createContext("/alumne",
                ex -> {
                    try {
                        enviarFitxer(ex, "web/alumneForm.html", "text/html", 200);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        server.createContext("/style.css",
                ex -> {
                    try {
                        enviarFitxer(ex, "web/style.css", "text/css", 200);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        server.createContext("/salutacio",
                ex -> {
                    String q = ex.getRequestURI().getQuery();
                    String nom = "Usuari";
                    if (q != null && q.startsWith("nom="))
                        nom = URLDecoder.decode(q.substring(4), StandardCharsets.UTF_8);
                    String h = "<html><body style='font-family:Arial;padding:40px'><h1>Hola, "
                            + nom + "</h1><p>Dades rebudes correctament.</p><a href='/form'>Tornar</a></body></html>";
                    ex.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    ex.sendResponseHeaders(200, h.getBytes().length);
                    OutputStream os = ex.getResponseBody();
                    os.write(h.getBytes());
                    os.close();
                });
        server.createContext("/resultat",
                ex -> {
                    String q = ex.getRequestURI().getQuery();
                    String h = "";
                    float a = 0;
                    float b = 0;
                    String op = "";
                    float r = 0;
                    String cad_r = ""; //Cadena resultat per si es divisiñó entre zero
                    Boolean todoBien = true; // Controlaré si hay valores que no sean números

                    if (q != null) {
                        String[] params = q.split("&");
                        for (String param : params) {
                            String[] params2 = param.split("=");
                            switch (params2[0]) {
                                case "a":
                                    try {
                                        a = Float.parseFloat(params2[1]);
                                    } catch (NumberFormatException e) {
                                        todoBien = false;
                                    }
                                    break;
                                case "b":
                                    try {
                                        b = Float.parseFloat(params2[1]);
                                    } catch (NumberFormatException e) {
                                        todoBien = false;
                                    }
                                    break;
                                case "op":
                                    op = params2[1];
                                    break;
                            }
                        }
                    }

                    if (todoBien) {
                        switch (op) {
                            case "suma":
                                System.out.println(a + b);
                                r = a + b;
                                cad_r = String.valueOf(r);
                                break;
                            case "resta":
                                r = a - b;
                                cad_r = String.valueOf(r);
                                break;
                            case "multiplicacio":
                                r = a * b;
                                cad_r = String.valueOf(r);
                                break;
                            case "divisio":
                                r = (float) a / b;
                                cad_r = String.valueOf(r);
                                if (b == 0) {
                                    cad_r = "Error: No es pot dividir entre zero";
                                }
                                break;
                        }


                        h = "<html><body style='font-family:Arial;padding:40px'><h1>Operació: "
                                + op + "</h1><h1>Resultat: " + cad_r + "</h1><a href='/calc'>Tornar</a></body></html>";
                    } else {
                        h = "<html><body style='font-family:Arial;padding:40px'><h1>Algun dels números no és correcte"
                                + "</h1><<a href='/calc'>Tornar</a></body></html>";
                    }

                    ex.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    ex.sendResponseHeaders(200, h.getBytes().length);
                    OutputStream os = ex.getResponseBody();
                    os.write(h.getBytes());
                    os.close();
                });
        server.createContext("/alumnat",
                ex -> {
                    String q = ex.getRequestURI().getQuery();
                    String nom = "";
                    String cognoms = "";
                    int edat = 0;
                    String curs = "";
                    String estat = "";

                    if (q != null) {
                        String[] params = q.split("&");
                        for (String param : params) {
                            String[] params2 = param.split("=");
                            switch (params2[0]) {
                                case "nom":
                                    nom = URLDecoder.decode(params2[1], StandardCharsets.UTF_8);
                                    break;
                                case "cognoms":
                                    cognoms = URLDecoder.decode(params2[1], StandardCharsets.UTF_8);
                                    break;
                                case "edat":
                                    edat = Integer.parseInt(params2[1]);
                                    if (edat < 18) {
                                        estat = "Menor d'edat";
                                    } else {
                                        estat = "Major d'edat";
                                    }
                                    break;
                                case "curs":
                                    curs = URLDecoder.decode(params2[1], StandardCharsets.UTF_8);
                                    break;
                            }
                        }
                    }

                    String h = "<html><body style='font-family:Arial;padding:40px'>" +
                            "<h1>Nom complet: " + nom + " " + cognoms + "</h1>" +
                            "<h1>Edat: " + edat + "</h1>" +
                            "<h1>Curs: " + curs + "</h1>" +
                            "<h1>Estat: " + estat + "</h1>" + "<a href='/alumne'>Tornar</a></body></html>";
                    ex.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    ex.sendResponseHeaders(200, h.getBytes().length);
                    OutputStream os = ex.getResponseBody();
                    os.write(h.getBytes());
                    os.close();
                });
        server.start();
        System.out.println("Servidor Formulari en http://localhost:8082");
    }


    static void enviarFitxer(HttpExchange ex, String ruta, String tipus, int c) throws Exception {
        String txt = Files.readString(Paths.get(ruta));
        ex.getResponseHeaders().set("Content-Type", tipus + "; charset=UTF-8");
        ex.sendResponseHeaders(c, txt.getBytes().length);
        OutputStream os = ex.getResponseBody();
        os.write(txt.getBytes());
        os.close();
    }

    static void enviar404(HttpExchange ex) throws Exception {
        String h = "<!DOCTYPE html><html lang=\"ca\"><head>" +
                "<meta charset=\"UTF-8\"><title>Error 404</title>" +
                "<link rel=\"stylesheet\" href=\"/style.css\">" +
                "</head><body><header><h1>Pàgina no trobada</h1></header>" +
                "<h1>Intenta-ho de nou...</h1>" +
                "<form action=\"/\" method=\"get\">" +
                "<button type=\"submit\">Tornar al inici</button>" +
                "</form>" +
                "</body></html>";
        ex.sendResponseHeaders(404, h.getBytes().length);
        OutputStream os = ex.getResponseBody();
        os.write(h.getBytes());
        os.close();
    }
}