package ui.ft.ccit.faculty.transaksi.karyawan.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KaryawanRepository extends JpaRepository<Karyawan, String> {

    // Cari berdasarkan nama mengandung kata tertentu
    List<Karyawan> findByNamaContainingIgnoreCase(String keyword);

    // Cari berdasarkan jenis kelamin
    List<Karyawan> findByJenisKelamin(String jenisKelamin);

    // Cari karyawan dengan gaji di atas nilai tertentu
    List<Karyawan> findByGajiGreaterThan(Double gaji);

    // Hitung berapa banyak karyawan dengan idKaryawan dalam daftar tertentu
    long countByIdKaryawanIn(List<String> idKaryawanList);
}