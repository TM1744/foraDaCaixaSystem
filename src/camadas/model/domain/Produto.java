package camadas.model.domain;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Produto {
    private String descricao;
    private Float valor;
    private Set<ItemMaterial> itensMateriais = new HashSet<>();
    private String cod;
    private Float margemLucro;

    public Produto(String descricao, Float valor, Set<ItemMaterial> materiais, String cod, Float margemLucro) {
        this.descricao = descricao;
        this.valor = valor;
        this.itensMateriais = materiais;
        this.cod = cod;
        this.margemLucro = margemLucro;
    }

    public Produto(String cod){
        this.cod = cod;
    }

    public Produto(){

    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) throws IllegalArgumentException{
        if (descricao.trim().isEmpty()){
            throw new IllegalArgumentException("Descrição do produto não pode ser nula");
        }
        this.descricao = descricao.toUpperCase();
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        if (valor < 0){
            throw new IllegalArgumentException("Valor de produto não pode ser negativo");
        } else if (valor == 0){
            Float valorMinimo = this.getValorMinimo();
            this.valor = valorMinimo + (valorMinimo * (getMargemLucro() / 100));
        } else {
            this.valor = valor;
        }
    }

    public Set<ItemMaterial> getItensMateriais() {
        return itensMateriais;
    }

    public void setItensMateriais(Set<ItemMaterial> materiais) throws IllegalArgumentException {
        this.itensMateriais = materiais;
    }

    public void addMateriais(ItemMaterial materiais) throws IllegalArgumentException{
        for(ItemMaterial material : this.itensMateriais){
            if(material.getMaterial().getDescricao().equals(materiais.getMaterial().getDescricao())) {
                throw new IllegalArgumentException("Material já utilizado");
            }
        }
        if (!this.itensMateriais.add(materiais)) {
            throw new IllegalArgumentException("Materiais repetidos");
        }
        this.itensMateriais.add(materiais);
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

    public Float getMargemLucro() {
        return margemLucro;
    }

    public void setMargemLucro(Float margemLucro) {
        if (margemLucro < 0){
            throw new IllegalArgumentException("Margem de lucro não pode ter um valor negativo");
        }
        this.margemLucro = margemLucro;
    }

    public Float getValorMinimo(){
        Float valorMinimo = 0f;
        for(ItemMaterial itemMaterial : this.itensMateriais){
            valorMinimo += itemMaterial.getMaterial().getValor() * itemMaterial.getQuantidade();
        }
        return valorMinimo;
    }
}
