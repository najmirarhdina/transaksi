package ui.ft.ccit.faculty.transaksi.transaksi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ui.ft.ccit.faculty.transaksi.transaksi.model.Transaksi;
import ui.ft.ccit.faculty.transaksi.transaksi.view.TransaksiService;
import ui.ft.ccit.faculty.transaksi.transaksi.dto.TransaksiRequest;

import java.util.List;

@RestController
@RequestMapping("/api/transaksi")
public class TransaksiController {

    private final TransaksiService service;

    public TransaksiController(TransaksiService service) {
        this.service = service;
    }

    // GET - list semua transaksi
    @GetMapping
    public List<Transaksi> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        // TANPA pagination
        if (page == null && size == null) {
            return service.getAll();
        }

        // DENGAN pagination
        int p = (page != null && page >= 0) ? page : 0;
        int s = (size != null && size > 0) ? size : 5;
        return service.getAllWithPagination(p, s);
    }

    // GET - satu transaksi by kode (dengan detail)
    @GetMapping("/{kode}")
    public Transaksi get(@PathVariable String kode) {
        return service.getById(kode);
    }

    // POST - create transaksi baru 
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaksi create(@RequestBody TransaksiRequest request) {
        return service.create(request);
    }

    
}