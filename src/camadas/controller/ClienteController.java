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
            Cliente cliente0 = new Cliente(dao.get(view.readCod()));
            Cliente cliente1 = new Cliente(view.updateNome(cliente0), view.updateTelefone(cliente0), view.updateEndereco(cliente0), cliente0.getCod());
            dao.update(cliente1);
            view.sucessoUpdate(cliente0, cliente1);
        } catch (RuntimeException e){
            view.falhaUpdate(e);
        }
    }
}
