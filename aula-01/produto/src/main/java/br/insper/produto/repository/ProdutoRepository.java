package br.insper.produto.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.insper.produto.model.Produto;

public interface ProdutoRepository extends MongoRepository<Produto, String> {
}
