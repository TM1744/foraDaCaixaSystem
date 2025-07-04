package camadas.view;

import camadas.model.domain.Material;

import java.util.List;
import java.util.Scanner;

public class MaterialView {
    Scanner scanner = new Scanner(System.in);

    public String readDescricao() {
        scanner = new Scanner(System.in);
        System.out.println("Informe a descrição do material:");
        return scanner.nextLine();
    }

    public Float readValor() {
        scanner = new Scanner(System.in);
        System.out.println("Informe o custo do material:");
        return scanner.nextFloat();
    }

    public Integer readQuantidadeEstoque() {
        scanner = new Scanner(System.in);
        System.out.println("Informe a quantida em estoque do material:");
        return scanner.nextInt();
    }

    public String readCod() {
        scanner = new Scanner(System.in);
        System.out.println("Informe a COD do material:");
        return scanner.nextLine();
    }

    public void sucessoCadastro(Material material) {
        System.out.println("Material cadastrado com sucesso!");
    }

    public void falhaCadastro(RuntimeException e) {
        System.out.println("Falha ao cadastrar o material - " + e.getMessage());
    }

    public void sucessoDelete() {
        System.out.println("Sucesso ao deletar material!");
    }

    public void falhaDelete(RuntimeException e) {
        System.out.println("Falha ao deletar o material - " + e.getMessage());
    }

    public String updateDescricao(String descricaoAntiga) {
        System.out.println("Descrição antiga: " + descricaoAntiga);
        System.out.println("Informe a nova descricao:");
        return scanner.nextLine();
    }

    public Float updateValor(Float valorAntigo) {
        System.out.println("Valor antigo: " + valorAntigo);
        System.out.println("Informe o novo custo:");
        return scanner.nextFloat();
    }

    public void sucessoUpdate(Material material0, Material material1) {
        System.out.println("Sucesso ao atualizar o material!");
        System.out.println("Material antigo:\n");
        printMaterial(material0);
        System.out.println("Material novo:\n");
        printMaterial(material1);
    }

    public void falhaUpdate(RuntimeException e) {
        System.out.println("Falha ao atualizar o material - " + e.getMessage());
    }

    public void printMaterialList(List<Material> list) {
        for(Material material : list){
            System.out.println();
            printMaterial(material);
            System.out.println();
        }
    }

    public void falhaGetList(RuntimeException e) {
        System.out.println("Falha ao retornar lista de materiais - " + e.getMessage());
    }

    public void falhaSearch(RuntimeException e) {
        System.out.println("Falha ao retornar lista de busca de materias - " +e.getMessage());
    }

    private void printMaterial(Material material){
        System.out.println("MATERIAL--------------------");
        System.out.println("Descrição: " + material.getDescricao());
        System.out.println("Custo: " + material.getValor());
        System.out.println("COD: " + material.getCod());
        System.out.println("Quantidade em estoque: " + material.getQuantidadeEstoque());
    }

    public void notFound(){
        System.err.println("Material(is) não encontrado(s)");
    }
}
