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
 * hibernateʹ�ýӿ�.
 */
public interface DataStore {
      /**
         * ɾ������vo
         * @param obj   ��Ҫ��������
         * @throws com.piliskys.exception.DAOException
         */
    public  boolean delete(Object obj)
        throws DAOException;
          /**
           * ɾ��һϵ��vo
           * @param obj �����ֶο�����null, ������Ϊ�ַ�����������Ϊ %,�Զ�ɾ��ƥ���VO
           * @return  ɾ���󷵻ص�����
           * @throws DAOException
           */
    public  int deleteByVO(Object obj)
        throws DAOException;
         /**
          * ����vo����
          * @param obj Ҫ������������Ϊ��ѯ������ ����set���ڸ����ֶ�
          * @throws DAOException
          */
    public  int update(Object obj)
        throws DAOException;
         /**
          * �����������ַ��������ô�%������������null����������Ϊ''�ַ���
          * ��ǰһ������Ϊ���������������º�һ�������õ�ֵ
          * @param src ����
          * @param tar  Ҫ���µ�ֵ
          * @throws DAOException
          */
     public  int update(Object src,Object tar)
        throws DAOException;
          /**
           * ����һVO
           * @param obj Ҫ����һЩ��Ӧ������
           * @return
           * @throws DAOException Υ�������������
           */
    public  boolean insert(Object obj)
        throws DAOException;
           /**
            * ����������������vo
            * @param class1
            * @return
            * @throws DAOException
            */
    public  List selectAll(Class class1)
        throws DAOException;

            /**
             * ������������vo
             * @param obj Ҫ�������� ����Ҳ��obj
             * @throws DAOException
             */
    public  void selectByPK(Object obj)
        throws DAOException;

            /**
             * ���ݶ������õ����Ե�ֵ���в���
             * @param obj ������null���ַ����ֶο����ô�%�ַ���
             * @return   ��Ӧ��list
             * @throws DAOException
             */
    public  List selectByVO(Object obj)
        throws DAOException;

            /**
             * ���ݶ������õ����Լ���ʼ�ͽ����ĸ�����ֵ���в���
             * �����ڷ�ҳ��
             * @param obj
             * @param i  ��ʼ����
             * @param j  ��������
             * @return   ��Ӧ��list
             * @throws DAOException
             */
    public  List selectByVO(Object obj, int i, int j)
        throws DAOException;

             /**
              * ����sql��䣨����update��delete)
              * @return  ��¼��
              */

    public  int sqlExcute(String s)
        throws DAOException;
              /**
               * ����sql��䣨����update��delete)
               * @param s  ����?�����ַ���
               * @param list  ����������
               * @return   ���»�ɾ���ĸ���
               * @throws DAOException
               */
    public  int sqlExcute(String s, List list)
        throws DAOException;
    
    
              /**
               * ����sql���޲����Ĳ�ѯ
               * @param s �� �������ַ����ַ���
               * @return  List ��map��list,keyΪ�ֶ�ȫ��Сд
               * @throws DAOException
               */
    public List selectBySQL(String s)
            throws DAOException;
    public ResultSet selectBySQLRS(String s) throws DAOException ;
               /**
                * SQL��ѯ
                * @param s
                * @param list   ������Ӧֵϵ��
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
     * @param list  ������Ӧֵϵ��
     * @param i  �������ʼ��
     * @param j  �������
     * @return
     * @throws DAOException
     */
    public  List selectByQuery(String s, List list,int i,int j)
        throws DAOException;
    /**
	 * 
	 * @param sql  like  select * from a  where a.a1=:a1 and a.a2 in a2
	 * @param parameterMap {a1:XX a2:xxx}
	 * @param dataSourceName //���ݿ�������
	 * @param endNum С��0��ʾ��ȫ��
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
				���� ���������  &�����������map������ֱ���滻
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
