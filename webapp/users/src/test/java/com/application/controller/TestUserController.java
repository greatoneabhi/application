package com.application.controller;

import com.application.common.CommonConstants;
import com.application.entity.UserEntity;
import com.application.entity.UserKey;
import com.application.model.User;
import com.application.repository.UserRepository;
import com.application.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestUserController {

    private static final String USERNAME = "testUsername";
    private static final String NAME = "Test";
    private static final String EMAIL_ID = "testuser@abc.com";
    private static final String PASSWORD = "abc@123";
    private static final String PHONE_NUMBER = "23424";

    private User user;

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Before
    public void setUp() {
        UserServiceImpl userService = new UserServiceImpl();
        ReflectionTestUtils.setField(userController, "userServiceImpl", userService);
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ReflectionTestUtils.setField(userService, "objectMapper", objectMapper);
        user = userModel(USERNAME, NAME, EMAIL_ID, PASSWORD, PHONE_NUMBER);
    }

    @Test
    public void testCreateUserSuccessful() {
        when(userRepository.findByPrimaryKeyUsername(anyString())).thenReturn(null);
        UserEntity userEntity = new UserEntity();
        UserKey primaryKey = new UserKey();
        primaryKey.setEmailId(EMAIL_ID);
        primaryKey.setUsername(USERNAME);
        userEntity.setPrimaryKey(primaryKey);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        String result = userController.createUser(user);
        Assert.assertEquals("User create failed", result, user.getUsername());

        verify(userRepository, times(1))
                .findByPrimaryKeyUsername(anyString());
        verify(userRepository, times(1))
                .save(any(UserEntity.class));
    }

    @Test
    public void testGetUserSuccess() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(NAME);
        UserKey primaryKey = new UserKey();
        primaryKey.setUsername(USERNAME);
        primaryKey.setEmailId(EMAIL_ID);
        userEntity.setPrimaryKey(primaryKey);
        ArrayList<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(userEntity);
        when(userRepository.findByPrimaryKeyUsername(anyString())).thenReturn(userEntities);

        UserEntity result = userController.getUser(USERNAME);
        Assert.assertEquals("Get user failed", result.getPrimaryKey().getUsername(), USERNAME);

        verify(userRepository, times(1)).findByPrimaryKeyUsername(anyString());
    }

    @Test
    public void testGetUserNotFound() {
        when(userRepository.findByPrimaryKeyUsername(anyString())).thenReturn(null);

        UserEntity result = null;
        try {
            result = userController.getUser(USERNAME);
        } catch (ResourceNotFoundException e) {
            Assert.assertEquals(e.getMessage(), CommonConstants.RESOURCE_NOT_FOUND);
        }
        Assert.assertNull(result);
    }

    private User userModel(final String username, final String name,
                           final String emailId, final String password,
                           final String phoneNumber) {
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setEmailId(emailId);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        return user;
    }


    /*
     * @Test public void testCreateUserAlreadyExists() {
     * Mockito.when(userRepository.exists(Matchers.anyString())).thenReturn(true);
     * UserEntity result = null; try { result =
     * userController.createUser(userEntity); } catch
     * (DataIntegrityViolationException e) { Assert.assertEquals(e.getMessage(),
     * CommonConstants.RESOURCE_ALREADY_EXISTS); } Assert.assertNull(result); }
     *
     * @Test public void testGetUserSuccess() {
     * Mockito.when(userRepository.findOne(GUID)).thenReturn(userEntity); UserEntity
     * result = userController.getUser(GUID);
     * Assert.assertEquals(result.getEmailId(), EMAIL_ID);
     * Assert.assertEquals(result.getPhoneNumber(), PHONE_NUMBER); }
     *
     * @Test public void testGetUserNotFound() {
     * Mockito.when(userRepository.findOne(GUID)).thenReturn(null);
     *
     * UserEntity result = null; try { result = userController.getUser(GUID); }
     * catch (ResourceNotFoundException e) { Assert.assertEquals(e.getMessage(),
     * CommonConstants.RESOURCE_NOT_FOUND); } Assert.assertNull(result); }
     */
}
