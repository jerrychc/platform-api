package com.xinleju.platform.base.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;

@RequestMapping("/generator")
@Controller
public class IDGeneratorController {
	private static Logger log = Logger.getLogger(IDGeneratorController.class);

	@RequestMapping(value = "/getGuuid", method = RequestMethod.GET)
	public @ResponseBody MessageResult getGuuid() {
		MessageResult result = new MessageResult();
		result.setResult(IDGenerator.getUUID());
		result.setSuccess(MessageInfo.GETSUCCESS.isResult());
		result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		return result;
	}

	@RequestMapping(value = "/getGuuids", method = RequestMethod.GET)
	public @ResponseBody MessageResult getGuuids(@RequestParam("count")int count) {
		MessageResult result = new MessageResult();
		List<String> ids = new ArrayList<String>();	
		for(int i=0; i<count; i++) {
			ids.add(IDGenerator.getUUID());
		}
		result.setResult(ids);
		result.setSuccess(MessageInfo.GETSUCCESS.isResult());
		result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		return result;
	}
}
