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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Utils
{

   public static Iterable<String> split(final String s)
   {
      return new Iterable<String>()
      {
         public Iterator<String> iterator()
         {
            return new Iterator<String>()
            {

               /** The index of previous. */
               private int from = 0;

               public boolean hasNext()
               {
                  return from <= s.length();
               }

               public String next()
               {
                  if (from > s.length())
                  {
                     throw new NoSuchElementException();
                  }
                  int to = s.indexOf('\n', from);
                  if (to == -1)
                  {
                     to = s.length();
                  }
                  String next = s.substring(from, to);
                  from = to + 1;
                  return next;
               }

               public void remove()
               {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }

   public static Reader read(URL url) throws IOException
   {
      return new InputStreamReader(url.openStream(), "UTF-8");
   }

   public static String read(Reader reader) throws IOException
   {
      StringWriter writer = new StringWriter();
      char[] buffer = new char[256];
      for (int amount = reader.read(buffer); amount != -1; amount = reader.read(buffer))
      {
         writer.write(buffer, 0, amount);
      }
      return writer.toString();
   }

   public static byte[] load(URL url) throws IOException
   {
      return load(url.openStream());
   }

   public static byte[] load(InputStream in) throws IOException
   {
      try
      {
         byte[] bytes = new byte[128];
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         for (int s = in.read(bytes); s != -1; s = in.read(bytes))
         {
            baos.write(bytes, 0, s);
         }
         bytes = baos.toByteArray();
         return bytes;
      }
      finally
      {
         try
         {
            in.close();
         }
         catch (IOException ignore)
         {
         }
      }
   }

   public static String toString(Throwable t)
   {
      StringWriter writer = new StringWriter();
      t.printStackTrace(new PrintWriter(writer));
      return writer.toString();
   }
}
