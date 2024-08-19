package com.example.SERVICE;

import com.example.CONFIG.EXCEPTION.ResourceNotFoundException;
import com.example.MODEL.Stock;
import com.example.MODEL.UserEntity;
import com.example.REPOSITORY.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {


    @Autowired
    private StockRepository stockRepository;


    private UserDetailsImpl userService;  // Cambiado a 'UserDetailsImpl' o el nombre correcto de la implementación



    // Listar todos los zapatos
    public List<Stock> listStock() {
        return stockRepository.findAll();
    }

    // Registrar un nuevo zapato
    public void guardarStock(Stock stock) {
        stockRepository.save(stock);
    }

    public Stock addProduct(Stock product) {
        Stock savedProduct = stockRepository.save(product);

        UserEntity currentUser = userService.getCurrentUser();
       /* ProductHistory history = new ProductHistory();
        history.setProduct(savedProduct);
        history.setUser(currentUser);
        history.setTimestamp(LocalDateTime.now());
        history.setUserRole(currentUser.getRoles().toString());
        history.setAccion("Agregado");

        productHistoryRepository.save(history);*/

        return savedProduct;
    }


    // Eliminar un zapato por su ID
    public void eliminarStock(Long id) {
        stockRepository.deleteById(id);
    }

    // Consultar el estado de un zapato específico por ID
    public Stock consultarEstado(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
    /*public Stock consultarEstado(Long id) {
        return stockRepository.findById(id).orElse(null);
    }*/

    // Consultar disponibilidad de zapatos en el inventario
    /*public List<Stock> listarDisponibles() {
        return stockRepository.findByEstado("Disponible");
    }*/
    // Consultar nodisponibilidad de zapatos en el inventario
/*
    public List<Stock> listarNoDisponibles() {
        return stockRepository.findByEstado("No disponible");
    }
*/

    // Búsqueda con filtros
    public List<Stock> buscarZapatos(String nombre) {
        if (nombre != null && !nombre.isEmpty()) {
            return stockRepository.findByNombreContaining(nombre);
       // } else if (categoria != null && !categoria.isEmpty()) {
           // return stockRepository.findByCategoria(categoria);
        //} else if (talla != null) {
       //     return stockRepository.findByTalla(talla);
      //  } else if (minPrecio != null && maxPrecio != null) {
       //     return stockRepository.findByPrecioBetween(minPrecio, maxPrecio);
        } else {
            return stockRepository.findAll(); // Retorna todos si no hay filtros
        }
    }

    public Stock saveProduct(Stock stock, MultipartFile foto) throws IOException {
        if (foto != null && !foto.isEmpty()) {
            // Validar el tipo de archivo
            String contentType = foto.getContentType();
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                throw new IllegalArgumentException("Formato de archivo no permitido. Solo JPEG y PNG son aceptados.");
            }

            // Validar el tamaño del archivo (por ejemplo, máximo 5MB)
            if (foto.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("El archivo es demasiado grande. El tamaño máximo permitido es de 5MB.");
            }

            // Asignar la foto al producto
            stock.setFoto(foto.getBytes());
        }

        // Guardar el producto en la base de datos
        return stockRepository.save(stock);
    }

    public Optional<Stock> getProductById(Long id) {
        return stockRepository.findById(id);
    }

    // Actualizar información de un zapato
    public void actualizarStock(Long id, Stock stockActualizado, MultipartFile foto) throws IOException {
        Optional<Stock> stockExistente = stockRepository.findById(id);
        if (stockExistente.isPresent()) {
            Stock stock = stockExistente.get();
            stock.setNombre(stockActualizado.getNombre());
            stock.setCategoria(stockActualizado.getCategoria());
            stock.setDescripcion(stockActualizado.getDescripcion());
            stock.setStock_inicial(stockActualizado.getStock_inicial());
            stock.setUbicacion(stockActualizado.getUbicacion());
            stock.setFecha_caducidad(stockActualizado.getFecha_caducidad());

            if (foto != null && !foto.isEmpty()) {
                // Validar el tipo de archivo
                String contentType = foto.getContentType();
                if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                    throw new IllegalArgumentException("Formato de archivo no permitido. Solo JPEG y PNG son aceptados.");
                }

                // Validar el tamaño del archivo (por ejemplo, máximo 5MB)
                if (foto.getSize() > 5 * 1024 * 1024) {
                    throw new IllegalArgumentException("El archivo es demasiado grande. El tamaño máximo permitido es de 5MB.");
                }

                // Asignar la foto al producto
                stock.setFoto(foto.getBytes());
            }

            stockRepository.save(stock);
        } else {
            throw new ResourceNotFoundException("Product not found");
        }
    }


}



