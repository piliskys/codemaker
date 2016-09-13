package com.piliskys.gen;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.piliskys.db.DataManager;
import com.piliskys.db.DataStore;
import com.piliskys.db.exception.DAOException;



public class OraTable2Doc {
	
	
	    private String              fileName;
	    private String              BH="4";

	    private String              filePath;

	    //private String streamPath="";
	 //   private String              exportDoc     = "success";              //导出word是否成功


	    public static void main(String[] args) {
			// TODO Auto-generated method stub
			
	    	OraTable2Doc cd= new OraTable2Doc();
	    	cd.createDocFile();

		}

	    
	 public void createDocFile()
	    {
	        ByteArrayInputStream bais = null;
	        FileOutputStream ostream = null;
	        
	        fileName = "rmxdb" + ".doc";
	        try
	        {
	              filePath = "D:/ihs/" + fileName;

	            String content = getContent();
	            
	           System.out.println("content=\n"+content); 
	            byte b[] = content.getBytes();//用默认的字符集将content解码为字节序列
	            bais = new ByteArrayInputStream( b );//创建一个BAIS,使用b作为缓冲区数组
	            POIFSFileSystem fs = new POIFSFileSystem();//管理文件系统生命周期的什么东西
	            DirectoryEntry directory = fs.getRoot();//获得root entry，不知是什么
	            directory.createDocument( "WordDocument" , bais );//是一个约束目录对象的接口，这个方法是建立了一个新的DocumentEntry

	            ostream = new FileOutputStream( filePath );//向指定的文件写入数据
	            fs.writeFilesystem( ostream );//将文件系统输出到ostream这个流中
	            bais.close();
	            ostream.close();
	        }
	        catch( Exception e )
	        {
	            e.printStackTrace();
	        
	        }
	        finally
	        {
	            try
	            {
	                bais.close();
	                ostream.close();
	            }
	            catch( IOException e )
	            {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }

	    /**
	     * 拼出word文档的内容
	     * @return
	     * @throws SQLException 
	     * @throws DAOException 
	     */
	    public String getContent() throws Exception, DAOException
	    {
	    	//头定义
	    	StringBuffer temp = new StringBuffer();
		        temp.append( "<html>" );
		        temp.append( "<head>" );
		        temp.append( "<meta http-equiv='Content-Type' content='text/html; charset=gbk' />" );
		         temp.append("<style>  .title1{   font-size:24pt; align:left;  font-family:宋体; } "+
		        		 " title2{   font-size:22pt; align:left;  font-family:宋体; }"
		        		 +"table{   border-collapse:collapse;   border:1px solid #000000;   font-family:宋体;   font-size:10pt; }th{   align:left;height:30px;border:1px solid #000000; background-color:#c0c0c0;  } td{   height:25px;border:1px solid #000000; } .td1{   background-color:#FFFFFF; } .td2{   background-color:#FFFFFF;   font-size:13pt; } </style>");
		        temp.append( "</head>" );
		        temp.append( "<body>" );
	    	
	    	
	    	
	    	DataStore ds =     DataManager.getInstance("rmxdb248");
	   String 	 tabSql="select table_name  from user_all_tables t " +

 "   ORDER BY T.table_name";
	      
	     String lastGroup="";
	     String thisGroup="";
	     String tabName="";
	     String gdVar="";
	    List<Map> aa = ds.selectBySQL(tabSql);
	   for(Map tapM:aa){
	    tabName=tapM.get("table_name").toString();
	   
	      
	    String  comSql="select e.comments||' ' comments from user_tab_comments e  where e.table_name='"+tabName+"'";
	     
	    List<Map> comL=ds.selectBySQL(comSql);
	    thisGroup=tabName.substring(0, tabName.indexOf("_")+1);
	    gdVar =tabName.substring(tabName.indexOf("_")+1);
//	
	   
	  temp.append( "<h"+BH+"   align='left'>" );
      temp.append( tabName.toLowerCase() );
      temp.append( "</h"+BH+">" ); 
  
      String eef=comL.get(0).get("comments").toString();   
      byte[] bt=	eef.getBytes("ISO-8859-1");
            
	            temp.append( "<p   align='left'>" );
	            temp.append( "表名:");
	            temp.append( eef);
	           // temp.append( new String(bt,"GBK"));
	            temp.append( "</p>" );
	        temp.append( "<table width='550'  CELLPADDING=0 CELLSPACING=0  >" );
	        temp.append( "<tr>" );
	            temp.append( "<th width='40' >" + "序号" + "</th>" );
	            temp.append( "<th width='120' align='center'>" + "字段名称" + "</th>" );
	            temp.append( "<th width='150' align='center'>" + "字段描述" + "</th>" );
	            temp.append( "<th width='85' align='center'>" + "类型" + "</th>" );
	            temp.append( "<th width='45' align='center'>" + "为空" + "</th>" );
	            temp.append( "<th width='100' align='center'>" + "说明" + "</th>" );
	        temp.append( "</tr>" );

	        String colSql=" SELECT  T. * , DECODE(T.FF,'7',t.DATA_TYPE,t.DATA_TYPE||'('||T.FF||')'  )DATA_TYPE1  FROM (" +
	        		"select  t.column_id,t.COLUMN_NAME, f.comments||' ' comments ,t.DATA_TYPE, "+
" NVL2( TO_CHAR(T.DATA_PRECISION),  "+
" NVL2( DECODE(T.DATA_SCALE||'','0',NULL,T.DATA_SCALE||''), T.DATA_PRECISION||','||T.DATA_SCALE,T.DATA_PRECISION ||''), "+
  "          T.DATA_LENGTH||'' ) ff ,   NULLABLE||' ' NULLABLE , DATA_DEFAULT  "+
 "　from user_tab_columns t ,user_col_comments f"+
 " where t.TABLE_NAME ='"+tabName+"'"+
" and t.TABLE_NAME = f.table_name"+
" and f.column_name=t.COLUMN_NAME ORDER BY  T.COLUMN_ID) T";
       
	        List<Map> colL =ds.selectBySQL(colSql);
	        
	        for( Map colM:colL )
	        {
	            temp.append( "<tr>" );
	           
	            temp.append( "<td  align='center'>" + colM.get("column_id") + "</td>" );
	            temp.append( "<td>&nbsp;" + colM.get("column_name").toString().toLowerCase() + "</td>" );
	           
	            String eefe=colM.get("comments").toString();   
	            byte[] btf=	eefe.getBytes("ISO-8859-1");
	            temp.append( "<td>&nbsp;" + eefe + "</td>" );
	          //  temp.append( "<td>" + new String(btf,"GBK") + "&nbsp;</td>" );
	            temp.append( "<td>&nbsp;" + colM.get("data_type1").toString().toLowerCase()+ "</td>" );
	            temp.append( "<td align='center'>" + colM.get("nullable") + "</td>" );
	            temp.append( "<td>&nbsp;</td>" );
	            temp.append( "</tr>" );
	        }
	        temp.append( "</table>" );
	        
           String sqll="SELECT  g.INDEX_NAME||'('||nvl(wm_concat(G.COLUMN_NAME),'无') ggg FROM USER_IND_COLUMNS G  "
+"WHERE G.TABLE_NAME ='"+tabName+"' group  by g.INDEX_NAME";
      // System.out.println(sqll);
           List<Map> pklist =ds.selectBySQL(sqll); 
         for(Map c:pklist){
           temp.append( "<p   align='left'>" );
           temp.append( "索引：&nbsp;" + c.get("ggg").toString().toLowerCase() + ")" );
          // temp.append( new String(bt,"GBK"));
           temp.append( "</p>" );
         }
	        lastGroup =thisGroup;  
	        
	   }
	        
	        temp.append( "</body>" );
	        temp.append( "</html>" );
	        return temp.toString();
	    }

}
