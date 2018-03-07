package com.xinleju.platform.generation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.generation.dao.GenSystableColumnDao;
import com.xinleju.platform.generation.entity.GenerationSystableColumn;
import com.xinleju.platform.generation.service.GenSystableColumnService;

@Service
public class GenSystableColumnServiceImpl extends  BaseServiceImpl<String,GenerationSystableColumn> implements GenSystableColumnService{
	

	@Autowired
	private GenSystableColumnDao genSystableColumnDao;
	

}
