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

package org.wikbook.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * An editor.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Person
{

   /** . */
   private final String firstName;

   /** . */
   private final String lastName;

   /** . */
   private final String email;

   /** . */
   private final String organization;

   /** . */
   private final Set<String> roles;

   public Person(String firstName, String lastName, String email, String organization, String... roleNames)
   {
      this(firstName, lastName, email, organization, new HashSet<String>(Arrays.asList(roleNames)));
   }

   public Person(String firstName, String lastName, String email, String organization, Set<String> roleNames)
   {
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.organization = organization;
      this.roles = new HashSet<String>(roleNames);
   }

   public String getFirstName()
   {
      return firstName;
   }

   public String getLastName()
   {
      return lastName;
   }

   public String getEmail()
   {
      return email;
   }

   public String getOrganization()
   {
      return organization;
   }

   public boolean hasRole(String roleName)
   {
      return roles.contains(roleName);
   }

   @Override
   public String toString()
   {
      return "Person[firstName=" + firstName + ",lastName=" + lastName + ",email=" + email + ",organization=" + organization + "]";
   }
}
