package camadas.model.domain;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cliente {
    private String nome;
    private String telefone;
    private String endereco;
    private String cod;

    public Cliente(String nome, String telefone, String endereco) throws RuntimeException {
        try{
            this.setNome(nome);
            this.setEndereco(endereco);
            this.setTelefone(telefone);
            this.setCod(telefone + nome, 6);
        } catch (RuntimeException e){
            throw new RuntimeException("Erro ao instanciar classe: " + e);
        }

    }

    public Cliente(Cliente cliente){
        this.nome = cliente.getNome();
        this.telefone = cliente.getTelefone();
        this.endereco = cliente.getEndereco();
        this.cod = cliente.getCod();
    }

    public Cliente(String nome, String telefone, String endereco, String cod) {
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws IllegalArgumentException{
        if(nome.isBlank()){
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if(nome.length() > 250){
            throw new IllegalArgumentException("Nome não pode ser ter mais de 250 caracteres");
        }
        this.nome = nome.toUpperCase();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) throws IllegalArgumentException {
        telefone = telefone.replaceAll("\\D", "").trim();
        if(telefone.length() > 11){
            throw new IllegalArgumentException("Não é um número de telefone");
        }
        if(telefone.isBlank()) {
            throw new IllegalArgumentException("Número de telefone não pode ser vazio");
        }
        if(telefone.length() < 10){
            throw new IllegalArgumentException("Não é um número de telefone");
        }
        if(telefone.charAt(2) != '9' && telefone.length() == 11){
            throw new IllegalArgumentException("Falta um 9 no número");
        }
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) throws IllegalArgumentException {
        if(endereco.length() > 250){
            throw new IllegalArgumentException("Endereço não pode ter mais de 250 caracteres");
        }
        if(endereco.isBlank()){
            this.endereco = "Endereço não informado";
        } else {
            this.endereco = endereco;
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
