package com.piliskys.db;

import com.piliskys.db.exception.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author : <a href="piliskys@163.com">liuquanbing</a>
 * @version 1.0
 * Date: 2006-1-12
 * Time: 12:37:13
 * hibernate使用接口.
 */
public interface DataStore {
      /**
         * 删除单个vo
         * @param obj   需要设置主键
         * @throws com.piliskys.exception.DAOException
         */
    public  boolean delete(Object obj)
        throws DAOException;
          /**
           * 删除一系列vo
           * @param obj 所有字段可设置null, 若属性为字符串，可设置为 %,自动删除匹配的VO
           * @return  删除后返回的行数
           * @throws DAOException
           */
    public  int deleteByVO(Object obj)
        throws DAOException;
         /**
          * 单个vo更新
          * @param obj 要设置主键（作为查询条件） 其它set属于更新字段
          * @throws DAOException
          */
    public  int update(Object obj)
        throws DAOException;
         /**
          * 条件可如是字符串可设置带%，其它可设置null，不能设置为''字符串
          * 以前一对像做为条件进行批量更新后一对象设置的值
          * @param src 条件
          * @param tar  要更新的值
          * @throws DAOException
          */
     public  int update(Object src,Object tar)
        throws DAOException;
          /**
           * 插入一VO
           * @param obj 要设置一些相应的属性
           * @return
           * @throws DAOException 违反插入操作报错
           */
    public  boolean insert(Object obj)
        throws DAOException;
           /**
            * 返回设置最大个数的vo
            * @param class1
            * @return
            * @throws DAOException
            */
    public  List selectAll(Class class1)
        throws DAOException;

            /**
             * 根据主键查找vo
             * @param obj 要设置主键 返回也是obj
             * @throws DAOException
             */
    public  void selectByPK(Object obj)
        throws DAOException;

            /**
             * 根据对象设置的属性的值进行查找
             * @param obj 可设置null，字符串字段可设置带%字符串
             * @return   对应的list
             * @throws DAOException
             */
    public  List selectByVO(Object obj)
        throws DAOException;

            /**
             * 根据对象设置的属性及起始和结束的个数的值进行查找
             * （用于分页）
             * @param obj
             * @param i  开始的行
             * @param j  结束的行
             * @return   对应的list
             * @throws DAOException
             */
    public  List selectByVO(Object obj, int i, int j)
        throws DAOException;

             /**
              * 调用sql语句（用于update和delete)
              * @return  记录数
              */

    public  int sqlExcute(String s)
        throws DAOException;
              /**
               * 调用sql语句（用于update和delete)
               * @param s  带“?”的字符串
               * @param list  参数的设置
               * @return   更新或删除的个数
               * @throws DAOException
               */
    public  int sqlExcute(String s, List list)
        throws DAOException;
    
    
              /**
               * 利用sql中无参数的查询
               * @param s 带 “？”字符的字符串
               * @return  List 含map的list,key为字段全部小写
               * @throws DAOException
               */
    public List selectBySQL(String s)
            throws DAOException;
    public ResultSet selectBySQLRS(String s) throws DAOException ;
               /**
                * SQL查询
                * @param s
                * @param list   参数对应值系列
                * @return
                * @throws DAOException
                */
    public  List selectByQuery(String s, List list)
        throws DAOException;
    public   Page pageByQuery(String sql, List parametersList, int currentPage, int pageRecords)
    throws DAOException ;
    public int getSqlRsCount(String sql, List parametersList)
	throws DAOException ;
    /**
     *
     * @param s   SQL
     * @param list  参数对应值系列
     * @param i  结果集开始数
     * @param j  结果集数
     * @return
     * @throws DAOException
     */
    public  List selectByQuery(String s, List list,int i,int j)
        throws DAOException;
    /**
	 * 
	 * @param sql  like  select * from a  where a.a1=:a1 and a.a2 in a2
	 * @param parameterMap {a1:XX a2:xxx}
	 * @param dataSourceName //数据库联接名
	 * @param endNum 小于0表示查全部
	 * @return  list
	 * @throws DAOException 
	 */
    public   List MapSqlQuery(String vsql, Map parameterMap,int startNum, int endNum) throws Exception;
    /**
	 * 
	 * @param vsql like "s&elect '2009'||':dsf' from aa a where 1=1 and ee=A and " +
				"#[&A] " +
				"#[and  a.cctv=&A_c] " +
				"#[and  a.flag in (:B_a) " +
				"   #[and  a.text=&C]]"
				其中 ：代表参数  &代表变量，用map中内容直接替换
	 * @param parameterMap
	 * @param startNum
	 * @param endNum
	 * @return
	 * @throws Exception
	 */ 
    //public   List VSqlQuery(String vsql, Map parameterMap,int startNum, int endNum) throws Exception;
    public   void commit()throws SQLException;
    public   void rollback()throws SQLException;
    public   void close()throws SQLException;
    public   void closeAll()throws SQLException;
    public   void commitAll()throws SQLException;
    public   void rollbackAll()throws SQLException;
}
