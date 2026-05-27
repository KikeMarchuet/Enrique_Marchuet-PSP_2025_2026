package com;

import java.sql.*;
import java.util.Scanner;

public class Exercici {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/empresa";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            System.out.println("Connexió correcta!");

            int opcio = -1;
            Scanner sc = new Scanner(System.in);
            int files = 0;
            int el_id = 0;

            while (opcio != 0) {
                System.out.println("\nMENU PRINCIPAL");
                System.out.println("1. Inserir usuari");
                System.out.println("2. Mostrar usuaris");
                System.out.println("3. Actualitzar usuari");
                System.out.println("4. Eliminar usuari");
                System.out.println("0. Eixir");
                System.out.print("Opcio: ");

                try {
                    opcio = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                switch (opcio) {
                    case 1:
                        insertaUsuari(conn, preguntaNom(), preguntaEmail());
                        break;
                    case 2:
                        mostraUsuaris(conn);
                        break;
                    case 3:
                        el_id = preguntaId();
                        if (existe(conn, el_id)) {
                            actualitzaUsuari(conn, el_id, preguntaNom(), preguntaEmail());
                        } else {
                            System.out.println("L'usuari no existeix!");
                        }
                        break;
                    case 4:
                        el_id = preguntaId();
                        if (existe(conn, el_id)) {
                            eliminaUsuari(conn, el_id);
                        } else {
                            System.out.println("L'usuari no existeix!");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opció invalida!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en la base de dades");
            e.printStackTrace();
        }

    }

    public static boolean existe(Connection conn, int el_id) {
        Boolean existe = false;
        String selectSQL = "SELECT * FROM usuaris WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
            ps.setInt(1, el_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                existe = true;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return existe;

    }

    public static String preguntaNom() {
        System.out.print("Nom del usuari: ");
        String nom = new Scanner(System.in).nextLine();
        return nom;
    }

    public static String preguntaEmail() {
        System.out.print("Email del usuari: ");
        String email = new Scanner(System.in).nextLine();
        return email;
    }

    public static int preguntaId() {
        System.out.print("ID del usuari: ");
        int id = new Scanner(System.in).nextInt();
        return id;
    }

    public static void insertaUsuari(Connection conn, String nom, String email) {
        String sql = "INSERT INTO usuaris (nom, email) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, email);
            int files = ps.executeUpdate();
            System.out.println("Usuaris inserits: " + files);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void mostraUsuaris(Connection conn) {
        String sql = "SELECT * FROM usuaris";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("\nLlistat d'usuaris:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String email = rs.getString("email");
                System.out.println(id + " " + nom + " " + email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void actualitzaUsuari(Connection conn, int id, String nom, String email) {
        String sql = "UPDATE usuaris SET nom = ?, email = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, email);
            ps.setInt(3, id);
            int files = ps.executeUpdate();
            System.out.println("Usuaris editats: " + files);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void eliminaUsuari(Connection conn, int id) {
        String sql = "DELETE FROM usuaris WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int files = ps.executeUpdate();
            System.out.println("Usuaris eliminats: " + files);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

