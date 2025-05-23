package camadas.model.domain;

import java.util.ArrayList;
import java.util.List;

public class Produto {
    private String descricao;
    private Float valor;
    private List<ItemMaterial> materiais = new ArrayList<>();

    public Produto(String descricao, Float valor, List<ItemMaterial> materiais) {
        this.setDescricao(descricao);
        this.setValor(valor);
        this.materiais = materiais;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.toUpperCase();
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        if (valor < 0){
            this.valor = valor * (-1);
        } else if (valor == 0){
            throw new IllegalArgumentException("Valor de material não pode ser 0.");
        } else {
            this.valor = valor;
        }
    }

    public List<ItemMaterial> getMateriais() {
        return materiais;
    }

    public void setMateriais(List<ItemMaterial> materiais) {
        this.materiais = materiais;
    }
}
