package camadas.controller;

import camadas.model.dao.OperacoesDao;
import camadas.view.OperacoesView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OperacoesController {
    OperacoesDao dao = new OperacoesDao();
    OperacoesView view = new OperacoesView();

    public void definirMargemLucro(){
        try{
            dao.setMargemLucro(view.readMargemLucro());
            view.printSucesso();
        } catch (RuntimeException e) {
            view.printFalha(e);
        }
    }

    public void getFaturamentoMesAtual(){
        try{
            view.printFaturamentoMensal(dao.getFaturamentoBrutoMesAtual(), dao.getFaturamentoLiquidoMesAtual());
        } catch (RuntimeException e) {
            view.printFalha(e);
        }
    }

    public void getFaturamentoPorPeriodo(){
        try{
            SimpleDateFormat formatoOriginal = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat novoFormato = new SimpleDateFormat("yyyy-MM-dd");

            Date dataInicial = formatoOriginal.parse(view.readDataInicial());
            Date dataFinal = formatoOriginal.parse(view.readDataFinal());

            String dataInicialFormatada = novoFormato.format(dataInicial);
            String dataFinalFormatada = novoFormato.format(dataFinal);

            view.printFaturamentoPorPeriodo(dao.getFaturamentoBrutoPorPeriodo(dataInicialFormatada, dataFinalFormatada), dao.getFaturamentoLiquidoPorPeriodo(dataInicialFormatada, dataFinalFormatada));
        }catch (RuntimeException | ParseException e){
            view.printFalha(e);
        }
    }

    public void getValorTotalCustoMateriais(){
        try{
            view.printValorTotalCustoMateriais(dao.getCustoTotalMateriaisNaoUtilizados());
        }catch (RuntimeException e){
            view.printFalha(e);
        }
    }






}
