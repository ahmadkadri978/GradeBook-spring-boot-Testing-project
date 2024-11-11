package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.spring5.expression.Mvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ControllerTest {
    static MockHttpServletRequest request;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Mock
    private StudentAndGradeService studentCreateService;
    @BeforeAll
    public static void setup(){
        request  = new MockHttpServletRequest();
        request.setParameter("firstname","chad");
        request.setParameter("lastname","Darby");
        request.setParameter("emailAddress","chad.example@shit.de");

    }
    @BeforeEach
    public void beforeEach()
    {
        jdbcTemplate.execute("insert into student(id,firstname,lastname,email_address)" +
                "values(1,'Eric','Roby','eric.example@shit.com')");
    }
    @AfterEach
    public void AfterEach()
    {
        jdbcTemplate.execute("delete from student");
    }
    @Test
    public void getStudentHttpRequest() throws Exception {
        CollegeStudent student1 = new GradebookCollegeStudent("Eric","Roby","eric.example@shit.com");
        CollegeStudent student2 = new GradebookCollegeStudent("Chad","Darby","chad.example@shit.com");
        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(student1,student2));
        when(studentCreateService.getAllStudents()).thenReturn(collegeStudentList);
        assertIterableEquals(collegeStudentList , studentCreateService.getAllStudents());


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav , "index");
    }
    @Test
    public void createStudentTest() throws Exception {
        CollegeStudent student1 = new GradebookCollegeStudent("Eric","Roby","eric.example@shit.com");
        CollegeStudent student2 = new GradebookCollegeStudent("Chad","Darby","chad.example@shit.com");
        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(student1,student2));
        when(studentCreateService.getAllStudents()).thenReturn(collegeStudentList);
        assertIterableEquals(collegeStudentList , studentCreateService.getAllStudents());

        MvcResult mvcResult = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname",request.getParameterValues("firstname"))
                .param("lastname", request.getParameterValues("lastname"))
                .param("emailAddress" , request.getParameterValues("emailAddress")))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav,"index");
        CollegeStudent verifyStudent = studentDao.findByEmailAddress("chad.example@shit.de");
        assertNotNull(verifyStudent,"student should be found");
    }
    @Test
    public void deleteStudentTest() throws Exception {
        assertTrue(studentDao.findById(1).isPresent());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}",1))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav,"index");
        assertFalse(studentDao.findById(1).isPresent());
    }
    @Test
    public void deleteStudentError() throws Exception {
        MvcResult mvcResult =mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}",0))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav,"error");
    }
}
