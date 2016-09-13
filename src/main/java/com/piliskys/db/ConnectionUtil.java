package com.piliskys.db;

import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.net.URL;

import org.apache.log4j.Logger;

import org.dom4j.Element;

/**
 * @author : <a href="piliskys@163.com">liuquanbing</a>
 * @version 1.0
 *          Date: 2006-3-25
 *          Time: 10:21:27
 *          jdbc中Connection的调用
 */
public class ConnectionUtil {
    private static Logger log;
    public static Map dsMap = null;
    private static Map productMap=new HashMap();
    private static final ThreadLocal threadLocal = new ThreadLocal();

    public ConnectionUtil() {
    }

    public static void init() {
    };
    /**
     * 以下为得到当前的数据库连接
     *
     * @param ss
     * @return
     * @throws SQLException
     */
    public static Connection currentConnection(String ss)
              throws SQLException {
         log.debug("---------  Connection 进入！ ");
          Connection conn = null;
          DataSource ds = null;
          Map connMap = (Map) threadLocal.get();
          if (connMap == null) {
              connMap = new HashMap();
              threadLocal.set(connMap);
          }
          if (connMap.get(ss) != null) {
            log.debug("---------  Connection已存在！ ");
          //  System.out.println("---------  Connection已存在！ ");
            return (Connection) connMap.get(ss);


          } else {
              Properties config = (Properties) dsMap.get(ss);
              String driver=null;
              String url=null;
              String username=null;
              String passwd=null;
              String jndi = null;
              if(null!=config.get("connection.driver_class"))
               driver = config.get("connection.driver_class").toString();
              if(null!=config.get("connection.url"))
                url = config.get("connection.url").toString();
              if(null!=config.get("connection.username"))
                username = config.get("connection.username").toString();
              if(null!=config.get("connection.password"))
                passwd = config.get("connection.password").toString();
              if (null != config.get("connection.jndi")) {
  				jndi = config.get("connection.jndi").toString();
  			}
              if (jndi==null||jndi.equals("")) {
                  try {
                      Class.forName(driver).newInstance();
                      conn = DriverManager.getConnection(url, username, passwd);

                  } catch (SQLException ex) {
                      ex.printStackTrace();
                  } catch (ClassNotFoundException ex) {
                      ex.printStackTrace();
                  } catch (IllegalAccessException ex) {
                      ex.printStackTrace();
                  } catch (InstantiationException ex) {
                      ex.printStackTrace();
                  }
              } else {
                 log.debug("---------[InitialContexts] Connection  starting ");
                  try {
                      InitialContext initialcontext = new InitialContext();
                      ds = (DataSource) initialcontext.lookup(jndi);
                  } catch (NamingException e) {
                      e.printStackTrace();
                  }
                  if (ds != null) {
                        log.debug("---------[InitialContexts] Connection get ");
                      conn = ds.getConnection();

                  } else {
                      conn = null;
                      log.debug("---------[error] Connection is null ");
                  }
              }
                log.debug(ss+"=数据库开启 : ");
            //    System.out.println(ss+"---------  =数据库开启！ ");
           conn.setAutoCommit(false);
              connMap.put(ss, conn);

              return conn;
          }
      }
    /**
     * newConnection 内部保留方法，需要自己提交及关闭
     * @param ss
     * @return
     * @throws SQLException
     */
    public static Connection newConnection(String ss)
    throws SQLException {
log.debug("---------  Connection 进入！ ");
Connection conn = null;
DataSource ds = null;

    Properties config = (Properties) dsMap.get(ss);
    String driver=null;
    String url=null;
    String username=null;
    String passwd=null;
    String jndi = null;
    if(null!=config.get("connection.driver_class"))
     driver = config.get("connection.driver_class").toString();
    if(null!=config.get("connection.url"))
      url = config.get("connection.url").toString();
    if(null!=config.get("connection.username"))
      username = config.get("connection.username").toString();
    if(null!=config.get("connection.password"))
      passwd = config.get("connection.password").toString();
    if (null != config.get("connection.jndi")) {
		jndi = config.get("connection.jndi").toString();
	}
    if (jndi==null||jndi.equals("")) {
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, username, passwd);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        }
    } else {
       log.debug("---------[InitialContexts] Connection  starting ");
        try {
            InitialContext initialcontext = new InitialContext();
            ds = (DataSource) initialcontext.lookup(jndi);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        if (ds != null) {
              log.debug("---------[InitialContexts] Connection get ");
            conn = ds.getConnection();

        } else {
            conn = null;
            log.debug("---------[error] Connection is null ");
        }
    }
      log.debug(ss+"=数据库开启 : ");
   conn.setAutoCommit(false);
    return conn;
}
    public static String getProductName(String  dataSourceName) throws SQLException{
    	String productName;
    	if (productMap.get(dataSourceName)==null)
    	{
			 productName = currentConnection(dataSourceName).getMetaData().getDatabaseProductName();	
			 productMap.put(dataSourceName, productName);
    	}else  productName =  productMap.get(dataSourceName).toString();
    	return productName;
    };

     static {
        log = Logger.getLogger(ConnectionUtil.class);
        dsMap = new HashMap();
        try {

              com.piliskys.db.common.XmlHelper aa = new com.piliskys.db.common.XmlHelper();

             URL urll = aa.gerPathUrl("db.config.xml");
            aa.openXML(urll.getPath());
            ;
            Element root = aa.getDocument().getRootElement();
            Element foo;
            for (Iterator i = root.elementIterator("db-config"); i.hasNext();) {
                foo = (Element) i.next();
                Element foof;
                String ds_name = foo.attribute("id").getStringValue();
                Properties config = new Properties();//记录配置项
                for (Iterator it = foo.elementIterator(); it.hasNext();) {
                    foof = (Element) it.next();
                    config.setProperty(foof.getName(), foof.getText());
                }
                dsMap.put(ds_name, config);
            }
            System.out.println("db.config.xml文件装载成功！");

         log.info("db.config.xml文件装载成功！");
        } catch (Exception e) {
            log.error("db.config.xml文件装载失败，确保db.config.xml在对应的config目录的classpath中！");
            e.printStackTrace();
        }
    }

     public static void closeConnection(final String ss) throws SQLException {
 		final Map connMap = (Map) ConnectionUtil.threadLocal.get();
 		if (connMap == null) {
 			return;
 		}
 		Connection cs = ((Connection) connMap.get(ss));
 		connMap.put(ss, null);
 		if (cs != null) {
 			try {
 				ConnectionUtil.log.debug("尝试关闭数据库:[" + ss + "],hashCode : "
 						+ cs.hashCode());
 				cs.close();
 				ConnectionUtil.log.debug("关闭数据库:[" + ss + "] 完成,hashCode : "
 						+ cs.hashCode());
 			} catch (final Exception e) {
 				 System.out.println("关闭数据库:[" + ss + "] "+e );
 				throw new SQLException("数据库关闭发生错误！");
 			}
 			cs = null;
 		}
 	}
    public static void closeAll() throws SQLException {
        Map connMap = (Map) threadLocal.get();
        if (connMap == null)
            return;
     Set  a=    connMap.keySet() ;
         Object[] obj =a.toArray();
       for( int i=0;i<obj.length;i++)
    	   closeConnection(obj[i].toString())  ;

    }
    
    public static void commitAll() throws SQLException {
        Map connMap = (Map) threadLocal.get();
        if (connMap == null)
            return;
     Set  a=    connMap.keySet() ;
         Object[] obj =a.toArray();
       for( int i=0;i<obj.length;i++)
    	   commit(obj[i].toString())  ;

    }
    public static void rollbackAll() throws SQLException {
        Map connMap = (Map) threadLocal.get();
        if (connMap == null)
            return;
     Set  a=    connMap.keySet() ;
         Object[] obj =a.toArray();
       for( int i=0;i<obj.length;i++)
    	   rollback(obj[i].toString())  ;

    }
    /**
     * 数据库提交
     *
     * @param ss
     * @throws SQLException
     */

    public static void commit(String ss) throws SQLException {
        Map connMap = (Map) threadLocal.get();
        if (connMap == null)
            return;
        Connection cs = ((Connection) connMap.get(ss));

        if (cs != null) {
            try {
                cs.commit();
                log.debug(ss+":数据库提交成功>>>>>>>>>>>>");
            } catch (Exception e) {
            	log.debug(ss+":数据库提交发生错误>>>>>>>>>>>>");
                throw new SQLException("数据库提交发生错误！");
            }
           // cs = null;
        }
    } 
    public static void rollback(String ss) throws SQLException {
        Map connMap = (Map) threadLocal.get();
        if (connMap == null)
            return;
        Connection cs = ((Connection) connMap.get(ss));
      
        if (cs != null) {
            try {
                cs.rollback();
                log.debug(ss+":数据库回滚成功>>>>>>>>>>>>"); 
            } catch (Exception e) {
            	log.debug(ss+":数据库回滚发生错误>>>>>>>>>>>>"); 
                throw new SQLException("数据库回滚发生错误！");
            }
          //  cs = null;
        }
    }

    
    
   //++++++++++++++++++++++++++++++++++++==和到分页数据ResultSet
    
  
    ///
 // 返回分页sql
	public static String getRollSql(String s,String productName) {
		if (isOracle(productName)) {

			return "select * from (select  t.*,rownum row_numm from (" + s
					+ ") t where rownum <= ?) where row_numm >= ?";
		}
		if (isDb2(productName)) {

			return "SELECT * FROM (" + s
					+ ") AS a1 WHERE a1.rn BETWEEN ? AND ?";
		}
		if (isMySQL(productName))
			return s + " limit :?,?";

		return s;
	}

	/**
	 * Returns whether the underlying database is db2.
	 */
	public static boolean isDb2(String productName) {
		return productName.toUpperCase().indexOf("DB2") >= 0;
	}

	/**
	 * Returns whether the underlying database is Firebird.
	 */
	public static boolean isFirebird(String productName) {
		return productName.toUpperCase().indexOf("FIREBIRD") >= 0;
	}

	/**
	 * Returns whether the underlying database is Informix.
	 */
	public static boolean isInformix(String productName) {
		return productName.startsWith("Informix");
	}

	/**
	 * Returns whether the underlying database is Ingres.
	 */
	public static boolean isIngres(String productName) {
		return productName.toUpperCase().equals("INGRES");
	}

	/**
	 * Returns whether the underlying database is Interbase.
	 */
	public static boolean isInterbase(String productName) {
		return productName.equals("Interbase");
	}

	/**
	 * Returns whether the underlying database is LucidDB.
	 */
	public static boolean isLucidDB(String productName) {
		return productName.toUpperCase().equals("LUCIDDB");
	}

	/**
	 * Returns whether the underlying database is Microsoft SQL Server.
	 */
	public static boolean isMSSQL(String productName) {
		return productName.toUpperCase().indexOf("SQL SERVER") >= 0;
	}

	/**
	 * Returns whether the underlying database is Oracle.
	 */
	public static boolean isOracle(String productName) {
		return productName.equals("Oracle");
	}

	/**
	 * Returns whether the underlying database is Postgres.
	 */
	public static boolean isPostgres(String productName) {
		return productName.toUpperCase().indexOf("POSTGRE") >= 0;
	}

	/**
	 * Returns whether the underlying database is MySQL.
	 */
	public static boolean isMySQL(String productName) {
		return productName.toUpperCase().equals("MYSQL");
	}

	/**
	 * Returns whether the underlying database is Sybase.
	 */
	public static boolean isSybase(String productName) {
		return productName.toUpperCase().indexOf("SYBASE") >= 0;
	}

	/**
	 * Returns whether the underlying database is Teradata.
	 */
	public static boolean isTeradata(String productName) {
		return productName.toUpperCase().indexOf("TERADATA") >= 0;
	}
    
    public static String show_sql = "true";
}