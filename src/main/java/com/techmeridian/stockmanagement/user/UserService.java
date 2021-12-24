package com.techmeridian.stockmanagement.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.techmeridian.stockmanagement.security.EncryptionUtils;
import com.techmeridian.stockmanagement.session.SessionUtils;

@Service("userService")
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CredentialsRepository credentialsRepository;

	@Autowired
	private UserCompanyRepository userCompanyRepository;

	public User authenticate(User user) throws Exception {
		User authUser = userRepository.findByUserName(user.getUserName());
		String encPwd = EncryptionUtils.getSecurePassword(user.getPassword());
		Credentials credentials = credentialsRepository.findByUserAndPassword(authUser, encPwd);
		if (credentials == null) {
			throw new Exception("User invalid");
		}
		List<UserCompany> userCompanies = userCompanyRepository.findByUser(authUser);
		authUser.setCompanies(userCompanies.stream().map(UserCompany::getCompany).collect(Collectors.toList()));
		return authUser;
	}

	public String changePassword(User user, String sessionId) throws Exception {
		if (StringUtils.isEmpty(user.getPassword())) {
			throw new Exception("Password is mandatory field");
		}
		if (user.getPassword().trim().length() < 5) {
			throw new Exception("Password must be greater than or equal to 5 characters.");
		}
		User sessionUser = SessionUtils.getInstance().getUser(sessionId);
		User dbUser = userRepository.findByUserName(sessionUser.getUserName());

		String encPwd = EncryptionUtils.getSecurePassword(user.getPassword());

		Credentials credentials = credentialsRepository.findByUser(dbUser);
		credentials.setPassword(encPwd);
		credentialsRepository.save(credentials);
		return "User " + sessionUser.getUserName() + " password change succesfull.";
	}

	public String resetPassword(User user) throws Exception {
		if (StringUtils.isEmpty(user.getUserName())) {
			throw new Exception("User name is mandatory field");
		}
		User dbUser = userRepository.findByUserName(user.getUserName());
		if (dbUser == null) {
			throw new Exception("User " + user.getUserName() + " not found.");
		}

		String encPwd = EncryptionUtils.getSecurePassword("changemenow");

		Credentials credentials = credentialsRepository.findByUser(dbUser);
		credentials.setPassword(encPwd);
		credentialsRepository.save(credentials);
		return "User " + user.getUserName() + " password reset succesfull.";
	}
}
