package com.example.SERVICE;

import com.example.CONFIG.EXCEPTION.ResourceNotFoundException;
import com.example.MODEL.Stock;
import com.example.MODEL.UserEntity;
import com.example.REPOSITORY.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private StockRepository productRepository;

    @Autowired
    private UserDetailsImpl userService;

    public Stock addProduct(Stock product) {
        Stock savedProduct = productRepository.save(product);

        UserEntity currentUser = userService.getCurrentUser();


        return savedProduct;
    }

    //historial_todo
   /*// public List<ProductHistory> getAllProductHistories() {
        return productHistoryRepository.findAll();
    }*/
    public void eliminarProducto(Long id) {
        Stock stock = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        // Eliminar todos los registros de historial asociados
     //   productHistoryRepository.deleteByProductId(id);

        // Eliminar el producto
        productRepository.deleteById(id);
    }



}