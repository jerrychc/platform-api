package com.xinleju.cloud.oa.sys.controller.util;

public enum LocationFlag {
	 
     TOP(1),LAST(2),NEXT(3),BOTTOM(4);
    private int index;

	private LocationFlag(int index) {
		this.index = index;
	}

    public static LocationFlag getLocation(String in) {  
        for (LocationFlag c : LocationFlag.values()) {  
            if (c.getIndex() == Integer.valueOf(in)) {  
                return c;  
            }  
        }
        return null;
    }

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	

}
