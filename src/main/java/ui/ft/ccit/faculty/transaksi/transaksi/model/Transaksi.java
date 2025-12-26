package ui.ft.ccit.faculty.transaksi.transaksi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transaksi")
public class Transaksi {

    @Id
    @Column(name = "kode_transaksi", length = 4)
    private String kodeTransaksi;

    @Column(name = "tgl_transaksi")
    private LocalDateTime tglTransaksi;

    @Column(name = "id_pelanggan", length = 4)
    private String idPelanggan;

    @Column(name = "id_karyawan", length = 4)
    private String idKaryawan;

    // ✅ TAMBAHKAN fetch = FetchType.EAGER
    @OneToMany(mappedBy = "transaksi", cascade = CascadeType.ALL, 
               orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetailTransaksi> details = new ArrayList<>();

    protected Transaksi() {
        // for JPA
    }

    public Transaksi(String kodeTransaksi, LocalDateTime tglTransaksi, 
                     String idPelanggan, String idKaryawan) {
        this.kodeTransaksi = kodeTransaksi;
        this.tglTransaksi = tglTransaksi;
        this.idPelanggan = idPelanggan;
        this.idKaryawan = idKaryawan;
    }

    // Helper method untuk add detail
    public void addDetail(DetailTransaksi detail) {
        details.add(detail);
        detail.setTransaksi(this);
    }

    // Getters & Setters

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public LocalDateTime getTglTransaksi() {
        return tglTransaksi;
    }

    public void setTglTransaksi(LocalDateTime tglTransaksi) {
        this.tglTransaksi = tglTransaksi;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(String idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public List<DetailTransaksi> getDetails() {
        return details;
    }

    public void setDetails(List<DetailTransaksi> details) {
        this.details = details;
    }
}