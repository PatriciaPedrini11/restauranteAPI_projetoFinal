package com.restauranteAPI.restauranteAPI.controller;

import com.restauranteAPI.restauranteAPI.model.*;
import com.restauranteAPI.restauranteAPI.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class RestauranteController {

    private final RestauranteService s;

    public RestauranteController(RestauranteService s) {
        this.s = s;
    }

    @Operation(summary = "Listar categorias",
            description = "Retorna a lista de categorias.",
            tags = "Categorias")
    @ApiResponse(responseCode = "200", description = "Operação concluída com sucesso")
    @GetMapping("/categorias")
    public List<Categoria> categorias() {
        return s.categorias();
    }

    @Operation(summary = "Consultar categoria",
            description = "Retorna o registro de categoria identificado pelo ID.",
            tags = "Categorias")
    @ApiResponse(responseCode = "200", description = "Operação concluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    @GetMapping("/categorias/{id}")
    public Categoria categoria(@Parameter(description = "ID de categoria", example = "1") @PathVariable Long id) {
        return s.categoria(id);
    }

    @Operation(summary = "Cadastrar categoria",
            description = "Cria o registro de categoria com os dados do corpo da requisição.",
            tags = "Categorias")
    @ApiResponse(responseCode = "201", description = "Categoria cadastrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Corpo da requisição inválido", content = @Content)
    @PostMapping("/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria criarCategoria(@Valid @RequestBody Categoria x) {
        x.setId(null);
        return s.salvar(x);
    }

    @Operation(summary = "Atualizar categoria",
            description = "Atualiza o registro de categoria com os dados do corpo da requisição.",
            tags = "Categorias")
    @ApiResponse(responseCode = "200", description = "Operação concluída com sucesso")
    @ApiResponse(responseCode = "400", description = "Corpo da requisição inválido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    @PutMapping("/categorias/{id}")
    public Categoria atualizarCategoria(@Parameter(description = "ID de categoria", example = "1") @PathVariable Long id, @Valid @RequestBody Categoria x) {
        x.setId(id);
        return s.salvar(x);
    }

    @Operation(summary = "Excluir categoria",
            description = "Remove o registro de categoria identificado pelo ID.",
            tags = "Categorias")
    @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    @ApiResponse(responseCode = "409", description = "Categoria vinculada a outro registro", content = @Content)
    @DeleteMapping("/categorias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagarCategoria(@Parameter(description = "ID de categoria", example = "1") @PathVariable Long id) {
        s.apagarCategoria(id);
    }

    @Operation(summary = "Listar pratos",
            description = "Retorna a lista de pratos.",
            tags = "Pratos")
    @ApiResponse(responseCode = "200", description = "Operação concluída com sucesso")
    @GetMapping("/pratos")
    public List<Prato> pratos() {
        return s.pratos();
    }

    @Operation(summary = "Listar pratos disponíveis",
            description = "Retorna a lista de pratos disponíveis.",
            tags = "Pratos")
    @ApiResponse(responseCode = "200", description = "Operação concluída com sucesso")
    @GetMapping("/pratos/disponiveis")
    public List<Prato> disponiveis() {
        return s.disponiveis();
    }

    @Operation(summary = "Consultar prato",
            description = "Retorna o registro de prato identificado pelo ID.",
            tags = "Pratos")
    @ApiResponse(responseCode = "200", description = "Operação concluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Prato não encontrado", content = @Content)
    @GetMapping("/pratos/{id}")
    public Prato prato(@Parameter(description = "ID de prato", example = "1") @PathVariable Long id) {
        return s.prato(id);
    }

    @Operation(summary = "Cadastrar prato",
            description = "Cria o registro de prato com os dados do corpo da requisição.",
            tags = "Pratos")
    @ApiResponse(responseCode = "201", description = "Prato cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Corpo da requisição inválido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
    @ApiResponse(responseCode = "422", description = "Regra de negócio violada", content = @Content)
    @PostMapping("/pratos")
    @ResponseStatus(HttpStatus.CREATED)
    public Prato criarPrato(@Valid @RequestBody Prato x) {
        return s.salvar(x);
    }

    @Operation(summary = "Atualizar prato",
            description = "Atualiza o registro de prato com os dados do corpo da requisição.",
            tags = "Pratos")
    @ApiResponse(responseCode = "200", description = "Operação concluída com sucesso")
    @ApiResponse(responseCode = "400", description = "Corpo da requisição inválido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Prato ou categoria não encontrado", content = @Content)
    @ApiResponse(responseCode = "422", description = "Regra de negócio violada", content = @Content)
    @PutMapping("/pratos/{id}")
    public Prato atualizarPrato(@Parameter(description = "ID de prato", example = "1") @PathVariable Long id, @Valid @RequestBody Prato x) {
        x.setId(id);
        return s.salvar(x);
    }

    @Operation(summary = "Excluir prato",
            description = "Remove o registro de prato identificado pelo ID.",
            tags = "Pratos")
    @ApiResponse(responseCode = "204", description = "Prato excluído com sucesso", content = @Content)
    @ApiResponse(responseCode = "404", description = "Prato não encontrado", content = @Content)
    @ApiResponse(responseCode = "409", description = "Prato vinculado a outro registro", content = @Content)
    @DeleteMapping("/pratos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagarPrato(@Parameter(description = "ID de prato", example = "1") @PathVariable Long id) {
        s.apagarPrato(id);
    }

    @Operation(summary = "Listar pedidos",
            description = "Retorna a lista de pedidos.",
            tags = "Pedidos")
    @ApiResponse(responseCode = "200", description = "Operação concluída com sucesso")
    @GetMapping("/pedidos")
    public List<Pedido> pedidos() {
        return s.pedidos();
    }

    @Operation(summary = "Consultar pedido",
            description = "Retorna o registro de pedido identificado pelo ID.",
            tags = "Pedidos")
    @ApiResponse(responseCode = "200", description = "Operação concluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    @GetMapping("/pedidos/{id}")
    public Pedido pedido(@Parameter(description = "ID de pedido", example = "1") @PathVariable Long id) {
        return s.pedido(id);
    }

    @Operation(summary = "Cadastrar pedido",
            description = "Cria o registro de pedido com os dados do corpo da requisição.",
            tags = "Pedidos")
    @ApiResponse(responseCode = "201", description = "Pedido cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Corpo da requisição inválido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Prato não encontrado", content = @Content)
    @ApiResponse(responseCode = "422", description = "Prato indisponível ou regra de negócio violada", content = @Content)
    @PostMapping("/pedidos")
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido criarPedido(@Valid @RequestBody Pedido x) {
        return s.salvar(x);
    }

    @Operation(summary = "Atualizar pedido",
            description = "Atualiza o registro de pedido com os dados do corpo da requisição.",
            tags = "Pedidos")
    @ApiResponse(responseCode = "200", description = "Operação concluída com sucesso")
    @ApiResponse(responseCode = "400", description = "Corpo da requisição inválido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Pedido ou prato não encontrado", content = @Content)
    @ApiResponse(responseCode = "422", description = "Prato indisponível ou regra de negócio violada", content = @Content)
    @PutMapping("/pedidos/{id}")
    public Pedido atualizarPedido(@Parameter(description = "ID de pedido", example = "1") @PathVariable Long id, @Valid @RequestBody Pedido x) {
        x.setId(id);
        return s.salvar(x);
    }

    @Operation(summary = "Excluir pedido",
            description = "Remove o registro de pedido identificado pelo ID.",
            tags = "Pedidos")
    @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso", content = @Content)
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    @ApiResponse(responseCode = "409", description = "Pedido vinculado a outro registro", content = @Content)
    @DeleteMapping("/pedidos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagarPedido(@Parameter(description = "ID de pedido", example = "1") @PathVariable Long id) {
        s.apagarPedido(id);
    }

    @Operation(summary = "Listar itens de pedido",
            description = "Retorna a lista de itens de pedido.",
            tags = "Itens de pedido")
    @ApiResponse(responseCode = "200", description = "Operação concluída com sucesso")
    @GetMapping("/itens-pedido")
    public List<ItemPedido> itens() {
        return s.itens();
    }

    @Operation(summary = "Cadastrar item de pedido",
            description = "Cria o registro de item de pedido com os dados do corpo da requisição.",
            tags = "Itens de pedido")
    @ApiResponse(responseCode = "201", description = "Item cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Corpo da requisição inválido", content = @Content)
    @ApiResponse(responseCode = "404", description = "Pedido ou prato não encontrado", content = @Content)
    @ApiResponse(responseCode = "422", description = "Prato indisponível ou regra de negócio violada", content = @Content)
    @PostMapping("/itens-pedido")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemPedido criarItem(@Valid @RequestBody ItemPedido x) {
        return s.salvar(x);
    }

    @Operation(summary = "Excluir item de pedido",
            description = "Remove o registro de item de pedido identificado pelo ID.",
            tags = "Itens de pedido")
    @ApiResponse(responseCode = "204", description = "Item excluído com sucesso", content = @Content)
    @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content)
    @DeleteMapping("/itens-pedido/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagarItem(@Parameter(description = "ID de item de pedido", example = "1") @PathVariable Long id) {
        s.apagarItem(id);
    }
}

