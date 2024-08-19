package com.example.CONTROLLER;

import com.example.MODEL.Stock;
import com.example.SERVICE.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:4200","http://10.0.2.2:8080", "http://192.168.1.5:8080"})
public class UserController {

    @Autowired
    private StockService stockService;

    // Listar todos los zapatos en el inventario
    @GetMapping("/listar")
    public ResponseEntity<List<Stock>> listarStock() {
        List<Stock> stockList = stockService.listStock();
        // Convertir la imagen a base64
        stockList.forEach(stock -> {
            if (stock.getFoto() != null) {
                String fotoBase64 = Base64.getEncoder().encodeToString(stock.getFoto());
                stock.setFotoBase64(fotoBase64); // Añadir el campo base64 al objeto Stock
            }
        });
        return ResponseEntity.ok(stockList);
    }

    /*// Listar todos los zapatos disponibles en el inventario
    @GetMapping("/disponibles")
    public ResponseEntity<List<Stock>> listarDisponibles() {
        List<Stock> disponibles = stockService.listarDisponibles();
        // Convertir la imagen a base64
        disponibles.forEach(stock -> {
            if (stock.getFoto() != null) {
                String fotoBase64 = Base64.getEncoder().encodeToString(stock.getFoto());
                stock.setFotoBase64(fotoBase64); // Añadir el campo base64 al objeto Stock
            }
        });
        return ResponseEntity.ok(disponibles);
    }*/

    // Búsqueda con filtros
    @GetMapping("/buscar")
    public ResponseEntity<List<Stock>> buscarZapatos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String talla,
            @RequestParam(required = false) Double minPrecio,
            @RequestParam(required = false) Double maxPrecio) {

        List<Stock> resultados = stockService.buscarZapatos(nombre);
        // Convertir la imagen a base64
        resultados.forEach(stock -> {
            if (stock.getFoto() != null) {
                String fotoBase64 = Base64.getEncoder().encodeToString(stock.getFoto());
                stock.setFotoBase64(fotoBase64); // Añadir el campo base64 al objeto Stock
            }
        });
        return ResponseEntity.ok(resultados);
    }
}