package com.xinleju.platform.flow.dao;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.entity.Ac;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 *
 */

public interface AcDao extends BaseDao<String, Ac> {

    /**
     * 查询模板环节列表
     * @param searchParams
     * @return
     */
    public Page queryModifyAcListByPage(Map<String,Object> searchParams);

    /**
     * 批量保存节点属性
     * @param searchParams
     * @return
     */
    public int batchUpdateAcAttr(Map<String,Object> batchUpdateAcData);

    /**
     * 更新模板操作人信息
     * @param batchUpdateAcData
     * @return
     */
    public int batchUpdateFlOperationPerson(Map<String,Object> batchUpdateAcData);

}
