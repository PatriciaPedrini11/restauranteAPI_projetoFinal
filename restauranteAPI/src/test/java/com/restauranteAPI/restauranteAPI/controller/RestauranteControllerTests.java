package com.restauranteAPI.restauranteAPI.controller;

import com.restauranteAPI.restauranteAPI.exception.*;
import com.restauranteAPI.restauranteAPI.model.Categoria;
import com.restauranteAPI.restauranteAPI.service.RestauranteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RestauranteControllerTests {

    @Mock RestauranteService service;
    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders
                .standaloneSetup(new RestauranteController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void deveCadastrarCategoriaValidaComStatus201() throws Exception {
        Categoria salva = new Categoria();
        salva.setId(1L);
        salva.setNome("Massas");
        salva.setDescricao("Pratos à base de massa");
        when(service.salvar(any(Categoria.class))).thenReturn(salva);

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Massas","descricao":"Pratos à base de massa"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Massas"));
    }

    @Test
    void deveRejeitarCategoriaSemNomeComStatus400() throws Exception {
        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"","descricao":"Categoria inválida"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Dados inválidos"))
                .andExpect(jsonPath("$.campos.nome").exists());

        verify(service, never()).salvar(any(Categoria.class));
    }

    @Test
    void deveRetornar404AoConsultarCategoriaInexistente() throws Exception {
        when(service.categoria(99L)).thenThrow(new ResourceNotFoundException("Categoria", 99L));

        mockMvc.perform(get("/api/categorias/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Categoria com ID 99 não encontrado"));
    }

    @Test
    void deveExcluirCategoriaComStatus204() throws Exception {
        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(service).apagarCategoria(1L);
    }

    @Test
    void deveRetornar422QuandoPratoDoPedidoEstaIndisponivel() throws Exception {
        when(service.salvar(any(com.restauranteAPI.restauranteAPI.model.Pedido.class)))
                .thenThrow(new BusinessRuleException("Não é possível criar um pedido com prato indisponível"));

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"prato":{"id":1},"quantidade":2}
                                """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.erro").value("Regra de negócio violada"));
    }
}
