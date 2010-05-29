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
public class MemberKeyTestCase extends TestCase
{

   public void testBad()
   {
      assertCannotBuild("b(");
      assertCannotBuild("b)");
   }

   private void assertCannotBuild(String member)
   {
      try
      {
         MemberKey.parse(member);
         fail("Should never have been able to build " + member);
      }
      catch (CodeSourceException ignore)
      {
      }
   }

   public void testOK()
   {
      assertEquals(MemberKey.createNamedKey("a"), MemberKey.parse("a"));
      assertEquals(MemberKey.createSignedKey("a"), MemberKey.parse("a()"));
      assertEquals(MemberKey.createSignedKey("a"), MemberKey.parse("a(  )"));
      assertEquals(MemberKey.createSignedKey("a", "java.lang.String"), MemberKey.parse("a(java.lang.String)"));
      assertEquals(MemberKey.createSignedKey("a", "java.lang.String"), MemberKey.parse("a( java.lang.String)"));
      assertEquals(MemberKey.createSignedKey("a", "java.lang.String"), MemberKey.parse("a(java.lang.String )"));
      assertEquals(MemberKey.createSignedKey("a", "java.lang.String"), MemberKey.parse("a( java.lang.String )"));
      assertEquals(MemberKey.createSignedKey("a", "java.lang.String", "java.lang.Integer"), MemberKey.parse("a(java.lang.String,java.lang.Integer)"));
      assertEquals(MemberKey.createSignedKey("a", "java.lang.String", "java.lang.Integer"), MemberKey.parse("a(java.lang.String, java.lang.Integer)"));
      assertEquals(MemberKey.createSignedKey("a", "java.lang.String", "java.lang.Integer"), MemberKey.parse("a(java.lang.String ,java.lang.Integer)"));
      assertEquals(MemberKey.createSignedKey("a", "java.lang.String", "java.lang.Integer"), MemberKey.parse("a(java.lang.String , java.lang.Integer)"));
   }
}
