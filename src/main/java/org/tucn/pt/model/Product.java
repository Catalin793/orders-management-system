package org.tucn.pt.model;


public class Product {
    private int id;
    private String name;
    private double price;
    private int stock_quantity;

    public Product() {}

    /**
     * Initializez un produs cu toate detaliile, inclusiv ID-ul.
     *
     * @param id identificatorul unic al produsului
     * @param name denumirea produsului
     * @param price pretul per unitate
     * @param stock_quantity cantitatea curenta disponibila pe stoc
     */
    public Product(int id, String name, double price, int stock_quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock_quantity = stock_quantity;
    }

    /**
     * Initializez un produs fara ID (l-am folosit inaintea inserarii in baza de date).
     *
     * @param name denumirea produsului
     * @param price pretul per unitate
     * @param stock_quantity cantitatea curenta disponibila pe stoc
     */
    public Product(String name, double price, int stock_quantity) {
        this.name = name;
        this.price = price;
        this.stock_quantity = stock_quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock_quantity() { return stock_quantity; }
    public void setStock_quantity(int stock_quantity) { this.stock_quantity = stock_quantity; }
}