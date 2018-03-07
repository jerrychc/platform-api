package com.xinleju.platform.generation.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.generation.entity.GenerationScheme;
import com.xinleju.platform.generation.entity.GenerationSystable;
import com.xinleju.platform.generation.entity.GenerationSystableColumn;


public interface GenSchemeService extends  BaseService <String,GenerationScheme>{

	
	/**
	 * 生成并下载File
	 * 
	 * @param g_systable
	 * @param l_column
	 * @param l_scheme
	 */
	public byte[] downloadFile(GenerationSystable g_systable,
			List<GenerationSystableColumn> l_column,
			List<GenerationScheme> l_scheme) throws Exception;
	
}
