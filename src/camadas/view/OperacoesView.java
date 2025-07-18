package camadas.view;

import java.util.Scanner;

public class OperacoesView {
    Scanner scanner = new Scanner(System.in);

    public Float readMargemLucro() {
        scanner = new Scanner(System.in);
        System.out.println("Informe a margem de lucro que deve ser aplicada aos produtos (não é necessário usar '%'): ");
        return scanner.nextFloat();
    }

    public void printSucesso() {
        System.out.println("Sucesso ao realizar a operação");
    }

    public void printFalha(Exception e) {
        System.err.println("Erro: " + e);
    }

    public void printFaturamentoBruto(Float faturamentoBrutoMesAtual) {
        System.out.println("Faturamento bruto do mês atual: $" + faturamentoBrutoMesAtual);
    }

    public String readDataInicial() {
        scanner = new Scanner(System.in);
        System.out.println("Informe a data inicial (padrão DD/MM/AAAA): ");
        return scanner.nextLine();
    }

    public String readDataFinal() {
        scanner = new Scanner(System.in);
        System.out.println("Informe a data final (padrão DD/MM/AAAA): ");
        return scanner.nextLine();
    }

    private void printFaturamentoBrutoPorPeriodo(Float faturamentoBrutoPorPeriodo) {
        System.out.println("Faturamento bruto por período informado: $" + faturamentoBrutoPorPeriodo);
    }

    public void printValorTotalCustoMateriais(Float custoTotalMateriaisNaoUtilizados) {
        System.out.println("Valor que deve ser obtido para pagar o custo dos materias restantes: $" + custoTotalMateriaisNaoUtilizados);
    }

    private void printFaturamentoLiquidoMes(Float faturamentoLiquidoMesAtual) {
        System.out.println("Faturamento liquido do mês atual: $" + faturamentoLiquidoMesAtual);
    }

    private void printFaturamentoLiquidoPorPeriodo(Float faturamento){
        System.out.println("Faturamento liquido por período informado: $" + faturamento);
    }

    public void printFaturamentoMensal(Float bruto, Float liquido){
        printFaturamentoBruto(bruto);
        printFaturamentoLiquidoMes(liquido);
        System.out.println();
    }

    public void printFaturamentoPorPeriodo(Float bruto, Float liquido){
        printFaturamentoBrutoPorPeriodo(bruto);
        printFaturamentoLiquidoPorPeriodo(liquido);
        System.out.println();
    }
}
