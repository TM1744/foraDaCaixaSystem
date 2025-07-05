package camadas.controller;

import camadas.model.dao.MaterialDao;
import camadas.model.domain.Material;
import camadas.view.MaterialView;

import java.util.ArrayList;
import java.util.List;

public class MaterialController {
    private final MaterialDao dao = new MaterialDao();
    private final MaterialView view = new MaterialView();

    public void cadastrar(){
        try{
            Material material = new Material(view.readDescricao(), view.readValor(), view.readQuantidadeEstoque());
            dao.create(material);
            view.sucessoCadastro(material);
        }catch (RuntimeException e){
            view.falhaCadastro(e);
        }
    }
    public void deletar(){
        try{
            search();
            dao.delete(view.readCod());
            view.sucessoDelete();
        }catch (RuntimeException e){
            view.falhaDelete(e);
        }
    }

    public void update(){
        try{
            search();
            Material material0 = new Material(dao.get(view.readCod()));
            Material material1 = new Material(view.updateDescricao(material0.getDescricao()), view.updateValor(material0.getValor()), material0.getQuantidadeEstoque());
            dao.update(material0.getCod(), material1);
            view.sucessoUpdate(material0, material1);
        }catch (RuntimeException e){
            view.falhaUpdate(e);
        }
    }

    public void getList(){
        try{
            List<Material> materials = dao.getList();
            if(materials.isEmpty()){
                view.notFound();
            } else {
                view.printMaterialList(materials);
            }
        } catch (RuntimeException e){
            view.falhaGetList(e);
        }
    }

    public void search(){
        try {
            List<Material> lista = dao.getSearch(view.readDescricao());
            if (lista.isEmpty()) {
                view.notFound();
            } else {
                view.printMaterialList(lista);
            }
        }catch (RuntimeException e){
            view.falhaSearch(e);
        }
    }

    public void updateQuantidadeEstoque(){
        try{
            search();
            dao.updateQuantidadeEstoque(view.readCod(), view.readQuantidadeEstoque());
            System.out.println("Sucesso ao atualizar quantidade em estoque!");
        } catch (RuntimeException e){
            view.falhaUpdate(e);
        }
    }

}
