package dao;

import core.Database;
import entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDao {

    private Connection connection;

    public CustomerDao() {
        this.connection = Database.getInstance();
    }

    public ArrayList<Customer> findAll(){
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            ResultSet rs = this.connection.createStatement().executeQuery("Select * from customer");
            while (rs.next()){
                customers.add(this.match(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }
    public Customer match(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("name"));
        customer.setMail(rs.getString("mail"));
        customer.setPhone(rs.getString("phone"));
        customer.setAddress(rs.getString("address"));
        customer.setType(Customer.TYPE.valueOf(rs.getString("type")));
        return customer;
    }

    public boolean save(Customer customer){
        String query = "INSERT INTO customer (name,type,phone,mail,address) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1,customer.getName());
            pr.setString(2,customer.getType().toString());
            pr.setString(3,customer.getPhone());
            pr.setString(4,customer.getMail());
            pr.setString(5,customer.getAddress());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public Customer getById(int id){
        Customer customer = null;
        String query = "Select * from customer where id = ?";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1,id);
            ResultSet rs = pr.executeQuery();
            if(rs.next()){
                customer = this.match(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

    public boolean update(Customer customer){
        String query = "UPDATE customer SET "+
                "name = ?, "+
                "type = ?, "+
                "phone = ?, "+
                "mail = ?, "+
                "address = ? "+
                "where id = ?";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1,customer.getName());
            pr.setString(2,customer.getType().toString());
            pr.setString(3,customer.getPhone());
            pr.setString(4,customer.getMail());
            pr.setString(5,customer.getAddress());
            pr.setInt(6,customer.getId());
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean delete(int customerId){
        String query = "Delete from customer where id = ?";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1,customerId);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Customer> query(String query){
        ArrayList<Customer> customers = new ArrayList<>();
        try {
            ResultSet rs = this.connection.createStatement().executeQuery(query);
            while (rs.next()){
                customers.add(this.match(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }
}
