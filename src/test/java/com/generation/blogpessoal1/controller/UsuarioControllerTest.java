package com.generation.blogpessoal1.controller;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start(){
        usuarioRepository.deleteAll();

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Root","root@root.com", "rootroot", ""));
    }

    @Test
    @DisplayName("Cadastrar um usuario")
    public void deveCriarUmUsuario(){

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L,
                "Paulo Antunes", "paulo@gmail.com", "12345678", ""));

        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .exchange("/usuario/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
        assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
    }

    @Test
    @DisplayName("Listar todos os usuarios")
    public void deveMostrarTodosUsuarios(){

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Sabrina","sabrina@gmail.com", "12345678", ""));

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Ricardo","ricardo@gmail.com", "12345678", ""));

        ResponseEntity<String> resposta = testRestTemplate
                .withBasicAuth( "rooot@root.com", "rootrooot")
                .exchange("usuario/all", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
}
