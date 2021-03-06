package com.xinleju.platform.base.utils;

public enum SQLMapperName {
	
	
	
	INSERT("insert","save","新增"),UPDATE("update","update","新增"),GET("get","get","根据Id获取"),DELET("deleteById","deleteById","删除"),DELETBATCHBYID("deleteBatchByIds","deleteBatchByIds","批量删除"),PseudoDELET("deletePseudoObjectById","deletePseudoObjectById","删除"),PseudoDELETBATCHBYID("deletePseudoAllObjectByIds","deletePseudoAllObjectByIds","批量删除")
	,QUERYLIST("queryList","queryList","删除"),QUERYPAGELIST("queryPageList","queryPageList","分页查询"),QUERYCOUNT("queryCount","queryCount","获取总记录数"){
		
	};
	
	private String code;
	
	private String sqlId;
	
	private String name;
	
	private SQLMapperName(String code,String sqlId, String name) {
		this.code=code;
		this.name=name;
		this.sqlId=sqlId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSqlId() {
		return sqlId;
	}

	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}
	
	

	
}
