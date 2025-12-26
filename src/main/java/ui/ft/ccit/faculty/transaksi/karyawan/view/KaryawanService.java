package ui.ft.ccit.faculty.transaksi.karyawan.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.karyawan.model.Karyawan;
import ui.ft.ccit.faculty.transaksi.karyawan.model.KaryawanRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class KaryawanService {

    private final KaryawanRepository karyawanRepository;

    public KaryawanService(KaryawanRepository karyawanRepository) {
        this.karyawanRepository = karyawanRepository;
    }

    public List<Karyawan> getAll() {
        return karyawanRepository.findAll();
    }

    public List<Karyawan> getAllWithPagination(int page, int size) {
        return karyawanRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public Karyawan getById(String id) {
        return karyawanRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Karyawan", id));
    }

    public List<Karyawan> searchByNama(String keyword) {
        return karyawanRepository.findByNamaContainingIgnoreCase(keyword);
    }

    // CREATE
    public Karyawan save(Karyawan karyawan) {
        if (karyawan.getIdKaryawan() == null || karyawan.getIdKaryawan().isBlank()) {
            throw new IllegalArgumentException("idKaryawan wajib diisi");
        }

        if (karyawanRepository.existsById(karyawan.getIdKaryawan())) {
            throw new DataAlreadyExistsException("Karyawan", karyawan.getIdKaryawan());
        }

        return karyawanRepository.save(karyawan);
    }

    @Transactional
    public List<Karyawan> saveBulk(List<Karyawan> karyawanList) {
        for (Karyawan karyawan : karyawanList) {
            if (karyawan.getIdKaryawan() == null || karyawan.getIdKaryawan().isBlank()) {
                throw new IllegalArgumentException("idKaryawan wajib diisi untuk setiap karyawan");
            }

            if (karyawanRepository.existsById(karyawan.getIdKaryawan())) {
                throw new DataAlreadyExistsException("Karyawan", karyawan.getIdKaryawan());
            }
        }
        return karyawanRepository.saveAll(karyawanList);
    }

    // UPDATE
    public Karyawan update(String id, Karyawan updated) {
        Karyawan existing = getById(id);

        existing.setNama(updated.getNama());
        existing.setJenisKelamin(updated.getJenisKelamin());
        existing.setAlamat(updated.getAlamat());
        existing.setTelepon(updated.getTelepon());
        existing.setTglLahir(updated.getTglLahir());
        existing.setGaji(updated.getGaji());

        return karyawanRepository.save(existing);
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

        long existingCount = karyawanRepository.countByIdKaryawanIn(ids);
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        karyawanRepository.deleteAllById(ids);
    }

    public void delete(String id) {
        if (!karyawanRepository.existsById(id)) {
            throw new DataNotFoundException("Karyawan", id);
        }
        karyawanRepository.deleteById(id);
    }
}