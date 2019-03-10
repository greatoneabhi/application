package com.application.test.common;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidatorContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.application.common.GuidValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class TestGuidValidator {

    private String guid = null;

    @InjectMocks
    GuidValidator guidValidator;

    @Mock
    HttpServletRequest request;

    @Mock
    ConstraintValidatorContext context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        guid = "fdea128e-cfa4-42ed-9513-d8b5fd814309";
    }

    @Test
    public void testIsValidWhenRequestMethodIsPOST() {
        Mockito.when(request.getMethod()).thenReturn("POST");

        Boolean result = guidValidator.isValid(guid, context);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsValidWhenRequestMethodIsPOSTWithNullGuid() {
        Mockito.when(request.getMethod()).thenReturn("POST");
        guid = null;
        Boolean result = guidValidator.isValid(guid, context);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsValidWhenRequestMethodIsPUT() {
        Mockito.when(request.getMethod()).thenReturn("PUT");
        guid = null;
        Boolean result = guidValidator.isValid(guid, context);
        Assert.assertTrue(result);

    }

}
