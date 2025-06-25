package camadas.model.domain;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ItemMaterial {
    private Material material;
    private Integer quantidade;
    private String cod;

    public ItemMaterial(Material material, Integer quantidade) {
        this.material = material;
        this.setQuantidade(quantidade);
        this.setCod(6, material.getCod() + material.getDescricao() + quantidade.toString());
    }

    public ItemMaterial(Material material, Integer quantidade, String cod) {
        this.material = material;
        this.quantidade = quantidade;
        this.cod = cod;
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

    public String getCod() {
        return cod;
    }

    public void setCod(Integer base, String valorBase){
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
