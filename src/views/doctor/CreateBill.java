/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.doctor;

import utils.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class CreateBill extends JFrame {
    private JComboBox<String> maKhamComboBox, trangThaiTTComboBox;
    private JTextField txtTienKham;
    private JTextField txtTongTien;
    private JTextArea txtGhiChu;
    private JButton btnSubmit;
    private String doctorId;

    public class ImagePanel extends JPanel {
        private Image backgroundImage;

        public ImagePanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public CreateBill(String doctorId) {
        this.doctorId = doctorId;
        
        setTitle("Tạo hóa đơn khám bệnh");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLayout(new BorderLayout());

        // Set background
        ImagePanel backgroundPanel = new ImagePanel("src/views/doctor/image/bill.png");
        setContentPane(backgroundPanel);

        // Tiêu đề
        JLabel titleLabel = new JLabel("TẠO HÓA ĐƠN KHÁM BỆNH", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font inputFont = new Font("Segoe UI", Font.PLAIN, 20);

        // Mã khám
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblMaKham = new JLabel("Mã khám:");
        lblMaKham.setForeground(Color.BLACK);
        lblMaKham.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblMaKham, gbc);
        gbc.gridx = 1;
        maKhamComboBox = new JComboBox<>();
        maKhamComboBox.setFont(inputFont);
        maKhamComboBox.setBackground(new Color(0xd9eef2));
        maKhamComboBox.setOpaque(true);
        loadMaKham();
        formPanel.add(maKhamComboBox, gbc);

        // Tiền khám
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblTienKham = new JLabel("Tiền khám:");
        lblTienKham.setForeground(Color.BLACK);
        lblTienKham.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblTienKham, gbc);
        gbc.gridx = 1;
        txtTienKham = new JTextField();
        txtTienKham.setFont(inputFont);
        txtTienKham.setBackground(new Color(0xd9eef2));
        formPanel.add(txtTienKham, gbc);

        // Tổng tiền
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblTongTien = new JLabel("Tổng tiền:");
        lblTongTien.setForeground(Color.BLACK);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblTongTien, gbc);
        gbc.gridx = 1;
        txtTongTien = new JTextField();
        txtTongTien.setFont(inputFont);
        txtTongTien.setBackground(new Color(0xd9eef2));
        formPanel.add(txtTongTien, gbc);

        // Ghi chú
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setForeground(Color.BLACK);
        lblGhiChu.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblGhiChu, gbc);
        gbc.gridx = 1;
        txtGhiChu = new JTextArea(4, 20);
        txtGhiChu.setFont(inputFont);
        txtGhiChu.setBackground(new Color(0xd9eef2));
        txtGhiChu.setOpaque(true);
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        txtGhiChu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(txtGhiChu,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        formPanel.add(scrollPane, gbc);
        
        // Trạng thái thanh toán
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblTrangThaiTT = new JLabel("Trạng thái thanh toán:");
        lblTrangThaiTT.setForeground(Color.BLACK);
        lblTrangThaiTT.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblTrangThaiTT, gbc);

        gbc.gridx = 1;
        trangThaiTTComboBox = new JComboBox<>(new String[] {"Chưa thanh toán", "Đã thanh toán"});
        trangThaiTTComboBox.setFont(inputFont);
        trangThaiTTComboBox.setBackground(new Color(0xd9eef2));
        trangThaiTTComboBox.setOpaque(true);
        formPanel.add(trangThaiTTComboBox, gbc);


        // tạo hóa đơn
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSubmit = new JButton("Tạo hóa đơn");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnSubmit.setBackground(new Color(0x2B4A59));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.setPreferredSize(new Dimension(120, 35));
        btnSubmit.addActionListener(e -> taoHD(doctorId));
        
        btnSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSubmit.setBackground(new Color(0xff9800)); 
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSubmit.setBackground(new Color(0x2B4A59));
            }
        });

        formPanel.add(btnSubmit, gbc);

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadMaKham() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT MAKHAM FROM KHAM WHERE MABS = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rs.getString("MAKHAM");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải mã khám: " + e.getMessage());
        }
    }
    
    private String generateAppointmentId(Connection conn) {
        String prefix = "HDKB";
        String sql = "SELECT MAX(MAHDKB) FROM HOADON_KHAMBENH";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString(1);
                if (lastId != null) {
                    int num = Integer.parseInt(lastId.replace(prefix, ""));
                    return prefix + String.format("%03d", num + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefix + "001"; // Nếu chưa có lịch hẹn nào
    }
    
    private String generateNotificationId(Connection conn) {
        String prefix = "TB";
        String sql = "SELECT MAX(MATB) FROM THONGBAO";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString(1);
                if (lastId != null && lastId.startsWith(prefix)) {
                    int num = Integer.parseInt(lastId.substring(prefix.length()));
                    return prefix + String.format("%03d", num + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefix + "001"; // Nếu chưa có thông báo nào
    }

    private void taoHD(String doctorId) {
        if (maKhamComboBox.getSelectedItem() == null
                || txtTienKham.getText().isEmpty()
                || txtTongTien.getText().isEmpty()
                || txtGhiChu.getText().isEmpty()
                || trangThaiTTComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        String maKham = (String) maKhamComboBox.getSelectedItem();
        String tienKham = txtTienKham.getText();
        String tongTien = txtTongTien.getText();
        String ghiChu = txtGhiChu.getText();
        String trangThaiTT = (String) trangThaiTTComboBox.getSelectedItem();

        try (Connection conn = DBConnection.getConnection()) {
            String maHDKB = generateAppointmentId(conn);

            String sql = "INSERT INTO HOADON_KHAMBENH (MAHDKB, MAKHAM, TIENKHAM, TONGTIEN, GHICHU, TRANGTHAITT) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maHDKB);
                ps.setString(2, maKham);
                ps.setString(3, tienKham);
                ps.setString(4, tongTien);
                ps.setString(5, ghiChu);
                ps.setString(6, trangThaiTT);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    // Lấy ngày hiện tại
                    String ngayTao = java.time.LocalDateTime.now().toString().replace("T", " ").substring(0, 19);
                    String noidung = "Bạn đã tạo hóa đơn thành công vào ngày " + ngayTao + ".";

                    String sqlTB = "INSERT INTO THONGBAO (MATB, USER_ID, NOIDUNG, LOAI) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement psTB = conn.prepareStatement(sqlTB)) {
                        psTB.setString(1, generateNotificationId(conn));
                        psTB.setString(2, doctorId); // doctorId là mã người nhận thông báo
                        psTB.setString(3, noidung);
                        psTB.setString(4, "Hóa đơn");
                        psTB.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(this, "Đặt lịch thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Đặt lịch thất bại.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi đặt lịch: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CreateBill("U001").setVisible(true);
        });
    }
}
