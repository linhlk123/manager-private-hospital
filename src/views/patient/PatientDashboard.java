/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import views.LoginForm;
import views.patient.AppointmentForm;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class PatientDashboard extends JFrame {
    private String currentPatient;
    private JPanel contentPanel;  // panel chứa các màn hình chức năng
    private CardLayout cardLayout;

    public PatientDashboard(String hoTen) {
        this.currentPatient = hoTen;

        setTitle("Dashboard Bệnh nhân");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLocationRelativeTo(null); // canh giữa
        setLayout(new BorderLayout());

        // Thanh menu trái (ví dụ đơn giản)
        JPanel sideMenu = new JPanel();
        sideMenu.setPreferredSize(new Dimension(300, getHeight()));
        sideMenu.setBackground(new Color(0x2B4A59));
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));

        sideMenu.add(Box.createVerticalStrut(20)); // thêm trước khi add menuLabel

        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setForeground(Color.WHITE);
        menuLabel.setFont(new Font("Arial", Font.BOLD, 20));
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        menuLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sideMenu.add(menuLabel);

        sideMenu.add(Box.createVerticalStrut(20)); // khoảng cách sau MENU

        // Các mục menu bên trái (để demo, không dùng để chuyển giao diện)
        String[] menuItems = {
            "Hồ sơ cá nhân",
            "Thông báo",
            "Điều khoản sử dụng",
            "An toàn bảo mật",
            "Liên hệ",
            "Đăng xuất"
        };

        for (String item : menuItems) {
            JLabel lbl = new JLabel(item);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.PLAIN, 16));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lbl.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            // Hover effect
            lbl.addMouseListener(new MouseAdapter() {
                Color normal = lbl.getForeground();
                @Override
                public void mouseEntered(MouseEvent e) {
                    lbl.setForeground(new Color(0x1ABC9C));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    lbl.setForeground(normal);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Clicked menu trái: " + item);
                    if(item.equals("Đăng xuất")){
                        dispose(); // đóng frame
                        // bạn có thể thêm logic chuyển về màn hình login ở đây
                    }
                }
            });

            sideMenu.add(lbl);
            sideMenu.add(Box.createVerticalStrut(10));
        }

        add(sideMenu, BorderLayout.WEST);

        // Panel chính: sử dụng CardLayout để chuyển đổi giao diện chức năng
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Tạo các panel chức năng tương ứng
        contentPanel.add(createWelcomePanel(), "Welcome");
        contentPanel.add(createProfilePanel(), "Hồ sơ cá nhân");
        contentPanel.add(createNotificationPanel(), "Thông báo");
        contentPanel.add(createTermsPanel(), "Điều khoản sử dụng");
        contentPanel.add(createSecurityPanel(), "An toàn bảo mật");
        contentPanel.add(createContactPanel(), "Liên hệ");

        // Tạo panel các chức năng chính dạng container giống như trước
        contentPanel.add(createMainFunctionPanel(), "MainFunctions");

        // Mặc định hiển thị màn hình chính (chức năng)
        cardLayout.show(contentPanel, "MainFunctions");

        add(contentPanel, BorderLayout.CENTER);
    }

    // Màn hình chào mừng (nếu muốn)
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("Chào mừng bệnh nhân: " + currentPatient, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);
        return panel;
    }

    // Các màn hình demo chức năng khác
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Màn hình Hồ sơ cá nhân"));
        return panel;
    }

    private JPanel createNotificationPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Màn hình Thông báo"));
        return panel;
    }

    private JPanel createTermsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Màn hình Điều khoản sử dụng"));
        return panel;
    }

    private JPanel createSecurityPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Màn hình An toàn bảo mật"));
        return panel;
    }

    private JPanel createContactPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel("Màn hình Liên hệ"));
        return panel;
    }

    // Panel chính chứa các container chức năng như trước
    private JPanel createMainFunctionPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        JPanel wrapper = new JPanel(new GridLayout(2, 3, 30, 30));
        wrapper.setPreferredSize(new Dimension(700, 400));
        wrapper.setBackground(Color.WHITE);

        // Các container chức năng
        wrapper.add(createFunctionContainer("Tra cứu thuốc", "src/views/patient/image/find.png"));
        wrapper.add(createFunctionContainer("Đặt lịch hẹn", "src/views/patient/image/calendar.png"));
        wrapper.add(createFunctionContainer("Lịch sử khám", "src/views/patient/image/history.png"));
        wrapper.add(createFunctionContainer("Mua thuốc", "src/views/patient/image/drug.png"));
        wrapper.add(createFunctionContainer("Dịch vụ khám", "src/views/patient/image/service.png"));

        centerPanel.add(wrapper);

        // Thêm label chào mừng trên đầu centerPanel
        JLabel welcomeLabel = new JLabel("Chào mừng bệnh nhân: " + currentPatient, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(welcomeLabel, BorderLayout.NORTH);
        centerWrapper.add(centerPanel, BorderLayout.CENTER);

        return centerWrapper;
    }

    private JPanel createFunctionContainer(String title, String iconPath) {
        RoundedPanel panel = new RoundedPanel(20);
        panel.setBackground(Color.WHITE);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Icon
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img), JLabel.CENTER);

        JLabel textLabel = new JLabel(title, JLabel.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(iconLabel, BorderLayout.CENTER);
        panel.add(textLabel, BorderLayout.SOUTH);

        // Hover hiệu ứng đổi màu nền
        Color normalColor = Color.WHITE;
        Color hoverColor = new Color(0xd9eef2);
        panel.setBackground(normalColor);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(normalColor);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleMenuClick(title);
            }
        });

        return panel;
    }

    private void handleMenuClick(String title) {
        System.out.println("Bạn đã chọn: " + title);
        
        // Chuyển sang panel tương ứng trong contentPanel
        switch (title) {
            case "Hồ sơ cá nhân":
                cardLayout.show(contentPanel, "Hồ sơ cá nhân");
                break;
            case "Thông báo":
                cardLayout.show(contentPanel, "Thông báo");
                break;
            case "Điều khoản sử dụng":
                cardLayout.show(contentPanel, "Điều khoản sử dụng");
                break;
            case "An toàn bảo mật":
                cardLayout.show(contentPanel, "An toàn bảo mật");
                break;
            case "Liên hệ":
                cardLayout.show(contentPanel, "Liên hệ");
                break;
            case "Đặt lịch hẹn":
                new AppointmentForm("Princess").setVisible(true);
                break;

            case "Tra cứu thuốc":
                cardLayout.show(contentPanel, "Tra cứu thuốc");
                break;
            case "Lịch sử khám":
                cardLayout.show(contentPanel, "Lịch sử khám");
                break;
            case "Mua thuốc":
                cardLayout.show(contentPanel, "Mua thuốc");
                break;
            case "Dịch vụ khám":
                cardLayout.show(contentPanel, "Dịch vụ khám");
                break;
            default:
                cardLayout.show(contentPanel, "MainFunctions");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PatientDashboard("Nguyễn Văn A").setVisible(true);
        });
    }
}

// Custom JPanel bo góc
class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Nền trắng bo góc
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);

        // Viền xám nhẹ
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }
}