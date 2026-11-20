package com.restauranteAPI.restauranteAPI.service;

import com.restauranteAPI.restauranteAPI.exception.BusinessRuleException;
import com.restauranteAPI.restauranteAPI.exception.ResourceNotFoundException;
import com.restauranteAPI.restauranteAPI.model.*;
import com.restauranteAPI.restauranteAPI.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteServiceTests {

    @Mock CategoriaRepository categoriaRepository;
    @Mock PratoRepository pratoRepository;
    @Mock PedidoRepository pedidoRepository;
    @Mock ItemPedidoRepository itemPedidoRepository;

    private RestauranteService service;

    @BeforeEach
    void configurar() {
        service = new RestauranteService(
                categoriaRepository, pratoRepository, pedidoRepository, itemPedidoRepository
        );
    }

    @Test
    void deveCalcularValorTotalDoPedido() {
        Prato prato = prato(1L, "32.90", true);
        Pedido pedido = pedidoComPrato(1L, 2);

        when(pratoRepository.findById(1L)).thenReturn(Optional.of(prato));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Pedido salvo = service.salvar(pedido);

        assertEquals(new BigDecimal("65.80"), salvo.getValorTotal());
        assertSame(prato, salvo.getPrato());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void deveImpedirPedidoComPratoIndisponivel() {
        when(pratoRepository.findById(1L)).thenReturn(Optional.of(prato(1L, "20.00", false)));

        BusinessRuleException erro = assertThrows(
                BusinessRuleException.class,
                () -> service.salvar(pedidoComPrato(1L, 1))
        );

        assertTrue(erro.getMessage().contains("indisponível"));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveExigirPratoNoPedido() {
        Pedido pedido = new Pedido();
        pedido.setPrato(null);

        assertThrows(BusinessRuleException.class, () -> service.salvar(pedido));
        verifyNoInteractions(pratoRepository, pedidoRepository);
    }

    @Test
    void deveUsarPrecoCadastradoNoItemDePedido() {
        Pedido pedidoCadastrado = new Pedido();
        pedidoCadastrado.setId(10L);
        Prato pratoCadastrado = prato(2L, "18.50", true);

        ItemPedido item = new ItemPedido();
        Pedido referenciaPedido = new Pedido();
        referenciaPedido.setId(10L);
        Prato referenciaPrato = new Prato();
        referenciaPrato.setId(2L);
        item.setPedido(referenciaPedido);
        item.setPrato(referenciaPrato);
        item.setQuantidade(3);

        when(pedidoRepository.findById(10L)).thenReturn(Optional.of(pedidoCadastrado));
        when(pratoRepository.findById(2L)).thenReturn(Optional.of(pratoCadastrado));
        when(itemPedidoRepository.save(any(ItemPedido.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        ItemPedido salvo = service.salvar(item);

        assertEquals(new BigDecimal("18.50"), salvo.getPrecoUnitario());
        assertSame(pedidoCadastrado, salvo.getPedido());
        assertSame(pratoCadastrado, salvo.getPrato());
    }

    @Test
    void deveCarregarCategoriaCadastradaAoSalvarPrato() {
        Categoria categoria = new Categoria();
        categoria.setId(3L);
        categoria.setNome("Bebidas");
        Prato prato = prato(null, "8.00", true);
        Categoria referencia = new Categoria();
        referencia.setId(3L);
        prato.setCategoria(referencia);

        when(categoriaRepository.findById(3L)).thenReturn(Optional.of(categoria));
        when(pratoRepository.save(any(Prato.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Prato salvo = service.salvar(prato);

        assertSame(categoria, salvo.getCategoria());
    }

    @Test
    void deveRetornarErroAoConsultarPratoInexistente() {
        when(pratoRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException erro = assertThrows(
                ResourceNotFoundException.class,
                () -> service.prato(99L)
        );

        assertEquals("Prato com ID 99 não encontrado", erro.getMessage());
    }

    @Test
    void deveRetornarErroAoExcluirItemInexistente() {
        when(itemPedidoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.apagarItem(99L));
        verify(itemPedidoRepository, never()).deleteById(anyLong());
    }

    private Prato prato(Long id, String preco, boolean disponivel) {
        Prato prato = new Prato();
        prato.setId(id);
        prato.setNome("Prato de teste");
        prato.setPreco(new BigDecimal(preco));
        prato.setDisponivel(disponivel);
        return prato;
    }

    private Pedido pedidoComPrato(Long pratoId, int quantidade) {
        Prato referencia = new Prato();
        referencia.setId(pratoId);
        Pedido pedido = new Pedido();
        pedido.setPrato(referencia);
        pedido.setQuantidade(quantidade);
        return pedido;
    }
}
