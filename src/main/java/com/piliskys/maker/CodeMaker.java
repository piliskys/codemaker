package com.piliskys.maker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import jxl.Sheet;
import jxl.Workbook;

public class CodeMaker {
	private static Logger log = Logger.getLogger(CodeMaker.class);
	static String folder = "";
	static String targ_folder = "D:\\workspace\\cctv\\1\\samyi\\src\\";
	static String sheetName = "user";
    static String actionName=sheetName.substring(0, 1).toUpperCase()+sheetName.substring(1);
	static Map<String,Map> gobleMap = new LinkedHashMap();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		 
		try {
			File directory = new File("");
			folder = directory.getCanonicalPath();// 获取标准的路径

			InputStream f = new FileInputStream(folder + "/config.xls");

			Workbook workbook = null;
			workbook = Workbook.getWorkbook(f);
			// 处理直接表etl
			// -----------------------------------------------------------------------
			Sheet sheet = workbook.getSheet(sheetName);
			TableHeader[][] a = ExcelHelper.getTab4Excel(sheet);
			
			Map	titieMap = new HashMap();
			titieMap.put("model", actionName);
			titieMap.put("sheetName", sheetName);
			titieMap.put("tabName", a[1][0].getText());
			titieMap.put("selectSql", a[1][1].getText().replace("\n", " "));
			titieMap.put("primary_id", a[1][4].getText().toLowerCase());
		if(!a[1][4].getText().equals(""))	{
			
			titieMap.put("update_flag", 1);
		}else
			titieMap.put("update_flag", 0);
   if(a[1][5].getText().toLowerCase().equals("y"))	{
			
			titieMap.put("export_flag", 1);
		}else
			titieMap.put("export_flag", 0);
			
			gobleMap.put("tabmap", titieMap);
			
			
			Map	paramMap = new LinkedHashMap();
			gobleMap.put("map", paramMap);
			for (int i = 3; i < a.length; i++) {
				  Map st =new HashMap();
				  paramMap.put(a[i][0].getText().toLowerCase(), st); 
			st.put("name", a[i][0].getText().toLowerCase())	;
			st.put("cnName", a[i][1].getText().toLowerCase())	;
			st.put("display", a[i][3].getText().toLowerCase())	;
			st.put("add", a[i][4].getText().toLowerCase())	;	
			
			 String text=a[i][4].getText();
             if (text.toLowerCase().startsWith("select")){
           	  Map stf =new HashMap();
      
          if(text.indexOf("[[")>0){
        	  stf.put("type", 0);    //静态数据
        	  stf.put("data", text.substring(text.indexOf("[[")));  
          } 	 else{
        	  stf.put("type", 1);    
        	  stf.put("data", text.substring(text.indexOf("[")+1,text.indexOf("]")));   
          }
          st.put("addMap", stf)	;	
			
			
			
			}
			
             st.put("search", a[i][5].getText().toLowerCase())	;	
       

			}	
			
			
			
			

			RepFile(new File(folder + "/template"), "");

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void RepFile(File file, String dir) throws IOException {
		
		File[] files = file.listFiles();
		//if(!dir.equals(""))
		
		{
			for (File c : files) {
				if (!c.getName().equals(".svn")){
				
			
				
				if (c.isDirectory()&&!c.getName().equals(".svn")) {
					
					//System.out.println(dir);
					File aa = new File(targ_folder + dir+ "/" + c.getName());
					if (!aa.exists())
						aa.mkdir();
					RepFile(c, dir+ "/" + c.getName());
				} else {

				  makerFile(c,dir);
				}

			}
			}
		 
		}
		

	}

	public static void makerFile(File file,String dir) throws IOException {

		System.out.println(dir);  

		Configuration freeMarkerCfg = new Configuration();
		freeMarkerCfg.setDefaultEncoding("utf-8");

		Template template = null;

		try {
			freeMarkerCfg.setDirectoryForTemplateLoading(file.getParentFile());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		freeMarkerCfg.setObjectWrapper(new DefaultObjectWrapper());
		try {
			template = freeMarkerCfg.getTemplate(file.getName());
			 
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
    String newFile ="";
      
      if(file.getName().endsWith(".java"))
    	  newFile=file.getName().replace("${model}", actionName);
      else 
    	  newFile=file.getName().replace("${model}", sheetName);
  	System.out.println(newFile);  
		File outFile = new File(targ_folder +dir +"/"+ newFile);
		Writer out = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outFile);

			OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");

			out = new BufferedWriter(oWriter);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			template.process(gobleMap, out);

			out.close();
			fos.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
