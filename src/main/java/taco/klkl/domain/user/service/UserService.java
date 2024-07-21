package taco.klkl.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.User;

@Slf4j
@Service
@Transactional
public class UserService {
	@Autowired
	UserRepository userRepository;

	/**
	 * 임시 나의 정보 조회
	 * Id 1번 유저를 반환합니다.
	 */
	public User me() {
		return userRepository.getReferenceById(1L);
	}

	/**
	 * @param user user object
	 * @return Long user_id
	 */
	public Long join(User user) {
		userRepository.save(user);
		return user.getId();
	}
}
