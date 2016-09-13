package com.piliskys.db;


import com.piliskys.db.common.StringUtils;
import com.piliskys.db.exception.DAOException;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.sql.*;





/**
 *
 * @author : <a href="piliskys@163.com">liuquanbing</a>
 * @version 1.0
 *          Date: 2006-1-12
 *          Time: 15:41:49
 *          dataSource使用实现
 */
public class DataStoreImpl implements DataStore {

	private String dataSourceName;
	DataStoreImpl(){
		//defaultDataSource 为默认的数据库
		dataSourceName="defaultDataSource";
    }
	DataStoreImpl(String dsname){
		dataSourceName=dsname;
    }
    public boolean delete(Object obj) throws DAOException {
        try {
        	((BaseVO) obj).fetchDAO().setDataSourceName(dataSourceName);
        	return	((BaseVO) obj).fetchDAO().delByPKInter(obj);
             
            } catch (Exception exception) {
              throw new DAOException("delete error", exception);
        }
    }


    public int deleteByVO(Object obj) throws DAOException {
        int num = 0;
        try {
        	((BaseVO) obj).fetchDAO().setDataSourceName(dataSourceName);
            num = ((BaseVO) obj).fetchDAO().delByVoInter(obj);

        } catch (Exception exception) {
            throw new DAOException("deleteByVO error", exception);
        }

        return num;
    }

    public int update(Object obj) throws DAOException {
        try {
        	((BaseVO) obj).fetchDAO().setDataSourceName(dataSourceName);
         return   ((BaseVO) obj).fetchDAO().updateByPkVoInter(obj);

        } catch (Exception exception) {
            throw new DAOException("updatevo error", exception);
        }

    }

    public int update(Object obj, Object obj1) throws DAOException {
    	((BaseVO) obj).fetchDAO().setDataSourceName(dataSourceName);
        return   ((BaseVO) obj).fetchDAO().updateByVoInter(obj,obj1);
    }

    public boolean insert(Object obj) throws DAOException {
        try {
        	((BaseVO) obj).fetchDAO().setDataSourceName(dataSourceName);
        	return ((BaseVO) obj).fetchDAO().insertInter(obj);

        } catch (Exception exception) {
            throw new DAOException("insert error", exception);
        }
    }

    public List selectAll(Class class1) throws DAOException {
        try {
        	Object obj =class1.newInstance();
        	((BaseVO) obj).fetchDAO().setDataSourceName(dataSourceName);
        return  ((BaseVO) obj).fetchDAO().selectByVoInter(obj,0,0);

        } catch (Exception exception) {
            throw new DAOException("selectAll error" + exception);
        }

    }

    public void selectByPK(Object obj) throws DAOException {
        try {
        	((BaseVO) obj).fetchDAO().setDataSourceName(dataSourceName);
             ((BaseVO) obj).fetchDAO().selectByPKInter(obj);

        } catch (Exception exception) {
            throw new DAOException("selectByPK error", exception);
        }

    }

    public List selectByVO(Object obj) throws DAOException {

        try {
        	((BaseVO) obj).fetchDAO().setDataSourceName(dataSourceName);
            return ((BaseVO) obj).fetchDAO().selectByVoInter(obj, 0, 0);

        } catch (Exception exception) {
            throw new DAOException("selectByVO error", exception);
        }
    }

    public List selectByVO(Object obj, int i, int j) throws DAOException {
        try {
        	((BaseVO) obj).fetchDAO().setDataSourceName(dataSourceName);
            return ((BaseVO) obj).fetchDAO().selectByVoInter(obj, i, j);

        } catch (Exception exception) {
            throw new DAOException("selectByVO error", exception);
        }
    }


    public int sqlExcute(String s) throws DAOException {
    	Statement st =null;
        Connection conn =null;
		try
		{
			conn = ConnectionUtil.currentConnection(dataSourceName);
			st =conn.createStatement();
			
		return	st.executeUpdate(s);
        } catch (Exception exception) {
            throw new DAOException("sqlExcute", exception);
        }
        finally{
            try{
                if(st!=null) {
                    st.close();
                }
            }catch(Exception e){}
        }

    }

    public int sqlExcute(String s, List list) throws DAOException {
    	PreparedStatement pt =null;
        Connection conn =null;
		try
		{
			conn = ConnectionUtil.currentConnection(dataSourceName);
			pt = conn.prepareStatement(s);
			for(int i= 0;i<list.size();i++)
			     if(list.get(i)==null)
			    	 pt.setNull(i+1, java.sql.Types.CHAR);
			     else
				pt.setObject(i+1,list.get(i));
	         
			
            return  pt.executeUpdate();

        } catch (Exception exception) {
            throw new DAOException("sqlExcute", exception);
        }
        finally{
            try{
                if(pt!=null) {
                    pt.close();
                }
                }catch(Exception e){}
        }
    }
    
    

    public List selectBySQL(String s) throws DAOException {
    	Statement pt =null;
        Connection conn =null;
		try
		{
			conn = ConnectionUtil.currentConnection(dataSourceName);
			pt = conn.createStatement();
          ResultSet rs = pt.executeQuery(s);
            return getResults(rs);

        } catch (Exception exception) {
            throw new DAOException("selectBySQL", exception);
        }
        finally{
            try{
                if(pt!=null) {
                    pt.close();
                }
                }catch(Exception e){}
        }
    }
    public ResultSet selectBySQLRS(String s) throws DAOException {
    	Statement pt =null;
        Connection conn =null;
		try
		{
			conn = ConnectionUtil.currentConnection(dataSourceName);
			pt = conn.createStatement();
          ResultSet rs = pt.executeQuery(s);
            return rs;

        } catch (Exception exception) {
            throw new DAOException("selectBySQL", exception);
        }
        finally{
            try{
                if(pt!=null) {
                    pt.close();
                }
                }catch(Exception e){}
        }
    }
    
    public List selectByQuery(String s, List list) throws DAOException {

      return  selectByQuery(s,list,0,0) ;
    }

    public List selectByQuery(String s, List list, int a, int b) throws DAOException {
    	PreparedStatement pt =null;
        Connection conn =null;
		try {
			String sqlStr=null;
			conn = ConnectionUtil.currentConnection(dataSourceName);
			String productName = ConnectionUtil.getProductName(dataSourceName);
			if(b>0){
				sqlStr=ConnectionUtil.getRollSql(s,productName);
				}
			else sqlStr=s;
		//	log.debug("sqlStr\n="+sqlStr);
			pt = conn.prepareStatement(sqlStr);
			int i;
			for (i = 0; i < list.size(); i++){
		    pt.setObject(i+1,list.get(i));
		   }
			if(b>0){
				   pt.setInt(i+1,b);
				   pt.setInt(i+2,a);
			}
		   ResultSet rs = pt.executeQuery();
		   return  getResults(rs);
        } catch (Exception exception) {
            throw new DAOException("connection selectByQuery", exception);
        }
        finally{
            try{
                if(pt!=null) {
                     pt.close();
                }
                }catch(Exception e){}
        }
    }
    
    
    /**
	 * 
	 * @param sql  like  select * from a  where a.a1=:a1 and a.a2 in a2
	 * @param parameterMap {a1:XX a2:xxx}
	 * @param dataSourceName //数据库联接名
	 * @param endNum 小于0表示查全部
	 * @return  list
	 * @throws DAOException 
	 */
	public   List MapSqlQuery(String sql, Map parameterMap,int startNum, int endNum) throws Exception {

		
		List parameterList = new ArrayList();
		String patternStr = "\\:([a-z|A-Z|0-9|_]+)"; //匹配:abcd
		List<String> strList = StringUtils.getContains(sql, patternStr);
		for (int i = 0; i < strList.size(); i++) {
			Object out = parameterMap.get(strList.get(i).substring(1));
			if (out != null) {
				if (out.getClass().isArray()) {
					Object[] outArr = (Object[]) out;
					if (outArr.length > 0) {
						String parSt = "";
						for (int j = 0; j < outArr.length; j++) {
							parameterList.add(outArr[j]);
							parSt += ",?";
						}
						parSt = "(" + parSt.substring(1) + ")";
						sql = sql.replaceFirst(strList.get(i), parSt);
					} else {
						parameterList.add(null);
						sql = sql.replaceFirst(strList.get(i), "?");
					}

				} else {
					parameterList.add(out);
					sql = sql.replaceFirst(strList.get(i), "?");

				}

			} else {
				parameterList.add(out);
				sql = sql.replaceFirst(strList.get(i), "?");

			}

		}
        //return null;
		 return selectByQuery(sql, parameterList,startNum,endNum);
	}
	/**
	 * \
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
	

    public void close() throws SQLException{
    	ConnectionUtil.closeConnection(dataSourceName);
    }
    public   void commit()throws SQLException{
    	ConnectionUtil.commit(dataSourceName);
    }
    public void rollback() throws SQLException {
        ConnectionUtil.rollback(dataSourceName);

    }

        public void closeAll() throws SQLException {
        ConnectionUtil.closeAll();

    }
        
        public   void commitAll()throws SQLException{
        	ConnectionUtil.commitAll();
        }
        public   void rollbackAll()throws SQLException{
        	ConnectionUtil.rollbackAll();
        	
        }    
    public void open() {

    }

    /**
     * 将结果转为一list||list所装是多个HashMap,key是字段的全部小写
     *
     * @param rs
     * @return List
     * @throws SQLException
     */
    private static List getResults(ResultSet rs) throws SQLException {
        List resultList = new ArrayList();
        try {

            ResultSetMetaData rsmd = rs.getMetaData();
            int colnum = rsmd.getColumnCount();
            Object obet = "";
            while (rs.next()) {
                Map classrow = new HashMap();
                for (int j = 0; j < colnum; j++) {
                	obet=rs.getObject(j + 1);
                    
                    classrow.put(rsmd.getColumnName(j + 1).toLowerCase(), obet);
                }
                resultList.add(classrow);
            }
            rs.close();
        } catch (SQLException e) {
            throw  (e);
        }

        return resultList;
    }
    public Page pageByQuery(String sql, List parametersList, int currentPage,
			int pageRecords) throws DAOException {
		Page pvo = new Page();
		pvo.setPageRecords(pageRecords);
		if (pageRecords > 0) {
			int pageStart = (currentPage - 1) * pageRecords + 1;
			int pageEnd = pageStart + pageRecords - 1;
			String pageSql = sql;

			int totalSize = getSqlRsCount(sql, parametersList);
			List result = selectByQuery(pageSql, parametersList, pageStart,
					pageEnd);

			int pageCount = totalSize / pageRecords + 1;
			if (totalSize % pageRecords == 0)
				pageCount--;
			pvo.setResults(result);
			pvo.setTotalSize(totalSize);
			pvo.setPages(pageCount);
			pvo.setCurrentPage(currentPage);
		} else {
			String allSql = sql;
			List result = selectByQuery(allSql, parametersList);
			int totalSize = result.size();
			pvo.setResults(result);
			pvo.setTotalSize(totalSize);
			pvo.setPages(1);
			pvo.setCurrentPage(1);
		}

		return pvo; //To change body of implemented methods use File | Settings | File Templates.

	}

	public int getSqlRsCount(String sql, List parametersList)
			throws DAOException {
		int count = -1;
		try {
			String countQuery = "select count(1) numb_ from (" + sql + ")";
			List countList = selectByQuery(countQuery, parametersList);
			count = Integer.parseInt(((Map) countList.get(0)).get("numb_")
					.toString());
		} catch (DAOException e) {
			throw e;

		}
		return count;
	}
    //public static int MAX_SIZE = 10000;//设置查询的最大条数
    
   
}
