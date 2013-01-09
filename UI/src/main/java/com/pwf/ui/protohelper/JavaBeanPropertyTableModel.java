package com.pwf.ui.protohelper;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author mfullen
 */
/**
 * This class implements TableModel and represents JavaBeans property data in a
 * way that the JTable component can display. If you've got some type of tabular
 * data to display, implement a TableModel class to describe that data, and the
 * JTable component will be able to display it.
 */
class JavaBeanPropertyTableModel extends AbstractTableModel
{
    PropertyDescriptor[] properties; // The properties to display

    /**
     * The constructor: use the JavaBeans introspector mechanism to get
     * information about all the properties of a bean. Once we've got this
     * information, the other methods will interpret it for JTable.
     */
    public JavaBeanPropertyTableModel(Class beanClass) throws
            java.beans.IntrospectionException
    {
        // Use the introspector class to get "bean info" about the class.
        BeanInfo beaninfo = Introspector.getBeanInfo(beanClass);

        // Get the property descriptors from that BeanInfo class
        properties = beaninfo.getPropertyDescriptors();

//        for (PropertyDescriptor propertyDescriptor : properties)
//        {
//            propertyDescriptor.createPropertyEditor(beaninfo).
//        }
        // Now do a case-insensitive sort by property name
        // The anonymous Comparator implementation specifies how to
        // sort PropertyDescriptor objects by name
        Arrays.sort(properties, new Comparator()
        {
            @Override
            public int compare(Object p, Object q)
            {
                PropertyDescriptor a = (PropertyDescriptor) p;
                PropertyDescriptor b = (PropertyDescriptor) q;
                return a.getName().compareToIgnoreCase(b.getName());
            }

            @Override
            public boolean equals(Object o)
            {
                return o == this;
            }
        });
    }
    // These are the names of the columns represented by this TableModel
    static final String[] columnNames = new String[]
    {
        "Name", "Type", "Access",
        "Bound", "Value"
    };
    // These are the types of the columns represented by this TableModel
    static final Class[] columnTypes = new Class[]
    {
        String.class, Class.class,
        String.class, Boolean.class, Object.class
    };

    // These simple methods return basic information about the table
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    @Override
    public int getRowCount()
    {
        return properties.length;
    }

    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }

    @Override
    public Class getColumnClass(int column)
    {
        return columnTypes[column];
    }

    /**
     * This method returns the value that appears at the specified row and
     * column of the table
     */
    @Override
    public Object getValueAt(int row, int column)
    {
        PropertyDescriptor prop = properties[row];
        switch (column)
        {
            case 0:
                return prop.getName();
            case 1:
                return prop.getPropertyType();
            case 2:
                return getAccessType(prop);
            case 3:
                return prop.isBound();
            case 4:
                return prop.getValue(prop.getShortDescription());
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        if (columnIndex == 3)
        {
            return true;
        }
        return false;
    }

    // A helper method called from getValueAt() above
    String getAccessType(PropertyDescriptor prop)
    {
        java.lang.reflect.Method reader = prop.getReadMethod();
        java.lang.reflect.Method writer = prop.getWriteMethod();
        if ((reader != null) && (writer != null))
        {
            return "Read/Write";
        }
        else if (reader != null)
        {
            return "Read-Only";
        }
        else if (writer != null)
        {
            return "Write-Only";
        }
        else
        {
            return "No Access"; // should never happen
        }
    }
}
