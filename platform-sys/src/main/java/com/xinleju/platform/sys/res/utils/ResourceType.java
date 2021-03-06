package com.xinleju.platform.sys.res.utils;

public enum ResourceType {
        APPSystem("系统", "APPSystem"),RESOURCE("资源", "RESOURCE"),OPERATION("功能控制点", "OPERATION"),DATACTRL("数据控制对象","DATACTRL"),DATAITEM("数据对象控制项","DATAITEM"),DATAPOINT("数据对象控制点","DATAPOINT");  
	    // 成员变量  
	    private String name;  
	    private String code;
	    
	    
		private ResourceType(String name, String code) {
			this.name = name;
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}  
	    
    
}
