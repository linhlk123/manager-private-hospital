/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import models.Product;
import utils.DBConnection;

import java.sql.*;
import java.util.*;

public class ProductDAO {
    public static List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product(
                    rs.getString("MASP"),
                    rs.getString("TENSP"),
                    rs.getString("TENNPP"),
                    rs.getString("DVT"),
                    rs.getString("THANHPHAN"),
                    rs.getString("LUUY"),
                    rs.getString("CACHDUNG"),
                    rs.getString("BAOQUAN"),
                    rs.getDate("NSX"),
                    rs.getDate("HSD"),
                    rs.getDouble("DONGIA"),
                    rs.getInt("UUDAI"),
                    rs.getString("MOTA")
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}

