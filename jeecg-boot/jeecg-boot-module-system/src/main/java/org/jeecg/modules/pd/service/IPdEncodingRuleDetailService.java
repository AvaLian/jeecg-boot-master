package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdEncodingRule;
import org.jeecg.modules.pd.entity.PdEncodingRuleDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 编码规则详情表
 * @Author: zxh
 * @Date:   2019-12-26
 * @Version: V1.0
 */
public interface IPdEncodingRuleDetailService extends IService<PdEncodingRuleDetail> {

    void removeByCodeId(PdEncodingRule pdEncodingRule);
}
