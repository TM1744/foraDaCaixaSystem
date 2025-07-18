package camadas.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

public class OperacoesDao {
    public void setMargemLucro(Float valor){
        String setMargemLucro = "update MargemDeLucro set porcentagem = ? where MargemDeLucro.id = 1";
        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement(setMargemLucro);
            stm.setFloat(1, valor);
            stm.executeUpdate();
            stm.close();
            db.connection.close();
        }catch (SQLException e){
            throw new RuntimeException("Erro ao definir margem de lucro");
        }
    }

    public Float getFaturamentoBrutoMesAtual(){
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate fimMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        String getValorTotalPedidos = """
                        SELECT valorTotal FROM pedidos
                        WHERE dataRegistro BETWEEN ? AND ?;
                        """;
        Float faturamentoBruto = 0f;

        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement(getValorTotalPedidos);
            stm.setString(1, inicioMes.toString());
            stm.setString(2, fimMes.toString());
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()){
                faturamentoBruto += resultSet.getFloat("valorTotal");
            }
            stm.close();
            resultSet.close();
            db.connection.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return faturamentoBruto;
    }

    public Float getFaturamentoBrutoPorPeriodo(String dataIncial, String datafinal){
        String getValorTotalPedidos = """
                        SELECT valorTotal FROM pedidos
                        WHERE isFinalizado = 1 and dataRegistro BETWEEN ? AND ?;
                        """;
        Float faturamentoBruto = 0f;

        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement(getValorTotalPedidos);
            stm.setString(1, dataIncial);
            stm.setString(2, datafinal);
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()){
                faturamentoBruto += resultSet.getFloat("valorTotal");
            }
            stm.close();
            resultSet.close();
            db.connection.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return faturamentoBruto;
    }

    public Float getCustoTotalMateriaisNaoUtilizados(){
        String getCustoTotalMateriais = """
                select
                       sum(valor * quantidadeEstoque) as valorFinal
                from materiais;
                """;

        Float custoTotalMateriais = 0f;

        try{
            Database db = new Database();
            ResultSet resultSet = db.connection.createStatement().executeQuery(getCustoTotalMateriais);
            while (resultSet.next()) {
                custoTotalMateriais += resultSet.getFloat("valorFinal");
            }
            resultSet.close();
            db.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter custo total de materiais - " + e);
        }

        return custoTotalMateriais;
    }

    public Float getFaturamentoLiquidoMesAtual(){
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate fimMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        String getFaturamentoLiquido = """
                select
                    valorTotal - sum((pedidos.valor - sum(materiais.valor) as valorLiquido
                from itemProduto join pedidos on itemproduto.idpedido = pedidos.id
                        join itemMaterial on itemProduto.idProduto = itemMaterial.idProduto
                        join materiais on itemMaterial.idMaterial = materiais.id
                where pedidos.isFinalizado = 1 and dataRegistro BETWEEN ? AND ?;
                """;
        Float valorTotalLiquido = 0f;

        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement(getFaturamentoLiquido);
            stm.setString(1, inicioMes.toString());
            stm.setString(2, fimMes.toString());
            ResultSet resultSet = stm.executeQuery();
            while(resultSet.next()){
                valorTotalLiquido += resultSet.getFloat("valorLiquido");
            }
            stm.close();
            resultSet.close();
            db.connection.close();

        }catch (SQLException e){
            throw new RuntimeException("Erro ao obter Faturamento líquido do mês atual - " + e);
        }
        return valorTotalLiquido;
    }

    public Float getFaturamentoLiquidoPorPeriodo(String dataInicial, String dataFinal){
        String getFaturamentoLiquido = """
                select
                    valorTotal - sum((pedidos.valor - sum(materiais.valor) as valorLiquido
                from itemProduto join pedidos on itemproduto.idpedido = pedidos.id
                        join itemMaterial on itemProduto.idProduto = itemMaterial.idProduto
                        join materiais on itemMaterial.idMaterial = materiais.id
                where pedidos.isFinalizado = 1 and dataRegistro BETWEEN ? AND ?;
                """;
        Float valorTotalLiquido = 0f;

        try{
            Database db = new Database();
            PreparedStatement stm = db.connection.prepareStatement(getFaturamentoLiquido);
            stm.setString(1, dataInicial);
            stm.setString(2, dataFinal);
            ResultSet resultSet = stm.executeQuery();
            while(resultSet.next()){
                valorTotalLiquido += resultSet.getFloat("valorLiquido");
            }
            stm.close();
            resultSet.close();
            db.connection.close();

        }catch (SQLException e){
            throw new RuntimeException("Erro ao obter Faturamento líquido do mês atual - " + e);
        }
        return valorTotalLiquido;
    }
}
