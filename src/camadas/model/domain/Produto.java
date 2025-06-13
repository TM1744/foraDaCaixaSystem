package camadas.model.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Produto {
    private String descricao;
    private Float valor;
    private Set<ItemMaterial> materiais = new HashSet<>();

    public Produto(String descricao, Float valor, Set<ItemMaterial> materiais) {
        this.setDescricao(descricao);
        this.setValor(valor);
        this.materiais = materiais;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) throws IllegalArgumentException{
        if (descricao.trim() == null){
            throw new IllegalArgumentException("Descrição nula");
        }
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

    public Set<ItemMaterial> getMateriais() {
        return materiais;
    }

    public void setMateriais(Set<ItemMaterial> materiais) throws IllegalArgumentException {
        this.materiais = materiais;
    }

    public void addMateriais(ItemMaterial materiais) throws IllegalArgumentException{
        for(ItemMaterial material : this.materiais){
            if(material.getMaterial().getDescricao() == materiais.getMaterial().getDescricao()) {
                throw new IllegalArgumentException("Material já utilizado");
            }
        }
        if (!this.materiais.add(materiais)) {
            throw new IllegalArgumentException("Materiais repetidos");
        }
    }
}
