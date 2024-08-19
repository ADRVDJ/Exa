package com.example.CONTROLLER;

import com.example.CONFIG.EXCEPTION.ResourceNotFoundException;
import com.example.MODEL.Categoria;
import com.example.MODEL.Stock;
import com.example.SERVICE.CategoriaService;
import com.example.SERVICE.ProductService;
import com.example.SERVICE.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoriaService categoriaService;



    // Listar todos los zapatos en el inventario
    @GetMapping("/listar")
    @PreAuthorize("hasAnyRole('INVITED', 'ADMIN')")//TAMBIEN PARA VARIOS
    public ResponseEntity<List<Stock>> listarStock() {
        List<Stock> stockList = stockService.listStock();
        return ResponseEntity.ok(stockList);
    }

    // Eliminar un zapato del inventario por su ID
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('INVITED', 'ADMIN')")
    public ResponseEntity<String> eliminarStock(@PathVariable Long id) {
        try {
        //    productHistoryRepository.deleteByProductId(id);
            productService.eliminarProducto(id);  // Eliminar el producto y el historial
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Producto eliminado con éxito.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    // Consultar el estado de un zapato específico por su ID
    @GetMapping("/consultar/{id}")
    @PreAuthorize("hasAnyRole('INVITED', 'ADMIN')")//TAMBIEN PARA VARIOS
    public ResponseEntity<Stock> consultarEstado(@PathVariable Long id) {
        Stock stock = stockService.consultarEstado(id);
        return stock != null ? ResponseEntity.ok(stock) : ResponseEntity.notFound().build();
    }

   /* // Listar todos los disponibles en el inventario
    @GetMapping("/disponibles")
    @PreAuthorize("hasAnyRole('INVITED', 'ADMIN')")//TAMBIEN PARA VARIOS
    public ResponseEntity<List<Stock>> listarDisponibles() {
        List<Stock> disponibles = stockService.listarDisponibles();
        return ResponseEntity.ok(disponibles);
    }*/

   /* // Listar todos los zapatos disponibles en el inventario
    @GetMapping("/Nodisponibles")
    @PreAuthorize("hasAnyRole('INVITED', 'ADMIN')")//TAMBIEN PARA VARIOS
    public ResponseEntity<List<Stock>> listarNoDisponibles() {
        List<Stock> disponibles = stockService.listarNoDisponibles();
        return ResponseEntity.ok(disponibles);
    }*/

    //guardar con foto
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('INVITED', 'ADMIN')")
    public ResponseEntity<String> addProduct(
            @RequestParam("nombre") String nombre,
            @RequestParam("categoriaId") Long categoriaId,  // Recibir ID de la categoría
            @RequestParam("descripcion") String descripcion,
            @RequestParam("Stock_inicial") String stockInicial,
            @RequestParam("Ubicacion") String ubicacion,
            @RequestParam("fecha_caducidad") String fechaCaducidad,
            @RequestParam(value = "foto", required = false) MultipartFile foto) {

        try {
            // Buscar la categoría por ID
            Categoria categoria = categoriaService.findById(categoriaId)
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

            // Crear el objeto Stock
            Stock stock = Stock.builder()
                    .nombre(nombre)
                    .categoria(categoria)  // Asignar la categoría encontrada
                    .descripcion(descripcion)
                    .Stock_inicial(stockInicial)
                    .Ubicacion(ubicacion)
                    .fecha_caducidad(fechaCaducidad)
                    .build();

            // Guardar el producto con la foto
            stockService.saveProduct(stock, foto);
            productService.addProduct(stock);

            return ResponseEntity.status(HttpStatus.CREATED).body("Producto añadido con éxito.");
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // Actualizar producto con foto
    @PutMapping("/actualizar/{id}")
    @PreAuthorize("hasAnyRole('INVITED', 'ADMIN')")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("categoriaId") Long categoriaId,  // Cambié el parámetro para recibir el ID de la categoría
            @RequestParam("descripcion") String descripcion,
            @RequestParam("Stock_inicial") String stockInicial,
            @RequestParam("Ubicacion") String ubicacion,
            @RequestParam("fecha_caducidad") String fechaCaducidad,
            @RequestParam(value = "foto", required = false) MultipartFile foto) {

        try {
            // Buscar la categoría por ID
            Categoria categoria = categoriaService.findById(categoriaId)
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

            // Crear el objeto Stock con la información actualizada
            Stock stockActualizado = new Stock();
            stockActualizado.setId(id);
            stockActualizado.setNombre(nombre);
            stockActualizado.setCategoria(categoria);  // Asignar la categoría encontrada
            stockActualizado.setDescripcion(descripcion);
            stockActualizado.setStock_inicial(stockInicial);
            stockActualizado.setUbicacion(ubicacion);
            stockActualizado.setFecha_caducidad(fechaCaducidad);

            // Actualizar el producto con la foto
            stockService.actualizarStock(id, stockActualizado, foto);

            return ResponseEntity.status(HttpStatus.OK).body("Producto actualizado con éxito.");
        } catch (IOException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }




}

