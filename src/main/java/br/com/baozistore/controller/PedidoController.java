package br.com.baozistore.controller;

import br.com.baozistore.model.Cliente;
import br.com.baozistore.model.Pedido;
import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ClienteRepository;
import br.com.baozistore.repository.PedidoRepository;
import br.com.baozistore.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                            ClienteRepository clienteRepository,
                            ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    // POST /pedidos - criar
    // Corpo esperado: { "cliente": {"id": 1}, "produto": {"id": 1}, "quantidade": 5 }
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            return ResponseEntity.badRequest().body("Informe o cliente: \"cliente\": {\"id\": 1}");
        }
        if (pedido.getProduto() == null || pedido.getProduto().getId() == null) {
            return ResponseEntity.badRequest().body("Informe o produto: \"produto\": {\"id\": 1}");
        }
        if (pedido.getQuantidade() == null || pedido.getQuantidade() <= 0) {
            return ResponseEntity.badRequest().body("A quantidade deve ser maior que zero.");
        }

        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId()).orElse(null);
        if (cliente == null) {
            return ResponseEntity.badRequest().body("Cliente não encontrado.");
        }
        Produto produto = produtoRepository.findById(pedido.getProduto().getId()).orElse(null);
        if (produto == null) {
            return ResponseEntity.badRequest().body("Produto não encontrado.");
        }

        pedido.setId(null);
        pedido.setCliente(cliente); // dados completos aparecem na resposta
        pedido.setProduto(produto);
        Pedido salvo = pedidoRepository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // GET /pedidos/{id} - consultar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /pedidos - listar todos
    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    // PUT /pedidos/{id} - atualizar a quantidade (opcional)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Pedido dados) {
        Pedido existente = pedidoRepository.findById(id).orElse(null);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        if (dados.getQuantidade() == null || dados.getQuantidade() <= 0) {
            return ResponseEntity.badRequest().body("A quantidade deve ser maior que zero.");
        }
        existente.setQuantidade(dados.getQuantidade());
        return ResponseEntity.ok(pedidoRepository.save(existente));
    }

    // DELETE /pedidos/{id} - apagar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
