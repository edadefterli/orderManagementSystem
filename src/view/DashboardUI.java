package view;

import business.BasketController;
import business.CartController;
import business.CustomerController;
import business.ProductController;
import core.Helper;
import core.Item;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DashboardUI extends JFrame {
    private JPanel container;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JTabbedPane tab_menu;
    private JPanel pnl_customer;
    private JScrollPane scrl_customer;
    private JTable tbl_customer;
    private JPanel pnl_customer_filter;
    private JTextField fld_f_customer_name;
    private JComboBox<Customer.TYPE> cmb_f_customer_type;
    private JButton btn_customer_filter;
    private JButton btn_customer_filter_reset;
    private JButton btn_customer_new;
    private JLabel lbl_f_customer_name;
    private JLabel lbl_f_customer_type;
    private JPanel pnl_product;
    private JScrollPane scrl_product;
    private JTable tbl_product;
    private JPanel pnl_product_filter;
    private JTextField fld_f_product_name;
    private JTextField fld_f_product_code;
    private JComboBox<Item> cmb_f_product_stock;
    private JButton btn_product_filter;
    private JButton btn_f_product_clear;
    private JButton btn_f_product_new;
    private JLabel lbl_f_product_name;
    private JLabel lbl_f_product_code;
    private JLabel lbl_f_product_stock;
    private JPanel pnl_basket;
    private JPanel pnl_basket_top;
    private JScrollPane scrl_basket;
    private JLabel lbl_;
    private JComboBox<Item> cmb_basket_customer;
    private JLabel lbl_basket_price;
    private JButton btn_basket_reset;
    private JButton btn_basket_new;
    private JLabel lbl_basket_count;
    private JTable tbl_basket;
    private JScrollPane scrl_cart;
    private JTable tbl_cart;
    private User user;
    private CustomerController customerController;
    private ProductController productController;
    private BasketController basketController;
    private CartController cartController;
    private DefaultTableModel mdl_customer_table = new DefaultTableModel();
    private DefaultTableModel mdl_product_table = new DefaultTableModel();
    private DefaultTableModel mdl_basket_table = new DefaultTableModel();
    private DefaultTableModel mdl_cart_table = new DefaultTableModel();
    private JPopupMenu popup_customer = new JPopupMenu();
    private JPopupMenu popup_product = new JPopupMenu();

    public DashboardUI(User user){
        this.user = user;
        this.customerController = new CustomerController();
        this.productController = new ProductController();
        this.basketController = new BasketController();
        this.cartController = new CartController();

        if(user==null){
            Helper.showMessage("error");
            dispose();
        }

        this.add(container);
        this.setTitle("Order Management System");
        this.setSize(1000,500);
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2;
        this.setLocation(x,y);
        this.setVisible(true);

        this.lbl_welcome.setText("Welcome "+this.user.getName());

        this.btn_logout.addActionListener(e -> {
            dispose();
            LoginUI loginUI = new LoginUI();
        });

        //CUSTOMER TAB
        loadCustomerTable(null);
        loadCustomerPopupMenu();
        loadCustomerButtonEvent();
        this.cmb_f_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));
        this.cmb_f_customer_type.setSelectedItem(null);

        //PRODUCT TAB
        loadProductTable(null);
        loadProductPopupMenu();
        loadProductButtonEvent();

        this.cmb_f_product_stock.addItem(new Item(1,"In stock"));
        this.cmb_f_product_stock.addItem(new Item(2,"Out of stock"));
        this.cmb_f_product_stock.setSelectedItem(null);

        //BASKET TAB
        loadBasketTable();
        loadBasketButtonEvent();
        loadBasketCustomerCombo();

        //CART TAB
        loadCartTable();


    }

    private void loadProductButtonEvent(){

        this.btn_f_product_new.addActionListener(e -> {
            ProductUI productUI = new ProductUI(new Product());
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                }
            });
        });

        this.btn_product_filter.addActionListener(e -> {
            ArrayList<Product> filteredProducts = this.productController.filter(
                    this.fld_f_product_name.getText(),
                    this.fld_f_product_code.getText(),
                    (Item) this.cmb_f_product_stock.getSelectedItem()
            );
            loadProductTable(filteredProducts);
        });

        this.btn_f_product_clear.addActionListener(e -> {
            loadProductTable(null);
            this.fld_f_product_name.setText(null);
            this.fld_f_product_code.setText(null);
            this.cmb_f_product_stock.setSelectedItem(null);
        });

    }

    private void loadCustomerButtonEvent(){
        this.btn_customer_new.addActionListener(e -> {
            CustomerUI customerUI = new CustomerUI(new Customer());
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }
            });
        });

        this.btn_customer_filter.addActionListener(e -> {
            ArrayList<Customer> filteredCustomers = this.customerController.filter(
                    this.fld_f_customer_name.getText(),
                    (Customer.TYPE) this.cmb_f_customer_type.getSelectedItem()
            );
            loadCustomerTable(filteredCustomers);
        });

        this.btn_customer_filter_reset.addActionListener(e -> {
            loadCustomerTable(null);
            this.fld_f_customer_name.setText(null);
            this.cmb_f_customer_type.setSelectedItem(null);
        });
    }

    private void loadCustomerTable(ArrayList<Customer> customers){
        Object[] columnCustomer = {"ID","Customer Name", "Type","Phone Number", "E-Mail","Address"};

        if(customers == null){
            customers = this.customerController.findAll();
        }

        //reset table
        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_customer.getModel();
        clearModel.setRowCount(0);

        this.mdl_customer_table.setColumnIdentifiers(columnCustomer);
        for (Customer customer : customers){
            Object[] rowObject = {customer.getId(),
                                  customer.getName(),
                                  customer.getType(),
                                  customer.getPhone(),
                                  customer.getMail(),
                                  customer.getAddress()
            };
            this.mdl_customer_table.addRow(rowObject);
        }

        this.tbl_customer.setModel(mdl_customer_table);
        this.tbl_customer.getTableHeader().setReorderingAllowed(false);
        this.tbl_customer.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_customer.setEnabled(false);

    }

    private void loadCustomerPopupMenu(){
        this.tbl_customer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_customer.rowAtPoint(e.getPoint());
                tbl_customer.setRowSelectionInterval(selectedRow,selectedRow);
            }
        });

        this.popup_customer.add("Update").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(),0).toString());
            CustomerUI customerUI = new CustomerUI(this.customerController.getById(selectId));
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }
            });
        });

        this.popup_customer.add("Delete").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(),0).toString());
            if(Helper.confirm("sure")){
                if(this.customerController.delete(selectId)){
                    Helper.showMessage("done");
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }else{
                    Helper.showMessage("error");
                }
            }

        });

        this.tbl_customer.setComponentPopupMenu(this.popup_customer);

    }

    private void loadProductTable(ArrayList<Product> products){
        Object[] columnProduct = {"ID","Name","Code","Price","Stock"};

        if(products == null){
            products = this.productController.findAll();
        }

        //RESET TABLE
        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_product.getModel();
        clearModel.setRowCount(0);

        this.mdl_product_table.setColumnIdentifiers(columnProduct);
        for (Product product : products){
            Object[] rowObject = {
                    product.getId(),
                    product.getName(),
                    product.getCode(),
                    product.getPrice(),
                    product.getStock()
            };
            this.mdl_product_table.addRow(rowObject);
        }

        this.tbl_product.setModel(mdl_product_table);
        this.tbl_product.getTableHeader().setReorderingAllowed(false);
        this.tbl_product.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_product.setEnabled(false);


    }

    private void loadProductPopupMenu(){
        this.tbl_product.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_product.rowAtPoint(e.getPoint());
                tbl_product.setRowSelectionInterval(selectedRow,selectedRow);
            }
        });

        this.popup_product.add("Update").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_product.getValueAt(tbl_product.getSelectedRow(),0).toString());
            ProductUI productUI = new ProductUI(this.productController.getById(selectId));
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                    loadBasketTable();
                }
            });
        });
        this.popup_product.add("Delete").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_product.getValueAt(tbl_product.getSelectedRow(),0).toString());
            if(Helper.confirm("sure")){
                if (this.productController.delete(selectId)){
                    Helper.showMessage("done");
                    loadProductTable(null);
                    loadBasketTable();
                }else{
                    Helper.showMessage("error");
                }
            }
        });

        this.popup_product.add("Add to basket").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_product.getValueAt(tbl_product.getSelectedRow(),0).toString());
            Product basketProduct = this.productController.getById(selectId);
            if (basketProduct.getStock() <= 0){
                Helper.showMessage("This product is out of stock!");
            }else{
                Basket basket = new Basket(basketProduct.getId());
                if (this.basketController.save(basket)){
                    Helper.showMessage("done");
                    loadBasketTable();
                }else{
                    Helper.showMessage("error");
                }
            }
        });

        this.tbl_product.setComponentPopupMenu(this.popup_product);

    }

    private void loadBasketTable(){
        Object[] columnBasket = {"ID","Name","Code","Price","Stock"};
        ArrayList<Basket> baskets = this.basketController.findAll();

        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_basket.getModel();
        clearModel.setRowCount(0);

        this.mdl_basket_table.setColumnIdentifiers(columnBasket);

        float totalPrice = 0;

        for(Basket basket : baskets){
            Object[] rowObject = {
                    basket.getId(),
                    basket.getProduct().getName(),
                    basket.getProduct().getCode(),
                    basket.getProduct().getPrice(),
                    basket.getProduct().getStock()
            };
            this.mdl_basket_table.addRow(rowObject);
            totalPrice += basket.getProduct().getPrice();
        }

        this.lbl_basket_price.setText(totalPrice+ " TL");
        this.lbl_basket_count.setText(baskets.size() + " Piece");

        this.tbl_basket.setModel(mdl_basket_table);
        this.tbl_basket.getTableHeader().setReorderingAllowed(false);
        this.tbl_basket.getColumnModel().getColumn(0).setMaxWidth(50);

    }

    private void loadBasketButtonEvent(){
        this.btn_basket_reset.addActionListener(e -> {
            if(this.basketController.clear()){
                Helper.showMessage("done");
                loadBasketTable();
            }else{
                Helper.showMessage("error");
            }
        });

        this.btn_basket_new.addActionListener(e -> {
            Item selectedCustomer = (Item) this.cmb_basket_customer.getSelectedItem();
            if(selectedCustomer == null){
                Helper.showMessage("Please select a customer!");
            }else{
                Customer customer = this.customerController.getById(selectedCustomer.getKey());
                ArrayList<Basket> baskets = this.basketController.findAll();
                if (customer.getId() == 0){
                    Helper.showMessage("No such customer was found!");
                }else if (baskets.size() == 0){
                    Helper.showMessage("Please add products to your cart!");
                }else{
                    CartUI cartUI = new CartUI(customer);
                    cartUI.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            loadBasketTable();
                            loadProductTable(null);
                            loadCartTable();
                        }
                    });
                }
            }
        });

    }

    private void loadBasketCustomerCombo(){
        ArrayList<Customer> customers = this.customerController.findAll();
        this.cmb_basket_customer.removeAllItems();
        for(Customer customer : customers){
            int comboKey = customer.getId();
            String comboValue = customer.getName();
            this.cmb_basket_customer.addItem(new Item(comboKey,comboValue));
        }

        this.cmb_basket_customer.setSelectedItem(null);
    }

    private void loadCartTable(){
        Object[] columnCart = {"ID","Customer Name","Product Name","Price","Order Date","Order Note"};
        ArrayList<Cart> carts = this.cartController.findAll();

        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_cart.getModel();
        clearModel.setRowCount(0);

        this.mdl_cart_table.setColumnIdentifiers(columnCart);

        for(Cart cart : carts){
            Object[] rowObject = {
                    cart.getId(),
                    cart.getCustomer().getName(),
                    cart.getProduct().getName(),
                    cart.getPrice(),
                    cart.getDate(),
                    cart.getNote()
            };
            this.mdl_cart_table.addRow(rowObject);
        }

        this.tbl_cart.setModel(mdl_cart_table);
        this.tbl_cart.getTableHeader().setReorderingAllowed(false);
        this.tbl_cart.getColumnModel().getColumn(0).setMaxWidth(50);
    }


}

