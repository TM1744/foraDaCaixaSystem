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

    public void getFaturamentoBruto(){
        try{
            view.printFaturamentoBruto(dao.getFaturamentoBrutoMesAtual());
        } catch (RuntimeException e) {
            view.printFalha(e);
        }
    }

    public void getFaturamentoBrutoPorPeriodo(){
        try{
            SimpleDateFormat formatoOriginal = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat novoFormato = new SimpleDateFormat("yyyy-MM-dd");

            Date dataInicial = formatoOriginal.parse(view.readDataInicial());
            Date dataFinal = formatoOriginal.parse(view.readDataFinal());

            view.printFaturamentoBrutoPorPeriodo(dao.getFaturamentoBrutoPorPeriodo(novoFormato.format(dataInicial), novoFormato.format(dataFinal)));
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
