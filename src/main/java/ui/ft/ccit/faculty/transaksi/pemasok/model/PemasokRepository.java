package ui.ft.ccit.faculty.transaksi.pemasok.model;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface PemasokRepository extends JpaRepository<Pemasok, String> {

    // cari pemasok berdasarkan nama (LIKE %keyword%)
    List<Pemasok> findByNamaContainingIgnoreCase(String keyword);

    // hitung berapa banyak pemasok dengan idPemasok dalam list
    long countByIdPemasokIn(List<String> idPemasokList);
}

