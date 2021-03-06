package com.xinleju.platform.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.dao.AccountCaptionDao;
import com.xinleju.platform.finance.dao.VoucherBillDao;
import com.xinleju.platform.finance.entity.AccountCaption;
import com.xinleju.platform.finance.entity.VoucherBill;
import com.xinleju.platform.finance.entity.VoucherBillEntry;
import com.xinleju.platform.finance.service.VoucherBillService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class VoucherBillServiceImpl extends  BaseServiceImpl<String,VoucherBill> implements VoucherBillService{
	

	@Autowired
	private VoucherBillDao voucherBillDao;
	@Autowired
	private AccountCaptionDao accountCaptionDao;
	
	/**
	 * @param map
	 * @return
	 */
	@Override
	public Page getVoucherBillPage(Map map)throws Exception{
		Page page =new Page();
		   List<VoucherBill> list=voucherBillDao.getVoucherBillPage(map);
		   Integer total=voucherBillDao.getVoucherBillListCount(map);
		   page.setLimit((Integer) map.get("limit"));
		   page.setList(list);
		   page.setStart((Integer) map.get("start"));
		   page.setTotal(total);
		   return page;
	}
	
	/**
	 * 校验是否为完整的凭证
	 * 校验规则：1.辅助核算没有匹配到   2：借贷金额不平
	 * @param voucher
	 * @param entryDataList
	 * @return
	 */
	public Map isFull(VoucherBill voucher,List<VoucherBillEntry> entryDataList,String accountSetId){
		Map<Integer,String> map = new HashMap<Integer, String>();
		boolean isFull = true;
		Double creditAmountSum = Double.valueOf(voucher.getCreditAmount().replace(",", ""));
		Double debitAmountSum = Double.valueOf(voucher.getDebitAmount().replace(",", ""));
		String error = "";
		
		map.put(1, "true");
		//判断借贷金额是否相等
		if (Math.abs(creditAmountSum) - Math.abs(debitAmountSum) != 0) {
			isFull = false;
			error = "借贷不平";
			//1:是否为完整凭证
			map.put(1, "false");
			map.put(2, error);
		}
		//未匹配到辅助核算
		for(VoucherBillEntry entryDate:entryDataList){
			String captionCode = entryDate.getCaptionCode();
			String assCompent = entryDate.getAssCompent();
			Map<String,Object> capmap = new HashMap<String,Object>();
			capmap.put("code", captionCode);
			capmap.put("accountSetId", accountSetId);
			List<AccountCaption> fcList = accountCaptionDao.queryList(capmap);
			if(fcList != null && fcList.size()>0){
				AccountCaption fc = accountCaptionDao.queryList(capmap).get(0);
				if(fc!=null){
		    	    String assName = fc.getAssNames();
		    	    if(StringUtils.isNotBlank(assName) && StringUtils.isBlank(assCompent)){
		    	    	isFull = false;
		    	    	error += "未匹配到辅助核算："+assName + ",";
		    	    	map.put(1, "false");
		    			map.put(2, error);
		    	    }
				}else{
					isFull = false;
	    	    	error += "未匹配到会计科目";
	    	    	map.put(1, "false");
	    			map.put(2, error);
				}
			}else{
				isFull = false;
    	    	error += "未匹配到会计科目";
    	    	map.put(1, "false");
    			map.put(2, error);
			}
		}
		
		return map;
	}
}
