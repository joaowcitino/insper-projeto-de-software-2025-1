package br.insper.loja.compra;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.insper.loja.produto.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.insper.loja.evento.EventoService;
import br.insper.loja.usuario.Usuario;
import br.insper.loja.usuario.UsuarioService;
import br.insper.loja.produto.ProdutoService;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private ProdutoService produtoService;

    public Compra salvarCompra(Compra compra) {
        Usuario usuario = usuarioService.getUsuario(compra.getUsuario());

        for (String produtoId: compra.getProdutos()) {
            Produto produto = produtoService.getProduto(produtoId);
        }

        for (String produtoId: compra.getProdutos()) {
            Produto produto = produtoService.diminuirQuantidade(produtoId, 1);
        }

        compra.setNome(usuario.getNome());
        compra.setDataCompra(LocalDateTime.now());

        eventoService.salvarEvento(usuario.getEmail(), "Compra realizada");
        return compraRepository.save(compra);
    }

    public List<Compra> getCompras() {
        return compraRepository.findAll();
    }
}
