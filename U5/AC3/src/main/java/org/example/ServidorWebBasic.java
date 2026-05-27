package org.example;

import java.io.*;
import java.nio.file.*;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;

public class ServidorWebBasic {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/",
                ex -> {
                    String p = ex.getRequestURI().getPath();
                    if (p.equals("/")) {
                        try {
                            enviarFitxer(ex, "web/index.html", "text/html", 200);
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
        server.createContext("/style.css",
                ex -> {
                    try {
                        enviarFitxer(ex, "web/style.css", "text/css", 200);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        server.start();
        System.out.println("Servidor Basic en http://localhost:8080");
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
        String h = "<html><body><h1>Error 404</h1><p>Pàgina no trobada</p></body></html>";
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        ex.sendResponseHeaders(404, h.getBytes().length);
        OutputStream os = ex.getResponseBody();
        os.write(h.getBytes());
        os.close();
    }
}