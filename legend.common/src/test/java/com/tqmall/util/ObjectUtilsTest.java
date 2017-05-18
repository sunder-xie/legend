package com.tqmall.util;

import org.junit.Test;

import static com.tqmall.common.util.ObjectUtils.*;

/**
 * Created by wanghui on 11/27/15.
 */
public class ObjectUtilsTest {

    @Test
    public void testToString(){
        Person p = new Person("Mokala", 38);
        System.out.println(objectToJSON(p));

        System.out.println(objectToFullString(p));

        System.out.println(objectToShortString(p));

        System.out.println(objectToBeautyString(p));
    }
}
