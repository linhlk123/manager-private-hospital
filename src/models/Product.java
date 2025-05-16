/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.Date;

public class Product {
    private String maSP;
    private String tenSP;
    private String tenNPP;
    private String dvt;
    private String thanhPhan;
    private String luuY;
    private String cachDung;
    private String baoQuan;
    private Date nsx;
    private Date hsd;
    private double donGia;
    private int uuDai;
    private String moTa;

    public Product(String maSP, String tenSP, String tenNPP, String dvt,
                   String thanhPhan, String luuY, String cachDung, String baoQuan,
                   Date nsx, Date hsd, double donGia, int uuDai, String moTa) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.tenNPP = tenNPP;
        this.dvt = dvt;
        this.thanhPhan = thanhPhan;
        this.luuY = luuY;
        this.cachDung = cachDung;
        this.baoQuan = baoQuan;
        this.nsx = nsx;
        this.hsd = hsd;
        this.donGia = donGia;
        this.uuDai = uuDai;
        this.moTa = moTa;
    }

    public Object[] toRowTable() {
        return new Object[] {
            maSP, tenSP, tenNPP, dvt, donGia, uuDai, nsx, hsd
        };
    }

    // Thêm getter/setter nếu cần
}

