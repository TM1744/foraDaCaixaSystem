package camadas.view;

import camadas.model.domain.ItemProduto;
import camadas.model.domain.Pedido;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class PedidoView {
    Scanner scanner = new Scanner(System.in);

    public String readCod(){
        scanner = new Scanner(System.in);
        System.out.println("Informe o COD do pedido:");
        return scanner.nextLine();
    }

    public boolean isAddProduto() {
        scanner = new Scanner(System.in);
        System.out.println("Deseja acrescentar mais algum produto? (1- Sim / 2- Não)");
        return scanner.nextInt() == 1;
    }

    public String readDataEntrega() {
        scanner = new Scanner(System.in);
        System.out.println("Informe a data de entrega do pedido (padrão: DD/MM/AAAA): ");
        return scanner.nextLine();
    }

    public Boolean readIsVenda() {
        scanner = new Scanner(System.in);
        System.out.println("Isso será uma venda imediata? (1- Sim / 2-Não)");
        return scanner.nextInt() == 1;
    }

    public void printSucessoCadastro(Pedido pedido) {
        System.out.println("Sucesso ao cadastrar pedido:");
        printPedido(pedido);
    }

    public void printErro(Exception e) {
        System.err.println("Erro: " + e);
    }

    public void printSucesso() {
        System.out.println("Sucesso ao realizar operação!");
    }

    public int isFinalizado() {
        scanner = new Scanner(System.in);
        System.out.println("Deseja ver pedidos já finalizados? (1- Sim / 2- Não / 3- Todos)");
        return switch (scanner.nextInt()) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            default -> 3;
        };
    }

    public void printPedidoList(List<Pedido> listFinalizados) {
        for (Pedido pedido : listFinalizados){
            printPedido(pedido);
        }
    }

    public Integer isByNomeCliente() {
        scanner = new Scanner(System.in);
        System.out.println("""
                Que tipo de busca deseja fazer?
                1- Por nome do cliente
                2- Por data de entrega
                """);
        return scanner.nextInt();
    }

    public Integer readQuantidadeItemProduto() {
        scanner = new Scanner(System.in);
        System.out.println("Informe a quantidade do produto selecionado:");
        return scanner.nextInt();
    }

    private void printPedido(Pedido pedido){
        String dataEnt = pedido.getDataEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String dataRes = pedido.getDataDeRegistro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        System.out.println();
        System.out.println("PEDIDO--------------------");
        System.out.println("Nome do cliente: " + pedido.getCliente().getNome());
        System.out.println("Telefone do cliente: " + pedido.getCliente().getTelefone());
        System.out.println("Endereço do cliente: " + pedido.getCliente().getEndereco());
        System.out.println("Data de registro: " + dataRes);
        System.out.println("Data de entrega: " + dataEnt);
        for(ItemProduto produto : pedido.getProdutos()){
            System.out.println("Produto: " + produto.getProduto().getDescricao() + " x " + produto.getQuantidade());
        }
        if(pedido.getFinalizado()){
            System.out.println("Pedido finalizado!");
        }else {
            System.out.println("Pedido não finalizado!");
        }
        System.out.println("Valor total: $" + pedido.getValorTotal());
        System.out.println("COD: " + pedido.getCod());
        System.out.println();
    }
}
