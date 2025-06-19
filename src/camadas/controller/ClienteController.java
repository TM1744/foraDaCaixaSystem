package camadas.controller;

import camadas.model.dao.ClienteDao;
import camadas.model.domain.Cliente;
import camadas.view.ClienteView;

public class ClienteController {
    private ClienteView view;
    private ClienteDao dao;

    public void cadastrar(){
        try{
            Cliente cliente;
            do{
                cliente = new Cliente(view.readNome(), view.readTelefone(), view.readEndereco());
                dao.create(cliente);
            }while (view.sucessoCadastro(cliente));
        } catch(RuntimeException e){
            view.falhaCadastro(e);
        }
    }

    public void deletar(){
        try{
            view.printClienteList(dao.getSearch(view.readNome()));
            do{
                dao.delete(view.readCod());
            } while (view.sucessoDelete());
        } catch (RuntimeException e){
            view.falhaDelete(e);
        }
    }

    public void update(){
        try {
            view.printClienteList(dao.getSearch(view.readNome()));
            Cliente cliente0;
            Cliente cliente1;
            do {
                cliente0 = new Cliente(dao.get(view.readCod()));
                cliente1 = new Cliente(view.updateNome(cliente0), view.updateTelefone(cliente0), view.updateEndereco(cliente0), cliente0.getCod());
                dao.update(cliente1);
            } while (view.sucessoUpdate(cliente0, cliente1));
        } catch (RuntimeException e){
            view.falhaUpdate(e);
        }
    }
}
