package com.yuluedu.maptest.treasure.map;

import com.yuluedu.maptest.treasure.Treasure;

import java.util.List;

/**
 * Created by gameben on 2017-05-07.
 */

public interface MapMvpView {
    void showMessage(String msg);// 显示信息

    void setTreasureData(List<Treasure> list);// 设置数据
}
