package es.grammata.evaluation.evs.data.services.repository;

import es.grammata.evaluation.evs.data.model.repository.UserProfile;
import es.grammata.evaluation.evs.data.services.base.BaseService;



public interface UserProfileService extends BaseService<UserProfile> {
	public boolean checkUserPermission(String username, Long id, String domainObject,
			String permission);
	public boolean checkUserProfile(String username, String userProfileCode);
	public UserProfile findByCode(String code);
}
