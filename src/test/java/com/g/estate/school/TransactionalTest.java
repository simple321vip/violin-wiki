package com.g.estate.school;

import com.g.estate.GEstateApplication;
import com.g.estate.entity.Location;
import com.g.estate.service.LocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.IllegalTransactionStateException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GEstateApplication.class)
public class TransactionalTest {

    @Autowired
    private LocationService locationService;

    /**
     * トランザクション「mandatory」にマークされたメソッドは必ずトランザクションを持っているメソッド内で呼び出すこと
     * locationService.get　メソッド　は　直接呼び出すと、このメソッドを呼び出すのはトランザクションがないので、エラーが発生
     * 異常終了
     *
     * @throws
     */
    @Test
    public void testTranFor_mandatory_E1() {
        try {
            locationService.get();
        } catch (Exception e) {
            // エラー断言
            assertEquals(e.getClass(), IllegalTransactionStateException.class);
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
        assertEquals(actual, expected);
    }

    /**
     * トランザクション「never」にマークされたメソッドは必ずトランザクションを持っていないメソッド内で呼び出すこと
     * locationService.neverTransactionTest　メソッド　は　トランザクションがないメソッド内で呼び出すので、エラーが発生しない
     * 正常終了
     */
    @Test
    public void testTran_never_N1() {
        List<Location> actual = locationService.neverTransactionTest();
        List<Location> expected = new ArrayList<>();
        // assert 断言
        assertEquals(actual, expected);
    }

    /**
     * トランザクション「never」にマークされたメソッドは必ずトランザクションを持っていないメソッド内で呼び出すこと
     * locationService.neverTransactionTest　メソッド　は　トランザクションがいるneverTransactionTestメソッド内で呼び出すので、エラーが発生する
     * 異常終了
     */
    @Test
    @Sql(value = "classpath:db/data.sql")
    public void testTran_never_E1() {
//        try {
            locationService.requiredTransaction();
//        } catch (Exception e) {
//            System.out.println(1);
//            e.printStackTrace(System.out);
//        }
    }

}
