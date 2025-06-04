/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class AppointmentDAO {
    private Connection conn;

    public AppointmentDAO(Connection conn) {
        this.conn = conn;
    }

    public DefaultTableModel getAppointmentsByStatus(String[] statuses) {
        String sql = "SELECT NGAYHEN, KHOA, TRANGTHAI FROM LICHHEN WHERE TRANGTHAI IN (";

        // Tạo chuỗi câu hỏi (?) cho prepared statement
        String[] questionMarks = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            questionMarks[i] = "?";
        }
        sql += String.join(",", questionMarks) + ") ORDER BY NGAYHEN";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < statuses.length; i++) {
                ps.setString(i + 1, statuses[i]);
            }

            ResultSet rs = ps.executeQuery();

            // Tạo table model
            Vector<String> columnNames = new Vector<>();
            columnNames.add("Ngày");
            columnNames.add("Giờ");
            columnNames.add("Khoa");
            columnNames.add("Trạng thái");

            Vector<Vector<Object>> data = new Vector<>();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                Timestamp ngayHen = rs.getTimestamp("NGAYHEN");

                // Format ngày và giờ từ Timestamp
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
                java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm");

                String ngay = dateFormat.format(ngayHen);
                String gio = timeFormat.format(ngayHen);

                row.add(ngay);
                row.add(gio);
                row.add(rs.getString("KHOA"));
                row.add(rs.getString("TRANGTHAI"));
                data.add(row);
            }

            return new DefaultTableModel(data, columnNames);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

