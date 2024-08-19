package com.example.MODEL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "zapatos")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private String Stock_inicial;
    private String Ubicacion;
    private String fecha_caducidad;

    @Lob
    @Column(name = "foto")
    private byte[] foto; // Tipo BLOB

    @Transient
    private String fotoBase64; // Solo para enviar la imagen en base64 al frontend

    // Getters y setters para fotoBase64
    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;


}
