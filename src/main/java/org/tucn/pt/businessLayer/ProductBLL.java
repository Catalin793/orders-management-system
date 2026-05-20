package org.tucn.pt.businessLayer;

import org.tucn.pt.dataAccessLayer.ProductDAO;
import org.tucn.pt.model.Product;
import java.util.List;

/**
 * Gestioneaza logica de business pentru produse, asigurand validarea datelor
 *
 */
public class ProductBLL {
    private ProductDAO productDAO = new ProductDAO();

    /**
     * Extrage lista tuturor produselor din baza de date.
     *
     * @return o lista de obiecte de tip Product
     */
    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    /**
     * Cauta un produs specific dupa identificatorul sau unic.
     *
     * @param id ID-ul produsului cautat
     * @return produsul gasit sau null daca nu exista
     */
    public Product findProductById(int id) {
        return productDAO.findById(id);
    }

    /**
     * Verifica integritatea datelor si insereaza un produs nou in sistem.
     *
     * @param product produsul care va fi inserat
     * @throws IllegalArgumentException daca datele produsului (nume, pret, stoc) sunt invalide
     */
    public void insertProduct(Product product) {
        if(product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Numele produsului nu poate fi gol!");
        }
        if(product.getPrice() <= 0) {
            throw new IllegalArgumentException("Pretul produsului trebuie sa fie strict pozitiv!");
        }
        if(product.getStock_quantity() < 0) {
            throw new IllegalArgumentException("Stocul nu poate fi o valoare negativa!");
        }

        productDAO.insert(product);
    }

    /**
     * Suprascrie datele unui produs existent.
     *
     * @param product obiectul cu noile atribute si ID-ul produsului original
     */
    public void updateProduct(Product product) {
        productDAO.update(product);
    }

    /**
     * Elimina produsul cu ID-ul specificat din baza de date.
     *
     * @param id ID-ul produsului vizat pentru stergere
     */
    public void deleteProduct(int id) {
        productDAO.delete(id);
    }
}