package camadas.controller;

import camadas.model.dao.MaterialDao;
import camadas.model.dao.ProdutoDao;
import camadas.model.domain.ItemMaterial;
import camadas.model.domain.Material;
import camadas.model.domain.Produto;
import camadas.view.MaterialView;
import camadas.view.ProdutoView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProdutoController {
    ProdutoView produtoView = new ProdutoView();
    ProdutoDao produtoDao = new ProdutoDao();
    MaterialDao materialDao = new MaterialDao();
    MaterialView materialView = new MaterialView();

    private Set<ItemMaterial> cadastrarItemMaterial(){
        Set<ItemMaterial> materials = new HashSet<>();
        try{
            do{
                searchMaterial();
                Material material = new Material(materialDao.get(materialView.readCod()));
                materials.add(new ItemMaterial(material, produtoView.readQuantidadeMaterial()));
            }while (produtoView.isAddMaterial());
        } catch (RuntimeException e){
            produtoView.falha(e);
        }
        return materials;
    }

    public void cadastrar (){
        try{
            Set<ItemMaterial> materialSet = cadastrarItemMaterial();
            float valorMinimo = 0f;
            for(ItemMaterial item : materialSet){
                valorMinimo += item.getMaterial().getValor() * item.getQuantidade();
            }
            Float margemLucro = produtoDao.getMargemLucro();
            Float valorSugerido = valorMinimo + (valorMinimo * (margemLucro / 100));
            String valorMinimoFormatado = String.format("%.2f", valorMinimo);
            String margemLucroFormatado = String.format("%.1f", margemLucro);
            String valorSugeridoFormatado = String.format("%.2f", valorSugerido);
            Produto produto = new Produto(
                    produtoView.readDescricao(),
                    produtoView.readValor(valorMinimoFormatado, margemLucroFormatado, valorMinimoFormatado),
                    materialSet,
                    margemLucro
            );
            produtoDao.create(produto);
            produtoView.sucesso();
        }catch (RuntimeException e){
            produtoView.falha(e);
        }
    }

    public void delete(){
        try{
            searchProduto();
            produtoDao.delete(produtoView.readCod());
            produtoView.sucesso();
        }catch (RuntimeException e){
            produtoView.falha(e);
        }
    }

    public void update(){
        try{
            searchProduto();
            Produto produto0 = new Produto(produtoDao.get(produtoView.readCod()));
            Produto produto1 = new Produto(produtoView.updateDescricao(produto0.getDescricao()), produtoView.updateValor(produto0.getValor()), cadastrarItemMaterial(), produtoDao.getMargemLucro());
            produtoDao.update(produto0.getCod(), produto1);
            produtoView.sucesso();
        }catch (RuntimeException e){
            produtoView.falha(e);
        }
    }

    public void getList(){
        try{
            List<Produto> produtos = new ArrayList<>(produtoDao.getList());
            if(produtos.isEmpty()){
                produtoView.notFound();
            } else {
                produtoView.printProdutoList(produtos);
            }
        } catch (RuntimeException e){
            produtoView.falha(e);
        }
    }

    private void searchMaterial(){
        try {
            List<Material> lista = materialDao.getSearch(materialView.readDescricao());
            if (lista.isEmpty()) {
                materialView.notFound();
            } else {
                materialView.printMaterialList(lista);
            }
        }catch (RuntimeException e){
            materialView.falhaSearch(e);
        }
    }

    public void searchProduto(){
        try{
            List<Produto> produtos = produtoDao.getSearch(produtoView.readDescricao());
            if(produtos.isEmpty()){
                produtoView.notFound();
            }else {
                produtoView.printProdutoList(produtos);
            }
        }catch (RuntimeException e){
            produtoView.falha(e);
        }
    }
}
