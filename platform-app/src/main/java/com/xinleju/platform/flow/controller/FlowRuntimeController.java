package com.xinleju.platform.flow.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.flow.dto.service.FlDtoServiceCustomer;
import com.xinleju.platform.flow.dto.service.InstanceDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;

import javassist.expr.NewArray;

/**
 * 流程模板控制层
 * 
 * @author admin
 *
 */
@Controller
@RequestMapping("/flow")
public class FlowRuntimeController {

	private static Logger log = Logger.getLogger(FlowRuntimeController.class);

	@Autowired
	private FlDtoServiceCustomer flDtoServiceCustomer;
	@Autowired
	private InstanceDtoServiceCustomer instanceDtoServiceCustomer;

	/**
	 * 获取流程审批路， 两种方式启动：
	 * 1、启动某个业务单据(businessId)的某个流程(flCode).
	 * 2、启动某个业务单据(businessId)所属的业务对象(businessObjectCode)的默认流程
	 * 
	 * 说明：businessId为第三方系统单据ID，不同系统间可能重复
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/start", method = { RequestMethod.POST }, consumes = "application/json")
	public @ResponseBody MessageResult start(@RequestBody Map<String, Object> map,HttpServletRequest req) {
		MessageResult result = new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			
			String dubboResultInfo = flDtoServiceCustomer.start(userInfo, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo,
					DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				Map resultMap=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(),Map.class);
				resultMap.put("returnSessionId",req.getSession().getId());
				resultMap.put("returnUserId",securityUserBeanInfo.getSecurityUserDto().getId());
				result.setResult(resultMap);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
			} else {
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			log.error("调用getPath方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}

	@RequestMapping(value = "/emulation", method = { RequestMethod.POST }, consumes = "application/json")
	public @ResponseBody MessageResult emulation(@RequestBody Map<String, Object> map) {
		MessageResult result = new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo = flDtoServiceCustomer.emulation(userInfo, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo,
					DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				result.setResult(JacksonUtils.fromJson(dubboServiceResultInfo.getResult(),List.class));
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
			} else {
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			log.error("调用emulation方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 流程执行路径变更处理 1、一般指由管理员或配置的代理引起的流程实例路径或审批人变更
	 * 2、目前已有的路径变更类型有：代理（4种情况）、加签、替换代理人、撤回、一键审结
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/processChange", method = { RequestMethod.POST }, consumes = "application/json")
	public @ResponseBody MessageResult processChange(@RequestBody Map<String, Object> map) {
		MessageResult result = new MessageResult();
		String paramJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo = instanceDtoServiceCustomer.processChange(userJson, paramJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo,
					DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				result.setResult(JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), Map.class));
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
			} else {
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			log.error("调用processChange方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}
	
	@RequestMapping(value = "/queryPostData", method = { RequestMethod.POST }, consumes = "application/json")
	public @ResponseBody MessageResult queryPostData(@RequestBody Map<String, Object> map) {
		MessageResult result = new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
//			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
//			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			result.setResult(JacksonUtils.toJson(resultMap));
			result.setSuccess(MessageInfo.GETSUCCESS.isResult());
			result.setMsg("查询人员岗位信息！");
		} catch (Exception e) {
			log.error("调用queryPostData方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}
	@RequestMapping(value = "/queryBusinessData", method = { RequestMethod.POST }, consumes = "application/json")
	public @ResponseBody MessageResult queryBusinessData(@RequestBody Map<String, Object> map) {
		MessageResult result = new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("flow_phone_data", "手机业务数据");
			Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("flow_business_company_id", "业务单据公司ID");
				dataMap.put("flow_business_dept_id", "业务单据部门ID");
				dataMap.put("flow_business_project_id", "业务单据项目ID");
				dataMap.put("flow_business_project_branch_id", "业务单据分期ID");
				dataMap.put("start_user_id", "发起人ID");
				dataMap.put("IntegerVariable", 10);
				dataMap.put("StringVariable", "魏武龙");
				dataMap.put("BooleanVariable", true);
				dataMap.put("code", "日常报销");
				dataMap.put("name222", "测试");
				dataMap.put("name", "");
			resultMap.put("flow_business_variable_data", dataMap);
			resultMap.put("flow_business_data", "业务单据数据");
			result.setResult(JacksonUtils.toJson(resultMap));
			result.setSuccess(MessageInfo.GETSUCCESS.isResult());
			result.setMsg("查询业务表单数据！");
		} catch (Exception e) {
			log.error("调用queryBusinessData方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}

}
