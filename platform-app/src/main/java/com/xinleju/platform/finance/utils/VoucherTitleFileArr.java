package com.xinleju.platform.finance.utils;

public class VoucherTitleFileArr {
	//会计科目title
	public static String[] captionTitle = {"主键","编码","名称","辅助核算类型主键","辅助核算类型名称","上级","账套主键","业务系统数据主键","业务系统数据名称","排序"};
	//会计科目filesname
	public static String[] captionFiles = {"id","code","name","assIds","assNames","parentId","accountSetId","bizItemIds","bizItemNames","sort"};
	//现金流量title
	public static String[] cashFlowItemTitle = {"主键","编码","名称","上级","账套主键","科目编码","科目名称","节点等级"};
	//现金流量filesname
	public static String[] cashFlowItemFiles = {"id","code","name","parentId","accountSetId","subjectCodes","subjectNames","nodeLevel"};
	//辅助核算类型title
	public static String[] assTypeTitle = {"主键","核算名称","账套主键","公司主键","业务对象主键","业务对象名称","类型","是否同步","传输类型"};
	//辅助核算类型filesname
	public static String[] assTypeFiles = {"id","assName","accountSetId","companyId","bizObjectId","bizObjectName","type","synchro","isDirectCode"};
	//辅助核算明细title
	public static String[] assMappingTitle = {"主键","核算代码","核算名称","辅助核算类型主键","业务对象代码","业务对象名称","业务对象主键"};
	//辅助核算明细filesname
	public static String[] assMappingFiles = {"id","assItemCode","assItemName","assMappingId","objectItemCode","objectItemName","objectId"};
	//凭证模板类型title
	public static String[] templateTypeTitle = {"主键","业务类型名称","账套主键","公司主键","上级"};
	//凭证模板类型filesname
	public static String[] templateTypeFiles = {"id","name","accountSetId","companyId","parentId"};
	//凭证模板title
	public static String[] templateTitle = {"主键","模板类型主键","模板名","业务类型说明","业务对象主键","业务对象名称","状态","筛选条件","凭证字"};
	//凭证模板filesname
	public static String[] templateFiles = {"id","typeId","name","remark","bizObjectId","bizObjectName","status","filter","word"};
	//凭证模板分录title
	public static String[] templateEntryTitle = {"主键","辅助核算编码","辅助核算名称","会计科目主键","会计科目名称","借方金额表达式","贷方金额表达式","摘要","凭证模板主键","筛选条件","备注","现金流量编码","现金流量名称","现金流量主键","排序"};
	//凭证模板分录filesname
	public static String[] templateEntryFiles = {"id","assCode","assName","captionId","captionName","drmnyexpr","crmnyexpr","summary","voucherTemplateId","filter","remark","cashFlowCode","cashFlowName","cashFlowId","sort"};
}
