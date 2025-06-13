package camadas.model.domain;

public class ItemMaterial {
    private Material material;
    private Integer quantidade;

    public ItemMaterial(Material material, Integer quantidade) {
        this.material = material;
        this.setQuantidade(quantidade);
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) throws IllegalArgumentException {
        if (quantidade < 0){
            this.quantidade = quantidade * (-1);
        } else if (quantidade == 0) {
            throw new IllegalArgumentException("Valor de quantidade não pode ser 0.");
        } else {
            this.quantidade = quantidade;
        }
    }
}
