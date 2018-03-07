package com.xinleju.platform.sys.logo.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.logo.dao.SysLogoDao;
import com.xinleju.platform.sys.logo.entity.SysLogo;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class SysLogoDaoImpl extends BaseDaoImpl<String,SysLogo> implements SysLogoDao{

	public SysLogoDaoImpl() {
		super();
	}

	
	
}
