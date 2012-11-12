package com.pwf.core.impl;

import org.junit.Test;
import static org.junit.Assert.*;

import com.pwf.core.impl.EngineUtilsTest.A.C.D;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author mfullen
 */
public class EngineUtilsTest
{
    public EngineUtilsTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    /**
     * Test of createExtensionRegistry method, of class EngineUtils.
     */
    @Test
    public void testCreateExtensionRegistry()
    {
    }

    /**
     * Test of getAllPossibleExtensionClasses method, of class EngineUtils.
     */
    @Test
    public void testGetAllPossibleExtensionClasses()
    {
        Set<Class<?>> extensionClasses = new LinkedHashSet<Class<?>>();
        Set<Class<?>> allPossibleExtensionClasses = EngineUtils.getAllPossibleExtensionClasses(extensionClasses, D.class);
        assertEquals(4, allPossibleExtensionClasses.size());
        Set<Class<?>> allPossibleExtensionClasses2 = EngineUtils.getAllPossibleExtensionClasses(D.class);
        assertEquals(4, allPossibleExtensionClasses2.size());
    }

    public static class A
    {
        public static class B
        {
        }

        public static class C
        {
            public static class D
            {
            }
        }
    }
}