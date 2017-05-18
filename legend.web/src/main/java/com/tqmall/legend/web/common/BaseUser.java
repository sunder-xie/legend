package com.tqmall.legend.web.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by litan on 14-10-23.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class BaseUser extends User{

    /**
     * 用戶角色
     */
    private String userRole;

    /**
     * 用户账户
     */
    private String account;

    /**
     * 用戶权限
     */
    private List<String> userRoleFunc;

    /**
     * 用戶所属门店ID
     */
    private Long shopId;

    /**
     * 用戶ID
     */
    private Long userId;


    public BaseUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    /**
     * Returns {@code true} if the supplied object is a {@code User} instance with the
     * same {@code username} value.
     * <p>
     * In other words, the objects are equal if they have the same username, representing the
     * same principal.
     */
    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof User) {
            return account.equals(((BaseUser) rhs).account);
        }
        return false;
    }

    /**
     * Returns the hashcode of the {@code username}.
     */
    @Override
    public int hashCode() {
        return account.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Account: ").append(this.account).append("; ");
        sb.append("UserRole: ").append(this.userRole).append("; ");
        sb.append("ShopId: ").append(this.shopId).append("; ");
        sb.append("UserId: ").append(this.userId).append("; ");

        if (!userRoleFunc.isEmpty()) {
            sb.append("Granted Authorities: ");

            boolean first = true;
            for (String func : userRoleFunc) {
                if (!first) {
                    sb.append(",");
                }
                first = false;

                sb.append(func);
            }
        } else {
            sb.append("Not granted any Func");
        }

        return sb.toString();
    }
}
