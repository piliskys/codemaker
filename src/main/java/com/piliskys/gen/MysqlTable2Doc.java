package com.piliskys.gen;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.piliskys.db.DataManager;
import com.piliskys.db.DataStore;
import com.piliskys.db.exception.DAOException;


public class MysqlTable2Doc {
	
	
	    private String              fileName;
	    private String              BH="4";

	    private String              filePath;

	    //private String streamPath="";
	 //   private String              exportDoc     = "success";              //导出word是否成功


	    public static void main(String[] args) {
			// TODO Auto-generated method stub
			
	    	MysqlTable2Doc cd= new MysqlTable2Doc();
	    	cd.createDocFile();

		}

	    
	 public void createDocFile()
	    {
	        ByteArrayInputStream bais = null;
	        FileOutputStream ostream = null;
	        
	        fileName = "svaccine" + ".doc";
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
	    	
	    	
	    	
	    	DataStore ds =     DataManager.getInstance("zmn");
	   String 	 tabSql="show tables " ;
	      
	     String lastGroup="";
	     String thisGroup="";
	     String tabName="";
	     String gdVar="";
	    List<Map> aa = ds.selectBySQL(tabSql);
	   for(Map tapM:aa){
	    tabName=tapM.get("table_name").toString();
	   
	      
	    String  comSql="select e.table_comment    from information_schema.tables e  where table_schema = 'svaccine'  and e.table_name='"+tabName+"'";
	     
	    List<Map> comL=ds.selectBySQL(comSql);
	    thisGroup=tabName.substring(0, tabName.indexOf("_")+1);
	    gdVar =tabName.substring(tabName.indexOf("_")+1);
//	
	   
	  temp.append( "<h"+BH+"   align='left'>" );
      temp.append( tabName.toLowerCase() );
      temp.append( "</h"+BH+">" ); 
  
      String eef=comL.get(0).get("table_comment").toString();   
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

	        String colSql=" select ordinal_position,e.column_name,column_type  , " +
	        		" is_nullable  , column_comment " +
	        		"from information_schema.columns e " +
	        		"" +
	        		" where table_schema = 'svaccine'  and e.table_name='"+tabName+"'" ;
       
	       // System.out.println(colSql);  
	        
	        List<Map> colL =ds.selectBySQL(colSql);
	       // System.out.println(colL);
	        for( Map colM:colL )
	        {
	            temp.append( "<tr>" );
	           
	            temp.append( "<td  align='center'>" + colM.get("ordinal_position") + "</td>" );
	            temp.append( "<td>&nbsp;" + colM.get("column_name").toString().toLowerCase() + "</td>" );
	           
	            String eefe=colM.get("column_comment").toString();   
	          //  byte[] btf=	eefe.getBytes("ISO-8859-1");
	            temp.append( "<td>&nbsp;" + eefe + "</td>" );
	          //  temp.append( "<td>" + new String(btf,"GBK") + "&nbsp;</td>" );
	          
	            
	            
	            temp.append( "<td>&nbsp;" + colM.get("column_type").toString().toLowerCase()+ "</td>" );
	            temp.append( "<td align='center'>" + colM.get("is_nullable") + "</td>" );
	            temp.append( "<td>&nbsp;</td>" );
	            temp.append( "</tr>" );
	        }
	        temp.append( "</table>" );
	        
       
	        lastGroup =thisGroup;  
	        
	   }
	        
	        temp.append( "</body>" );
	        temp.append( "</html>" );
	        return temp.toString();
	    }

}
