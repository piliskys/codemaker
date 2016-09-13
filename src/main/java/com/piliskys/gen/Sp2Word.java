package com.piliskys.gen;

import java.io.BufferedWriter;
 
import java.io.File;
 
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
 
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
 
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import com.piliskys.db.ConnectionUtil;
import com.piliskys.db.DataManager;
import com.piliskys.db.DataStore;

 

import oracle.jdbc.OracleTypes;



import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Sp2Word {

	/**
	 * @param args
	 * @throws Exception
	 */
	 
	    private static String              filePath;	
	public static Map<String, String> colMent = new HashMap();
 
	public static Map<String, Map> parMap = new LinkedHashMap();
	public static void main(String[] args) throws Exception {
		 
 
       
        
        // TODO Auto-generated method stub
		 

		DataStore ds = DataManager.getInstance("ihs92");
		Connection con = null;
		con= ConnectionUtil.currentConnection("ihs92");
		CallableStatement cstmt = null;
		String prtStr="";
		String tabSql = "select distinct g.column_name,g.comments from user_col_comments g"
				+

				"  where g.comments is not null";

		List<Map> out = ds.selectBySQL(tabSql);
		String spExecStr="";
		for (Map m : out) {
			

			colMent.put(m.get("column_name").toString(), m.get("comments")
					.toString());
		}
		String amgStr = "";
		String sql = "select  g.object_type," +
				"row_number() over (partition by object_type order by object_name ,g.SUBPROGRAM_ID) rn, " +
				" nvl(g.PROCEDURE_NAME,g.object_name) objectname, nvl2(g.PROCEDURE_NAME,g.object_name,null) pkgname  from user_procedures g   "  +
			//	"  where g.object_name= 'SP_GET_ODDS_HISTORY' " +
				"    order by object_name ,g.SUBPROGRAM_ID";
		out = ds.selectBySQL(sql);
		int isexeSp=0;
		//得到存储过程列表
		for (Map m : out) {
			
			 
			isexeSp=0;
			String spName = m.get("objectname").toString();
			
		//if (spName.equals("sp_get_matchpool_list".toUpperCase())) continue;	

			parMap.put(spName, m);
			
           try{
        	Map<String,List> cctvM= new HashMap();   
        	   
			amgStr = "select g.OBJECT_NAME,g.POSITION,g.DATA_TYPE,g.in_out ,g.ARGUMENT_NAME,g.type_name from user_arguments g "
					+ " where g.OBJECT_NAME='" + spName + "'  and ( g.PACKAGE_NAME is null or g.PACKAGE_NAME='"+m.get("pkgname")+"') and g.ARGUMENT_NAME is not null order by g.POSITION ";

			List<Map> argout = ds.selectBySQL(amgStr);
			
		//System.out.println(argout);
		if(argout.size()>0)
			parMap.get(spName).put("param", argout);
			
			String spStr="";
			
			if(m.get("pkgname")!=null)
				spStr=m.get("pkgname")+"."+spName;
			else  spStr= spName;
				
			spExecStr="{call "+spStr+" (?";
			// 存储过程参数
			for (Map arg : argout) {
				
				
				arg.put("desc", colMent.get(arg.get("argument_name").toString().substring(3)));
				
				if(arg.get("POSITION".toLowerCase()).toString().equals("1"))
					 
				{}
					 	
				else  {  
				spExecStr+=",?";
				}
				
				 
				String argType= arg.get("DATA_TYPE".toLowerCase()).toString();
			 
			if(argType.equals("REF CURSOR"))	{
			 
				isexeSp=1;
				cctvM.put(arg.get("argument_name").toString(), new ArrayList());
				
				arg.put("curMap", cctvM.get(arg.get("argument_name")));
			} 
			//sf.append( "\n");		
			}
			spExecStr+=")}"; 
			
		//调用存储过程
			if(isexeSp==1){
				cstmt = con.prepareCall(spExecStr);	
			       int idx=0;
			     Map<Integer,String>  ourMap = new HashMap();  
				for (Map arg : argout) {
					idx++;
					if( arg.get("in_out".toLowerCase()).toString().equals("IN")){
						
						if( arg.get("DATA_TYPE".toLowerCase()).toString().equals("TABLE"))
						{
							oracle.sql.ArrayDescriptor des_USERSEQID_ARRAY =
                                    oracle.sql.ArrayDescriptor.createDescriptor(arg.get("type_name").toString(),con);
							  java.sql.Array js=null;
                            oracle.sql.ARRAY ora_array1 = new oracle.sql.ARRAY(des_USERSEQID_ARRAY, con,js);
                            
                            cstmt.setArray(idx, ora_array1);	
							
							
						}
						else
						cstmt.setInt(idx, 0);
					
						
					}else{
						
						if( arg.get("DATA_TYPE".toLowerCase()).toString().equals("NUMBER")){
							
							cstmt.registerOutParameter(idx, OracleTypes.INTEGER);
							
						}else{	
							cstmt.registerOutParameter(idx, OracleTypes.CURSOR);
							ourMap.put(idx, arg.get("ARGUMENT_NAME".toLowerCase()).toString());	
							
						}
						
						
					}
					
				}
				  prtStr="";
				cstmt.execute();
				for(Integer ii:ourMap.keySet()) {
					
				ResultSet	rs = (ResultSet) cstmt.getObject(ii);
				ResultSetMetaData rsmd = rs.getMetaData();
				StringBuffer oucrStr= new StringBuffer();
				int colnum = rsmd.getColumnCount();
				
				for(int k=0;k<colnum;k++) {
				Map rowM = new HashMap();
				String cname=rsmd.getColumnName(k + 1);
		// System.out.println(toCamelCase(cname))	 ; 
				rowM.put("name", toCamelCase(cname));
		//	System.out.println(rsmd.getPrecision(k+1))	 ; 
				int ale=rsmd.getPrecision(k + 1);
				rowM.put("type", rsmd.getColumnTypeName(k + 1)+(ale>0?"("+ale+")":"")
						);
				rowM.put("desc", colMent.get(rsmd.getColumnName(k + 1)));
				cctvM.get(ourMap.get(ii)).add(rowM);
				 
				}
			 	 
				
				} 
				
			
			
			
		}	
			
			amgStr="select g.referenced_name table_name,e.comments ,g.referenced_type from user_dependencies g left join user_tab_comments e " +
  
" on  g.referenced_name=e.table_name" +
" where g.name='"+spName+"'  and g.referenced_type in ('TABLE','FUNCTION','PROCEDURE','SEQUENCE' )" +
		"  and g.referenced_name not like 'BIN$%'";	
			
			List<Map> tab = ds.selectBySQL(amgStr);
			if(tab.size()>0)
			parMap.get(spName).put("reftab", tab);
			
	//处理过程
			if(m.get("pkgname")!=null)
				spStr=m.get("pkgname").toString();
			else  spStr= spName;
			
			amgStr="select g.text from user_source g" +
					" where g.name='"+spStr+"' and g.type!='PACKAGE' " +
							"  order by g.line";	
								
								List<Map> exelist = ds.selectBySQL(amgStr);
								
				int flag=0;  String str=""; String brstr=""; String br="";
				List<Map>  newExeList =new  ArrayList();
								for(Map c:exelist)	{
									str= c.get("text").toString();
						 if(str.toUpperCase().indexOf("PROCEDURE "+spName)>=0||str.toUpperCase().indexOf("FUNCTION "+spName)>=0)  flag=1;
						 if(flag==1&&str.toUpperCase().indexOf("BEGIN")>=0)  flag=2;	 
						 if(flag==2&&str.trim().startsWith("--")&&!str.trim().startsWith("---")) 
							 {
							 
							 
							 brstr=brstr+br+str.trim().substring(2);
							 br="<br>";
							 }else{
								if(!brstr.equals("")) {
									c.put("text", brstr);
									newExeList.add(c);
								}
								 
								 brstr="";
								 br="";
							 }
						 
						 if(flag==2&&(str.toUpperCase().indexOf("PROCEDURE ")>=0||str.toUpperCase().indexOf("FUNCTION ")>=0)) 
							 break;
 
					 }		
								
								
								if(newExeList.size()>0)
								parMap.get(spName).put("exelist", newExeList);
			
		}catch(Exception e){
			System.out.println("spName"+spName);			
		e.printStackTrace();	
		}
 
		 
		}
		
		Sp2Word  sw =new Sp2Word();
		Map aa = new HashMap();
		aa.put("map", parMap);
		sw.outFile(aa, "D:\\lqb\\rm\\DB\\数据库程序1.doc");

}
	
	
	 
	
	public   void outFile(Map dataMap ,String fileName){
		
		 Configuration freeMarkerCfg = new Configuration();  
		 freeMarkerCfg.setDefaultEncoding("utf-8");  

		    Template template = null;  
		    
		    try {
				freeMarkerCfg.setDirectoryForTemplateLoading(new File("D:\\html\\bootstrap\\template"));
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}  

		           freeMarkerCfg.setObjectWrapper(new DefaultObjectWrapper());  
		            try {  
		                 template = freeMarkerCfg.getTemplate("sp.html");  
		                
		            } catch (IOException e) {  
		                 // TODO   
		                 e.printStackTrace();  
		             } 
		            
		            File outFile = new File(fileName);  
		            Writer out = null;  
		            FileOutputStream fos=null;  
		            try {  
		                fos = new FileOutputStream(outFile);  
		                
		                OutputStreamWriter oWriter = new OutputStreamWriter(fos,"UTF-8");  
		               
		                //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));   
		                 out = new BufferedWriter(oWriter);   
		            } catch ( Exception e1) {  
		                e1.printStackTrace();  
		            }  
		               
		            try {  
		            	template.process(dataMap, out);  
		    
		                out.close();  
		                fos.close();  
		            } catch (TemplateException e) {  
		                e.printStackTrace();  
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            }  

    

	}
	static String toCamelCase(String s){
		 String[] parts = s.split("_");
		 String camelCaseString = "";
		 int idx=0;
		 for (String part : parts){
			 if(part.equals("ID")||part.equals("NO")) 
				 camelCaseString = camelCaseString +"_"+ part;	
			 else
			 if(idx==0)
		  camelCaseString = camelCaseString + part.toLowerCase();
			 else
				 camelCaseString = camelCaseString +"_"+ toProperCase(part);		 
		  idx++;
		 }
		 return camelCaseString;
		}
	static String toProperCase(String s) {
		 return s.substring(0, 1).toUpperCase() +
		    s.substring(1).toLowerCase();
		}




	
	
	}
