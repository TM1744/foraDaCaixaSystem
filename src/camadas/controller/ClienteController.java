package camadas.controller;

import camadas.model.dao.ClienteDao;
import camadas.model.domain.Cliente;
import camadas.view.ClienteView;

public class ClienteController {
    private ClienteView view;
    private ClienteDao dao;

    public void cadastrarCliente(){
        try{
            Cliente cliente = new Cliente(view.readNome(), view.readTelefone(), view.readEndereco());
            dao.create(cliente);
            view.sucessoCadastro(cliente);
        } catch(RuntimeException e){
            view.falhaCadastro(e);
        }
    }

    public void deletarCliente(){
        try{
            dao.delete(view.readCod());
            view.sucessoDelete();
        } catch (RuntimeException e){
            view.falhaDelete(e);
        }
    }

    public void updateCliente(){
        try{

        }
    }
}
