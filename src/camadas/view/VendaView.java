package camadas.view;

import camadas.model.domain.Cliente;
import camadas.model.domain.ItemProduto;

import java.time.LocalDate;
import java.util.List;

public class VendaView {
    public void exibirVenda(Float valorTotal, LocalDate dataEntrega, List<ItemProduto> produtos, Cliente cliente){
        System.out.println("Cliente: " + cliente);
        System.out.println("Valor total: " + valorTotal);
        System.out.println("Data entrega: " + dataEntrega.getDayOfMonth() + "/" + dataEntrega.getMonthValue() + "/" + dataEntrega.getYear());
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println("Produto: " + produtos.get(i).getProduto().getDescricao() + " / " + "Quantidade: " + produtos.get(i).getQuantidade());
        }

    }
}
