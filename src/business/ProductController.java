package business;

import core.Helper;
import core.Item;
import dao.ProductDao;
import entity.Product;

import java.util.ArrayList;

public class ProductController {
    private final ProductDao productDao = new ProductDao();

    public ArrayList<Product> findAll(){
        return this.productDao.findAll();
    }

    public Product getById (int id){
        return this.productDao.getById(id);
    }

    public boolean save(Product product){
        return this.productDao.save(product);
    }

    public boolean update(Product product){
        if (this.getById(product.getId()) == null){
            Helper.showMessage("The product with ID "+product.getId()+" was not found.");
            return false;
        }
        return this.productDao.update(product);
    }

    public boolean delete(int id){
        if (this.getById(id)==null){
            Helper.showMessage("The product with ID "+id+" was not found.");
            return false;
        }
        return this.productDao.delete(id);
    }

    public ArrayList<Product> filter (String name,String code, Item isStock){
        String query = "select * from product";
        ArrayList<String> whereList = new ArrayList<>();

        if (name.length()>0){
            whereList.add("name like '%"+name+"%'");
        }
        if (code.length()>0){
            whereList.add("code like '%"+code+"%'");
        }
        if (isStock!=null){
            if (isStock.getKey()==1){
                whereList.add("stock > 0");
            }else{
                whereList.add("stock <= 0");
            }
        }
        if (whereList.size()>0){
            String whereQuery = String.join(" and ",whereList);
            query += " where "+whereQuery;
        }

        return this.productDao.query(query);

    }
}
