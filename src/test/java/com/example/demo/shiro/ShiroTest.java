package com.example.demo.shiro;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.demo.shiro.realm.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class ShiroTest {
    @Before
    public void before(){ }

    @Test
    public void simpleAccountRealmTest(){
        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
        simpleAccountRealm.addAccount("name", "123456", "teacher", "student");

        UsernamePasswordToken token = new UsernamePasswordToken("name", "123456");
        authenticationTest(simpleAccountRealm, token);

    }

    @Test
    public void iniRealmTest(){
        IniRealm iniRealm = new IniRealm("classpath:user.ini");
        UsernamePasswordToken token = new UsernamePasswordToken("Tyson", "123456");
        authenticationTest(iniRealm, token);

    }

    @Test
    public void jdbcRealm(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/db_shiro_demo");
        dataSource.setUsername("root");
        dataSource.setPassword("");

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        UsernamePasswordToken token = new UsernamePasswordToken("Tyson", "123456");
        authenticationTest(jdbcRealm, token);
    }

    @Test
    public void myRealmTest(){
        MyRealm myRealm = new MyRealm();
        UsernamePasswordToken token = new UsernamePasswordToken("Tyson", "123456");
        authenticationTest(myRealm, token);
    }

    @Test
    public void hashedCredentialsMatcherTest(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);

        SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
        simpleAccountRealm.addAccount("Tyson", "e10adc3949ba59abbe56e057f20f883e", "teacher");
        simpleAccountRealm.setCredentialsMatcher(matcher);

        Md5Hash md5Hash = new Md5Hash("123456");
        System.out.println("MD5(123456): " + md5Hash.toString());

        UsernamePasswordToken token = new UsernamePasswordToken("Tyson", "123456");
        authenticationTest(simpleAccountRealm, token);
    }


    private void authenticationTest(AuthorizingRealm realm, AuthenticationToken token){
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(realm);
        SecurityUtils.setSecurityManager( defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        //登录
        subject.login(token);
        System.out.println("isAuthenticated: " + subject.isAuthenticated());
        //鉴权
        subject.checkRole("teacher");
        //subject.checkPermission("delete");
        //登出
        subject.logout();
        System.out.println("isAuthenticated: " + subject.isAuthenticated());
    }
}
