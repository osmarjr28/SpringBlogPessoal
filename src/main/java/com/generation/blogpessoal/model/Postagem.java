package com.generation.blogpessoal.model;

import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

import javax.persistence.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Entity
@Table(name = "tb_postagens")
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O Atributo título é obrigatório e não pode ser vazio!")
    @Size(min = 5, max = 100, message = "O Atributo título deve conter no minimo 5 e no máximo 100 caracteres")
    private String titulo;

    @NotNull(message = "O Atributo texto é obrigatório")
    @Size(min = 10, max = 1000, message = "O Atributo texto deve conter no minimo 10 e no máximo 1000 caracteres")
    private String texto;

    @UpdateTimestamp
    private LocalDate data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}





