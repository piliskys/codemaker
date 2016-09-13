package com.piliskys.db;

import com.piliskys.db.exception.DAOException;

import java.util.List;

/**
 *
 * @author : <a href="piliskys@163.com">liuquanbing</a>
 * @version 1.0
 * Date: 2006-1-10
 * Time: 11:24:48
 * DAO基类
 */
public abstract class BaseDAO {
	private String  dataSourceName="defaultDataSource";
 	
     public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public BaseDAO()
    {
    }
    /**
     * 根据vo的一些条件，找出符合此vo的所有的vo的集合
     * @param vo
     * @return
     */
    public abstract List selectByVoInter(Object vo ,int i, int j)   throws DAOException;
     /**
     * 根据vo的主键，找出符合此vo
     * @param vo vo是输入参数，也是输出参数
     */
    public abstract void selectByPKInter(Object vo)   throws DAOException;
    /**
     * 根据vo的主键进行删除vo
     * @param vo 要对主键进行设置，否则会抛出异常
     * @return  true代表删除成功 false表示未删除任何记录
     * @throws DAOException
     */
    public abstract boolean delByPKInter(Object vo)   throws DAOException;
    /**
     * 插入一vo于数据库
     * @param vo
     * @return true代表成功 false代表失败
     * @throws DAOException
     */
    public abstract boolean insertInter(Object vo)   throws DAOException;
    /**
     * 根据vo的一些条件，删除符合此vo的所有的vo的集合
     * @param vo
     */
    /**
     * 根据vo的一些条件，删除符合此vo的所有的vo的集合
     * @param vo
     */
    public abstract int delByVoInter(Object vo)   throws DAOException;
    /**
     * 前vo，set的值为条件，后vo,set的值为要更新成的vo
     * @param vo 此Vo为条件Vo,指要找出符合此vo条件的vo
     * @param vo1  此vo为要更新的vo,
     */
    public abstract int updateByVoInter(Object vo,Object vo1)   throws DAOException;
    /**
     * 脱管下的vo，不需要查出后再更新，需要设置主键，作为更新的条件
     * 其它设置的字段为需更新的字段属性
     * @param vo1  此vo要设置主键其它为要更新的字段
     */
    public abstract int updateByPkVoInter(Object vo1)   throws DAOException;

}
