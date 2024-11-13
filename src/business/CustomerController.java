package business;

import core.Helper;
import dao.CustomerDao;
import entity.Customer;

import java.util.ArrayList;

public class CustomerController {
    private final CustomerDao customerDao = new CustomerDao();

    public ArrayList<Customer> findAll(){
        return this.customerDao.findAll();
    }

    public boolean save(Customer customer){
        return this.customerDao.save(customer);
    }

    public Customer getById(int id){
        return this.customerDao.getById(id);
    }

    public boolean update(Customer customer){
        if(this.getById(customer.getId()) == null){
            Helper.showMessage("The customer with ID "+customer.getId()+" was not found.");
            return false;
        }
        return this.customerDao.update(customer);

    }

    public boolean delete(int id){
        if (this.getById(id) == null){
            Helper.showMessage("The customer with ID "+id+" was not found.");
            return false;
        }

        return this.customerDao.delete(id);
    }

    public ArrayList<Customer> filter (String customerName, Customer.TYPE customerType){
        String query = "select * from customer";
        ArrayList<String> whereList = new ArrayList<>();

        if(customerName.length() > 0){
            whereList.add("name like '%"+ customerName+"%'");
        }

        if(customerType != null){
            whereList.add("type = '"+customerType+"'");
        }

        if (whereList.size()>0){
            String whereQuery = String.join(" and ",whereList);
            query += " where "+whereQuery;
        }

        return this.customerDao.query(query);
    }

}
