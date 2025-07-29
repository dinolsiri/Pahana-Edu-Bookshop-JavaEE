package com.pahanaedu.util;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                System.out.println("✅ Database connection successful!");
            } else {
                System.out.println("❌ Database connection failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
