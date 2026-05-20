package org.tucn.pt.model;


public class Client {
    private int id;
    private String name;
    private String address;
    private String email;

    public Client() {}

    /**
     * Initializez un client nou cu toate detaliile, inclusiv ID-ul (l-am folosit pentru extragerea din DB).
     *
     * @param id identificatorul unic al clientului
     * @param name numele complet al clientului
     * @param address adresa fizica a clientului
     * @param email adresa de contact a clientului
     */
    public Client(int id, String name, String address, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    /**
     * Initializez un client nou fara ID (l-am folosit inaintea inserarii in DB).
     *
     * @param name numele complet al clientului
     * @param address adresa fizica a clientului
     * @param email adresa de contact a clientului
     */
    public Client(String name, String address, String email) {
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}