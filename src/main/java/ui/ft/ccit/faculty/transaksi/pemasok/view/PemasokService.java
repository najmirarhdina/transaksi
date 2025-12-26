package ui.ft.ccit.faculty.transaksi.pemasok.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.pemasok.model.Pemasok;
import ui.ft.ccit.faculty.transaksi.pemasok.model.PemasokRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PemasokService {

    private final PemasokRepository pemasokRepository;

    public PemasokService(PemasokRepository pemasokRepository) {
        this.pemasokRepository = pemasokRepository;
    }

    // =============================
    // GET ALL
    // =============================
    public List<Pemasok> getAll() {
        return pemasokRepository.findAll();
    }

    public List<Pemasok> getAllWithPagination(int page, int size) {
        return pemasokRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    // =============================
    // GET BY ID
    // =============================
    public Pemasok getById(String id) {
        return pemasokRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Pemasok", id));
    }

    // =============================
    // SEARCH
    // =============================
    public List<Pemasok> searchByNama(String keyword) {
        return pemasokRepository.findByNamaContainingIgnoreCase(keyword);
    }

    // =============================
    // CREATE
    // =============================
    public Pemasok save(Pemasok pemasok) {
        if (pemasok.getIdPemasok() == null || pemasok.getIdPemasok().isBlank()) {
            throw new IllegalArgumentException("idPemasok wajib diisi");
        }

        if (pemasokRepository.existsById(pemasok.getIdPemasok())) {
            throw new DataAlreadyExistsException("Pemasok", pemasok.getIdPemasok());
        }

        return pemasokRepository.save(pemasok);
    }

    @Transactional
    public List<Pemasok> saveBulk(List<Pemasok> pemasokList) {
        for (Pemasok pemasok : pemasokList) {
            if (pemasok.getIdPemasok() == null || pemasok.getIdPemasok().isBlank()) {
                throw new IllegalArgumentException("idPemasok wajib diisi untuk setiap pemasok");
            }

            if (pemasokRepository.existsById(pemasok.getIdPemasok())) {
                throw new DataAlreadyExistsException("Pemasok", pemasok.getIdPemasok());
            }
        }
        return pemasokRepository.saveAll(pemasokList);
    }

    // =============================
    // UPDATE
    // =============================
    public Pemasok update(String id, Pemasok updated) {
        Pemasok existing = getById(id); // akan lempar DataNotFoundException

        existing.setNama(updated.getNama());
        existing.setAlamat(updated.getAlamat());
        existing.setTelepon(updated.getTelepon());
        existing.setEmail(updated.getEmail());

        return pemasokRepository.save(existing);
    }

    // =============================
    // DELETE
    // =============================
    @Transactional
    public void deleteBulk(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List ID tidak boleh kosong");
        }

        // hard limit untuk keamanan
        if (ids.size() > 100) {
            throw new IllegalArgumentException("Maksimal 100 data per bulk delete");
        }

        // validasi: pastikan semua ID ada
        long existingCount = pemasokRepository.countByIdPemasokIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        pemasokRepository.deleteAllById(ids);
    }

    public void delete(String id) {
        if (!pemasokRepository.existsById(id)) {
            throw new DataNotFoundException("Pemasok", id);
        }
        pemasokRepository.deleteById(id);
    }
}