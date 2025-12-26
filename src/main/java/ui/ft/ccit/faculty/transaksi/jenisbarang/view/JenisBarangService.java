package ui.ft.ccit.faculty.transaksi.jenisbarang.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.jenisbarang.model.JenisBarang;
import ui.ft.ccit.faculty.transaksi.jenisbarang.model.JenisBarangRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JenisBarangService {

    private final JenisBarangRepository jenisBarangRepository;

    public JenisBarangService(JenisBarangRepository jenisBarangRepository) {
        this.jenisBarangRepository = jenisBarangRepository;
    }

    public List<JenisBarang> getAll() {
        return jenisBarangRepository.findAll();
    }

    public List<JenisBarang> getAllWithPagination(int page, int size) {
        return jenisBarangRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public JenisBarang getById(Integer id) {
        return jenisBarangRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("JenisBarang", String.valueOf(id)));
    }

    // CREATE
    public JenisBarang save(JenisBarang jenisBarang) {
        // Karena ID auto increment, kita cek apakah ID sudah ada (opsional)
        if (jenisBarang.getIdJenisBarang() != null && 
            jenisBarangRepository.existsById(jenisBarang.getIdJenisBarang().intValue())) {
            throw new DataAlreadyExistsException("JenisBarang", String.valueOf(jenisBarang.getIdJenisBarang()));
        }

        return jenisBarangRepository.save(jenisBarang);
    }

    @Transactional
    public List<JenisBarang> saveBulk(List<JenisBarang> jenisBarangList) {
        for (JenisBarang jenisBarang : jenisBarangList) {
            // Validasi jika ID sudah diset secara manual
            if (jenisBarang.getIdJenisBarang() != null && 
                jenisBarangRepository.existsById(jenisBarang.getIdJenisBarang().intValue())) {
                throw new DataAlreadyExistsException("JenisBarang", String.valueOf(jenisBarang.getIdJenisBarang()));
            }
        }
        return jenisBarangRepository.saveAll(jenisBarangList);
    }

    // UPDATE
    public JenisBarang update(Integer id, JenisBarang updated) {
        JenisBarang existing = getById(id);

        existing.setNamaJenis(updated.getNamaJenis());

        return jenisBarangRepository.save(existing);
    }

    // DELETE
    @Transactional
    public void deleteBulk(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List ID tidak boleh kosong");
        }

        if (ids.size() > 100) {
            throw new IllegalArgumentException("Maksimal 100 data per bulk delete");
        }

        // Validasi: pastikan semua ID ada
        long existingCount = jenisBarangRepository.findAllById(ids).size();
        if (existingCount != ids.size()) {
            throw new IllegalStateException("Sebagian ID tidak ditemukan, operasi dibatalkan");
        }

        jenisBarangRepository.deleteAllById(ids);
    }

    public void delete(Integer id) {
        if (!jenisBarangRepository.existsById(id)) {
            throw new DataNotFoundException("JenisBarang", String.valueOf(id));
        }
        jenisBarangRepository.deleteById(id);
    }
}