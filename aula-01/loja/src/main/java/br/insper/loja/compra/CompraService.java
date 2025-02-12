package br.insper.loja.compra;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.insper.loja.evento.EventoService;
import br.insper.loja.produto.Produto;
import br.insper.loja.produto.ProdutoService;
import br.insper.loja.usuario.Usuario;
import br.insper.loja.usuario.UsuarioService;

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
        Usuario usuario;
        try {
            usuario = usuarioService.getUsuario(compra.getUsuario());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.", e);
        }

        double valorCompra = 0;

        for (String produtoId : compra.getProdutos()) {
            Produto produto;
            try {
                produto = produtoService.getProduto(produtoId);
            } catch (ResponseStatusException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto com ID " + produtoId + " não encontrado.", e);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar produto com ID " + produtoId, e);
            }
            valorCompra += produto.getPreco();
        }

        for (String produtoId : compra.getProdutos()) {
            try {
                produtoService.diminuirQuantidade(produtoId, 1);
            } catch (ResponseStatusException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto com ID " + produtoId + " não encontrado ao tentar diminuir quantidade.", e);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao diminuir quantidade do produto com ID " + produtoId, e);
            }
        }

        compra.setNome(usuario.getNome());
        compra.setDataCompra(LocalDateTime.now());

        eventoService.salvarEvento(usuario.getEmail(), "Compra realizada com sucesso no valor de R$ " + valorCompra);
        return compraRepository.save(compra);
    }

    public List<Compra> getCompras() {
        return compraRepository.findAll();
    }
}
