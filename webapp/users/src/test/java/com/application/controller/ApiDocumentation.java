package com.application.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.application.Application;
import com.application.model.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.application.entity.UserEntity;
import com.application.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ApiDocumentation {

    private static final String USERNAME = "testUsername";
    private static final String NAME = "Test";
    private static final String EMAIL_ID = "testuser@abc.com";
    private static final String PASSWORD = "abc@123";
    private static final String PHONE_NUMBER = "23424";
    private static String GUID = "ca1b7fad-bbf1-4758-bf8f-9bf25a5e2003";
    private static String FIRST_NAME = "Test";
    private static String LAST_NAME = "User";
    private static String MESSAGE = "test message";
    private static final String INDEX_FILE = "src/main/asciidoc/userIndex.adoc";

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static boolean doCreate = true;

    private UserEntity user;
    private User user1;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("src/main/asciidoc");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    UserEntity createEntity(final String guid, final String firstName, final String lastName, final String emailId,
            final String message, final String phoneNumber, final String password) {
        UserEntity entity = new UserEntity();
        entity.setName(firstName);
        entity.setPhoneNumber(phoneNumber);
        entity.setPassword(password);
        return entity;
    }

    public synchronized void ceateIndexAdocFile() {
        if (doCreate) {
            File indexDoc = new File(INDEX_FILE);
            if (indexDoc.exists()) {
                indexDoc.delete();
            }
            try {

                indexDoc.createNewFile();
                FileWriter fw = new FileWriter(indexDoc.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("= Reference Documentation\n");
                bw.write(":toc: left\n\n");

                bw.write("This is documentation for rest API's\n\n");

                bw.close();
            } catch (IOException e) {
                log.warn("Error creating index.adoc file required for Doumentation");
            }
        }
        doCreate = false;
    }

    private void addIncludeIndex(String heading, String methodName, boolean isResponseField) {
        File indexDoc = new File(INDEX_FILE);
        FileWriter fw;
        try {
            fw = new FileWriter(indexDoc.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(heading+"\n\n");
            bw.write("=== Curl Command\n\n");
            bw.write("include::" + methodName + "/curl-request.adoc[]\n\n");
            bw.write("=== Http Request\n\n");
            bw.write("include::" + methodName + "/http-request.adoc[]\n\n");
            bw.write("=== Http Response\n\n");
            bw.write("include::" + methodName + "/http-response.adoc[]\n\n");
            bw.write("=== Httpie Request\n\n");
            bw.write("include::" + methodName + "/httpie-request.adoc[]\n\n");
            bw.write("=== Request Body\n\n");
            bw.write("include::" + methodName + "/request-body.adoc[]\n\n");
            bw.write("=== Request Fields\n\n");
            bw.write("include::" + methodName + "/request-fields.adoc[]\n\n");
            if (isResponseField) {
                bw.write("=== Response Fields\n\n");
                bw.write("include::" + methodName + "/response-fields.adoc[]\n\n");
            }

            bw.close();
        } catch (IOException e) {
            log.warn("Error updating index.adoc with include macro");
        }
    }

    @Before
    public void setUp() {
        ceateIndexAdocFile();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build();
        user = createEntity(GUID, FIRST_NAME, LAST_NAME, EMAIL_ID, MESSAGE, PHONE_NUMBER, PASSWORD);
        user1 = userModel(USERNAME, NAME, EMAIL_ID, PASSWORD, PHONE_NUMBER);
    }

    @After
    public void tearDown() {
        //userRepository.delete(EMAIL_ID);
    }

    @Test
    public void createUser() throws Exception {
        this.mockMvc
                .perform(post("/applications/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(fieldWithPath("username").description("Username of the user"),
                                fieldWithPath("name").description("Name of the user"),
                                fieldWithPath("emailId").description("User Email Id"),
                                fieldWithPath("password").description("Password"),
                                fieldWithPath("phoneNumber").description("Phone number of the user"))));

        addIncludeIndex("== Create User request", "createUser", false);
    }

    //@Test
    public void getUser() throws Exception {

        this.mockMvc.perform(post("/applications/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(user))).andExpect(status().isCreated());

        this.mockMvc.perform(get("/applications/api/v1/users/" + GUID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        responseFields(fieldWithPath("firstName").description("First name of the user"),
                                fieldWithPath("emailId").description("Email Id of the user"),
                                fieldWithPath("phoneNumber").description("Phone Number of the user"),
                                fieldWithPath("password").description("Password"))));

        addIncludeIndex("== Get user request", "getUser", true);

    }

    //@Test
    public void getUserNotFound() throws Exception {
        this.mockMvc
                .perform(get("/applications/api/v1/users/ca1b7fad-bbf1-4758-bf8f-9bf25a5e20a1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(document("{methodName}"));

        addIncludeIndex("== Get user not found", "getUserNotFound", false);
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
}
