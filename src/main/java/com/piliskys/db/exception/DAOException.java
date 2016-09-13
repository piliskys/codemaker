package com.piliskys.db.exception;

/**
 *
 * @author : <a href="piliskys@163.com">liuquanbing</a>
 * @version 1.0
 * Date: 2006-1-10
 * Time: 11:29:44
 * ¶¨ÒåµÄDAO´íÎó.
 */
public class DAOException extends Exception{
    public DAOException()
       {
       }

       public DAOException(String message)
       {
           super(message);
       }

       public DAOException(String message, Throwable cause)
       {
           super(message, cause);
       }
    public DAOException(String s,Exception e){

          super(s,e);
    }
    public DAOException(Exception e){

          super(e);
    }
}