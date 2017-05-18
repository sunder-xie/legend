package test;

import com.tqmall.legend.entity.account.vo.CouponVo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wushuai on 16/6/16.
 */
public class CommTest {
    @Test
    public void test(){
        Pattern p = Pattern.compile("(^0\\d{2,3}-*\\d{7,8}$)|(^0\\d{2,3}-*\\d{7,8}-\\d+$)");
        Matcher m = p.matcher("0121-1234567-1");
        System.out.println(m.matches());
    }


    @Test
    public void test2(){
        final CouponVo couponVo = new CouponVo();
        couponVo.setId(1l);
        couponVo.setNum(1);//领取一张
        List<CouponVo> couponVoList = new ArrayList<CouponVo>() {{
            add(couponVo);
        }};
        System.out.println(couponVoList);

    }

    @Test
    public void testString(){

        String a = "123";
        String b = new String("123");

        String c = a.intern();
        String d = "123";

        System.out.println(a.equals(b)); //true
        System.out.println(a.equals(c)); //true
        System.out.println(a == b); // false
        System.out.println(a == c); // false
        System.out.println(a == d); // false
    }


    @Test
    public void testFormat(){
        System.out.println(String.format("%.2s","23132"));
    }
}
