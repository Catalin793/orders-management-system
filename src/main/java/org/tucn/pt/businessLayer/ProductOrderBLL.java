package org.tucn.pt.businessLayer;

import org.tucn.pt.dataAccessLayer.ConnectionFactory;
import org.tucn.pt.dataAccessLayer.ProductOrderDAO;
import org.tucn.pt.model.Bill;
import org.tucn.pt.model.Client;
import org.tucn.pt.model.Product;
import org.tucn.pt.model.ProductOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Gestioneaza logica de plasare a unei comenzi, actualizarea stocurilor si generarea facturilor (Bill).
 */
public class ProductOrderBLL {
    private ProductOrderDAO orderDAO = new ProductOrderDAO();
    private ProductBLL productBLL = new ProductBLL();
    private ClientBLL clientBLL = new ClientBLL();

    /**
     * Valideaza disponibilitatea stocului, creeaza o comanda noua, actualizeaza stocul
     * produsului si genereaza factura aferenta.
     *
     * @param clientId ID-ul clientului care plaseaza comanda
     * @param productId ID-ul produsului comandat
     * @param quantity numarul de unitati comandate
     * @param clientName numele clientului (folosit pentru facturare)
     * @param productName numele produsului (folosit pentru facturare)
     * @throws Exception daca produsul nu exista in baza de date sau stocul este insuficient
     */
    /**
     * Valideaza disponibilitatea stocului, creeaza o comanda noua, actualizeaza stocul
     * produsului si genereaza factura aferenta cu numele reale.
     */
    public void createOrder(int clientId, int productId, int quantity) throws Exception {
        Product product = productBLL.findProductById(productId);
        Client client = clientBLL.findClientById(clientId);


        if (product == null) {
            throw new Exception("Produsul selectat nu exista!");
        }
        if (client == null) {
            throw new Exception("Clientul selectat nu exista!");
        }
        if (product.getStock_quantity() < quantity) {
            throw new Exception("Under-stock! Nu exista suficiente produse in stoc.");
        }

        product.setStock_quantity(product.getStock_quantity() - quantity);
        productBLL.updateProduct(product);

        ProductOrder order = new ProductOrder(clientId, productId, quantity);
        orderDAO.insert(order);

        double totalPrice = product.getPrice() * quantity;

        Bill bill = new Bill(0, client.getName(), product.getName(), quantity, totalPrice);
        insertLog(bill);
    }

    /**
     * Salveaza datele facturii in tabela Log.
     *
     * @param bill obiectul imuabil (record) continand detaliile facturii
     */
    private void insertLog(Bill bill) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "INSERT INTO Log (order_id, client_name, product_name, ordered_quantity, total_price) VALUES (?, ?, ?, ?, ?)";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, bill.orderId());
            statement.setString(2, bill.clientName());
            statement.setString(3, bill.productName());
            statement.setInt(4, bill.orderedQuantity());
            statement.setDouble(5, bill.totalPrice());

            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Eroare la salvarea facturii in tabela Log: " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}