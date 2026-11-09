package br.com.baozistore.controller;

import br.com.baozistore.model.Cliente;
import br.com.baozistore.repository.ClienteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    // POST /clientes - criar
    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
        cliente.setId(null);
        if (cliente.getClienteDesde() == null) {
            cliente.setClienteDesde(LocalDate.now()); // data do cadastro
        }
        Cliente salvo = repository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // GET /clientes/{id} - consultar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /clientes - listar todos
    @GetMapping
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    // PUT /clientes/{id} - atualizar (opcional)
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente dados) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setNome(dados.getNome());
                    if (dados.getClienteDesde() != null) {
                        existente.setClienteDesde(dados.getClienteDesde());
                    }
                    return ResponseEntity.ok(repository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /clientes/{id} - apagar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            // cliente ainda possui pedidos
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
