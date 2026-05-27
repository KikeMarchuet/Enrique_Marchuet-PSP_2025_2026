package com;

import java.sql.*;

public class ExempleJDBC {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/empresa";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connexió correcta!");

            String insertSQL = "INSERT INTO usuaris (nom, email) VALUES (?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setString(1, "Jorge");
                ps.setString(2, "inventat@email.com");
                int files = ps.executeUpdate();
                System.out.println("Files inserides: " + files);
            }

            String selectSQL = "SELECT * FROM usuaris";

            try (PreparedStatement ps = conn.prepareStatement(selectSQL);
                ResultSet rs = ps.executeQuery()) {
                System.out.println("\nLlistat d'usuaris:");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nom = rs.getString("nom");
                    String email = rs.getString("email");
                    System.out.println(id + " " + nom + " " + email);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error en la base de dades");
            e.printStackTrace();
        }
    }

}
