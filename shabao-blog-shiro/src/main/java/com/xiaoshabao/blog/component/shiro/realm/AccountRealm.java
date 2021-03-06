package com.xiaoshabao.blog.component.shiro.realm;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiaoshabao.blog.component.shiro.authc.AccountAuthenticationInfo;
import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.AuthMenu;
import com.xiaoshabao.blog.dto.User;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.UserService;


public class AccountRealm extends AuthorizingRealm {
	@Autowired
    private UserService userService;

    public AccountRealm() {
        super(new AllowAllCredentialsMatcher());
        setAuthenticationTokenClass(UsernamePasswordToken.class);

        //FIXME: 暂时禁用Cache
        setCachingEnabled(false);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        String username = (String) principals.fromRealm(getName()).iterator().next();
        Object userObject=principals.getPrimaryPrincipal();
        if (userObject != null) {
        	String username=userObject.toString();
            User user = userService.getByUsername(username);
            if (user != null){
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                List<AuthMenu> menuList = userService.getMenuList(user.getId());
                for (AuthMenu menu : menuList){
                    if (StringUtils.isNotBlank(menu.getPermission())){
                        // 添加基于Permission的权限信息
                        for (String permission : StringUtils.split(menu.getPermission(),",")){
                            info.addStringPermission(permission);
                        }
                    }
                }
//                info.addRole(role.getKey());
//                for (Role r : user.getRoles()) {
//                    info.addRole(r.getName());
//                    ArrayList<String> ps = new ArrayList<String>();
//                    for(Permission p: r.getPermissions()){
//                        ps.add(p.getName());
//                    }
//                    
//                    info.addStringPermissions(ps);
//                }
                return info;
            }
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AccountProfile profile = getAccount(userService, token);

        if(profile.getStatus() == Consts.STATUS_CLOSED){
            throw new LockedAccountException(profile.getName());
        }

        AccountAuthenticationInfo info = new AccountAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
        info.setProfile(profile);

        return info;
    }

    protected AccountProfile getAccount(UserService userService, AuthenticationToken token){
        UsernamePasswordToken upToken = (UsernamePasswordToken)token;
        return userService.login(upToken.getUsername(), String.valueOf(upToken.getPassword()));
    }
}
