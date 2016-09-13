package com.piliskys.imp;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.piliskys.db.ConnectionUtil;
import com.piliskys.db.DataManager;
import com.piliskys.db.DataStore;

public class Excel2Db {
	static String targ_folder = "D:\\tmp\\file\\zmn\\";
	static Map provinceMap =new  HashMap();
	static Map areaMap ,cityMap  ;
	static Map<String,Integer> otherAreaMap;
	static String provinceName="";
	static  int  provinceId;
	static DataStore  ds ;
	/**
	 * @param args
	 * @throws IOException 
	 * CREATE TABLE `tt_org` (
	`name` VARCHAR(150) NULL DEFAULT NULL,
	`address` VARCHAR(250) NULL DEFAULT NULL,
	`phone` VARCHAR(250) NULL DEFAULT NULL,
	`province` VARCHAR(250) NULL DEFAULT NULL,
	`city` VARCHAR(250) NULL DEFAULT NULL,
	`area` VARCHAR(250) NULL DEFAULT NULL,
	`province_id` INT(11) NULL DEFAULT NULL,
	`city_id` INT(11) NULL DEFAULT NULL,
	`area_id` INT(11) NULL DEFAULT NULL,
	`zuobiao` VARCHAR(150) NULL DEFAULT NULL
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;

	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ds =  DataManager.getInstance("rmxdb");
		
	 	List<Map> provList = ds.selectBySQL("select * from zmn_v_province"); 
		
		for(Map m:provList){
			provinceMap.put( m.get("province_name").toString().replace("省", "").replace("市", ""),m);	
			
		}
		 System.out.println("provinceMap" + provinceMap);		
 
		File aa = new File(targ_folder);
		File[] files = aa.listFiles();
		 
		for (File c : files) {

			String  sName=c.getName();
			if(sName.endsWith(".xlsx")){
				sName=sName.replace("~$", "");
		sName=sName.substring(0,sName.indexOf('.'));
		provinceName=sName;
		 System.out.println(sName+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" );	
	        provinceId= (Integer) ((Map) provinceMap.get(provinceName)).get("province_id");
	        
	        
	     
	         
	       
			
			XSSFWorkbook   xwb = new XSSFWorkbook(targ_folder+sName+".xlsx");   
			for(int i=0;i<xwb.getNumberOfSheets();i++){
				
				XSSFSheet xSheet = xwb.getSheetAt(i); 
			try{
				xlsx2db(xSheet);
			}catch(Exception e){
				System.out.println("error=="+provinceId+"---" + provinceName + "....."+xSheet.getSheetName());	
			e.printStackTrace();
			}
				
			}	
			}
		}
		
		
	}
	
	
	public static void xlsx2db(XSSFSheet xSheet
		 ) throws Exception {
 
		String sheetName=xSheet.getSheetName();
	//	System.out.println("===================：【" + sheetName + "】开始迁移...");
		Long startTime = System.currentTimeMillis();
		
		
		
	 
		Connection	oracon=	ConnectionUtil.currentConnection("rmxdb");
		PreparedStatement orapst = (PreparedStatement) oracon
				.prepareStatement("insert into tt_org  values (?,?,?,?,?,?,?,?,?,?)");
		int idx = 1, allidx = 0; // 计数器
		// ================================
             
		XSSFRow xRow = xSheet.getRow(idx);  
		int city_id=0,area_id=0;
	String provn=	(String) ((Map) provinceMap.get(provinceName)).get("province_name");
	
	if(provn.endsWith("市")){
		   List<Map> areaList = ds.selectBySQL("select t.*  from zmn_r_area t,zmn_r_city c where t.city_id =c.id" +
	        		" and c.province_id="+provinceId); 
	        areaMap = new HashMap();
	        for(Map m:areaList){
	        	areaMap.put( m.get("name").toString(),m);	
				
			}
	    //    System.out.println("areaMap===================：" + areaMap); 
		city_id =(Integer) ((Map) areaMap.get(sheetName)).get("city_id");
		area_id =(Integer) ((Map) areaMap.get(sheetName)).get("id");
		
	}else{
		 cityMap = new HashMap();
		 	List<Map> cityList = ds.selectBySQL("select * from zmn_r_city t where t.province_id="+provinceId); 	
		 	 for(Map m:cityList){
		 		cityMap.put( m.get("name").toString().replace("市", ""),m.get("id"));	
				 }
		 	city_id =(Integer)   cityMap.get(sheetName);    
		 
		 	otherAreaMap = new HashMap();
			 	List<Map> oareaList = ds.selectBySQL("select t.*  from zmn_r_area t  where t.city_id="+city_id); 	
			 	 for(Map m:oareaList){
			 		otherAreaMap.put( m.get("name").toString(), (Integer)m.get("id"));	
					 } 
		 	 
        
	
	 	 
		
	}	
		 
		while (xRow != null) {
			
		 
			idx++;
			allidx++;
		 
				// obj =null;
			if(xRow.getCell(0)!=null&&xRow.getCell(0).toString()!=null&&!xRow.getCell(0).toString().equals(""))
				{
				String hname=xRow.getCell(0).toString();
				String hname2="";
				if(xRow.getCell(1)!=null&&xRow.getCell(1).toString()!=null)
				  hname2=xRow.getCell(1).toString();
				if(!provn.endsWith("市"))	{
					area_id=0;
				for(String c:otherAreaMap.keySet()) {
					
					if(hname.indexOf(c)>0||hname2.indexOf(c)>0) {
						area_id=otherAreaMap.get(c);	
						break;
					}
					
				 
				}
                
				
				if(area_id==0) {
					area_id=otherAreaMap.get("市辖区");	
				System.out.println(provinceName+ "=======================市辖区=="+sheetName+"===="+hname);
				}
				}
			if(city_id==0||area_id==0){
				
				System.out.println(provinceName+ "=======================no dataFound=="+sheetName+"===="+hname);
			}	
				
				orapst.setString(1, hname);
				orapst.setString(2, hname2);
				 
				
				if(xRow.getCell(2)!=null&&xRow.getCell(2).toString()!=null)
					orapst.setString(3, xRow.getCell(2).toString());
					else
						orapst.setNull(3, 12);

				
				orapst.setString(4, provinceName);
				orapst.setString(5, sheetName);
				orapst.setString(6, "");
				orapst.setInt( 7,provinceId);
				orapst.setInt( 8,city_id);
				orapst.setInt( 9,area_id);
				
				if(xRow.getCell(3)!=null&&xRow.getCell(3).toString()!=null)
					orapst.setString(10, xRow.getCell(3).toString());
				else	
				orapst.setNull(10, 2);
 
		 
			// 把一个SQL命令加入命令列表
			orapst.addBatch();
			// 一定量批量提交
			if (idx > 500) {

				orapst.executeBatch();
				oracon.commit();
				  System.out.println("已插入500条数据");
				idx = 0;
			}
				}
		 
			xRow = xSheet.getRow(allidx+1);  
		}
		orapst.executeBatch();
		oracon.commit();
		Long endTime = System.currentTimeMillis();
		  System.out.println(sheetName+"             共插入【" + allidx + "】条数据，用时：" + (endTime -
		  startTime) + "毫秒");
		         
		orapst.close();

	}

}
