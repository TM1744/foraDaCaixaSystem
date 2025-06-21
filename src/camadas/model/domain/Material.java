package camadas.model.domain;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Material {
    private String descricao;
    private Float valor;
    private String cod;

    public Material(String descricao, Float valor) {
        this.setDescricao(descricao);
        this.setValor(valor);
        this.setCod(valor.toString() + descricao, 6);
    }

    public Material(String descricao, Float valor, String cod) {
        this.descricao = descricao;
        this.valor = valor;
        this.cod = cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao.trim().isEmpty()){
            throw new IllegalArgumentException("Descrição não pode ser nula");
        }
        this.descricao = descricao.toUpperCase();
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        if (valor < 0){
            this.valor = valor * (-1);
        }
        if (valor.isInfinite()){
            throw new IllegalArgumentException("Valor de material não pode ser infinito");
        }
        if (valor == 0){
            throw new IllegalArgumentException("Valor de material não pode ser 0.");
        } else {
            this.valor = valor;
        }
    }

    public String getCod() {
        return cod;
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
