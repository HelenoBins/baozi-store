package br.com.baozistore.controller;

import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ProdutoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository repository;

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    // POST /produtos - criar
    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        produto.setId(null); // garante que será um INSERT
        Produto salvo = repository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // GET /produtos/{id} - consultar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /produtos - listar todos
    @GetMapping
    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    // PUT /produtos/{id} - atualizar (opcional)
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto dados) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setNome(dados.getNome());
                    existente.setPreco(dados.getPreco());
                    existente.setEstoque(dados.getEstoque());
                    return ResponseEntity.ok(repository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /produtos/{id} - apagar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            // produto ainda usado por algum pedido
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
