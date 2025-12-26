package ui.ft.ccit.faculty.transaksi.transaksi.view;

import ui.ft.ccit.faculty.transaksi.DataAlreadyExistsException;
import ui.ft.ccit.faculty.transaksi.DataNotFoundException;
import ui.ft.ccit.faculty.transaksi.transaksi.model.Transaksi;
import ui.ft.ccit.faculty.transaksi.transaksi.model.DetailTransaksi;
import ui.ft.ccit.faculty.transaksi.transaksi.model.TransaksiRepository;
import ui.ft.ccit.faculty.transaksi.transaksi.dto.TransaksiRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransaksiService {

    private final TransaksiRepository transaksiRepository;

    public TransaksiService(TransaksiRepository transaksiRepository) {
        this.transaksiRepository = transaksiRepository;
    }

    public List<Transaksi> getAll() {
        return transaksiRepository.findAll();
    }

    public List<Transaksi> getAllWithPagination(int page, int size) {
        return transaksiRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public Transaksi getById(String kode) {
        return transaksiRepository.findById(kode)
                .orElseThrow(() -> new DataNotFoundException("Transaksi", kode));
    }

    // CREATE - buat transaksi beserta detailnya
    @Transactional
    public Transaksi create(TransaksiRequest request) {
        // Validasi kode transaksi wajib diisi
        if (request.getKodeTransaksi() == null || request.getKodeTransaksi().isBlank()) {
            throw new IllegalArgumentException("kodeTransaksi wajib diisi");
        }

        // Cek apakah kode transaksi sudah ada
        if (transaksiRepository.existsById(request.getKodeTransaksi())) {
            throw new DataAlreadyExistsException("Transaksi", request.getKodeTransaksi());
        }

        // Validasi detail tidak boleh kosong
        if (request.getDetails() == null || request.getDetails().isEmpty()) {
            throw new IllegalArgumentException("Detail transaksi tidak boleh kosong");
        }

        // Buat transaksi header
        Transaksi transaksi = new Transaksi(
            request.getKodeTransaksi(),
            request.getTglTransaksi(),
            request.getIdPelanggan(),
            request.getIdKaryawan()
        );

        // Tambahkan detail transaksi
        for (TransaksiRequest.DetailItem item : request.getDetails()) {
            DetailTransaksi detail = new DetailTransaksi(
                request.getKodeTransaksi(),
                item.getIdBarang(),
                item.getJumlah()
            );
            transaksi.addDetail(detail);
        }

        // Save transaksi (detail akan auto-save karena cascade)
        return transaksiRepository.save(transaksi);
    }

}