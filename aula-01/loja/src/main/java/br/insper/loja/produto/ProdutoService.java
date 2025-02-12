package br.insper.loja.produto;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProdutoService {

    private static final String BASE_URL = "http://localhost:8082/api/produtos/";
    private final RestTemplate restTemplate;

    public ProdutoService() {
        this.restTemplate = new RestTemplate();
    }

    public Produto getProduto(String id) {
        try {
            return restTemplate.getForEntity(BASE_URL + id, Produto.class).getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado", e);
        }
    }

    public Produto diminuirQuantidade(String id, int quantidade) {
        try {
            String url = BASE_URL + id + "/diminuir/" + quantidade;
            restTemplate.put(url, null);
            return getProduto(id);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado", e);
        }
    }
}