package com.xinleju.platform.finance.utils;

public enum IsDirectCode {
	ASSCODE("核算代码", "1"),ASSNAME("核算名称", "2"),OBJCODE("业务对象代码", "3"),OBJNAME ("业务对象名称", "4"),DEFAULT("默认设置", "5");  
	    // 成员变量  
	    private String name;  
	    private String code;
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
		private IsDirectCode(String name, String code) {
			this.name = name;
			this.code = code;
		}
	
	    
}
