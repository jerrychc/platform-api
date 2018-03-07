package com.xinleju.platform.sys.num.utils;

public enum OutType {
	//是否输出
	RulerOut("是", "1"),RulerNotOut ("否", "0");  
	    // 成员变量  
	    private String name;  
	    private String code;
	    
	    
		private OutType(String name, String code) {
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
