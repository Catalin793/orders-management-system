package org.tucn.pt.dataAccessLayer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clasa pentru operatiunile CRUD la nivelul bazei de date.
 * Formeaza interogari SQL dinamice bazate pe numele campurilor declarate in clasa modelului T.
 * * @param <T> Tipul entitatii de date
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    /**
     * Constructor care utilizeaza reflexia pentru a determina tipul generic la rulare
     * .
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Extrage toate inregistrarile din tabela corespunzatoare clasei T.
     *
     * @return o lista continand toate obiectele de tip T din baza de date
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return new ArrayList<>();
    }

    /**
     * Returneaza un obiect specificat prin ID-ul sau cautand in baza de date.
     *
     * @param id identificatorul inregistrarii cautate
     * @return obiectul gasit, sau null daca ID-ul nu exista
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName() + " WHERE id = ?";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            List<T> objects = createObjects(resultSet);
            if (!objects.isEmpty()) {
                return objects.get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Construieste INSERT pentru obiectul furnizat, sarind peste ID.
     *
     * @param t obiectul care va fi inserat in baza de date
     */
    public void insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();

            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            Field[] fields = type.getDeclaredFields();

            for (Field field : fields) {
                if (!field.getName().equals("id")) {
                    columns.append(field.getName()).append(",");
                    values.append("?,");
                }
            }

            String colsStr = columns.substring(0, columns.length() - 1);
            String valsStr = values.substring(0, values.length() - 1);
            String query = "INSERT INTO " + type.getSimpleName() + " (" + colsStr + ") VALUES (" + valsStr + ")";

            statement = connection.prepareStatement(query);

            int index = 1;
            for (Field field : fields) {
                if (!field.getName().equals("id")) {
                    field.setAccessible(true);
                    statement.setObject(index, field.get(t));
                    index++;
                }
            }
            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Construieste UPDATE pentru a actualiza o inregistrare dupa ID.
     *
     * @param t obiectul continand atributele de updatat
     */
    public void update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();

            StringBuilder setClause = new StringBuilder();
            Field[] fields = type.getDeclaredFields();
            Object idValue = null;

            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getName().equals("id")) {
                    setClause.append(field.getName()).append("=?,");
                } else {
                    idValue = field.get(t);
                }
            }

            String setStr = setClause.substring(0, setClause.length() - 1);
            String query = "UPDATE " + type.getSimpleName() + " SET " + setStr + " WHERE id=?";

            statement = connection.prepareStatement(query);

            int index = 1;
            for (Field field : fields) {
                if (!field.getName().equals("id")) {
                    field.setAccessible(true);
                    statement.setObject(index, field.get(t));
                    index++;
                }
            }
            statement.setObject(index, idValue);
            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Sterge din baza de date o inregistrare in functie de ID.
     *
     * @param id identificatorul unic al inregistrarii vizate
     */
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "DELETE FROM " + type.getSimpleName() + " WHERE id = ?";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Metoda care mapeaza rezultatele unui query SQL in lista de obiecte Java corespunzatoare,
     *
     * @param resultSet datele brute returnate din baza de date
     * @return o lista mapata corespunzator clasei de tip T
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                T inst = type.getDeclaredConstructor().newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();

                    if (value != null && value instanceof Number) {
                        Class<?> parameterType = method.getParameterTypes()[0];
                        if (parameterType == int.class || parameterType == Integer.class) {
                            value = ((Number) value).intValue();
                        } else if (parameterType == double.class || parameterType == Double.class) {
                            value = ((Number) value).doubleValue();
                        }
                    }

                    method.invoke(inst, value);
                }
                list.add(inst);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:createObjects " + e.getMessage());
        }
        return list;
    }
}