package ui.ft.ccit.faculty.transaksi.transaksi.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TransaksiRequest {

    private String kodeTransaksi;
    private LocalDateTime tglTransaksi;
    private String idPelanggan;
    private String idKaryawan;
    private List<DetailItem> details;

    // Inner class untuk detail
    public static class DetailItem {
        private String idBarang;
        private Short jumlah;

        // Getters & Setters
        public String getIdBarang() {
            return idBarang;
        }

        public void setIdBarang(String idBarang) {
            this.idBarang = idBarang;
        }

        public Short getJumlah() {
            return jumlah;
        }

        public void setJumlah(Short jumlah) {
            this.jumlah = jumlah;
        }
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

    public List<DetailItem> getDetails() {
        return details;
    }

    public void setDetails(List<DetailItem> details) {
        this.details = details;
    }
}