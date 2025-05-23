package camadas.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    private Cliente cliente;
    private List<ItemProduto> produtos = new ArrayList<>();
    private Float valorTotal;
    private LocalDate dataEntrega;

    public Venda(Float valorTotal, LocalDate dataEntrega, List<ItemProduto> produtos, Cliente cliente) {
        this.setValorTotal(valorTotal);
        this.dataEntrega = dataEntrega;
        this.produtos = produtos;
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemProduto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemProduto> produtos) {
        this.produtos = produtos;
    }

    public Float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Float valorTotal) {
        for (int i = 0; i < getProdutos().size(); i++) {
            this.valorTotal += (this.produtos.get(i).getProduto().getValor() * this.produtos.get(i).getQuantidade());
        }
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
}
