/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DBConnection;

public class ProductView extends JFrame {
    private JTable productTable;
    private JTextField tfSearch;
    private DefaultTableModel model;

    public ProductView() throws SQLException, ClassNotFoundException {
        setTitle("📄 Danh sách sản phẩm");
        setSize(1090, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLocationRelativeTo(null); // canh giữa
        initComponents();
        loadProducts("");
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // ===== TOP PANEL: TÌM KIẾM =====
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        tfSearch = new JTextField();
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton btnSearch = new JButton("🔍Tìm kiếm");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topPanel.add(new JLabel("Nhập tên sản phẩm: "), BorderLayout.WEST);
        topPanel.add(tfSearch, BorderLayout.CENTER);
        topPanel.add(btnSearch, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== TABLE: DANH SÁCH SẢN PHẨM =====
        String[] columns = {
            "Mã SP", "Tên SP", "Nhà PP", "ĐVT", "Thành phần", "Lưu ý",
            "Cách dùng", "Bảo quản", "NSX", "HSD", "Đơn giá", "Ưu đãi", "Mô tả"
        };
        model = new DefaultTableModel(columns, 0);
        productTable = new JTable(model);
        productTable.setRowHeight(28);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // ===== SỰ KIỆN TÌM KIẾM =====
        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim();
            try {
                loadProducts(keyword);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // ===== CLICK XEM CHI TIẾT =====
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    showProductDetails(selectedRow);
                }
            }
        });
    }

    private void loadProducts(String keyword) throws SQLException, ClassNotFoundException {
        model.setRowCount(0);
        String sql;
        boolean isSearching = keyword != null && !keyword.trim().isEmpty();

        if (isSearching) {
            sql = "SELECT * FROM SANPHAM WHERE UPPER(TENSP) LIKE UPPER(?)";
        } else {
            sql = "SELECT * FROM SANPHAM";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (isSearching) {
                ps.setString(1, "%" + keyword + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
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
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void showProductDetails(int row) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < model.getColumnCount(); i++) {
            sb.append(model.getColumnName(i)).append(": ")
              .append(model.getValueAt(row, i)).append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Chi tiết sản phẩm", JOptionPane.INFORMATION_MESSAGE);
    }
}
