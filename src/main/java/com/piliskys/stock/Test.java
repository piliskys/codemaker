package com.piliskys.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.piliskys.db.DataManager;
import com.piliskys.db.DataStore;
import com.piliskys.db.exception.DAOException;

public class Test {

	/**
	 * @param args
	 * 
	 * -- Create table
create table TB_STOCK_DETAIL
(
  sdate    DATE,
  sopen    NUMBER(9,2),
  shigh    NUMBER(9,2),
  slow     NUMBER(9,2),
  sclose   NUMBER(9,2),
  volume   NUMBER(19),
  sadclose NUMBER(9,2),
  scode    VARCHAR2(10)
)
tablespace BAS_BD_DATA_TBS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate indexes 
create index IDX_STOCKDETAIL_CODE on TB_STOCK_DETAIL (SCODE)
  tablespace BAS_BD_DATA_TBS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
create index IDX_STOCKDETAIL_DATE on TB_STOCK_DETAIL (SDATE)
  tablespace BAS_BD_DATA_TBS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

	 * 
	 * 
	 */
	static DataStore ds =    null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		 
		try {
			ds =     DataManager.getInstance("samyi");
			
			List<Map> out = ds.selectBySQL("select t.code, " +
					"  to_char(t.end_date+1,'yyyy-mm-dd')  startdate ," +
					" to_char(sysdate-1,'yyyy-mm-dd') enddate from tb_stock_main t" +
					" where t.end_date<trunc(sysdate)-1 --order by t.code");
			
			for( Map c:out) {
				
				try{
				get_stockData(c);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
       
		 
		
	}
	
	
	
	
	public static void get_stockData(Map<String,String> stockM) throws Exception {
		// TODO Auto-generated method stub
		
		
		
		System.out.println(stockM.get("code"));
		GetDataFromYahooUtil stockUtil = new GetDataFromYahooUtil();
		List<StockData> dlist = stockUtil.getStockCsvData(stockM.get("code"), stockM.get("startdate"),stockM.get("enddate"));
	 
		System.out.println(dlist.size());
		
		
		List parL= new ArrayList();
		for(StockData s:dlist){
			parL.clear();
			parL.add(s.getDate());
			parL.add(s.getOpen());
			parL.add(s.getHigh());
			parL.add(s.getLow());
			parL.add(s.getClose());
			parL.add(s.getVolume());
			parL.add(s.getAdj());
			parL.add(stockM.get("code"));
			ds.sqlExcute("insert into TB_STOCK_DETAIL values(to_date(?,'yyyy-mm-dd'),?,?,?,?,?,?,?) ", parL);
		}
		
		ds.sqlExcute("update tb_stock_main t set t.end_date=to_date('"+stockM.get("enddate")+"','yyyy-mm-dd')" +
				" where t.code='"+stockM.get("code")+"'");
		ds.commit();
	}

}
