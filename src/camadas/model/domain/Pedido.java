package camadas.model.domain;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
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
        setDataDeRegistro();
        setDataEntrega(dataEntrega);
        setProdutos(produtos);
        setCliente(cliente);
        gerarValorTotal();
        setFinalizado(false);
        setCod(cliente.getNome() + produtos.getFirst().getProduto().getDescricao() + dataEntrega + this.getDataDeRegistro(), 6);
    }

    public Pedido(List<ItemProduto> produtos, Cliente cliente, Boolean isFinalizado) {
        setDataDeRegistro();
        setDataEntrega();
        setProdutos(produtos);
        setCliente(cliente);
        gerarValorTotal();
        setFinalizado(isFinalizado);
        setCod(cliente.getNome() + produtos.getFirst().getProduto().getDescricao() + dataDeRegistro, 6);
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
        Float valor = 0f;
        for(ItemProduto item : this.getProdutos()){
            valor += (item.getProduto().getValor() * item.getQuantidade());
        }
        this.valorTotal = valor;
    }

    public void setValorTotal(Float valorTotal){
        this.valorTotal = valorTotal;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(String dataEntrega) throws IllegalArgumentException{
        DateTimeFormatter formatoIso = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd
        DateTimeFormatter formatoBr = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // dd/MM/yyyy

        try {
            this.dataEntrega = LocalDate.parse(dataEntrega, formatoIso);
        } catch (DateTimeParseException e1) {
            try {
                this.dataEntrega = LocalDate.parse(dataEntrega, formatoBr);
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("Formato de data inválido: " + dataEntrega);
            }
        }
    }

    public void setDataEntrega(){
        this.dataEntrega = LocalDate.now();
    }

    public LocalDateTime getDataDeRegistro() {
        return dataDeRegistro;
    }

    public void setDataDeRegistro() {
        this.dataDeRegistro = LocalDateTime.now();
    }

    public void setDataDeRegistro(String dataRegistro){
        try{
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .toFormatter();
            this.dataDeRegistro = LocalDateTime.parse(dataRegistro, formatter);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Valor de data de registro informada está incorreto!");
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
