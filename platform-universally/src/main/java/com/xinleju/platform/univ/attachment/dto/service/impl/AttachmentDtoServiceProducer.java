package com.xinleju.platform.univ.attachment.dto.service.impl;

import java.util.*;

import com.xinleju.platform.utils.HanziComparator;
import com.xinleju.platform.utils.SortUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.AttachmentDto;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentDtoServiceCustomer;
import com.xinleju.platform.univ.attachment.entity.Attachment;
import com.xinleju.platform.univ.attachment.service.AttachmentService;

/**
 * @author haoqp
 * 
 *
 */
public class AttachmentDtoServiceProducer implements AttachmentDtoServiceCustomer {
	private static Logger log = Logger.getLogger(AttachmentDtoServiceProducer.class);
	@Autowired
	private AttachmentService attachmentService;

	public String save(String userInfo, String saveJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			Attachment attachment = JacksonUtils.fromJson(saveJson, Attachment.class);
			attachmentService.save(attachment);
			info.setResult(JacksonUtils.toJson(attachment));
			info.setSucess(true);
			info.setMsg("保存对象成功!");
		} catch (Exception e) {
			log.error("保存对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("保存对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String saveBatch(String userInfo, String saveJsonList) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			List<Attachment> attachmentList = JacksonUtils.fromJson(saveJsonList, ArrayList.class, Attachment.class);
			attachmentService.saveBatch(attachmentList);
			info.setResult(JacksonUtils.toJson(attachmentList));
			info.setSucess(true);
			info.setMsg("保存对象成功!");
		} catch (Exception e) {
			log.error("保存对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("保存对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String updateBatch(String userInfo, String updateJsonList) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			List<Attachment> attachmentList = JacksonUtils.fromJson(updateJsonList, ArrayList.class, Attachment.class);
			int result = attachmentService.updateBatch(attachmentList);
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("更新对象成功!");
		} catch (Exception e) {
			log.error("更新对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String update(String userInfo, String updateJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			Attachment attachment = JacksonUtils.fromJson(updateJson, Attachment.class);
			int result = attachmentService.update(attachment);
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("更新对象成功!");
		} catch (Exception e) {
			log.error("更新对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteObjectById(String userInfo, String deleteJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			Attachment attachment = JacksonUtils.fromJson(deleteJson, Attachment.class);
			int result = attachmentService.deleteObjectById(attachment.getId());
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("删除对象成功!");
		} catch (Exception e) {
			log.error("更新对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("删除更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteAllObjectByIds(String userInfo, String deleteJsonList) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(deleteJsonList)) {
				Map map = JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				List<String> list = Arrays.asList(map.get("id").toString().split(","));
				int result = attachmentService.deleteAllObjectByIds(list);
				info.setResult(JacksonUtils.toJson(result));
				info.setSucess(true);
				info.setMsg("删除对象成功!");
			}
		} catch (Exception e) {
			log.error("删除对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("删除更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getObjectById(String userInfo, String getJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			Attachment attachment = JacksonUtils.fromJson(getJson, Attachment.class);
			Attachment result = attachmentService.getObjectById(attachment.getId());
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("获取对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPage(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				Page page = attachmentService.getPage(map, (Integer) map.get("start"), (Integer) map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			} else {
				Page page = attachmentService.getPage(new HashMap(), null, null);
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取分页对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取分页对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryList(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				List<Attachment> list = attachmentService.queryList(map);
				List<AttachmentDto> listDto=JacksonUtils.fromJson(JacksonUtils.toJson(list), ArrayList.class,AttachmentDto.class);
				//先将字符串中的数组转换为指定长度??数字
				for (AttachmentDto attachmentDto : listDto) {
					String tempName = SortUtil.changeIntToSpecifyLength(attachmentDto.getName());
					if(tempName.length()==0){
						attachmentDto.setTempName(attachmentDto.getName());
					}else {
						attachmentDto.setTempName(tempName);
					}
				}
				//对转换后的数据进行排??
				HanziComparator hanziComparator = new HanziComparator();
				Collections.sort(listDto, hanziComparator);

				info.setResult(JacksonUtils.toJson(listDto));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			} else {
				List list = attachmentService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getCount(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			Attachment attachment = JacksonUtils.fromJson(deleteJson, Attachment.class);
			int result = attachmentService.deletePseudoObjectById(attachment.getId());
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("删除对象成功!");
		} catch (Exception e) {
			log.error("更新对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("删除更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo, String deleteJsonList) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(deleteJsonList)) {
				Map map = JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				List<String> list = Arrays.asList(map.get("id").toString().split(","));
				int result = attachmentService.deletePseudoAllObjectByIds(list);
				info.setResult(JacksonUtils.toJson(result));
				info.setSucess(true);
				info.setMsg("删除对象成功!");
			}
		} catch (Exception e) {
			log.error("删除对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("删除更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryListByCategoryIds(String userInfo, String paramater) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			List<AttachmentDto> list = null;
			if (StringUtils.isNotBlank(paramater) && !"[]".equals(paramater)) {
				String[] ids = JacksonUtils.fromJson(paramater, String[].class);
				list = attachmentService.queryListByCategoryIds(ids);
			} else {
				list = attachmentService.queryListByCategoryIds(null);
			}
			info.setResult(JacksonUtils.toJson(list));
			info.setSucess(true);
			info.setMsg("获取列表对象成功!");
		} catch (Exception e) {
			log.error("获取列表对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPageByCategoryIds(String userInfo, String paramater) {
		
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
		 System.out.println("getClass().getResource(\"\"):" + getClass().getResource(""));
			System.out.println("getClass().getResource(\"/\"):" + getClass().getResource("/"));
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				Page page = attachmentService.getPageByCategoryIds(map, (Integer) map.get("start"), (Integer) map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			} else {
				Page page = attachmentService.getPage(new HashMap(), null, null);
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取分页对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取分页对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	public String queryListByObject(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				List<AttachmentDto> list = attachmentService.queryListByObject(map);

				//先将字符串中的数组转换为指定长度??数字
				for (AttachmentDto attachmentDto : list) {
					String tempName = SortUtil.changeIntToSpecifyLength(attachmentDto.getName());
					attachmentDto.setTempName(tempName);
				}
				//对转换后的数据进行排??
				HanziComparator hanziComparator = new HanziComparator();
				Collections.sort(list, hanziComparator);


				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			} else {
				List list = attachmentService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

    @Override
    public String queryFlowList(String s, String paramater) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
        Map<String,List<AttachmentDto>> resultMap = new HashMap();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				List<Attachment> list = attachmentService.queryFlowList(map);
				List<AttachmentDto> listDto=JacksonUtils.fromJson(JacksonUtils.toJson(list), ArrayList.class,AttachmentDto.class);
                for(AttachmentDto att : listDto){
                    if(resultMap.containsKey(att.getCategoryId())){
                        resultMap.get(att.getCategoryId()).add(att);
                    }else{
                        List<AttachmentDto> tmpList = new ArrayList<AttachmentDto>();
                        tmpList.add(att);
                        resultMap.put(att.getCategoryId(), tmpList);
                    }
                }
				//先将字符串中的数组转换为指定长度??数字
                for(Map.Entry<String, List<AttachmentDto>> entry : resultMap.entrySet()){
                    for (AttachmentDto attachmentDto : entry.getValue()) {
                        String tempName = SortUtil.changeIntToSpecifyLength(attachmentDto.getName());
                        if(tempName.length()==0){
                            attachmentDto.setTempName(attachmentDto.getName());
                        }else {
                            attachmentDto.setTempName(tempName);
                        }
                    }
                    //对转换后的数据进行排??
                    HanziComparator hanziComparator = new HanziComparator();
                    Collections.sort(entry.getValue(), hanziComparator);
                }
				info.setResult(JacksonUtils.toJson(resultMap));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			} else {
				List list = attachmentService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
    }

}