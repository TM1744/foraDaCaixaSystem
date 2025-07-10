package camadas.model.domain;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Cliente cliente;
    private List<ItemProduto> produtos = new ArrayList<>();
    private Float valorTotal;
    private LocalDateTime dataDeRegistro;
    private LocalDate dataEntrega;
    private Boolean isFinalizado;
    private String cod;

    public Pedido(String dataEntrega, List<ItemProduto> produtos, Cliente cliente) {
        gerarDataDeRegistro();
        setDataEntrega(dataEntrega);
        setProdutos(produtos);
        setCliente(cliente);
        gerarValorTotal();
        setFinalizado(false);
        setCod(cliente.getNome() + produtos.getFirst().getProduto().getDescricao() + dataEntrega, 6);
    }

    public Pedido(Cliente cliente, List<ItemProduto> produtos, Float valorTotal, LocalDateTime dataDeRegistro, LocalDate dataEntrega, Boolean isFinalizado, String cod){
        this.cliente = cliente;
        this.produtos = produtos;
        this.valorTotal = valorTotal;
        this.dataDeRegistro = dataDeRegistro;
        this.dataEntrega = dataEntrega;
        this.isFinalizado = isFinalizado;
        this.cod = cod;
    }

    public Pedido(Pedido pedido){
        this.cliente = pedido.getCliente();
        this.produtos = pedido.getProdutos();
        this.valorTotal = pedido.getValorTotal();
        this.dataDeRegistro = pedido.getDataDeRegistro();
        this.dataEntrega = pedido.getDataEntrega();
        this.isFinalizado = pedido.getFinalizado();
        this.cod = pedido.getCod();
    }

    public Pedido(String cod){
        this.cod = cod;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemProduto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemProduto> produtos) {
        List<ItemProduto> produtosVerificados = new ArrayList<>();
        for(int i = 0; i < produtos.size(); i ++){
            String cod = produtos.get(i).getProduto().getCod();
            int quantidadeTotal = 0;
            for(int j = i + 1; j < produtos.size(); j ++){
                if(produtos.get(j).getProduto().getCod().equals(cod)){
                    quantidadeTotal += produtos.get(j).getQuantidade();
                    produtos.remove(j);
                }
            }
            produtosVerificados.add(new ItemProduto(produtos.get(i).getProduto(), produtos.get(i).getQuantidade() + quantidadeTotal));
        }
        this.produtos = produtosVerificados;
    }

    public Float getValorTotal() {
        return valorTotal;
    }

    public void gerarValorTotal() {
        for(ItemProduto item : this.produtos){
            this.valorTotal += (item.getProduto().getValor() * item.getQuantidade());
        }
    }

    public void setValorTotal(Float valorTotal){
        this.valorTotal = valorTotal;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(String dataEntrega) throws IllegalArgumentException{
        try{
            this.dataEntrega = LocalDate.parse(dataEntrega, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Valor de data de entrega informado está incorreto!");
        }
    }

    public LocalDateTime getDataDeRegistro() {
        return dataDeRegistro;
    }

    public void gerarDataDeRegistro() {
        this.dataDeRegistro = LocalDateTime.now();
    }

    public void setDataDeRegistro(String dataRegistro){
        try{
            this.dataDeRegistro = LocalDateTime.parse(dataRegistro, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Valor de data de entrega informado está incorreto!");
        }
    }

    public Boolean getFinalizado() {
        return isFinalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        isFinalizado = finalizado;
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

    public String getCod(){
        return cod;
    }
}
