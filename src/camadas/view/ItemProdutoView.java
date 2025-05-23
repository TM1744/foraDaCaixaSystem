package camadas.view;

import camadas.model.domain.Produto;

public class ItemProdutoView {
    public void exibirItemProduto(Produto produto, Integer quantidade){
        System.out.println("Produto: " + produto.getDescricao() + " / " + "Valor: " + produto.getValor());
        System.out.println("Quantidade: " + quantidade);
    }
}
