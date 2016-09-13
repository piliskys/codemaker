package com.piliskys.db;

import com.piliskys.db.exception.DAOException;

import java.util.List;

/**
 *
 * @author : <a href="piliskys@163.com">liuquanbing</a>
 * @version 1.0
 * Date: 2006-1-10
 * Time: 11:24:48
 * DAO����
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
     * ����vo��һЩ�������ҳ����ϴ�vo�����е�vo�ļ���
     * @param vo
     * @return
     */
    public abstract List selectByVoInter(Object vo ,int i, int j)   throws DAOException;
     /**
     * ����vo���������ҳ����ϴ�vo
     * @param vo vo�����������Ҳ���������
     */
    public abstract void selectByPKInter(Object vo)   throws DAOException;
    /**
     * ����vo����������ɾ��vo
     * @param vo Ҫ�������������ã�������׳��쳣
     * @return  true����ɾ���ɹ� false��ʾδɾ���κμ�¼
     * @throws DAOException
     */
    public abstract boolean delByPKInter(Object vo)   throws DAOException;
    /**
     * ����һvo�����ݿ�
     * @param vo
     * @return true����ɹ� false����ʧ��
     * @throws DAOException
     */
    public abstract boolean insertInter(Object vo)   throws DAOException;
    /**
     * ����vo��һЩ������ɾ�����ϴ�vo�����е�vo�ļ���
     * @param vo
     */
    /**
     * ����vo��һЩ������ɾ�����ϴ�vo�����е�vo�ļ���
     * @param vo
     */
    public abstract int delByVoInter(Object vo)   throws DAOException;
    /**
     * ǰvo��set��ֵΪ��������vo,set��ֵΪҪ���³ɵ�vo
     * @param vo ��VoΪ����Vo,ָҪ�ҳ����ϴ�vo������vo
     * @param vo1  ��voΪҪ���µ�vo,
     */
    public abstract int updateByVoInter(Object vo,Object vo1)   throws DAOException;
    /**
     * �ѹ��µ�vo������Ҫ������ٸ��£���Ҫ������������Ϊ���µ�����
     * �������õ��ֶ�Ϊ����µ��ֶ�����
     * @param vo1  ��voҪ������������ΪҪ���µ��ֶ�
     */
    public abstract int updateByPkVoInter(Object vo1)   throws DAOException;

}
