package camadas;

import camadas.controller.ClienteController;
import camadas.model.dao.Database;
import camadas.model.domain.Cliente;
import camadas.view.ClienteView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();
    }


    public void function1() {
        ClienteController controller = new ClienteController();

        Scanner scanner = new Scanner(System.in);
        String menuPrincipal = """
                ------------MENU------------
                - 1) Vendas
                - 2) Produtos
                - 3) Materiais
                - 4) Clientes
                - 5) Operações
                - 0) Sair...
                ----------------------------\n
                """;
        String subMenuClientes = """
                ----------Clientes----------
                - 1) Cadastrar
                - 2) Atualizar
                - 3) Deletar
                - 4) Ver todos
                - 5) Procurar
                - 0) Voltar...
                ---------------------------\n
                """;
        Integer opcao = 0;
        Integer subOpcao = 0;

        do {
            System.out.println(menuPrincipal);
            System.out.println("Informe um número: ");
            opcao = scanner.nextInt();
            switch (opcao) {
                case 4:
                    do {
                        System.out.println(subMenuClientes);
                        System.out.println("Informe um número: ");
                        subOpcao = scanner.nextInt();
                        switch (subOpcao) {
                            case 1:
                                controller.cadastrar();
                                break;
                            case 2:
                                controller.update();
                                break;
                            case 3:
                                controller.deletar();
                                break;
                            case 4:
                                controller.getList();
                                break;
                            case 5:
                                controller.search();
                                break;
                            case 0:
                                System.out.println("Voltando...");
                                break;
                            default:
                                System.err.println("Opção inválida!");
                        }
                    } while (subOpcao != 0);
                    break;
                default:
                    System.out.println("Opção inválida!");
                case 0:
                    System.out.println("Saindo...");
            }
        } while (opcao != 0);
    }
}