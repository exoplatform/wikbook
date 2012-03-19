package org.wikbook.codesource;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public abstract class Response
{
   public static Object status(final int code, final String content)
   {
      return new Object()
      {
         public int getStatus()
         {
            return code;
         }
      };
   }
}
