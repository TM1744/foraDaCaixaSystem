package camadas.model.domain;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Material {
    private String descricao;
    private Float valor;
    private String cod;
    private Integer quantidadeEstoque = 0;

    public Material(String descricao, Float valor, Integer quantidadeEstoque) throws RuntimeException{
        try{
            this.setDescricao(descricao);
            this.setValor(valor);
            this.setQuantidadeEstoque(quantidadeEstoque);
            this.setCod(valor.toString() + descricao, 6);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public Material(String descricao, Float valor, String cod, Integer quantidadeEstoque) {
        this.descricao = descricao;
        this.valor = valor;
        this.cod = cod;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) throws IllegalArgumentException{
        if (descricao.trim().isEmpty()){
            throw new IllegalArgumentException("Descrição não pode ser nula");
        }
        this.descricao = descricao.toUpperCase();
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) throws IllegalArgumentException{
        if (valor.isInfinite()){
            throw new IllegalArgumentException("Valor de material não pode ser infinito");
        }
        if (valor == 0){
            throw new IllegalArgumentException("Valor de material não pode ser 0.");
        } else  if(valor < 0){
            this.valor = valor * (-1);
        } else {
            this.valor = valor;
        }
    }

    public String getCod() {
        return cod;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) throws IllegalArgumentException {
        if(quantidadeEstoque < 0){
            throw new IllegalArgumentException("Quantidade no estoque não pode ser negativa");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public void setCod(String valorBase, Integer base) throws RuntimeException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(valorBase.getBytes());

            BigInteger numero = new BigInteger(1, hash);

            String codigoNumerico = numero.toString(10);

            if (codigoNumerico.length() > base) {
                codigoNumerico = codigoNumerico.substring(0, base);
            } else {
                codigoNumerico = String.format("%1$" + base + "s", codigoNumerico).replace(' ', '0');
            }

            this.cod = codigoNumerico;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash SHA-256", e);
        }
    }
}
