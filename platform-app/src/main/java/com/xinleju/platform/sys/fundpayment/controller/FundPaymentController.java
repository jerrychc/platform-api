package com.xinleju.platform.sys.fundpayment.controller;

import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.base.utils.MessageResult;
import jxl.common.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by luoro on 2017/11/6.
 */
@Controller
@RequestMapping("/sys/fundPayment")
public class FundPaymentController {
    private final Logger logger  = Logger.getLogger(getClass());
    @RequestMapping(value = "/page",method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public MessageResult page(@RequestBody Map map){
        MessageResult result = new MessageResult();
        try{

            result.setSuccess(true);
        }catch (Exception e){
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setSuccess(false);
            e.printStackTrace();
        }
        return result;
    }
    @RequestMapping(value = "/importCapitalPlatform",method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public MessageResult importCapitalPlatform(@RequestBody Map map){
        MessageResult result = new MessageResult();
        try{


        }catch (Exception e){
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setSuccess(false);
            e.printStackTrace();
        }
        return result;
    }
    @RequestMapping(value ="/syncCapitalPlatform",method = RequestMethod.POST,consumes = "application/json")
    @ResponseBody
    public MessageResult syncCapitalPlatform(@RequestBody Map map){
        MessageResult result = new MessageResult();
        try{


        }catch (Exception e){
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setSuccess(false);
            e.printStackTrace();
        }
        return result;
    }
    @RequestMapping(value ="/update/{id}",method = RequestMethod.PUT,consumes = "application/json")
    @ResponseBody
    public MessageResult update(@PathVariable("id")String id,@RequestBody Map map){
        MessageResult result = new MessageResult();
        try{


        }catch (Exception e){
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setSuccess(false);
            e.printStackTrace();
        }
        return result;
    }
    @RequestMapping(value ="/get/{id}",method = RequestMethod.GET,consumes = "application/json")
    @ResponseBody
    public MessageResult get(@PathVariable("id")String id,@RequestBody Map map){
        MessageResult result = new MessageResult();
        try{


        }catch (Exception e){
            result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
            result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
            result.setSuccess(false);
            e.printStackTrace();
        }
        return result;
    }
}
