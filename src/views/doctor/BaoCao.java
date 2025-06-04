/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.doctor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.general.DefaultPieDataset;

import utils.DBConnection;

public class BaoCao extends JFrame {
    private JPanel contentPanel;
    private CardLayout contentLayout;
    private JLabel khamBenhLabel, lichHenLabel, donThuocLabel, benhNhanLabel, hoaDonLabel;
    private String doctorId;

    public BaoCao(String doctorId) {
        this.doctorId = doctorId;
        
        setTitle("📄 Danh sách hóa đơn");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLayout(new BorderLayout());
        
        // Menu đầu trang
        JPanel topMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        topMenu.setBackground(new Color(0xe8faf8));

        khamBenhLabel = createMenuLabel("Khám bệnh");
        lichHenLabel = createMenuLabel("Lịch hẹn");
        donThuocLabel = createMenuLabel("Đơn thuốc");
        benhNhanLabel = createMenuLabel("Bệnh nhân");
        hoaDonLabel = createMenuLabel("Hóa đơn");

        topMenu.add(khamBenhLabel);
        topMenu.add(lichHenLabel);
        topMenu.add(donThuocLabel);
        topMenu.add(benhNhanLabel);
        topMenu.add(hoaDonLabel);

        add(topMenu, BorderLayout.NORTH);

        // Panel nội dung
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);

        contentPanel.add(createKhamBenhPanel(), "khambenh");
        contentPanel.add(createLichHenPanel(), "lichhen");
        contentPanel.add(createDonThuocPanel(), "donthuoc");
        contentPanel.add(createBenhNhanPanel(), "benhnhan");
        contentPanel.add(createDonThuocPanel(), "hoadon");

        add(contentPanel, BorderLayout.CENTER);

        switchTab("khambenh");
        addMenuListeners();
    }
    
     private JLabel createMenuLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(new Color(0x2B4A59));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    private void switchTab(String tabName) {
        resetMenuStyle();
        contentLayout.show(contentPanel, tabName);
        if (tabName.equals("khambenh")) highlightLabel(khamBenhLabel);
        else if (tabName.equals("lichhen")) highlightLabel(lichHenLabel);
        else if (tabName.equals("donthuoc")) highlightLabel(donThuocLabel);
        else if (tabName.equals("benhnhan")) highlightLabel(benhNhanLabel);
        else if (tabName.equals("hoadon")) highlightLabel(hoaDonLabel);
    }

    private void highlightLabel(JLabel label) {
        label.setForeground(new Color(0xff9800));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0xff9800)));
    }

    private void resetMenuStyle() {
        JLabel[] labels = {khamBenhLabel, lichHenLabel, donThuocLabel, benhNhanLabel, hoaDonLabel};
        for (JLabel label : labels) {
            label.setForeground(new Color(0x2B4A59));
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
            label.setBorder(null);
        }
    }

    private void addMenuListeners() {
        khamBenhLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("khambenh");
            }
        });
        lichHenLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("lichhen");
            }
        });
        donThuocLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("donthuoc");
            }
        });
        benhNhanLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("benhnhan");
            }
        });
        hoaDonLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("hoadon");
            }
        });
    }
    
    private JPanel createKhamBenhPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DBConnection.getConnection()) {
            LocalDate startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            LocalDate today = LocalDate.now();
            
            String sql = "SELECT MAKHAM, COUNT(*) AS SOLAN_KHAM FROM KHAM WHERE NGAYKHAM >= ? AND NGAYKHAM <=? GROUP BY MAKHAM";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(startOfYear));
            ps.setDate(2, java.sql.Date.valueOf(today));
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maSP = rs.getString("MAKHAM");
                int soLanKe = rs.getInt("SOLAN_KHAM");
                dataset.addValue(soLanKe, "Số lần khám", maSP);
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu: " + e.getMessage());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê số lần khám bệnh trong năm nay",
                "Mã khám", "Số lần khám",
                dataset
        );
        
        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                Color[] colors = {
                    new Color(228, 139, 139)
                };
                return colors[column % colors.length];
            }
        };
        renderer.setSeriesPaint(0, new Color(0xCDE8E5));
        plot.setRenderer(renderer);
        
        barChart.setBackgroundPaint(new Color(0xCDE8E5));// màu nền ngoài biểu đồ
        plot.setBackgroundPaint(Color.WHITE); //màu nền trong biểu đồ
        plot.setRangeGridlinePaint(Color.GRAY); //màu đường kẻ đứt nét ngang

        return new ChartPanel(barChart);
    }
    
    private JPanel createLichHenPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DBConnection.getConnection()) {
            LocalDate startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            LocalDate today = LocalDate.now();
            
            String sql = "SELECT MALICH, COUNT(*) AS SOLAN_DATLICH FROM LICHHEN WHERE NGAYDAT >= ? AND NGAYDAT <=? GROUP BY MALICH";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(startOfYear));
            ps.setDate(2, java.sql.Date.valueOf(today));
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maSP = rs.getString("MALICH");
                int soLanKe = rs.getInt("SOLAN_DATLICH");
                dataset.addValue(soLanKe, "Số lần đặt lịch hẹn", maSP);
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu: " + e.getMessage());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê số lần khám bệnh trong năm nay",
                "Mã khám", "Số lần đặt lịch hẹn",
                dataset
        );
        
        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                Color[] colors = {
                    new Color(196, 220, 246)
                };
                return colors[column % colors.length];
            }
        };
        renderer.setSeriesPaint(0, new Color(0xCDE8E5));
        plot.setRenderer(renderer);
        
        barChart.setBackgroundPaint(new Color(0xCDE8E5));// màu nền ngoài biểu đồ
        plot.setBackgroundPaint(Color.WHITE); //màu nền trong biểu đồ
        plot.setRangeGridlinePaint(Color.GRAY); //màu đường kẻ đứt nét ngang

        return new ChartPanel(barChart);
    }
    
    private JPanel createDonThuocPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DBConnection.getConnection()) {
            LocalDate startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            LocalDate today = LocalDate.now();
            
            String sql = "SELECT MASP, COUNT(*) AS SOLAN_KE FROM CTDT C JOIN DONTHUOC_DONTHUOCYC D ON C.MADT = D.MADT "
                         + "WHERE NGAYBAN >= ? AND NGAYBAN <=? GROUP BY MASP";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(startOfYear));
            ps.setDate(2, java.sql.Date.valueOf(today));
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String maSP = rs.getString("MASP");
                int soLanKe = rs.getInt("SOLAN_KE");
                dataset.addValue(soLanKe, "Số lần kê", maSP);
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu: " + e.getMessage());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê thuốc đã kê trong năm nay",
                "Tên sản phẩm", "Số lần kê",
                dataset
        );
        
        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                Color[] colors = {
                    new Color(0xCDE8E5)
                };
                return colors[column % colors.length];
            }
        };
        renderer.setSeriesPaint(0, new Color(0xCDE8E5));
        plot.setRenderer(renderer);
        
        barChart.setBackgroundPaint(new Color(0xCDE8E5));// màu nền ngoài biểu đồ
        plot.setBackgroundPaint(Color.WHITE); //màu nền trong biểu đồ
        plot.setRangeGridlinePaint(Color.GRAY); //màu đường kẻ đứt nét ngang
        
        return new ChartPanel(barChart);
    }

    private JPanel createBenhNhanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xCDE8E5));

        JLabel totalLabel = new JLabel("Tổng số bệnh nhân: " + getTongSoBenhNhan(), SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 22));
        totalLabel.setForeground(new Color(0x2B4A59));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panel.add(totalLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Giới tính", createPieChart("Giới tính",
                "SELECT GIOITINH, COUNT(*) AS SL FROM USERS WHERE ROLE = 'Bệnh nhân' GROUP BY GIOITINH"));

        tabbedPane.addTab("Bảo hiểm y tế", createPieChart("Bảo hiểm y tế",
                "SELECT COALESCE(SOBHYT, 'Không') AS BAOHIEM, COUNT(*) AS SL FROM BENHNHAN GROUP BY SOBHYT"));

        tabbedPane.addTab("Nhóm máu", createLineChart("Nhóm máu",
                "SELECT NHOMMAU, COUNT(*) AS SL FROM BENHNHAN GROUP BY NHOMMAU"));

        tabbedPane.addTab("Độ tuổi", createLineChart("Độ tuổi",
                "SELECT CASE " +
                "WHEN FLOOR(MONTHS_BETWEEN(SYSDATE, NGAYSINH)/12) < 18 THEN 'Dưới 18' " +
                "WHEN FLOOR(MONTHS_BETWEEN(SYSDATE, NGAYSINH)/12) BETWEEN 18 AND 35 THEN '18-35' " +
                "WHEN FLOOR(MONTHS_BETWEEN(SYSDATE, NGAYSINH)/12) BETWEEN 36 AND 60 THEN '36-60' " +
                "ELSE 'Trên 60' END AS DO_TUOI, COUNT(*) AS SL " +
                "FROM USERS WHERE ROLE = 'Bệnh nhân' GROUP BY " +
                "CASE " +
                "WHEN FLOOR(MONTHS_BETWEEN(SYSDATE, NGAYSINH)/12) < 18 THEN 'Dưới 18' " +
                "WHEN FLOOR(MONTHS_BETWEEN(SYSDATE, NGAYSINH)/12) BETWEEN 18 AND 35 THEN '18-35' " +
                "WHEN FLOOR(MONTHS_BETWEEN(SYSDATE, NGAYSINH)/12) BETWEEN 36 AND 60 THEN '36-60' " +
                "ELSE 'Trên 60' END"));

        tabbedPane.addTab("Dị ứng phổ biến", createBarChart("Dị ứng phổ biến",
                "SELECT DIUNG, COUNT(*) AS SL FROM BENHNHAN GROUP BY DIUNG"));

        tabbedPane.addTab("Lịch sử bệnh lý", createBarChart("Lịch sử bệnh lý",
                "SELECT LICHSU_BENHLY AS BENH, COUNT(*) AS SL FROM BENHNHAN GROUP BY LICHSU_BENHLY"));

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }


    private int getTongSoBenhNhan() {
        int total = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM BENHNHAN";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private ChartPanel createPieChart(String title, String sql) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dataset.setValue(rs.getString(1), rs.getInt(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, true, false);
        chart.setBackgroundPaint(new Color(0xCDE8E5));

        // Đặt nền cho phần plot (biểu đồ bên trong)
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        //plot.setOutlineVisible(false); // Ẩn viền ngoài nếu muốn
        
         // Gán màu cụ thể cho giới tính
        for (Object key : dataset.getKeys()) {
            String label = key.toString().toLowerCase();
            if (label.contains("nam")) {
                plot.setSectionPaint((Comparable) key, new Color(196, 220, 246)); 
            } else if (label.contains("nữ")) {
                plot.setSectionPaint((Comparable) key, new Color(246, 196, 235));
            } else if (label.contains("không")) {
                plot.setSectionPaint((Comparable) key, new Color(237, 221, 250)); 
            } else {
                plot.setSectionPaint((Comparable) key, new Color(226, 198, 247)); 
            }
            
        }
        
        return new ChartPanel(chart);
    }

    private ChartPanel createLineChart(String title, String sql) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dataset.addValue(rs.getInt(2), title, rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createLineChart(
                title, "", "Số lượng", dataset,
                PlotOrientation.VERTICAL, false, true, false);
        chart.setBackgroundPaint(new Color(0xCDE8E5));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        return new ChartPanel(chart);
    }

    private ChartPanel createBarChart(String title, String sql) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dataset.addValue(rs.getInt(2), title, rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                title, "", "Số lượng",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, new Color(102, 204, 204));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        chart.setBackgroundPaint(new Color(0xCDE8E5));
        return new ChartPanel(chart);
    }


     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BaoCao("U001").setVisible(true);
        });
    }
}

