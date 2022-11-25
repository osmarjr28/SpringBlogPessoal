package com.generation.blogpessoal.controller;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

    @Autowired
    private PostagemRepository postagemRepository;

    @GetMapping
    public ResponseEntity<List<Postagem>> getAll(){
        return ResponseEntity.ok(postagemRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Postagem> getById(@PathVariable Long id) {

      /*  Optional<Postagem> buscaPostagem = postagemRepository.findById(id);

        if(buscaPostagem.isPresent())
            return ResponseEntity.ok(buscaPostagem.get());
        else
            return ResponseEntity.notFound().build();*/

        return postagemRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.notFound().build());

        /* SELECT * FROM tb_postagens WHERE id = 1;*/
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
        return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
        /*SELECT * FROM tb_postagens WHERE titulo LIKE "%titulo%";*/
    }

    @PostMapping
    public ResponseEntity<Postagem> postPostagem(@Valid @RequestBody Postagem postagem){
        return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
    }

    @PutMapping
    public ResponseEntity<Postagem> putPostagem(@Valid @RequestBody Postagem postagem){

       /* return postagemRepository.findById(postagem.getId())
                .map(resposta -> ResponseEntity.ok().body(postagemRepository.save(postagem)))
                .orElse(ResponseEntity.notFound().build());*/
        if(postagem.getId() == null)
            return ResponseEntity.notFound().build();
        else
             return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePostagem(@PathVariable Long id) {

        return postagemRepository.findById(id)
                .map(resposta -> {
                    postagemRepository.deleteById(id);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                })
                .orElse(ResponseEntity.notFound().build());
        }

    }


