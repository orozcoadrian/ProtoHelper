package com.pwf.ui.protohelper;

import com.pwf.plugin.network.client.DefaultNetworkClientSettings;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author mfullen
 */
public class PropertyTable extends JTable
{
    /**
     * This main method allows the class to be demonstrated standalone
     */
    public static void main(String[] args)
    {
        // Specify the name of the class as a command-line argument
        Class beanClass = null;
        try
        {
            // Use reflection to get the Class from the classname
            beanClass = Class.forName("javax.swing.JLabel");
            DefaultNetworkClientSettings settings = new DefaultNetworkClientSettings();
            settings.setIpAddress("localhost");
            settings.setPort(5011);
            settings.setSSL(true);
           
            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(settings.getClass());

            beanClass = settings.getClass();
        }
        catch (Exception e)
        { // Report errors
            System.out.println("Can't find specified class: " + e.getMessage());
            System.out.println("Usage: java TableDemo <JavaBean class name>");
            System.exit(0);
        }

        // Create a table to display the properties of the specified class
        JTable table = new PropertyTable(beanClass);

        // Then put the table in a scrolling window, put the scrolling
        // window into a frame, and pop it all up on to the screen
        JScrollPane scrollpane = new JScrollPane(table);
        JFrame frame = new JFrame("Properties of JavaBean: ");
        frame.getContentPane().add(scrollpane);
        frame.setSize(500, 400);
        frame.setVisible(true);
    }

    /**
     * This constructor method specifies what data the table will display (the
     * table model) and uses the TableColumnModel to customize the way that the
     * table displays it. The hard work is done by the TableModel implementation
     * below.
     */
    public PropertyTable(Class beanClass)
    {
        // Set the data model for this table
        try
        {
            setModel(new JavaBeanPropertyTableModel(beanClass));
        }
        catch (IntrospectionException e)
        {
            System.err.println("WARNING: can't introspect: " + beanClass);
        }

        // Tweak the appearance of the table by manipulating its column model
        TableColumnModel colmodel = getColumnModel();

        // Set column widths
        colmodel.getColumn(0).setPreferredWidth(125);
        colmodel.getColumn(1).setPreferredWidth(200);
        colmodel.getColumn(2).setPreferredWidth(75);
        colmodel.getColumn(3).setPreferredWidth(50);

        // Right justify the text in the first column
        TableColumn namecol = colmodel.getColumn(0);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        namecol.setCellRenderer(renderer);
    }
}