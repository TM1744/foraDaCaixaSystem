package camadas.view;

import camadas.model.domain.Material;

public class ItemMaterialView {
    public void exibirItemMaterial(Material material, Integer quantidade){
        System.out.println("Material: " + material.getDescricao() + " / " + "Valor: " + material.getValor());
        System.out.println("Quantidade: " + quantidade);
    }
}
