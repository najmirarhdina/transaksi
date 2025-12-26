package ui.ft.ccit.faculty.transaksi.transaksi.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailTransaksiRepository extends JpaRepository<DetailTransaksi, DetailTransaksiId> {

    // ========== BASIC QUERIES ==========
    
    /**
     * Cari semua detail berdasarkan kode transaksi
     * Contoh: findByKodeTransaksi("T001")
     * Return: semua item dalam transaksi T001
     */
    List<DetailTransaksi> findByKodeTransaksi(String kodeTransaksi);

    /**
     * Cari semua detail berdasarkan id barang
     * Contoh: findByIdBarang("B001")
     * Return: semua transaksi yang beli barang B001
     */
    List<DetailTransaksi> findByIdBarang(String idBarang);

    /**
     * Cari detail spesifik berdasarkan kode transaksi DAN id barang
     * Contoh: findByKodeTransaksiAndIdBarang("T001", "B001")
     * Return: detail barang B001 di transaksi T001
     */
    DetailTransaksi findByKodeTransaksiAndIdBarang(String kodeTransaksi, String idBarang);

    // ========== AGGREGATE QUERIES ==========
    
    /**
     * Hitung total jumlah barang yang terjual berdasarkan id barang
     * Contoh: sumJumlahByIdBarang("B001")
     * Return: total qty barang B001 yang terjual
     */
    @Query("SELECT SUM(d.jumlah) FROM DetailTransaksi d WHERE d.idBarang = :idBarang")
    Long sumJumlahByIdBarang(@Param("idBarang") String idBarang);

    /**
     * Hitung berapa kali barang tertentu muncul di transaksi
     * Contoh: countByIdBarang("B001")
     * Return: berapa kali barang B001 dibeli
     */
    long countByIdBarang(String idBarang);

    /**
     * Hitung berapa banyak item berbeda dalam satu transaksi
     * Contoh: countByKodeTransaksi("T001")
     * Return: jumlah jenis barang berbeda di transaksi T001
     */
    long countByKodeTransaksi(String kodeTransaksi);

    // ========== ADVANCED QUERIES ==========
    
    /**
     * Cari detail transaksi dengan jumlah lebih dari nilai tertentu
     * Contoh: findByJumlahGreaterThan(10)
     * Return: semua detail yang qty-nya > 10
     */
    List<DetailTransaksi> findByJumlahGreaterThan(Short jumlah);

    /**
     * Cari detail transaksi dengan jumlah dalam range tertentu
     * Contoh: findByJumlahBetween(5, 10)
     * Return: semua detail dengan qty antara 5-10
     */
    List<DetailTransaksi> findByJumlahBetween(Short min, Short max);

    /**
     * Cari barang terlaris (paling banyak terjual)
     * Return: list [idBarang, totalQty] diurutkan descending
     */
    @Query("SELECT d.idBarang, SUM(d.jumlah) as total " +
           "FROM DetailTransaksi d " +
           "GROUP BY d.idBarang " +
           "ORDER BY total DESC")
    List<Object[]> findTopSellingProducts();

    /**
     * Cari barang yang sering dibeli bersamaan dengan barang tertentu
     * Contoh: findFrequentlyBoughtTogether("B001")
     * Return: list barang yang sering dibeli bareng dengan B001
     */
    @Query("SELECT DISTINCT d2.idBarang, COUNT(d2.idBarang) as frequency " +
           "FROM DetailTransaksi d1 " +
           "JOIN DetailTransaksi d2 ON d1.kodeTransaksi = d2.kodeTransaksi " +
           "WHERE d1.idBarang = :idBarang AND d2.idBarang != :idBarang " +
           "GROUP BY d2.idBarang " +
           "ORDER BY frequency DESC")
    List<Object[]> findFrequentlyBoughtTogether(@Param("idBarang") String idBarang);

    // ========== DELETE QUERIES (OPSIONAL - HATI-HATI!) ==========
    
    /**
     * Hapus semua detail transaksi berdasarkan kode transaksi
     * HATI-HATI: ini akan hapus semua item dalam transaksi!
     * Contoh: deleteByKodeTransaksi("T001")
     */
    void deleteByKodeTransaksi(String kodeTransaksi);

    /**
     * Hapus detail transaksi untuk barang tertentu
     * HATI-HATI: ini akan hapus semua transaksi yang ada barang ini!
     * Contoh: deleteByIdBarang("B001")
     */
    void deleteByIdBarang(String idBarang);
}