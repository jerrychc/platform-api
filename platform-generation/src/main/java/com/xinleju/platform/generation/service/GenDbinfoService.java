package com.xinleju.platform.generation.service;

import java.util.List;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.generation.entity.GenerationDbinfo;
import com.xinleju.platform.generation.entity.GenerationScheme;
import com.xinleju.platform.generation.entity.GenerationSystable;
import com.xinleju.platform.generation.entity.GenerationSystableColumn;


public interface GenDbinfoService extends  BaseService <String,GenerationDbinfo>{

	public void SaveTable(GenerationSystable g_systable,List<GenerationSystableColumn> l_column,List<GenerationScheme> l_scheme);
}
