package camadas;

import camadas.controller.*;
import camadas.model.domain.Material;

import java.time.LocalDate;
import java.time.Month;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        function1();
    }

    public static void function1() {
        ClienteController clienteController = new ClienteController();
        MaterialController materialController = new MaterialController();
        ProdutoController produtoController = new ProdutoController();
        PedidoController pedidoController = new PedidoController();
        OperacoesController operacoesController = new OperacoesController();


        Scanner scanner = new Scanner(System.in);
        String menuPrincipal = """
                ------------MENU------------
                - 1) Pedidos
                - 2) Produtos
                - 3) Materiais
                - 4) Clientes
                - 5) Operações
                - 0) Sair...
                ----------------------------
                """;
        String subMenuClientes = """
                ----------Clientes----------
                - 1) Cadastrar
                - 2) Atualizar
                - 3) Deletar
                - 4) Ver todos
                - 5) Procurar
                - 0) Voltar...
                ---------------------------
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
                ---------------------------
                """;
        String subMenuProdutos = """
                ----------Produtos----------
                - 1) Cadastrar
                - 2) Atualizar
                - 3) Deletar
                - 4) Ver todos
                - 5) Procurar
                - 0) Voltar...
                ---------------------------
                """;
        String subMenuPedidos = """
                ----------Pedidos----------
                - 1) Cadastrar
                - 2) Finalizar
                - 3) Deletar
                - 4) Ver todos
                - 5) Procurar
                - 0) Voltar...
                ---------------------------
                """;

        String subMenuOperacoes = """
                ----------Pedidos----------
                - 1) Definir margem de lucro
                - 2) Ver faturamento bruto do mês atual
                - 3) Ver faturamento bruto por período
                - 4) Ver custo total dos materiais restantes
                - 0) Voltar...
                ---------------------------
                """;
        Integer opcao = 0;
        Integer subOpcao = 0;

        do {
            System.out.println(menuPrincipal);
            System.out.println("Informe um número: ");
            opcao = scanner.nextInt();
            switch (opcao) {
                case 5:
                    do {
                        System.out.println(subMenuOperacoes);
                        System.out.println("Informe um número: ");
                        subOpcao = scanner.nextInt();
                        switch (subOpcao) {
                            case 1:
                                operacoesController.definirMargemLucro();
                                break;
                            case 2:
                                operacoesController.getFaturamentoBruto();
                                break;
                            case 3:
                                operacoesController.getFaturamentoBrutoPorPeriodo();
                                break;
                            case 4:
                                operacoesController.getValorTotalCustoMateriais();
                                break;
                            case 5:
                                break;
                            case 0:
                                System.out.println("Voltando...");
                                break;
                            default:
                        }
                    } while (subOpcao != 0);
                    break;

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
                            case 6:
                                materialController.updateQuantidadeEstoque();
                            case 0:
                                System.out.println("Voltando...");
                                break;
                            default:
                                System.err.println("Opção inválida!");
                        }
                    } while (subOpcao != 0);
                    break;

                case 2:
                    do{
                        System.out.println(subMenuProdutos);
                        System.out.println("Informe um número: ");
                        subOpcao = scanner.nextInt();
                        switch (subOpcao) {
                            case 1:
                                produtoController.cadastrar();
                                break;
                            case 2:
                                produtoController.update();
                                break;
                            case 3:
                                produtoController.delete();
                                break;
                            case 4:
                                produtoController.getList();
                                break;
                            case 5:
                                produtoController.searchProduto();
                                break;
                            case 0:
                                System.out.println("Voltando...");
                                break;
                            default:
                                System.err.println("Opção inválida!");
                        }
                    } while (subOpcao != 0);
                    break;

                case 1:
                    do{
                        System.out.println(subMenuPedidos);
                        System.out.println("Informe um número: ");
                        subOpcao = scanner.nextInt();
                        switch (subOpcao) {
                            case 1:
                                pedidoController.cadastrar();
                                break;
                            case 2:
                                pedidoController.finalizar();
                                break;
                            case 3:
                                pedidoController.deletar();
                                break;
                            case 4:
                                pedidoController.getList();
                                break;
                            case 5:
                                pedidoController.search();
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