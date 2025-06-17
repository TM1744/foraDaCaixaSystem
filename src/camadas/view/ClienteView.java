package camadas.view;

import camadas.model.domain.Cliente;

import java.util.Scanner;

public class ClienteView {
    private Scanner scanner = new Scanner(System.in);

    public void exibirCliente(Cliente cliente){
        System.out.println("---Cliente:");
        printNome(cliente);
        printTelefone(cliente);
        printEndereco(cliente);
        System.out.println("------------------------------------------------------------------");
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

    public String readNome(){
        System.out.println("Informe o nome do cliente: ");
        return scanner.next();
    }

    public String readTelefone(){
        System.out.println("Informe o telefone do cliente: ");
        return scanner.next();
    }

    public String readEndereco(){
        System.out.println("Informe o endereço do cliente: ");
        return scanner.next();
    }

    public void sucessoCadastro(Cliente cliente){
        System.out.println("Cliente cadastrado com sucesso!\n");
        exibirCliente(cliente);
    }

    public void falhaCadastro(RuntimeException e) {
        System.out.println("Falha ao cadastrar cliente!");
        System.err.println(e.getMessage());
    }

    public String readCod(){
        System.out.println("Informe o COD do cliente: ");
        return scanner.next();
    }

    public void sucessoDelete(){
        System.out.println("Cliente deletado com sucesso!");
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
}
