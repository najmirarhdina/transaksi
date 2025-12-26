package ui.ft.ccit.faculty.transaksi.pelanggan.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.pelanggan.model.Pelanggan;
import ui.ft.ccit.faculty.transaksi.pelanggan.model.PelangganRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PelangganService {

    private final PelangganRepository pelangganRepository;

    public PelangganService(PelangganRepository pelangganRepository) {
        this.pelangganRepository = pelangganRepository;
    }

    public List<Pelanggan> getAll() {
        return pelangganRepository.findAll();
    }

    public List<Pelanggan> getAllWithPagination(int page, int size) {
        return pelangganRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public Pelanggan getById(String id) {
        return pelangganRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Pelanggan", id));
    }

    public List<Pelanggan> searchByNama(String keyword) {
        return pelangganRepository.findByNamaContainingIgnoreCase(keyword);
    }

    // CREATE
    public Pelanggan save(Pelanggan pelanggan) {
        if (pelanggan.getIdPelanggan() == null || pelanggan.getIdPelanggan().isBlank()) {
            throw new IllegalArgumentException("idPelanggan wajib diisi");
        }

        if (pelangganRepository.existsById(pelanggan.getIdPelanggan())) {
            throw new DataAlreadyExistsException("Pelanggan", pelanggan.getIdPelanggan());
        }

        return pelangganRepository.save(pelanggan);
    }

    @Transactional
    public List<Pelanggan> saveBulk(List<Pelanggan> pelangganList) {
        for (Pelanggan pelanggan : pelangganList) {
            if (pelanggan.getIdPelanggan() == null || pelanggan.getIdPelanggan().isBlank()) {
                throw new IllegalArgumentException("idPelanggan wajib diisi untuk setiap pelanggan");
            }

            if (pelangganRepository.existsById(pelanggan.getIdPelanggan())) {
                throw new DataAlreadyExistsException("Pelanggan", pelanggan.getIdPelanggan());
            }
        }
        return pelangganRepository.saveAll(pelangganList);
    }

    // UPDATE
    public Pelanggan update(String id, Pelanggan updated) {
        Pelanggan existing = getById(id);

        existing.setNama(updated.getNama());
        existing.setJenisKelamin(updated.getJenisKelamin());
        existing.setAlamat(updated.getAlamat());
        existing.setTelepon(updated.getTelepon());
        existing.setTglLahir(updated.getTglLahir());
        existing.setJenisPelanggan(updated.getJenisPelanggan());

        return pelangganRepository.save(existing);
    }

    // DELETE
    @Transactional
    public void deleteBulk(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List ID tidak boleh kosong");
        }

        if (ids.size() > 100) {
            throw new IllegalArgumentException("Maksimal 100 data per bulk delete");
        }

        long existingCount = pelangganRepository.countByIdPelangganIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        pelangganRepository.deleteAllById(ids);
    }

    public void delete(String id) {
        if (!pelangganRepository.existsById(id)) {
            throw new DataNotFoundException("Pelanggan", id);
        }
        pelangganRepository.deleteById(id);
    }
}