package camadas.model.domain;

public class Material {
    private String descricao;
    private Float valor;

    public Material(String descricao, Float valor) {
        this.setDescricao(descricao);
        this.setValor(valor);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
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
}
