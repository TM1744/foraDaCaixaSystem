package camadas.model.domain;

public class ItemProduto {
    private Produto produto;
    private Integer quantidade;

    public ItemProduto(Produto produto, Integer quantidade) {
        this.produto = produto;
        this.setQuantidade(quantidade);
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        if (quantidade < 0){
            this.quantidade = quantidade * (-1);
        } else if (quantidade == 0) {
            throw new IllegalArgumentException("Valor de quantidade não pode ser 0.");
        } else {
            this.quantidade = quantidade;
        }
    }
}
