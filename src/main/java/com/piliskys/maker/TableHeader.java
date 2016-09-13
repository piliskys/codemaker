/*
 * Created on 2008-7-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.piliskys.maker;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TableHeader {

	private String text;  //显示值
	private String align;  //位置
    private int colspan=1; //占行
    private int rowspan=1; //占列
    private String font;   //字体
    private String selectAlias; 
    private String boder;
    
    
    public String toString(){
    	
    	StringBuffer sf=new StringBuffer();
    	
    	sf.append("{text:"+this.text
    			    +"}{selectAlias:"+this.selectAlias
    			    +"}{align:"+this.align
    			    +"}{colspan:"+this.colspan
    			    +"}{rowspan:"+this.rowspan
    			    +"}{font:"+this.font
    			    +"}{boder:"+this.boder+"}");
    	
    	return sf.toString();
    }
    private boolean isBorder =false;
	/**
	 * @return Returns the isBorder.
	 */
	public boolean isBorder() {
		return isBorder;
	}
	/**
	 * @param isBorder The isBorder to set.
	 */
	public void setBorder(boolean isBorder) {
		this.isBorder = isBorder;
	}
   // private 

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }
	public String getSelectAlias() {
		return selectAlias;
	}
	public void setSelectAlias(String selectAlias) {
		this.selectAlias = selectAlias;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getBoder() {
		return boder;
	}
	public void setBoder(String boder) {
		this.boder = boder;
	}
}

