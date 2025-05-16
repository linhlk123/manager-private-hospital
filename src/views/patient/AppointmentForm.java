/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient;

import utils.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AppointmentForm extends JFrame {
    private JComboBox<String> doctorComboBox;
    private JTextField txtNgayHen;
    private JTextField txtDiadiem;
    private JTextArea txtTrieuchung;
    private JButton btnSubmit;

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

    public AppointmentForm(String patientId) {
        setTitle("Đặt lịch khám bệnh");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLocationRelativeTo(null); // canh giữa
        setLayout(new BorderLayout());

        // Set background
        ImagePanel backgroundPanel = new ImagePanel("src/views/patient/image/BackgroundDL.jpg");
        setContentPane(backgroundPanel);

        // Tiêu đề
        JLabel titleLabel = new JLabel("ĐẶT LỊCH HẸN KHÁM BỆNH", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font inputFont = new Font("Segoe UI", Font.PLAIN, 20);

        // Bác sĩ
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblDoctor = new JLabel("👨‍⚕️ Bác sĩ:");
        lblDoctor.setForeground(Color.BLACK);
        formPanel.add(lblDoctor, gbc);
        gbc.gridx = 1;
        doctorComboBox = new JComboBox<>();
        doctorComboBox.setFont(inputFont);
        doctorComboBox.setBackground(new Color(0xd9eef2));
        doctorComboBox.setOpaque(true);
        loadDoctors();
        formPanel.add(doctorComboBox, gbc);

        // Ngày hẹn
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblDate = new JLabel("📅 Ngày hẹn (yyyy-MM-dd):");
        lblDate.setForeground(Color.BLACK);
        formPanel.add(lblDate, gbc);
        gbc.gridx = 1;
        txtNgayHen = new JTextField();
        txtNgayHen.setFont(inputFont);
        txtNgayHen.setBackground(new Color(0xd9eef2));
        formPanel.add(txtNgayHen, gbc);

        // Địa điểm
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblLocation = new JLabel("🏥 Địa điểm khám:");
        lblLocation.setForeground(Color.BLACK);
        formPanel.add(lblLocation, gbc);
        gbc.gridx = 1;
        txtDiadiem = new JTextField("Offline");
        txtDiadiem.setFont(inputFont);
        txtDiadiem.setBackground(new Color(0xd9eef2));
        formPanel.add(txtDiadiem, gbc);

        // Triệu chứng
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblSymptoms = new JLabel("🤒 Triệu chứng:");
        lblSymptoms.setForeground(Color.BLACK);
        formPanel.add(lblSymptoms, gbc);
        gbc.gridx = 1;
        txtTrieuchung = new JTextArea(4, 20);
        txtTrieuchung.setFont(inputFont);
        txtTrieuchung.setBackground(new Color(0xd9eef2));
        txtTrieuchung.setOpaque(true);
        txtTrieuchung.setLineWrap(true);
        txtTrieuchung.setWrapStyleWord(true);
        txtTrieuchung.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(txtTrieuchung,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        formPanel.add(scrollPane, gbc);

        // Nút đặt lịch
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSubmit = new JButton("Đặt lịch");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnSubmit.setBackground(new Color(0x588EA7));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.setPreferredSize(new Dimension(120, 35));
        btnSubmit.addActionListener(e -> datLich(patientId));
        formPanel.add(btnSubmit, gbc);

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadDoctors() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, HOTENND FROM USERS WHERE ROLE = 'Bác sĩ'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                doctorComboBox.addItem(rs.getString("ID") + " - " + rs.getString("HOTENND"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải bác sĩ: " + e.getMessage());
        }
    }

    private void datLich(String patientId) {
        if (doctorComboBox.getSelectedItem() == null || txtNgayHen.getText().isEmpty() || txtDiadiem.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        java.sql.Date ngayHen;
        try {
            ngayHen = java.sql.Date.valueOf(txtNgayHen.getText());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ! Định dạng: yyyy-MM-dd");
            return;
        }

        String doctorId = doctorComboBox.getSelectedItem().toString().split(" - ")[0];
        String diadiem = txtDiadiem.getText();
        String trieuchung = txtTrieuchung.getText();

        try (Connection conn = DBConnection.getConnection()) {
            String malich = "LH" + System.currentTimeMillis() % 100000;
            String sql = "INSERT INTO LICHHEN (MALICH, MABN, MABS, NGAYHEN, DIADIEM, TRIEUCHUNG, TRANGTHAI) " +
                         "VALUES (?, ?, ?, ?, ?, ?, 'Chờ xác nhận')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, malich);
            ps.setString(2, patientId);
            ps.setString(3, doctorId);
            ps.setDate(4, ngayHen);
            ps.setString(5, diadiem);
            ps.setString(6, trieuchung);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Đặt lịch thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Đặt lịch thất bại.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi đặt lịch: " + e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new AppointmentForm("BN001").setVisible(true);
//        });
//    }
}

