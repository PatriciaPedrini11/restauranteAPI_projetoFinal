package com.restauranteAPI.restauranteAPI.service;

import com.restauranteAPI.restauranteAPI.exception.BusinessRuleException;
import com.restauranteAPI.restauranteAPI.exception.ResourceNotFoundException;
import com.restauranteAPI.restauranteAPI.model.*;
import com.restauranteAPI.restauranteAPI.repository.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class RestauranteService {
    private final CategoriaRepository categorias;
    private final PratoRepository pratos;
    private final PedidoRepository pedidos;
    private final ItemPedidoRepository itens;

    public RestauranteService(CategoriaRepository categorias, PratoRepository pratos,
                              PedidoRepository pedidos, ItemPedidoRepository itens) {
        this.categorias = categorias;
        this.pratos = pratos;
        this.pedidos = pedidos;
        this.itens = itens;
    }

    public List<Categoria> categorias() { return categorias.findAll(); }
    public Categoria categoria(Long id) {
        return categorias.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
    }
    public Categoria salvar(Categoria categoria) {
        if (categoria.getId() != null) categoria(categoria.getId());
        return categorias.save(categoria);
    }
    public void apagarCategoria(Long id) { categoria(id); categorias.deleteById(id); }

    public List<Prato> pratos() { return pratos.findAll(); }
    public List<Prato> disponiveis() { return pratos.findByDisponivelTrue(); }
    public Prato prato(Long id) {
        return pratos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Prato", id));
    }
    public Prato salvar(Prato prato) {
        if (prato.getId() != null) prato(prato.getId());
        if (prato.getCategoria() == null || prato.getCategoria().getId() == null) {
            throw new BusinessRuleException("A categoria do prato deve ser informada");
        }
        prato.setCategoria(categoria(prato.getCategoria().getId()));
        return pratos.save(prato);
    }
    public void apagarPrato(Long id) { prato(id); pratos.deleteById(id); }

    public List<Pedido> pedidos() { return pedidos.findAll(); }
    public Pedido pedido(Long id) {
        return pedidos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
    }
    public Pedido salvar(Pedido pedido) {
        if (pedido.getId() != null) pedido(pedido.getId());
        if (pedido.getPrato() == null || pedido.getPrato().getId() == null) {
            throw new BusinessRuleException("O prato do pedido deve ser informado");
        }
        Prato pratoCadastrado = prato(pedido.getPrato().getId());
        if (!pratoCadastrado.isDisponivel()) {
            throw new BusinessRuleException("Não é possível criar um pedido com prato indisponível");
        }
        pedido.setPrato(pratoCadastrado);
        pedido.setValorTotal(pratoCadastrado.getPreco().multiply(BigDecimal.valueOf(pedido.getQuantidade())));
        return pedidos.save(pedido);
    }
    public void apagarPedido(Long id) { pedido(id); pedidos.deleteById(id); }

    public List<ItemPedido> itens() { return itens.findAll(); }
    public ItemPedido salvar(ItemPedido item) {
        if (item.getPedido() == null || item.getPedido().getId() == null) {
            throw new BusinessRuleException("O pedido do item deve ser informado");
        }
        if (item.getPrato() == null || item.getPrato().getId() == null) {
            throw new BusinessRuleException("O prato do item deve ser informado");
        }
        Pedido pedidoCadastrado = pedido(item.getPedido().getId());
        Prato pratoCadastrado = prato(item.getPrato().getId());
        if (!pratoCadastrado.isDisponivel()) {
            throw new BusinessRuleException("Não é possível adicionar um prato indisponível ao pedido");
        }
        item.setPedido(pedidoCadastrado);
        item.setPrato(pratoCadastrado);
        item.setPrecoUnitario(pratoCadastrado.getPreco());
        return itens.save(item);
    }
    public void apagarItem(Long id) {
        if (!itens.existsById(id)) throw new ResourceNotFoundException("Item de pedido", id);
        itens.deleteById(id);
    }
}
