package com.yuluedu.maptest.treasure.detail;

import java.util.List;

/**
 * Created by gameben on 2017-05-15.
 */

public interface TreasureDetailView {
    void showMessage(String msg);
    void setDetailData(List<TreasureDetailResult> list);// 设置数据
}
