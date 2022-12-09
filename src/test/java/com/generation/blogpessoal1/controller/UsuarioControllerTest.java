package com.generation.blogpessoal1.controller;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
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

import java.util.Optional;

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
    void start() {
        usuarioRepository.deleteAll();

        usuarioService.cadastrarUsuario(new Usuario(0L, "root", "root@root.com", "rootroot", ""));
    }

    @Test
    @DisplayName("Cadastrar Usuario")
    public void deveCriarUmUsuario() {
        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "paulo", "paulo@gmail.com", "12345678", "123"));

        ResponseEntity<Usuario> response = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        // faz uma comparação de criar um usuário, esperando nos dois objetos através da vírgula passado a mesma resposta.
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), response.getBody().getNome());
        assertEquals(corpoRequisicao.getBody().getUsuario(), response.getBody().getUsuario());
    }

    @Test
    @DisplayName("Listar todos os Usuario")
    public void deveMostrarTodosUsuario() {
        usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina", "sabrina@gmail.com", "12345678", ""));
        usuarioService.cadastrarUsuario(new Usuario(0L, "Ricardo", "ricardo@gmail.com", "12345678", ""));

        //o <String> serve para listar apenas o status code
        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root@root.com", "rootroot")
                .exchange("/usuarios/listartodos", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Login de um Usuario")
    public void Login() {

        HttpEntity<UsuarioLogin> corpoRequisicao = new HttpEntity<>(
                new UsuarioLogin("root@root.com","rootroot"));

        ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange("/usuarios/logar", HttpMethod.POST,
                corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Atualizar um Usuário")
    public void deveAtualizarUmUsuario() {

        Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L,
                "Neymar Junior", "neymar@email.com.br", "neymar10", "https://i.imgur.com/yDRVeK7.jpg"));

        Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),
                "Lionel Messi", "messi@email.com.br", "messi123" , "https://i.imgur.com/yDRVeK7.jpg");

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);

        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .withBasicAuth("root@root.com", "rootroot")
                .exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
        assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
    }


    @Test
    @DisplayName("Não deve permitir duplicação do Usuário")
    public void naoDuplicarUsuario() {

        usuarioService.cadastrarUsuario(new Usuario(0L,
                "Jubileu dos Santos", "jubileu.santos@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<>(new Usuario(0L,
                "Dolores da Graça", "dolores.dolores@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));

        ResponseEntity<Usuario> corpoResposta = testRestTemplate
                .exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Buscar Usuário por ID")
    public void buscarUsuarioPorId() {
        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root@root.com", "rootroot")
                .exchange("/usuarios/buscarporid/1", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}

