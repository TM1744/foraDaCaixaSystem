package camadas;

import camadas.controller.ClienteController;
import camadas.controller.MaterialController;
import camadas.model.domain.Material;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        function1();
    }


    public static void function1() {
        ClienteController clienteController = new ClienteController();
        MaterialController materialController = new MaterialController();

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
        String subMenuMateriais = """
                ----------Materiais---------
                - 1) Cadastrar
                - 2) Atualizar
                - 3) Deletar
                - 4) Ver todos
                - 5) Procurar
                - 6) Definir quantidade em estoque
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
                                clienteController.cadastrar();
                                break;
                            case 2:
                                clienteController.update();
                                break;
                            case 3:
                                clienteController.deletar();
                                break;
                            case 4:
                                clienteController.getList();
                                break;
                            case 5:
                                clienteController.search();
                                break;
                            case 0:
                                System.out.println("Voltando...");
                                break;
                            default:
                                System.err.println("Opção inválida!");
                        }
                    } while (subOpcao != 0);
                    break;

                case 3:
                    do{
                        System.out.println(subMenuMateriais);
                        System.out.println("Informe um número: ");
                        subOpcao = scanner.nextInt();
                        switch (subOpcao) {
                            case 1:
                                materialController.cadastrar();
                                break;
                            case 2:
                                materialController.update();
                                break;
                            case 3:
                                materialController.deletar();
                                break;
                            case 4:
                                materialController.getList();
                                break;
                            case 5:
                                materialController.search();
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