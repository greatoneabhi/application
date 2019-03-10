/*package com.application.controller;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.application.common.CommonConstants;
import com.application.entity.UserEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class TestUserController {

    private static String GUID = "ca1b7fad-bbf1-4758-bf8f-9bf25a5e20af";
    private static String FIRST_NAME = "Test";
    private static String LAST_NAME = "User";
    private static String EMAIL_ID = "testuser@abc.com";
    private static String MESSAGE = "test message";
    private static String PHONE_NUMBER = "23424";

    private UserEntity user;

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = createEntity(GUID, FIRST_NAME, LAST_NAME, EMAIL_ID, MESSAGE,
                PHONE_NUMBER);
    }

    UserEntity createEntity(final String guid, final String firstName,
            final String lastName, final String emailId, final String message,
            final String phoneNumber) {
        UserEntity entity = new UserEntity();
        entity.setGuid(guid);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmailId(emailId);
        entity.setMessage(message);
        entity.setPhoneNumber(phoneNumber);
        return entity;
    }

    @Test
    public void testCreateUserSuccessful() {
        Mockito.when(userRepository.exists(Matchers.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userController.createUser(user);
        Assert.assertEquals(result.getGuid(), GUID);
    }

    @Test
    public void testCreateUserAlreadyExists() {
        Mockito.when(userRepository.exists(Matchers.anyString())).thenReturn(true);
        UserEntity result = null;
        try {
            result = userController.createUser(user);
        } catch (DataIntegrityViolationException e) {
            Assert.assertEquals(e.getMessage(), CommonConstants.RESOURCE_ALREADY_EXISTS);
        }
        Assert.assertNull(result);
    }

    @Test
    public void testGetUserSuccess() {
        Mockito.when(userRepository.findOne(GUID)).thenReturn(user);
        UserEntity result = userController.getUser(GUID);
        Assert.assertEquals(result.getGuid(), GUID);
        Assert.assertEquals(result.getFirstName(), FIRST_NAME);
        Assert.assertEquals(result.getLastName(), LAST_NAME);
        Assert.assertEquals(result.getEmailId(), EMAIL_ID);
        Assert.assertEquals(result.getPhoneNumber(), PHONE_NUMBER);
        Assert.assertEquals(result.getMessage(), MESSAGE);
    }

    @Test
    public void testGetUserNotFound() {
        Mockito.when(userRepository.findOne(GUID)).thenReturn(null);

        UserEntity result = null;
        try {
            result = userController.getUser(GUID);
        } catch (ResourceNotFoundException e) {
            Assert.assertEquals(e.getMessage(), CommonConstants.RESOURCE_NOT_FOUND);
        }
        Assert.assertNull(result);
    }
}
*/