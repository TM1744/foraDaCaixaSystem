package camadas.view;

import camadas.model.domain.Cliente;

import java.util.List;
import java.util.Scanner;

public class ClienteView {
    private final Scanner scanner = new Scanner(System.in);

    public void printCliente(Cliente cliente){
        System.out.println("\n---Cliente:");
        printNome(cliente);
        printTelefone(cliente);
        printEndereco(cliente);
        printCod(cliente);
        System.out.println("------------------------------------------------------------------");
    }

    public void printClienteList(List<Cliente> clientes){
        for (Cliente cliente : clientes){
            printCliente(cliente);
        }
    }

    public void printNome(Cliente cliente){
        System.out.println("Nome: " + cliente.getNome());
    }
    public void printEndereco(Cliente cliente){
        System.out.println("Endereço: " + cliente.getEndereco());
    }
    public void printTelefone(Cliente cliente){
        System.out.println("Telefone: " + cliente.getTelefone());
    }
    public void printCod(Cliente cliente){
        System.out.println("Cod: " + cliente.getCod());
    }

    public String readNome(){
        System.out.println("Informe o nome do cliente: ");
        return scanner.nextLine();
    }

    public String readTelefone(){
        System.out.println("Informe o telefone do cliente: ");
        return scanner.nextLine();
    }

    public String readEndereco(){
        System.out.println("Informe o endereço do cliente: ");
        return scanner.nextLine();
    }

    public boolean sucessoCadastro(Cliente cliente){
        System.out.println("Cliente cadastrado com sucesso!\n");
        printCliente(cliente);
        System.out.println("\nDeseja cadastrar mais um cliente?(1 para sim /0 para não)");
        return scanner.nextBoolean();
    }

    public void falhaCadastro(RuntimeException e) {
        System.out.println("Falha ao cadastrar cliente!");
        System.err.println(e.getMessage());
    }

    public String readCod(){
        System.out.println("Informe o COD do cliente: ");
        return scanner.nextLine();
    }

    public boolean sucessoDelete(){
        System.out.println("Cliente deletado com sucesso!");
        System.out.println("\nDeseja deletar mais um cliente?(1 para sim /0 para não)");
        return scanner.nextBoolean();
    }

    public void falhaDelete(RuntimeException e){
        System.out.println("Falha ao deletar cliente!");
        System.err.println(e.getMessage());
    }

    public String updateNome(Cliente cliente){
        System.out.println("Nome atual do cliente: ");
        printNome(cliente);
        return readNome();
    }

    public String updateEndereco(Cliente cliente){
        System.out.println("Endereço atual do cliente: ");
        printEndereco(cliente);
        return readEndereco();
    }

    public String updateTelefone(Cliente cliente){
        System.out.println("Telefone atual do cliente: ");
        printTelefone(cliente);
        return readTelefone();
    }

    public boolean sucessoUpdate(Cliente cliente0, Cliente cliente1) {
        System.out.println("Cliente atualizado com sucesso!");
        System.out.println("Cliente anteriormente: ");
        printCliente(cliente0);
        System.out.println("Cliente atualmente: ");
        printCliente(cliente1);
        System.out.println("\nDeseja atualizar mais um cliente?(1 para sim /0 para não)");
        return scanner.nextBoolean();
    }

    public void falhaUpdate(RuntimeException e){
        System.out.println("Falha ao atualizar cliente!");
        System.err.println(e.getMessage());
    }

    public void falhaSearch(RuntimeException e){
        System.out.println("Falha ao procurar cliente: ");
        System.err.println(e.getMessage());
    }

    public void falhaGetList(RuntimeException e){
        System.out.println("Falha ao buscar todos os clientes no banco de dados:");
        System.err.println(e.getMessage());
    }
}
