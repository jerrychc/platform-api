package com.xinleju.platform.univ.attachment.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.csource.common.NameValuePair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.FastDFSClient;
import com.xinleju.platform.univ.attachment.dao.AttachmentTempDao;
import com.xinleju.platform.univ.attachment.dto.AttachmentTempDto;
import com.xinleju.platform.univ.attachment.entity.AttachmentTemp;
import com.xinleju.platform.univ.attachment.service.AttachmentTempService;

/**
 * @author haoqp
 * 
 * 
 */
@Transactional
@Service
public class AttachmentTempServiceImpl extends  BaseServiceImpl<String,AttachmentTemp> implements AttachmentTempService{
	

	@Autowired
	private AttachmentTempDao attachmentTempDao;

	@Override
	public int saveFileUpload(List<AttachmentTempDto> list) throws Exception {
		FastDFSClient fastDFSClient = new FastDFSClient();
		NameValuePair[] metaList = new NameValuePair[3];
		List<AttachmentTemp> entityList = new ArrayList<>();
		AttachmentTemp entity;
		for (AttachmentTempDto dto : list) {
			entity = new AttachmentTemp();
			BeanUtils.copyProperties(dto, entity, "fileBytes");
			entityList.add(entity);
			metaList[0] = new NameValuePair("fileName",dto.getName());
			metaList[1] = new NameValuePair("fileExtName", dto.getExtendName());
			metaList[2] = new NameValuePair("fileLength",dto.getFileSize().toString());
			fastDFSClient.upload(dto.getFileBytes(), dto.getExtendName(), metaList);
		}
		return attachmentTempDao.saveBatch(entityList);
	}

}
