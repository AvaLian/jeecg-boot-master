package org.jeecg.modules.pd.service;

import org.jeecg.modules.pd.entity.PdPackageDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 定数包明细
 * @Author: jiangxz
 * @Date:   2020-02-02
 * @Version: V1.0
 */
public interface IPdPackageDetailService extends IService<PdPackageDetail> {

	public List<PdPackageDetail> selectByMainId(String mainId);
}
