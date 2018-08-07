package com.example.demo.shiro.realm;

import com.example.demo.dao.RolePermissionDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dao.UserRoleDao;
import com.example.demo.model.RolePermission;
import com.example.demo.model.UserInfo;
import com.example.demo.model.UserRole;
import com.example.demo.shiro.utils.JWTToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.shiro.utils.JWTUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JWTRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = JWTUtil.getUsername(principals.toString());
        List<UserRole> userRoles = userRoleDao.findByUserName(userName);

        Set<String> roles = new HashSet<>(16);
        Set<String> permissions = new HashSet<>(16);

        for(UserRole userRole : userRoles){
            roles.add(userRole.getRoleName());
            List<RolePermission> rolePermissions = rolePermissionDao.findByRoleName(userRole.getRoleName());
            for(RolePermission rolePermission : rolePermissions){
                permissions.add(rolePermission.getPermission());
            }
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roles);
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();

        String userName = JWTUtil.getUsername(token);
        if (userName == null) {
            throw new AuthenticationException("token invalid");
        }

        UserInfo userInfo = userDao.findByUserName(userName);
        if (userInfo == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (! JWTUtil.verify(token, userName, userInfo.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }

        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
