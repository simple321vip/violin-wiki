package com.g.estate.h2;

import com.g.estate.entity.Location;
import com.g.estate.service.LocationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TransactionalTest {

    @Autowired
    private LocationService locationService;

    /**
     * トランザクション「mandatory」にマークされたメソッドは必ずトランザクションを持っているメソッド内で呼び出すこと
     * locationService.get　メソッド　は　直接呼び出すと、このメソッドを呼び出すのはトランザクションがないので、エラーが発生
     * 異常終了
     * @throws
     */
    @Test
    public void testTranFor_mandatory_E1() {
        try {
            locationService.get();
        } catch (Exception e) {
            // エラー断言
//            assertEquals
        }

    }

    /**
     * トランザクション「mandatory」にマークされたメソッドは必ずトランザクションを持っているメソッド内で呼び出すこと
     * locationService.get　メソッド　は　トランザクションが付いているgetAllメソッド内で呼び出すので、エラーが発生しない
     * 正常終了
     */
    @Test
    public void testTran_mandatory_N1() {
        List<Location> actual = locationService.getAll();
        List<Location> expected = new ArrayList<>();
        // assert 断言
    }
}
