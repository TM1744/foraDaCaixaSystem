package camadas.controller;

import camadas.model.dao.ClienteDao;
import camadas.model.dao.PedidoDao;
import camadas.model.dao.ProdutoDao;
import camadas.model.domain.Cliente;
import camadas.model.domain.ItemProduto;
import camadas.model.domain.Pedido;
import camadas.model.domain.Produto;
import camadas.view.ClienteView;
import camadas.view.PedidoView;
import camadas.view.ProdutoView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoController {
    private final PedidoDao pedidoDao = new PedidoDao();
    private final PedidoView pedidoView = new PedidoView();
    private final ClienteDao clienteDao = new ClienteDao();
    private final ClienteView clienteView = new ClienteView();
    private final ProdutoDao produtoDao = new ProdutoDao();
    private final ProdutoView produtoView = new ProdutoView();

    private List<ItemProduto> cadastrarItemProduto(){
        List<ItemProduto> itemProdutoList = new ArrayList<>();
        do{
            List<Produto> produtoSearch = produtoDao.getSearch(produtoView.readDescricao());
            if(produtoSearch.isEmpty()){
                throw new RuntimeException("Nenhum produto encontrado com a descrição informada");
            }else {
                produtoView.printProdutoList(produtoSearch);
            }
            itemProdutoList.add(
                    new ItemProduto(
                            new Produto(produtoDao.get(produtoView.readCod())),
                            pedidoView.readQuantidadeItemProduto()
                    )
            );
        }while (pedidoView.isAddProduto());
        return itemProdutoList;
    }

    public void cadastrar(){
        try{
            List<ItemProduto> produtos = cadastrarItemProduto();
            List<Cliente> clienteList = clienteDao.getSearch(clienteView.readNome());
            if(clienteList.isEmpty()){
                throw new RuntimeException("Nenhum cliente encontrado com o nome informado");
            }
            clienteView.printClienteList(clienteList);
            Cliente cliente = clienteDao.get(clienteView.readCod());
            boolean isVenda = pedidoView.readIsVenda();
            if(isVenda){
                Pedido pedido = new Pedido(produtos, cliente, true);
                pedidoDao.create(pedido);
                pedidoView.printSucessoCadastro(pedido);
            }else {
                Pedido pedido = new Pedido(pedidoView.readDataEntrega(), produtos, cliente);
                pedidoDao.create(pedido);
                pedidoView.printSucessoCadastro(pedido);
            }
        }catch (RuntimeException e){
            pedidoView.printErro(e);
        }
    }

    public void deletar(){
        try{
            search();
            pedidoDao.delete(pedidoView.readCod());
            pedidoView.printSucesso();
        } catch (Exception e) {
            pedidoView.printErro(e);
        }
    }

    public void getList(){
        try{
            switch (pedidoView.isFinalizado()){
                case 1:
                    pedidoView.printPedidoList(pedidoDao.getListFinalizados());
                    break;

                case 2:
                    pedidoView.printPedidoList(pedidoDao.getListNaoFinalizados());
                    break;

                case 3:
                    pedidoView.printPedidoList(pedidoDao.getList());
                    break;
            }
        }catch (RuntimeException e){
            pedidoView.printErro(e);
        }
    }

    public void search(){
        Integer valor = pedidoView.isByNomeCliente();

        if(valor == 1){
            List<Pedido> pedidos = pedidoDao.getSearchByNomeCliente(clienteView.readNome(), pedidoView.isFinalizado());
            if(pedidos.isEmpty()){
                throw new RuntimeException("Nenhum produto encontrado com a descrição informada");
            }else {
                pedidoView.printPedidoList(pedidos);
            }
        }if(valor == 2){
            String dataFormatada;
            try {
                SimpleDateFormat formatoOriginal = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat novoFormato = new SimpleDateFormat("yyyy-MM-dd");
                Date data = formatoOriginal.parse(pedidoView.readDataEntrega());
                dataFormatada = novoFormato.format(data);
            }catch (ParseException e){
                throw new RuntimeException("Erro ao converter data");
            }
            List<Pedido> pedidos = pedidoDao.getSearchByDataEntrega(dataFormatada, pedidoView.isFinalizado());
            if(pedidos.isEmpty()){
                throw new RuntimeException("Nenhum produto encontrado com a descrição informada");
            }else {
                pedidoView.printPedidoList(pedidos);
            }
        }
        else{
            throw new RuntimeException("Valor informado não corresponde as opções");
        }
    }

    public void finalizar(){
        try{
            search();
            pedidoDao.finalizar(pedidoView.readCod());
            pedidoView.printSucesso();
        }catch (RuntimeException e){
            pedidoView.printErro(e);
        }
    }
}
