/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wikbook.codesource;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class CodeSourceTestCase extends TestCase
{

   /** . */
   private TypeSource fooSource;

   @Override
   protected void setUp() throws Exception
   {
      CodeSourceBuilder builder = new CodeSourceBuilder();
      fooSource = builder.buildClass(Foo.class.getName());
   }

   public void testFieldClip()
   {
      FieldSource fieldSource = (FieldSource)fooSource.findMember("juu");
      assertNotNull(fieldSource);
      assertEquals("   private String juu = null;\n", fieldSource.getClip());
      assertEquals("   /**\n" +
         "    * The juu String.\n" +
         "    */\n", fieldSource.getJavaDoc());
   }

   public void testMethodClip()
   {
      MethodSource methodSource = (MethodSource)fooSource.findMember("bar");
      assertNotNull(methodSource);
      assertEquals("   public void bar()\n" +
         "   {\n" +
         "   }\n", methodSource.getClip());
      assertEquals("   /**\n" +
         "    * Bar method.\n" +
         "    */\n", methodSource.getJavaDoc());
   }

   public void testConstructorClip()
   {
      MethodSource methodSource = (MethodSource)fooSource.findMember("Foo");
      assertNotNull(methodSource);
      assertEquals("   public Foo()\n" +
         "   {\n" +
         "   }\n", methodSource.getClip());
      assertEquals("   /**\n" +
         "    * Empty constructor.\n" +
         "    */\n", methodSource.getJavaDoc());
   }

   public void testClassClip()
   {
      assertEquals("public class Foo\n" +
         "{\n" +
         "\n" +
         "   /**\n" +
         "    * The juu String.\n" +
         "    */\n" +
         "   private String juu = null;\n" +
         "\n" +
         "   /**\n" +
         "    * Empty constructor.\n" +
         "    */\n" +
         "   public Foo()\n" +
         "   {\n" +
         "   }\n" +
         "\n" +
         "   public void bar(String s)\n" +
         "   {\n" +
         "   }\n" +
         "\n" +
         "   /**\n" +
         "    * Bar method.\n" +
         "    */\n" +
         "   public void bar()\n" +
         "   {\n" +
         "   }\n" +
         "\n" +
         "   public void bar(byte[] bytes)\n" +
         "   {\n" +
         "   }\n" +
         "\n" +
         "}\n", fooSource.getClip());
      assertEquals("/**\n" +
         " * @author <a href=\"mailto:julien.viet@exoplatform.com\">Julien Viet</a>\n" +
         " * @version $Revision$\n" +
         " */\n", fooSource.getJavaDoc());
   }

   public void testMethodLookup1()
   {
      MethodSource methodSource = (MethodSource)fooSource.findMember("bar");
      assertNotNull(methodSource);
      assertEquals("bar", methodSource.getName());
      assertEquals(new Signature(), methodSource.getSignature());
   }

   public void testMethodLookup2()
   {
      MethodSource methodSource = (MethodSource)fooSource.findMember("bar()");
      assertNotNull(methodSource);
      assertEquals("bar", methodSource.getName());
      assertEquals(new Signature(), methodSource.getSignature());
   }

   public void testMethodLookup3()
   {
      MethodSource methodSource = (MethodSource)fooSource.findMember("bar(byte[])");
      assertNotNull(methodSource);
      assertEquals("bar", methodSource.getName());
      assertEquals(new Signature("byte[]"), methodSource.getSignature());
   }

   public void testMethodLookup4()
   {
      MethodSource methodSource = (MethodSource)fooSource.findMember("bar(java.lang.String)");
      assertNotNull(methodSource);
      assertEquals("bar", methodSource.getName());
      assertEquals(new Signature("java.lang.String"), methodSource.getSignature());
   }

   public void testMethodConstructorLookup1()
   {
      MethodSource methodSource = (MethodSource)fooSource.findMember("Foo()");
      assertNotNull(methodSource);
      assertEquals("Foo", methodSource.getName());
      assertEquals(new Signature(), methodSource.getSignature());
   }

   public void testNoSource()
   {
      CodeSourceBuilder builder = new CodeSourceBuilder();
      try
      {
         builder.buildClass("DoesNotExists");
         fail();
      }
      catch (CodeSourceException expected)
      {
      }
   }

   public void testNoClass()
   {
      CodeSourceBuilder builder = new CodeSourceBuilder();
      try
      {
         builder.buildClass("org.wikbook.codesource.Uncompiled");
         fail();
      }
      catch (CodeSourceException expected)
      {
      }
   }
}
