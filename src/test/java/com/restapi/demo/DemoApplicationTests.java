package com.restapi.demo;

import com.restapi.demo.entity.UserInfo;
import com.restapi.demo.repository.UserInfoRepository;
import com.restapi.demo.service.UserInfoService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = DemoApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
		locations = "classpath:application.properties")
class DemoApplicationTests {


	@Autowired
	private PasswordEncoder mvc;

	@InjectMocks
	private UserInfoService service;

	@InjectMocks
	private UserInfo userInfo;

	@Autowired
	private UserInfoRepository repository;

	@Test
	void addUserStatus()
	{

		userInfo.setName("mock");
		userInfo.setPassword("mocking");
		userInfo.setRoles("ROLE_USER");
		ReflectionTestUtils.setField(service,"encoder",mvc);
		ReflectionTestUtils.setField(service,"repository",repository);

		service.addUser(userInfo);
		Assert.assertNotNull(service.loadUserByUsername("mock"));
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities=Arrays.stream("ROLE_USER".split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		Assert.assertEquals(authorities,service.loadUserByUsername("mock").getAuthorities());


	}
}
