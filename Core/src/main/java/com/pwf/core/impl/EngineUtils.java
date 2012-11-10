package com.pwf.core.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.TextFormat;
import com.pwf.core.ProtoFilter;
import com.pwf.plugin.impl.PluginUtils;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfullen
 */
public final class EngineUtils
{
    private static final Logger logger = LoggerFactory.getLogger(EngineUtils.class);
    private static final Map<Descriptors.FieldDescriptor.JavaType, Class> classMap = new EnumMap<Descriptors.FieldDescriptor.JavaType, Class>(Descriptors.FieldDescriptor.JavaType.class);
    private static final Map<Class, Object> defaultObjectMap = new HashMap<Class, Object>();

    static
    {
        classMap.put(Descriptors.FieldDescriptor.JavaType.INT, Integer.class);
        classMap.put(Descriptors.FieldDescriptor.JavaType.STRING, String.class);
        classMap.put(Descriptors.FieldDescriptor.JavaType.DOUBLE, Double.class);
        classMap.put(Descriptors.FieldDescriptor.JavaType.FLOAT, Float.class);
        classMap.put(Descriptors.FieldDescriptor.JavaType.BOOLEAN, Boolean.class);
        classMap.put(Descriptors.FieldDescriptor.JavaType.BYTE_STRING, Byte.class);
        classMap.put(Descriptors.FieldDescriptor.JavaType.ENUM, Enum.class);
        classMap.put(Descriptors.FieldDescriptor.JavaType.LONG, Long.class);
        classMap.put(Descriptors.FieldDescriptor.JavaType.MESSAGE, Message.class);


        defaultObjectMap.put(Integer.class, 0);
        defaultObjectMap.put(String.class, "");
        defaultObjectMap.put(Double.class, 0.0);
        defaultObjectMap.put(Float.class, 0.0);
        defaultObjectMap.put(Boolean.class, false);
        defaultObjectMap.put(Byte.class, 0);
        defaultObjectMap.put(Long.class, 0);
    }

    public static Collection<Builder> filter(Set<Class<? extends Message>> loadedClasses, ProtoFilter filter)
    {
        Collection<Class<? extends Message>> filteredProtos = new ArrayList<Class<? extends Message>>();

        for (Class<? extends Message> c : loadedClasses)
        {
            if (!c.getName().contains(filter.getClassPackagePath()))
            {
                filteredProtos.add(c);
            }
        }

        return EngineUtils.getBuildersFromClasses(filteredProtos);
    }

    private EngineUtils()
    {
    }

    public static Message.Builder constructBuilder(Message.Builder newInstance)
    {
        for (Descriptors.FieldDescriptor fieldDescriptor : newInstance.getDescriptorForType().getFields())
        {

            // newInstance.setField(fieldDescriptor, fieldDescriptor.);
            if (fieldDescriptor.isRequired())
            {
                if (!fieldDescriptor.isRepeated())
                {
                    Class convertFromJavaType = convertFromJavaType(fieldDescriptor.getJavaType());

                    boolean hasDefaultValue = newInstance.getField(fieldDescriptor) != "";
                    // logger.trace("NewinstanceValue:" + newInstance.getField(fieldDescriptor) + hasDefaultValue);
                    Object o = hasDefaultValue ? newInstance.getField(fieldDescriptor) : getDefaultValue(convertFromJavaType);

                    if (convertFromJavaType.equals(Message.class))
                    {
                        // logger.trace("&&&&&&&&&&&&&&&&&Message TYPE");
                        Message.Builder newBuilderForField = newInstance.newBuilderForField(fieldDescriptor);
                        o = constructBuilder(newBuilderForField).build();
                    }
                    if (fieldDescriptor.getJavaType().equals(Descriptors.FieldDescriptor.JavaType.ENUM))
                    {
                        // logger.trace("&&&&&&&&&&&&&&&&&ENUM TYPE");
                        Descriptors.EnumDescriptor enumType = fieldDescriptor.getEnumType();
                        o = enumType.getValues().get(0);
                    }

                    newInstance.setField(fieldDescriptor, o);
                    // logger.trace("NewinstanceValue CHANGED:" + newInstance.getField(fieldDescriptor));
                }
                else
                {
                    logger.trace("Repeated and Requied: " + fieldDescriptor.getFullName());
                }
            }
            logger.trace("Value: " + newInstance.getField(fieldDescriptor));
            logger.trace(getDescription(fieldDescriptor));
        }
        return newInstance;
    }

    public static List<Message.Builder> getBuildersFromClasses(Collection<Class<? extends Message>> classes)
    {
        List<Message.Builder> builders = new ArrayList<Message.Builder>();

        for (Class<? extends Message> c : classes)
        {
            try
            {
                Class<? extends Message> forName = c;
                Method m = forName.getMethod("newBuilder", (Class<?>[]) null);
                if (m != null)
                {
                    Message.Builder newInstance = (Message.Builder) m.invoke(forName, new Object[]
                            {
                            });
                    Message.Builder constructBuilder = EngineUtils.constructBuilder(newInstance);
                    builders.add(constructBuilder);
                    logger.trace(TextFormat.shortDebugString(constructBuilder.build()));
                }
                else
                {
                    logger.trace("Method newBuilder not found for {}", forName.getName());
                }
            }
            catch (Exception e)
            {
                // logger.error("Error creating message for {}");
            }
        }
        return builders;
    }

    public static Map<URL, Set<Class<? extends Message>>> getMessageFilesonClasspath(String directory)
    {
        List<URL> jarFilesonClasspathUrl = PluginUtils.getJarFilesonClasspathUrl(directory);
        URL[] urlArray = jarFilesonClasspathUrl.toArray(new URL[0]);
        Map<URL, Set<Class<? extends Message>>> urlClassMessageMap = new LinkedHashMap<URL, Set<Class<? extends Message>>>();

        for (URL url : urlArray)
        {
            URL[] u = new URL[]
            {
                url
            };
            URLClassLoader urlClassLoader = new URLClassLoader(u);

            Reflections reflections = new Reflections(new ConfigurationBuilder().addClassLoader(urlClassLoader).setUrls(ClasspathHelper.forClassLoader(urlClassLoader)).setScanners(new SubTypesScanner(),
                    new TypeAnnotationsScanner(),
                    new ResourcesScanner()));
            Set<Class<? extends Message>> classes = reflections.getSubTypesOf(Message.class);
            urlClassMessageMap.put(url, classes);
        }
        return urlClassMessageMap;
    }

    public static String getDescription(Descriptors.FieldDescriptor fieldDescriptor)
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
            Descriptors.EnumDescriptor enumType = fieldDescriptor.getEnumType();
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
//            logger.trace("Enums:" + enums);
        }
        catch (Exception e)
        {
            // e.printStackTrace();
        }

        // logger.trace("Object: " + getObject(convertFromJavaType, fieldDescriptor));


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

    public static Class convertFromJavaType(Descriptors.FieldDescriptor.JavaType javaType)
    {
        return classMap.get(javaType);
    }

    public static Object getDefaultValue(Class classz)
    {
        return defaultObjectMap.get(classz);
    }
}
