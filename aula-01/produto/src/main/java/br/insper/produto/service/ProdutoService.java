package br.insper.produto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.insper.produto.exception.ProdutoNotFoundException;
import br.insper.produto.model.Produto;
import br.insper.produto.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto criarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto buscarProdutoPorId(String id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto com ID " + id + " não encontrado."));
    }

    public Produto diminuirQuantidade(String id, int quantidade) {
        Produto produto = buscarProdutoPorId(id);
        if (produto.getQuantidade() < quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente.");
        }
        produto.setQuantidade(produto.getQuantidade() - quantidade);
        return produtoRepository.save(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }
}