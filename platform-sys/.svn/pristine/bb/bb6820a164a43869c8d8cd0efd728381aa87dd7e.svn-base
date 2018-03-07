package com.xinleju.platform.sys.security.dto.service.impl;

import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.flow.utils.DateUtils;
import com.xinleju.platform.flow.utils.StringUtil;
import com.xinleju.platform.party.dto.UserConfigDto;
import com.xinleju.platform.party.dto.service.UserConfigDtoServiceCustomer;
import com.xinleju.platform.party.entity.UserConfig;
import com.xinleju.platform.party.service.UserConfigService;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;
import com.xinleju.platform.sys.org.entity.User;
import com.xinleju.platform.sys.org.service.UserService;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.sys.security.dto.service.AuthenticationDtoServiceCustomer;
import com.xinleju.platform.sys.security.dto.service.ThirdAuthServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 */

public class ThirdAuthDtoServiceProducer implements ThirdAuthServiceCustomer {
    private static Logger log = Logger.getLogger(ThirdAuthDtoServiceProducer.class);
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationDtoServiceCustomer authenticationDtoServiceCustomer;
    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    /**
     * 更新第三方授权信息
     *
     * @param userInfo
     * @param paramater
     * @return
     */
    @Override
    public void resetThirdUserAuthData(String userInfo, String paramater) throws Exception {
        // 查询第三方用户配置信息
        Map userConfigParam = new HashMap();
        userConfigParam.put("delflag", "0");
        userConfigParam.put("status", "1");
        List<UserConfig> list = userConfigService.queryList(userConfigParam);
        String endTime_ = null;
        Long endTime = null;
        String thirdToken = null;
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                UserConfig thirdUser = list.get(i);
                thirdToken = thirdUser.getToken();
                endTime_ = thirdUser.getEndTime();
                //更新第三方用户信息
                User userDto = userService.getObjectById( thirdUser.getUserId() );
                    if (userDto != null) {
                        String authenticationInfodubboResultInfo = authenticationDtoServiceCustomer.getUserAuthenticationInfo(userInfo, JacksonUtils.toJson(userDto));
                        SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
                        SecurityUserBeanRelationInfo securityUserBeanRelationInfo = new SecurityUserBeanRelationInfo();
                        DubboServiceResultInfo authenticationInfodubboServiceResultInfo = JacksonUtils.fromJson(authenticationInfodubboResultInfo, DubboServiceResultInfo.class);
                        if (authenticationInfodubboServiceResultInfo.isSucess()) {
                            String authenticationInforesultInfo = authenticationInfodubboServiceResultInfo.getResult();
                            AuthenticationDto authenticationDto = JacksonUtils.fromJson(authenticationInforesultInfo, AuthenticationDto.class);
                            //获取用户标准岗位
                            List<SecurityStandardRoleDto> securityStandardRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getStandardRoleDtoList()), ArrayList.class, SecurityStandardRoleDto.class);
                            securityUserBeanInfo.setSecurityStandardRoleDtoList(securityStandardRoleDtoList);
                            //当前用户的菜单清单（未授权和已授权的）
                            List<SecurityResourceDto> SecurityResourceDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getResourceDtoList()), ArrayList.class, SecurityResourceDto.class);
                            securityUserBeanRelationInfo.setResourceDtoList(SecurityResourceDtoList);
                            //获取用户通用角色
                            List<SecurityStandardRoleDto> securityCurrencyRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getCurrencyRoleDtoList()), ArrayList.class, SecurityStandardRoleDto.class);
                            securityUserBeanInfo.setSecurityCurrencyRoleDtoList(securityCurrencyRoleDtoList);
                            //获取用户岗位
                            List<SecurityPostDto> securityPostDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getPostDtoList()), ArrayList.class, SecurityPostDto.class);
                            securityUserBeanInfo.setSecurityPostDtoList(securityPostDtoList);
                            //当前用户所在组织的类型
                            String securityOrganizationType = authenticationDto.getOrganizationType();
                            securityUserBeanInfo.setSecurityOrganizationType(securityOrganizationType);
                            //当前用户的一级公司
                            SecurityOrganizationDto securityTopCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopCompanyDto()), SecurityOrganizationDto.class);
                            securityUserBeanInfo.setSecurityTopCompanyDto(securityTopCompanyDto);
                            //当前用户的直属公司
                            SecurityOrganizationDto securityDirectCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectCompanyDto()), SecurityOrganizationDto.class);
                            securityUserBeanInfo.setSecurityDirectCompanyDto(securityDirectCompanyDto);
                            //当前用户的一级部门
                            SecurityOrganizationDto securityTopDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopDeptDto()), SecurityOrganizationDto.class);
                            securityUserBeanInfo.setSecurityTopDeptDto(securityTopDeptDto);
                            //当前用户的直属部门
                            SecurityOrganizationDto securityDirectDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectDeptDto()), SecurityOrganizationDto.class);
                            securityUserBeanInfo.setSecurityDirectDeptDto(securityDirectDeptDto);
                            //当前用户的项目
                            SecurityOrganizationDto securityGroupDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getGroupDto()), SecurityOrganizationDto.class);
                            securityUserBeanInfo.setSecurityGroupDto(securityGroupDto);
                            //当前用户的分期
                            SecurityOrganizationDto securityBranchDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getBranchDto()), SecurityOrganizationDto.class);
                            securityUserBeanInfo.setSecurityBranchDto(securityBranchDto);
                        }
                        if(StringUtils.isNotBlank(endTime_)){
                            endTime = DateUtils.parseDate(endTime_,"yyyy-MM-dd HH:mm:ss").getTime();
                        }
                        redisTemplate.opsForValue().set(SecurityUserBeanInfo.TOKEN_TEND_USER + thirdToken, JacksonUtils.toJson(securityUserBeanInfo), endTime, TimeUnit.MILLISECONDS);
                        redisTemplate.opsForValue().set(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + thirdToken, JacksonUtils.toJson(securityUserBeanRelationInfo), endTime, TimeUnit.MILLISECONDS);
                    }

            }
        }

    }
}
