package com.xiaoshabao.blog.component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.BadgesCount;
import com.xiaoshabao.blog.service.NotifyService;
/**
 * 配置员方案
 * @author Administrator
 *
 */
@Service("contextHolderCloudImpl")
public class ContextHolderCloudImpl implements ContextHolder{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NotifyService notifyService;
	
	@Override
	public AccountProfile getProfile() {
		String userName= SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal().toString();
		AccountProfile profile=null;
		if(!"anonymousUser".equals(userName)) {
			try {
				profile=getProfile(userName);
				
				BadgesCount badgesCount = new BadgesCount();
				badgesCount.setNotifies(notifyService.unread4Me(profile.getId()));
				profile.setBadgesCount(badgesCount);
			} catch (Exception e) {
			}
		}
		return profile;
	}
	
	private AccountProfile getProfile(String userName) {
		String sql = "select b.id,a.user_name,b.name,b.avatar,b.status from upms_user a,mto_users b "
					+"WHERE a.user_name=b.username and a.user_name=?";
		return jdbcTemplate.queryForObject(sql,new Object[] {userName},new int[]{Types.VARCHAR}, new RowMapper<AccountProfile>(){
			@Override
			public AccountProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
				AccountProfile profile=new AccountProfile();
				profile.setId(rs.getInt(1));
				profile.setUsername(rs.getString(2));
				profile.setName(rs.getString(3));
				profile.setAvatar(rs.getString(4));
				profile.setStatus(rs.getInt(5));
				return profile;
			}
		});
	}

	@Override
	public void putProfile(AccountProfile profile) {
//		SecurityUtils.getSubject().getSession(true).setAttribute("profile", profile);		
	}

	@Override
	public boolean isAuthenticated() {
		// TODO Auto-generated method stub
		return false;
	}

}
