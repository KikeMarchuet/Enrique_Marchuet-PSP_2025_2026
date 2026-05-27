package com;

import java.sql.*;

public class SetupDB {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/empresa";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            System.out.println("Connexió al servidor OK");

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS empresa");
            System.out.println("BD creada");
            stmt.executeUpdate("USE empresa");

            stmt.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS usuaris (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            nom VARCHAR(100),
                            email VARCHAR(100)
                        )
                    """);

            System.out.println("Taula creada");

            stmt.executeUpdate("""
                    INSERT INTO usuaris (nom, email) VALUES
                    ('Anna', 'anna@email.com'),
                    ('Joan', 'joan@email.com')
                    """);

            System.out.println("Dades inserides");

            ResultSet rs = stmt.executeQuery("SELECT * FROM usuaris");
            System.out.println("\nUsuaris:");

            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " +
                        rs.getString("nom") + " - " +
                        rs.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}