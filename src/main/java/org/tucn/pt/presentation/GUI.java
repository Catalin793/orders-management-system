package org.tucn.pt.presentation;

import org.tucn.pt.businessLayer.ClientBLL;
import org.tucn.pt.businessLayer.ProductBLL;
import org.tucn.pt.businessLayer.ProductOrderBLL;
import org.tucn.pt.model.Client;
import org.tucn.pt.model.Product;


import javax.swing.*;
import java.awt.*;


public class GUI extends JFrame {
    private final ClientBLL clientBLL = new ClientBLL();
    private final ProductBLL productBLL = new ProductBLL();
    private final ProductOrderBLL orderBLL = new ProductOrderBLL();

    private final JTable clientTable = new JTable();
    private final JTable productTable = new JTable();

    private final JLabel clientIdLabel = new JLabel("ID:");
    private final JTextField clientIdInput = new JTextField();
    private final JLabel clientNameLabel = new JLabel("Nume:");
    private final JTextField clientNameInput = new JTextField();
    private final JLabel clientAddressLabel = new JLabel("Adresa:");
    private final JTextField clientAddressInput = new JTextField();
    private final JLabel clientEmailLabel = new JLabel("Email:");
    private final JTextField clientEmailInput = new JTextField();
    private final JButton addClientBtn = new JButton("Adauga");
    private final JButton editClientBtn = new JButton("Editeaza");
    private final JButton delClientBtn = new JButton("Sterge");

    private final JLabel productIdLabel = new JLabel("ID:");
    private final JTextField productIdInput = new JTextField();
    private final JLabel productNameLabel = new JLabel("Nume:");
    private final JTextField productNameInput = new JTextField();
    private final JLabel productPriceLabel = new JLabel("Pret:");
    private final JTextField productPriceInput = new JTextField();
    private final JLabel productStockLabel = new JLabel("Stock:");
    private final JTextField productStockInput = new JTextField();
    private final JButton addProductBtn = new JButton("Adauga");
    private final JButton editProductBtn = new JButton("Editeaza");
    private final JButton delProductBtn = new JButton("Sterge");

    private final JLabel orderClientIdLabel = new JLabel("Client ID:");
    private final JTextField orderClientIdInput = new JTextField();
    private final JLabel orderProductIdLabel = new JLabel("Product ID:");
    private final JTextField orderProductIdInput = new JTextField();
    private final JLabel orderQuantityLabel = new JLabel("Cantitate:");
    private final JTextField orderQuantityInput = new JTextField();
    private final JButton placeOrderBtn = new JButton("Plaseaza comanda");

    /**
     * Initializez fereastra principala a aplicatiei si asambleaza componentele grafice.
     */
    public GUI() {
        prepareGUI();
    }

    /**
     * Adaug setarile de baza ale ferestrei si sistemul de tab-uri.
     */
    private void prepareGUI() {
        this.setSize(800, 600);
        this.setTitle("Order Management System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Clienti", prepareClientPanel());
        tabs.addTab("Produse", prepareProductPanel());
        tabs.addTab("Comenzi", prepareOrderPanel());

        this.add(tabs);
        refreshTables();
        this.setVisible(true);
    }

    /**
     * Creez panoul dedicat operatiunilor pentru clienti.
     *
     * @return un JPanel continand tabelul si formularele pentru clienti
     */
    private JPanel prepareClientPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(clientTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(clientIdLabel); inputPanel.add(clientIdInput);
        inputPanel.add(clientNameLabel); inputPanel.add(clientNameInput);
        inputPanel.add(clientAddressLabel); inputPanel.add(clientAddressInput);
        inputPanel.add(clientEmailLabel); inputPanel.add(clientEmailInput);

        JPanel btnPanel = new JPanel();
        btnPanel.add(addClientBtn);
        btnPanel.add(editClientBtn);
        btnPanel.add(delClientBtn);

        addClientBtn.addActionListener(e -> addClientAction());
        editClientBtn.addActionListener(e -> editClientAction());
        delClientBtn.addActionListener(e -> delClientAction());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Construiesc panoul dedicat operatiunilor pentru produse.
     *
     * @return un JPanel continand tabelul si formularele pentru produse
     */
    private JPanel prepareProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(productIdLabel); inputPanel.add(productIdInput);
        inputPanel.add(productNameLabel); inputPanel.add(productNameInput);
        inputPanel.add(productPriceLabel); inputPanel.add(productPriceInput);
        inputPanel.add(productStockLabel); inputPanel.add(productStockInput);

        JPanel btnPanel = new JPanel();
        btnPanel.add(addProductBtn);
        btnPanel.add(editProductBtn);
        btnPanel.add(delProductBtn);

        addProductBtn.addActionListener(e -> addProductAction());
        editProductBtn.addActionListener(e -> editProductAction());
        delProductBtn.addActionListener(e -> delProductAction());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Construiesc panoul dedicat plasarii comenzilor.
     *
     * @return un JPanel continand formularul pentru generarea unei comenzi
     */
    private JPanel prepareOrderPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(orderClientIdLabel); panel.add(orderClientIdInput);
        panel.add(orderProductIdLabel); panel.add(orderProductIdInput);
        panel.add(orderQuantityLabel); panel.add(orderQuantityInput);

        panel.add(new JLabel(""));
        panel.add(placeOrderBtn);

        placeOrderBtn.addActionListener(e -> placeOrderAction());

        return panel;
    }

    /**
     * Preiau datele din formular, apelez logica de business si inserez un nou client in baza de date.
     */
    private void addClientAction() {
        try {
            Client c = new Client(clientNameInput.getText(), clientAddressInput.getText(), clientEmailInput.getText());
            clientBLL.insertClient(c);
            refreshTables();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Eroare la adaugare client: " + ex.getMessage());
        }
    }

    /**
     * Preiau ID-ul si datele actualizate din formular pentru a edita un client existent.
     */
    private void editClientAction() {
        try {
            int id = Integer.parseInt(clientIdInput.getText());
            Client c = new Client(id, clientNameInput.getText(), clientAddressInput.getText(), clientEmailInput.getText());
            clientBLL.updateClient(c);
            refreshTables();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Introdu un ID valid pentru editare!");
        }
    }

    /**
     * Preiau ID-ul din formular si sterg clientul corespunzator din baza de date.
     */
    private void delClientAction() {
        try {
            int id = Integer.parseInt(clientIdInput.getText());
            clientBLL.deleteClient(id);
            refreshTables();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Introdu un ID valid pentru stergere!");
        }
    }

    /**
     * Preiau datele din formular, le validez ca fiind numerice unde este cazul si adaug un produs nou.
     */
    private void addProductAction() {
        try {
            Product p = new Product(productNameInput.getText(), Double.parseDouble(productPriceInput.getText()), Integer.parseInt(productStockInput.getText()));
            productBLL.insertProduct(p);
            refreshTables();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pretul sau stocul trebuie sa fie numere!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Eroare la adaugare produs: " + ex.getMessage());
        }
    }

    /**
     * Preiau ID-ul si datele actualizate pentru a edita un produs existent.
     */
    private void editProductAction() {
        try {
            int id = Integer.parseInt(productIdInput.getText());
            Product p = new Product(id, productNameInput.getText(), Double.parseDouble(productPriceInput.getText()), Integer.parseInt(productStockInput.getText()));
            productBLL.updateProduct(p);
            refreshTables();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pretul si stocul trebuie sa fie numere!");
        }
    }

    /**
     * Preiau ID-ul din formular si sterg produsul corespunzator din baza de date.
     */
    private void delProductAction() {
        try {
            int id = Integer.parseInt(productIdInput.getText());
            productBLL.deleteProduct(id);
            refreshTables();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Introdu un ID valid pentru stergere!");
        }
    }

    /**
     * Procesez plasarea unei comenzi luand in calcul clientul, produsul si cantitatea dorita.
     */
    private void placeOrderAction() {
        try {
            int clientId = Integer.parseInt(orderClientIdInput.getText());
            int productId = Integer.parseInt(orderProductIdInput.getText());
            int qty = Integer.parseInt(orderQuantityInput.getText());

            orderBLL.createOrder(clientId, productId, qty);

            JOptionPane.showMessageDialog(this, "Comanda plasata cu succes! Factura salvata in Log.");
            refreshTables();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Toate campurile trebuie sa fie numere intregi!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Eroare: " + ex.getMessage());
        }
    }

    /**
     * Reincarc datele din baza de date in modelele tabelelor grafice.
     */
    private void refreshTables() {
        clientTable.setModel(TableBuilder.buildTable(clientBLL.findAllClients()));
        productTable.setModel(TableBuilder.buildTable(productBLL.findAllProducts()));
    }
}