package com.csljc.dataext.action.opt;

import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.csljc.common.OptUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.piliskys.dataStore.Page;

public class ${tabmap['model']}Action extends ActionSupport {
	// 查询sql
	private static Map<String,String>  selectMap;
	private String list_Sql = "${tabmap['selectSql']}"
	<#list   map?keys as mykey>
	<#if  map[mykey].search == 's'>
	+ " ${r"#{"}and  ${mykey} in @${mykey} }"
    </#if>
	<#if  map[mykey].search == 'm'>
	+ " ${r"#{"}and  ${mykey} between &b_${mykey} and &e_${mykey} } "
    </#if>
	</#list>
	  ;
<#if  tabmap['update_flag'] == 1>
	private String del_Sql = "delete from ${tabmap['tabName']} "
			+ "where ${tabmap['primary_id']}  in  @${tabmap['primary_id']} ";
	
	private String insert_sql = "insert into  ${tabmap['tabName']}( "
	  <#assign aa_idx = 0>  
   +" <#list   map?keys as mykey><#if map[mykey].add !='none' ><#if aa_idx==0><#else>,</#if>${mykey}<#assign aa_idx = aa_idx + 1></#if> </#list>) values ("
	<#assign aa_idx = 0>  
   +" <#list   map?keys as mykey><#if map[mykey].add !='none' ><#if aa_idx==0><#else>,</#if>@${mykey}<#assign aa_idx = aa_idx + 1></#if> </#list>)";
	
	private String update_sql = "update  ${tabmap['tabName']} set " 
	<#assign aa_idx = 0>  
   <#list   map?keys as mykey><#if map[mykey].add !='none' >
   +"<#if aa_idx==0><#else>,</#if>${mykey}=@${mykey} "
   <#assign aa_idx = aa_idx + 1></#if> 
   </#list>
	+" where ${tabmap['primary_id']}=@${tabmap['primary_id']} ";
 
</#if>
	public String list() {
		com.piliskys.dataStore.DataStore ds = null;
		Map<String, Object[]> mp = ServletActionContext.getRequest()
				.getParameterMap();
		int limit = Integer.parseInt(mp.get("limit")[0].toString());
		int page = Integer.parseInt(mp.get("page")[0].toString());
		Map paraMap = new HashMap();

		for (String a : mp.keySet()) {
			System.out.println(a + "------------" + mp.get(a)[0]);
			if (mp.get(a) != null && mp.get(a)[0] != null
					& !mp.get(a)[0].equals(""))
				paraMap.put(a, mp.get(a)[0]);
		}

	//	System.out.println("paraMap=" + paraMap);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		try {
			ds = com.piliskys.dataStore.DataManager.getInstance("rmx");

			Page outp = ds.pageByQuery(list_Sql, paraMap, page, limit);

		//	System.out.println(outp.getResults());

			response.getWriter().write(
					"{\"totalCount\":" + outp.getTotalSize() + ",\"topics\":"
							+ JSONUtil.serialize(outp.getResults()) + "}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return NONE;

	}
<#if  tabmap['update_flag'] == 1>
	// 删除数据
	public String delete() {
		com.piliskys.dataStore.DataStore ds = null;
		Map<String, Object[]> mp = ServletActionContext.getRequest()
				.getParameterMap();

		Map paraMap = new HashMap();

		for (String a : mp.keySet()) {
			System.out.println(a + "------------" + mp.get(a)[0]);
			if (mp.get(a) != null && mp.get(a)[0] != null
					& !mp.get(a)[0].equals(""))
				paraMap.put(a, mp.get(a)[0]);
		}

		String[] objs = paraMap.get("${tabmap['primary_id']}").toString().split(",");
		Integer[] its = new Integer[objs.length];
		for (int i = 0; i < its.length; i++) {
			its[i] = Integer.valueOf(objs[i]);
		}

		paraMap.put("${tabmap['primary_id']}", its);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		try {
			ds = com.piliskys.dataStore.DataManager.getInstance("rmx");

			int delnum = ds.sqlExcute(del_Sql, paraMap);

			response.getWriter().write("已成功删除" + delnum + "条！");
			ds.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ds.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return NONE;

	}

	// 保存
	public String save() {
		com.piliskys.dataStore.DataStore ds = null;
		Map<String, Object[]> mp = ServletActionContext.getRequest()
				.getParameterMap();

		Map paraMap = new HashMap();
		Object obj;

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		try {

			for (String a : mp.keySet()) {
				System.out.println(a + "------------" + mp.get(a)[0]);
				if (mp.get(a) != null && mp.get(a)[0] != null
						& !mp.get(a)[0].equals("")) {
					if (a.equals("match_date") || a.equals("update_time"))
						obj = OptUtil.getSqlTimeStamp(mp.get(a)[0].toString());
					else
						obj = mp.get(a)[0];
					paraMap.put(a, obj);
				}
			}

			String opter = paraMap.get("$opt").toString();
			String optStr = null;
			String optSql = null;
			if (opter.equals("0")) {
				optSql = insert_sql;
				optStr = "已成功保存";
			} else {
				optSql = update_sql;
				optStr = "已成功修改";
			}

			ds = com.piliskys.dataStore.DataManager.getInstance("rmx");

			int delnum = ds.sqlExcute(optSql, paraMap);
			;

			response.getWriter().write(optStr + delnum + "条！");
			ds.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ds.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return NONE;

	}
</#if>
	public String export() {
		System.out.println("--------export----");
		com.piliskys.dataStore.DataStore ds = null;
		Map<String, Object[]> mp = ServletActionContext.getRequest()
				.getParameterMap();
		Map paraMap = new HashMap();

		for (String a : mp.keySet()) {
			System.out.println(a + "------------" + mp.get(a)[0]);
			if (mp.get(a) != null && mp.get(a)[0] != null
					& !mp.get(a)[0].equals(""))
				paraMap.put(a, mp.get(a)[0]);
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.reset();
		response.setContentType("application/msexcel;charset=UTF-8");
		try {
			ds = com.piliskys.dataStore.DataManager.getInstance("rmx");
			ResultSet rs = ds.selectBySQLRS(list_Sql, paraMap);

			response.addHeader("Content-Disposition", "attachment;filename=\""
					+ new String(("结果" + ".xlsx").getBytes("GBK"),
							"ISO8859_1") + "\"");
			OutputStream out = response.getOutputStream();

			// workBook.write(out);
			com.csljc.common.OptUtil.writeXlsX(rs, out);
			System.out.println("excel完成！");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ds.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String select() {
		System.out.println("select====================================================");
	try {	
		com.piliskys.dataStore.DataStore ds = null;
		Map<String, Object[]> mp = ServletActionContext.getRequest()
				.getParameterMap();
		String selectName =  (mp.get("selectName")[0].toString());
	 if (selectName==null) return NONE;
	//	System.out.println("paraMap=" + paraMap);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		List outList ;
			ds = com.piliskys.dataStore.DataManager.getInstance("rmx");
			outList  = ds.selectBySQL( selectMap.get(selectName));
			response.getWriter().write(   JSONUtil.serialize(outList)   );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NONE;

	}
		static {
		 selectMap=new HashMap();
		<#list   map?keys as mykey>
   <#if map[mykey].addMap?exists>   
   <#assign st=map[mykey].addMap > 
<#if  st.type == 1>   
		 selectMap.put("${mykey}", "${st.data}");
	  </#if>
  </#if>
</#list>  
	}
}
