package es.grammata.evaluation.evs.data.services.repository.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.grammata.evaluation.evs.data.model.repository.UserProfile;
import es.grammata.evaluation.evs.data.services.base.impl.BaseServiceImpl;
import es.grammata.evaluation.evs.data.services.repository.UserProfileService;


@Repository
@Transactional(readOnly = true)
public class UserProfileServiceImpl extends BaseServiceImpl<UserProfile> implements UserProfileService {
	
	public UserProfile findByCode(String code) {
		TypedQuery<UserProfile> query = this.em.createQuery("select r from " + UserProfile.class.getSimpleName() + 
				" r where r.code='" + code + "'", UserProfile.class);
		
		List<UserProfile> userProfiles = query.getResultList();
		UserProfile userProfile = null;
		if(userProfiles != null && userProfiles.size() > 0) {
			userProfile = userProfiles.get(0);
		}
		
		return userProfile;
	}
	

	@Override
	public boolean checkUserPermission(String username, Long id, String domainObject,
			String permission) {
		String query = "select count(*) from evs_user u, permission p, user_permission ups where " +				
			    " u.username = '" + username + "' and " +
			    " p.permission = '" + permission + "' and " +
			    " p.domainobject = '" + domainObject + "' and " +
			    " ups.user_id = u.id and ups.permission_id = p.id;";
		
		if(id != null)
			query += " and p.id = " + id;
		
		long total = ((Number)em.createNativeQuery(query).getSingleResult()).longValue();
		
		boolean res = (total>0)?true:false;	
		
		if (res)
			return true;
		
		query = "select count(*) from evs_user u, permission p, user_profile up, user_user_profile uup, " +
				" user_profile_permission upp  where " +				
			    " u.username = '" + username + "' and " +
			    " p.permission = '" + permission + "' and " +
			    " p.domainobject = '" + domainObject + "' and " +
			    " upp.permission_id = p.id and upp.user_profile_id = up.id and " +
			    " uup.user_profile_id = up.id and u.id = uup.user_id";
		
		if(id != null)
			query += " and p.id = " + id;
		
		total = ((Number)em.createNativeQuery(query).getSingleResult()).longValue();
		
		res = (total>0)?true:false;	
		
		return res;
	}
	
	
	public boolean checkUserProfile(String username, String userProfileCode) {
		
		String query = "select count(*) from user_user_profile uup, evs_user u, user_profile up where " +
				" uup.user_profile_id = up.id and uup.user_id = u.id and " +
				" up.code = '" + userProfileCode  + "' and u.username = '" + username + "'"; 
		
		long total = ((Number)em.createNativeQuery(query).getSingleResult()).longValue();
		
		return (total>0)?true:false;	
	}
}
