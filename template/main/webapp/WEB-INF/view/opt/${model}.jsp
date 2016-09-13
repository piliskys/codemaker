<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>cctv</title>
<script type="text/javascript">
var  actionurl='${r"${ctx}"}/opt/${tabmap['sheetName']}';
var  export_flag=${tabmap['export_flag']};
var  update_flag =${tabmap['update_flag']};
var  vprimaryid ='${tabmap['primary_id']}';
//查询区
var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
<#list   map?keys as mykey>
   <#if map[mykey].addMap?exists>   
   <#assign st=map[mykey].addMap > 
<#if  st.type == 0>
   var   store_${mykey} = new Ext.data.SimpleStore({
   fields : ['value', 'text'],
   data : ${st.data}
});
   
  </#if>
<#if  st.type == 1>
  var store_${mykey} = new Ext.data.JsonStore({   
       proxy:new Ext.data.HttpProxy({         
                      url:actionurl+'!select.html?selectName=${mykey}'    
                       }),
       fields:['text','value']		
    });   
    store_${mykey}.load(); 
	
  </#if> 
 //增加的
   var i_${mykey} = Ext.create('Ext.form.field.ComboBox', {
        fieldLabel: '${map[mykey].cnName}',
        displayField: 'text',
        valueField : 'value',
        store: store_${mykey},
        queryMode: 'local',
        name:'${mykey}' 
    });  
<#--单个的查询下拉 -->
  <#if  map[mykey].search == 's'>
var q_${mykey} = Ext.create('Ext.form.field.ComboBox', {
        fieldLabel: '${map[mykey].cnName}',
        displayField: 'text',
        valueField : 'value',
        store: store_${mykey},
        queryMode: 'local',
        name:'${mykey}',
        colspan:1
    });
</#if>
<#--个的查询下拉 -->
  <#if  map[mykey].search == 'm'>
var qb_${mykey} = Ext.create('Ext.form.field.ComboBox', {
        fieldLabel: '${map[mykey].cnName}起',
        displayField: 'text',
        valueField : 'value',
        store: store_${mykey},
        queryMode: 'local',
        name:'s_${mykey}'
    });
var qe_${mykey} = Ext.create('Ext.form.field.ComboBox', {
        fieldLabel: '${map[mykey].cnName}止',
        displayField: 'text',
        valueField : 'value',
        store: store_${mykey},
        queryMode: 'local',
        name:'e_${mykey}'
    });	
</#if>
  </#if>
</#list>
 <#assign aa_idx = 0> 
var queryitem=[
 <#list   map?keys as mykey>
  <#if map[mykey].search == 's' || map[mykey].search == 'm'>
	<#if aa_idx==0><#else>,</#if>
	<#assign aa_idx = aa_idx + 1>
	</#if>
   <#if map[mykey].addMap?exists>  
    <#if map[mykey].search == 's'>
		 q_${mykey}
</#if>
 <#if map[mykey].search == 'm'>
		 qb_${mykey},qe_${mykey}
</#if>
<#else>  
   <#if map[mykey].search == 's'>
		 {
            fieldLabel: '${map[mykey].cnName}',
            name: '${mykey}'
        }
</#if>
 <#if map[mykey].search == 'm'>
		 {
            fieldLabel: '${map[mykey].cnName}起',
            name: 'b_${mykey}'
        },{
            fieldLabel: '${map[mykey].cnName}止',
            name: 'e_${mykey}'  
        }
</#if>
</#if>				 
</#list>];
	
//结果内容        
   var  optmodel= [
 <#list   map?keys as mykey>
 {name: '${mykey}'}<#if mykey_has_next>,</#if>   
 </#list>
 ];
 <#assign aa_idx = 0>   
  var listcolumn= [ 
  <#list   map?keys as mykey>
  <#if map[mykey].display !='n' >
  <#if aa_idx==0><#else>,</#if>{text: "${map[mykey].cnName}",   dataIndex: '${mykey}'}  
  <#assign aa_idx = aa_idx + 1>
  </#if> 
 </#list>
 ];
 
  <#assign aa_idx = 0>  
   var add_items=[
    <#list   map?keys as mykey>
  <#if map[mykey].add !='none' >
    <#if map[mykey].addMap?exists>  
	<#if aa_idx==0><#else>,</#if>i_${mykey}
    <#else> 
  <#if aa_idx==0><#else>,</#if> {fieldLabel: '${map[mykey].cnName}',  name:'${mykey}'}
  </#if>
<#else>
<#if aa_idx==0><#else>,</#if>{xtype:'hidden',    name:'${mykey}'  }
 </#if>
<#assign aa_idx = aa_idx + 1>  
 </#list>
 ]; 
                  
</script>
<script src="${r"${ctx}"}/scripts/opt.js"></script>
<body></body>
</html>
