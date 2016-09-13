/*
 * Created on 2008-7-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.piliskys.maker;


import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * excel 解析类
 */
public class ExcelHelper {
	private static int sheetNo = 1;
	/**
	 * 得到sheetname
	 * @param in
	 * @return
	 * @throws Exception
	 */
	
	public static  String[]  getSheetNames(Workbook in) throws  Exception{
		 //Workbook workbook = Workbook.getWorkbook(in);
		 return	 in.getSheetNames();
		 
	}
	/**
	 * 得到行高
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	public static int[] getHeight(Sheet sheet) throws Exception {
		int row=sheet.getRows();
		
		int[] heig=new int[row];
		for(int i=0;i<row;i++)
			heig[i]=sheet.getRowHeight(i);
		return heig;
	}
	/**
	 * 得到列宽
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	public static int[] getWidth(Sheet sheet) throws Exception {
		int col=sheet.getColumns();
		
		int[] whth=new int[col];
		for(int i=0;i<col;i++)
			whth[i]=sheet.getColumnWidth(i);
		return whth;
	}
	 public static TableHeader[][] getTab4Excel(Sheet sheet) throws Exception {
        // 计算合并的单元格
        Range[] mergedCells = sheet.getMergedCells();
        
        // 合并单元格的坐标存为二维数组
        int[][] mergedMatrix = new int[mergedCells.length][4];
      
        for (int i = 0; i < mergedCells.length; i++) {
            Cell topLeft = mergedCells[i].getTopLeft();
            Cell bottomRight = mergedCells[i].getBottomRight();
            mergedMatrix[i][0] = topLeft.getColumn();
            mergedMatrix[i][1] = topLeft.getRow();
            mergedMatrix[i][2] = bottomRight.getColumn();
            mergedMatrix[i][3] = bottomRight.getRow();
          //  System.out.println(topLeft.getColumn() + ", " + topLeft.getRow() + ", " + bottomRight.getColumn() + ", " + bottomRight.getRow());
        }
      //  System.out.println("=============");
        TableHeader[][] headers = new TableHeader[sheet.getRows()][sheet.getColumns()];
        
        for (int i = 0; i < sheet.getRows(); i++) {
        	int flagnull=0;
            for (int j = 0; j < sheet.getColumns(); j++) {
                TableHeader header = null;
        //        System.out.println("j = " + j + ", i = " + i);
                boolean boo = true;
                boolean isBorder =false;
                String boderS="";
                String align="";
                // 判断单元格是否在合并的区域内
                for (int m = 0; m < mergedMatrix.length; m++) {
          //          System.out.println(mergedMatrix[m][0] + ", " + mergedMatrix[m][1] + ", " + mergedMatrix[m][2] + ", " + mergedMatrix[m][3]);
                    if (j == mergedMatrix[m][0] && i == mergedMatrix[m][1]) {
                        //位于矩阵开始格
                        Cell cell = sheet.getCell(j, i);
            //            System.out.println("位于矩阵开始格: " + cell.getContents());
                        header = new TableHeader();
                        try{ 
                        	isBorder=cell.getCellFormat().hasBorders() ; 
                        	boderS= cell.getCellFormat().getBorder(Border.TOP).getValue()+","+	cell.getCellFormat().getBorder(Border.BOTTOM).getValue()
                        	   +","+cell.getCellFormat().getBorder(Border.LEFT).getValue()+","+cell.getCellFormat().getBorder(Border.RIGHT).getValue();

                        	align = cell.getCellFormat().getAlignment().getValue()+"";
                        	}catch(Exception e){}
                        if(flagnull==0&&(cell.getContents()==null||cell.getContents().equals(""))&&!isBorder)
                        {
                        	header=null;
                     
                   //     System.out.println(i+"========"+j+"=");	
                        }else{       
                        	flagnull++;
                        	header.setText(cell.getContents());
                        header.setColspan(1 + mergedMatrix[m][2] - mergedMatrix[m][0]);
                        header.setRowspan(1 + mergedMatrix[m][3] - mergedMatrix[m][1]);
                        header.setBoder(boderS);
                        header.setBorder(isBorder);
                        header.setAlign(align);
                        boo = false;
                        flagnull++;
                        }break;
                    
                    } else if (j >= mergedMatrix[m][0] && i >= mergedMatrix[m][1] && j <= mergedMatrix[m][2] && i <= mergedMatrix[m][3]) {
                        //位于被合并的单元格
                       // System.out.println("被合并的单元格");
                        //TableHeader header = null;
                        boo = false;
                        flagnull++;
                        break;
                    }
                }
                if(boo) {
                //不相关的单元格
                        Cell cell = sheet.getCell(j, i);
                  //      System.out.println("不相关的单元格: " + cell.getContents() + ", " + i + ", " + j);
                      {       
                    	  
                        	flagnull++;
                        	try{   
                        		boderS= cell.getCellFormat().getBorder(Border.TOP).getValue()+","+	cell.getCellFormat().getBorder(Border.BOTTOM).getValue()
                         	   +","+cell.getCellFormat().getBorder(Border.LEFT).getValue()+","+cell.getCellFormat().getBorder(Border.RIGHT).getValue();
                        		align = cell.getCellFormat().getAlignment().getValue()+"";
                            	isBorder=cell.getCellFormat().hasBorders() ; 
                            	}catch(Exception e){}
                        	header = new TableHeader();
                        header.setText(cell.getContents());
                        header.setColspan(1);
                        header.setRowspan(1);
                        header.setBoder(boderS);
                        header.setAlign(align);
                        header.setBorder(isBorder);
                        if((cell.getCellFormat()==null||!cell.getCellFormat().hasBorders())&&(cell.getContents()==null||cell.getContents().equals("")))
                        header.setText("");
                        
                        }
                }
                headers[i][j] = header;
             //   if (headers[i][j]!=null)		System.out.println(i+":"+j+"=="+headers[i][j])	;    
            }
            
            
        }
       
        
        return headers;
    }    //private boolean in

}
