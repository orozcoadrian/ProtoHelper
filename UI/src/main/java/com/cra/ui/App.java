package com.cra.ui;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.TextFormat;
import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App
{
    private static final String CLASSPATH_STRING = "com.cra.messaging.";
    private static final String PROTOBUF_CLASSPATH_STRING = "com.google.protobuf.";
    private static final Map<FieldDescriptor.JavaType, Class> classMap = new EnumMap<FieldDescriptor.JavaType, Class>(FieldDescriptor.JavaType.class);
    private static final Map<Class, Object> defaultObjectMap = new HashMap<Class, Object>();

    static
    {
        classMap.put(FieldDescriptor.JavaType.INT, Integer.class);
        classMap.put(FieldDescriptor.JavaType.STRING, String.class);
        classMap.put(FieldDescriptor.JavaType.DOUBLE, Double.class);
        classMap.put(FieldDescriptor.JavaType.FLOAT, Float.class);
        classMap.put(FieldDescriptor.JavaType.BOOLEAN, Boolean.class);
        classMap.put(FieldDescriptor.JavaType.BYTE_STRING, Byte.class);
        classMap.put(FieldDescriptor.JavaType.ENUM, Enum.class);
        classMap.put(FieldDescriptor.JavaType.LONG, Long.class);
        classMap.put(FieldDescriptor.JavaType.MESSAGE, Message.class);


        defaultObjectMap.put(Integer.class, 0);
        defaultObjectMap.put(String.class, "");
        defaultObjectMap.put(Double.class, 0.0);
        defaultObjectMap.put(Float.class, 0.0);
        defaultObjectMap.put(Boolean.class, false);
        defaultObjectMap.put(Byte.class, 0);
        defaultObjectMap.put(Long.class, 0);


    }

    public static Message.Builder constructBuilder(Message.Builder newInstance)
    {
        for (FieldDescriptor fieldDescriptor : newInstance.getDescriptorForType().getFields())
        {

            // newInstance.setField(fieldDescriptor, fieldDescriptor.);
            if (fieldDescriptor.isRequired())
            {
                if (!fieldDescriptor.isRepeated())
                {
                    Class convertFromJavaType = convertFromJavaType(fieldDescriptor.getJavaType());

                    boolean hasDefaultValue = newInstance.getField(fieldDescriptor) != "";
                    // System.out.println("NewinstanceValue:" + newInstance.getField(fieldDescriptor) + hasDefaultValue);
                    Object o = hasDefaultValue ? newInstance.getField(fieldDescriptor) : getDefaultValue(convertFromJavaType);

                    if (convertFromJavaType.equals(Message.class))
                    {
                        // System.out.println("&&&&&&&&&&&&&&&&&Message TYPE");
                        Builder newBuilderForField = newInstance.newBuilderForField(fieldDescriptor);
                        o = constructBuilder(newBuilderForField).build();
                    }
                    if (fieldDescriptor.getJavaType().equals(FieldDescriptor.JavaType.ENUM))
                    {
                        // System.out.println("&&&&&&&&&&&&&&&&&ENUM TYPE");
                        EnumDescriptor enumType = fieldDescriptor.getEnumType();
                        o = enumType.getValues().get(0);
                    }

                    newInstance.setField(fieldDescriptor, o);
                    // System.out.println("NewinstanceValue CHANGED:" + newInstance.getField(fieldDescriptor));
                }
                else
                {
                    System.out.println("Repeated and Requied: " + fieldDescriptor.getFullName());
                }
            }
            System.out.println("Value: " + newInstance.getField(fieldDescriptor));
            System.out.println(getDescription(fieldDescriptor));
        }
        return newInstance;
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
                                                  NoSuchMethodException, IllegalArgumentException,
                                                  InvocationTargetException
    {

        long start = System.currentTimeMillis();
        ClassFinder cf = new ClassFinder(Message.class);
        Set<String> classes = cf.getClasses();
        Set<String> filteredClasses = new HashSet<String>();
        for (String s : classes)
        {
            if (!s.contains(PROTOBUF_CLASSPATH_STRING))
            {
                filteredClasses.add(s);
            }
        }
        List<String> sorted = new ArrayList<String>(filteredClasses);
        Collections.sort(sorted);

        List<Builder> builders = new ArrayList<Builder>();

        int i = 1;
        for (String string : sorted)
        {
            //  System.out.println(i + ":" + string);
            i++;
            Class<Message> forName = null;
            Message.Builder newInstance = null;
            try
            {
                forName = (Class<Message>) Class.forName(string);
                newInstance = (Message.Builder) forName.getMethod("newBuilder").invoke(forName, new Object[]
                        {
                        });
                Builder constructBuilder = constructBuilder(newInstance);
                builders.add(constructBuilder);
            }
            catch (Exception e)
            {
            }

            // System.out.println(TextFormat.shortDebugString(constructBuilder.build()));
        }

        DefaultListModel listModel = new DefaultListModel();
        for (Builder builder : builders)
        {
            listModel.addElement(builder);
        }

        long finish = System.currentTimeMillis();

        System.out.println("Took this many MS: " + (finish - start));
        JList list = new JList(listModel);

        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JFrame frame = new JFrame("Protos");
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

    }

    public static String getDescription(FieldDescriptor fieldDescriptor)
    {
        StringBuilder sb = new StringBuilder();
        final String newline = "\r\n";
        sb.append("Name:");
        sb.append(fieldDescriptor.getName());
        sb.append(newline);

        sb.append("Type:");
        sb.append(fieldDescriptor.getType());
        sb.append(newline);

        sb.append("JavaType:");
        sb.append(fieldDescriptor.getJavaType());
        sb.append(newline);

        Class convertFromJavaType = convertFromJavaType(fieldDescriptor.getJavaType());

        sb.append("Converted from JavaType:");
        sb.append(convertFromJavaType);
        sb.append(newline);

        if (!convertFromJavaType.isEnum())
        {
            // Object cast = convertFromJavaType.cast(fieldDescriptor.getDefaultValue());
        }

        sb.append("LiteJavaType:");
        sb.append(fieldDescriptor.getLiteJavaType());
        sb.append(newline);

        sb.append("File:");
        sb.append(fieldDescriptor.getFile().getPackage());
        sb.append(".");
        sb.append(fieldDescriptor.getFile().getName());
        sb.append(newline);

        try
        {
            EnumDescriptor enumType = fieldDescriptor.getEnumType();
            sb.append("EnumType:");
            sb.append(enumType.getFullName());
            sb.append(newline);

            for (Descriptors.EnumValueDescriptor enumValueDescriptor : enumType.getValues())
            {
                sb.append(enumValueDescriptor.getFullName());
                sb.append(newline);
            }

//            Class<?> c = Class.forName("com.cra.messaging.DefsProto$Defs$" + enumType.getName());
//
//            List<Object> enums = Arrays.asList(c.getEnumConstants());
//            System.out.println("Enums:" + enums);
        }
        catch (Exception e)
        {
            // e.printStackTrace();
        }

        // System.out.println("Object: " + getObject(convertFromJavaType, fieldDescriptor));


        sb.append("IsExtension:");
        sb.append(fieldDescriptor.isExtension());
        sb.append(newline);

        sb.append("Isoptional:");
        sb.append(fieldDescriptor.isOptional());
        sb.append(newline);

        sb.append("IsRepeated:");
        sb.append(fieldDescriptor.isRepeated());
        sb.append(newline);

        sb.append("IsRequired:");
        sb.append(fieldDescriptor.isRequired());
        sb.append(newline);

        return sb.toString();
    }

    public static Class convertFromJavaType(FieldDescriptor.JavaType javaType)
    {
        return classMap.get(javaType);
    }

    public static Object getDefaultValue(Class classz)
    {
        return defaultObjectMap.get(classz);
    }
}
