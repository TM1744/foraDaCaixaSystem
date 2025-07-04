package camadas.view;

import camadas.model.domain.ItemMaterial;
import camadas.model.domain.Produto;

import java.util.List;
import java.util.Scanner;

public class ProdutoView {
    Scanner scanner = new Scanner(System.in);

    public void exibirProduto(Produto produto){
        System.out.println();
        System.out.println("PRODUTO--------------------");
        System.out.println("Descrição: " + produto.getDescricao());
        System.out.println("Valor: " + produto.getValor());
        for(ItemMaterial item : produto.getItensMateriais()){
            System.out.println("Material: " + item.getMaterial().getDescricao() + " x " + item.getQuantidade());
        }
        System.out.println("COD: " + produto.getCod());
        System.out.println();
    }

    public Integer readQuantidadeMaterial() {
        scanner = new Scanner(System.in);
        System.out.println("Informe a quantidade desse material que será utilizado:");
        return scanner.nextInt();
    }

    public boolean isAddMaterial() {
        scanner = new Scanner(System.in);
        System.out.println("Deseja acrescentar mais algum material? (1- Sim / 2- Não)");
        return scanner.nextInt() == 1;
    }

    public void falha(RuntimeException e) {
        System.err.println("Erro ao realizar operação - " + e);
    }

    public String readDescricao() {
        scanner = new Scanner(System.in);
        System.out.println("Informe a descrição do produto: ");
        return scanner.nextLine();
    }

    public Float readValor(String valorMinimo, String margemLucro, String valorSugerido) {
        scanner = new Scanner(System.in);
        System.out.println("--- Valor mínimo de venda: $" + valorMinimo + " / Margem de lucro: " + margemLucro + "%" + " / Valor sugerido: $" + valorSugerido + " ---");
        System.out.println("Informe valor de venda do produto: ");
        return scanner.nextFloat();
    }

    public void sucesso() {
        System.out.println("Sucesso ao realizar a operação!");
    }

    public String readCod() {
        scanner = new Scanner(System.in);
        System.out.println("Informe o COD do produto: ");
        return scanner.nextLine();
    }

    public String updateDescricao(String descricao) {
        scanner = new Scanner(System.in);
        System.out.println("Descrição atual do produto: " + descricao);
        System.out.println("Informe a nova descrição do produto: ");
        return scanner.nextLine();
    }

    public Float updateValor(Float valor) {
        scanner = new Scanner(System.in);
        System.out.println("Valor atual do produto: " + valor);
        System.out.println("Informe o novo valor do produto: ");
        return scanner.nextFloat();
    }

    public void notFound() {
        System.out.println("Nenhum produto encontrado...");
    }

    public void printProdutoList(List<Produto> produtos) {
        for (Produto produto : produtos){
            exibirProduto(produto);
        }
    }
}
