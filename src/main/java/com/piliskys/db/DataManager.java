package com.piliskys.db;

import com.piliskys.db.exception.DAOException;

/**
 * @author : <a href="piliskys@163.com">liuquanbing</a>
 * @version 1.0
 * Time: 15:09:44
 * 得到dataSource实现的一实例
 */
public class DataManager {
    public static DataStore getInstance() throws DAOException {

          return new DataStoreImpl();
   }
    public static DataStore getInstance(String dataSourceName) throws DAOException {

        return new DataStoreImpl(dataSourceName);
 } 
}
