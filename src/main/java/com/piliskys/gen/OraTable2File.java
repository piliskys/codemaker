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

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.piliskys.db.ConnectionUtil;
import com.piliskys.db.DataManager;
import com.piliskys.db.DataStore;

 

import oracle.jdbc.OracleTypes;



import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class OraTable2File {

	/**
	 * @param args
	 * @throws Exception
	 */
	 
	    private static String              filePath;	
	public static Map<String, String> colMent = new HashMap();
 
	public static Map<String, Map> parMap = new LinkedHashMap();
	public static void main(String[] args) throws Exception {
		 
 
       
        
        // TODO Auto-generated method stub
		 

		DataStore ds = DataManager.getInstance("rmxo");
		Connection con = null;
		con= ConnectionUtil.currentConnection("rmxo");
		CallableStatement cstmt = null;
		  String 	 tabSql="select table_name ,DECODE(T.temporary,'Y','global temporary',' ') tempa, DECODE(T.temporary,'Y','on commit delete rows',' ') tempb  from user_all_tables t " +

		 "   ORDER BY T.table_name";
			      
		
			    List<Map> aa = ds.selectBySQL(tabSql);
			   
			   for(Map tapM:aa){
			String    tabName=tapM.get("table_name").toString();
			   
			 parMap.put(tabName, tapM);     
			    String  comSql="select nvl(trim(e.comments),' ') comments, to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') timess from user_tab_comments e  where e.table_name='"+tabName+"'";
			     
			    List<Map> comL=ds.selectBySQL(comSql);
			    tapM.put("comments", comL.get(0).get("comments"));
			    tapM.put("timess", comL.get(0).get("timess")); 
			        String colSql=" SELECT  T. * , DECODE(T.FF,'7',t.DATA_TYPE,t.DATA_TYPE||'('||T.FF||')'  )DATA_TYPE1  " +
			        		"  , rpad (t.COLUMN_NAME, 33,' ') newcolname  FROM (" +
			        		"select  t.column_id,t.COLUMN_NAME, nvl(trim(f.comments),' ') comments ,t.DATA_TYPE,   data_default,"+
		" NVL2( TO_CHAR(T.DATA_PRECISION),  "+
		" NVL2( DECODE(T.DATA_SCALE||'','0',NULL,T.DATA_SCALE||''), T.DATA_PRECISION||','||T.DATA_SCALE,T.DATA_PRECISION ||''), "+
		  "          T.DATA_LENGTH||'' ) ff ,   NULLABLE||' ' NULLABLE   "+
		 "　from user_tab_columns t ,user_col_comments f"+
		 " where t.TABLE_NAME ='"+tabName+"'"+
		" and t.TABLE_NAME = f.table_name"+
		" and f.column_name=t.COLUMN_NAME ORDER BY  T.COLUMN_ID) T";
		       
			        List<Map> colL =ds.selectBySQL(colSql);
			//  System.out.println(colSql)   ;   
			       for(Map c:colL){
			    	   String[] coa=c.get("comments").toString().split("--");
			    	   
			    	  c.put("newcomments", coa[0]);
			    	  c.put("newdesc", "");
			    	  if ( coa.length>1)    c.put("newdesc", coa[1]);
			       } 
			        
			        
			        tapM.put("collist", colL);
			 
			     //分区
			        String sqll="select 'partition by range('||b.column_name||')'||chr(10)||'interval('||a.interval||')'||chr(10)|| "+
" '(partition p1 values less than(0))'  part"+
 " from user_part_tables a,user_part_key_columns b "+
" where a.table_name=b.name and a.table_name='"+tabName+"'";
			        		  
			        		           List<Map> partlist =ds.selectBySQL(sqll); 
			        		           tapM.put("partition", partlist.size()>0? partlist.get(0).get("part"):"");
			        
			        
		             sqll="select 'create '||decode(f.uniqueness,'NONUNIQUE','',f.uniqueness)||' index '||f.index_name||' on '||f.table_name||' ('||LISTAGG(t.COLUMN_NAME,',') "+ 
		            		 " WITHIN GROUP(order by t.COLUMN_POSITION)||')   '||decode(f.partitioned,'YES','local','')||';' indxx "+
"  from user_ind_columns  t , user_indexes f "+
  " where t.INDEX_NAME=f.index_name "+
" and t.TABLE_NAME=f.table_name and f.table_name= '"+tabName+"'"+
" group by f.table_name, f.index_name,f.partitioned,f.uniqueness ";
		  
		           List<Map> pklist =ds.selectBySQL(sqll); 
		          if(pklist.size()>0)
		           tapM.put("indList", pklist);
			        
			   }
		
		OraTable2File  sw =new OraTable2File();
		Map ccc = new HashMap();
		ccc.put("tabmap", parMap);
		sw.outFile(ccc, "D:\\lqb\\rm\\DB\\表结构.doc","table.html");
		
		for(String m:parMap.keySet())
			{ccc = new HashMap();
				ccc.put("tabmap", parMap.get(m));
			sw.outFile(ccc, "D:\\lqb\\rm\\DB\\table\\"+m.toLowerCase()+".sql","sqltable.html");
			}

}
	
	
	 
	
	public   void outFile(Map dataMap ,String fileName,String templateStr){
		
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
		                 template = freeMarkerCfg.getTemplate(templateStr);  
		                
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
 
	
	
	}
