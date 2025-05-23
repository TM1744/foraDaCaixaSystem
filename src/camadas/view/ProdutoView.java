package camadas.view;

import camadas.model.domain.ItemMaterial;

import java.util.List;

public class ProdutoView {
    public void exibirProduto(String descricao, Float valor, List<ItemMaterial> materiais){
        System.out.println("Descrição: " + descricao);
        System.out.println("Valor: " + valor);
        for (int i = 0; i < materiais.size(); i++) {
            System.out.println("Material " + (i + 1) + ": " + materiais.get(i).getMaterial().getDescricao());
        }
    }
}
