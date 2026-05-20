package org.tucn.pt.businessLayer;

import org.tucn.pt.dataAccessLayer.ClientDAO;
import org.tucn.pt.model.Client;
import java.util.List;

/**
 * Gestioneaza logica de business pentru clienti.
 */
public class ClientBLL {
    private ClientDAO clientDAO = new ClientDAO();

    /**
     * Returneaza o lista cu toti clientii inregistrati in baza de date.
     *
     * @return o lista de obiecte Client
     */
    public List<Client> findAllClients() {
        return clientDAO.findAll();
    }

    /**
     * Valideaza datele clientului si, daca sunt corecte, il insereaza in baza de date.
     *
     * @param client obiectul Client ce urmeaza a fi salvat
     * @throws IllegalArgumentException daca numele este gol sau email-ul are un format invalid
     */
    public void insertClient(Client client) {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Numele clientului nu poate fi gol!");
        }
        if (client.getEmail() == null || !client.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email-ul introdus nu este valid!");
        }
        clientDAO.insert(client);
    }

    /**
     * Actualizeaza informatiile unui client existent in baza de date.
     *
     * @param client obiectul Client continand datele noi si ID-ul existent
     */
    public void updateClient(Client client) {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Numele clientului nu poate fi gol!");
        }
        if (client.getEmail() == null || !client.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email-ul introdus nu este valid!");
        }
        clientDAO.update(client);
    }

    /**
     * Sterge un client din baza de date pe baza identificatorului sau.
     *
     * @param id identificatorul unic al clientului
     */
    public void deleteClient(int id) {
        clientDAO.delete(id);
    }

    /**
     * Cauta un client in baza de date dupa ID.
     *
     * @param id identificatorul clientului
     * @return clientul gasit sau null
     */
    public Client findClientById(int id) {
        return clientDAO.findById(id);
    }
}