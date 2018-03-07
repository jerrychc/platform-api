package com.xinleju.platform.sys.security.controller;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.EncryptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by admin on 2018/2/26.
 */
@Controller
@RequestMapping("/sys/superMgr")
public class SuperPasswordMgrController {


    @RequestMapping(value = "/generateEncryPassword", method = {RequestMethod.POST}, consumes = "application/json")
    public @ResponseBody MessageResult page(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String userName = (String) map.get("userName");
        String password = (String) map.get("password");
        try {
            EncryptionUtils encryptionUtils = new EncryptionUtils();
            String newPassword = encryptionUtils.getEncryptInfo(userName, password);
            result.setSuccess(true);
            result.setResult(newPassword);
            result.setMsg("加密密码生成成功！");
        } catch (Exception e) {
            ////e.printStackTrace();
            result.setSuccess(false);
            result.setMsg("加密密码生成失败！");
        }

        return result;
    }
}
