package org.tucn.pt.presentation;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Am creat aceasta clasa pentru generarea dinamica a tabelelor pentru interfata grafica.
 */
public class TableBuilder {

    /**
     * Construiesc un model de tabel (DefaultTableModel) dintr-o lista de obiecte generice.
     * Am folosit Java Reflection si Streams API pentru a extrage automat numele campurilor si valorile acestora.
     *
     * @param objects lista de obiecte care vor popula randurile tabelului
     * @param <T> tipul generic al obiectelor (ex. Client, Product)
     * @return un DefaultTableModel completat cu date, sau un model gol daca lista este vida
     */
    public static <T> DefaultTableModel buildTable(List<T> objects) {
        if (objects == null || objects.isEmpty()) {
            return new DefaultTableModel();
        }

        Field[] fields = objects.get(0).getClass().getDeclaredFields();

        List<String> columnNamesList = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            columnNamesList.add(field.getName());
        }

        List<Object[]> rowDataList = objects.stream().map(object -> {
            Object[] row = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                try {
                    fields[i].setAccessible(true);
                    row[i] = fields[i].get(object);
                } catch (IllegalAccessException e) {
                    System.out.println("Eroare la extragerea datelor pentru tabel: " + e.getMessage());
                    row[i] = "N/A";
                }
            }
            return row;
        }).collect(Collectors.toList());

        Object[][] data = rowDataList.toArray(new Object[0][0]);
        Object[] columns = columnNamesList.toArray();

        return new DefaultTableModel(data, columns);
    }
}