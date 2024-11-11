package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.HistoryGradeDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {
    @Autowired
    StudentAndGradeService studentService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    StudentDao studentDao;
    @Autowired
    private MathGradeDao mathGradeDao;
    @Autowired
    private ScienceGradeDao scienceGradeDao;
    @Autowired
    private HistoryGradeDao historyGradeDao;

    @BeforeEach
    public void setUpDatabase()
    {
        jdbcTemplate.execute("insert into student(id,firstname,lastname,email_address) " +
                "values(1,'Eric','Roby','Eric@example.com')");
        jdbcTemplate.execute("insert into student(id,firstname,lastname,email_address) " +
                "values(2,'Ahmad','kadri','ahmad@example.com')");
        jdbcTemplate.execute("insert into math_grade(id,student_id,grade) values (1,1,77.00)");
        jdbcTemplate.execute("insert into history_grade(id,student_id,grade) values (1,1,77.00)");
        jdbcTemplate.execute("insert into science_grade(id,student_id,grade) values (1,1,77.00)");

    }
    @Test
    public void createStudentService(){
        studentService.createStudent("ahmad","kadri","kadri@example.com");
        CollegeStudent student = studentDao.findByEmailAddress("kadri@example.com");
        assertEquals("kadri@example.com",student.getEmailAddress());

    }
    @Test
    public void isStudentNull(){
        assertTrue(studentService.checkIfStudentIsNull(1));
        assertTrue(studentService.checkIfStudentIsNull(2));
        assertFalse((studentService.checkIfStudentIsNull(0)));
    }
    @Test
    public void deleteStudentService()
    {
        Optional<CollegeStudent> student = studentDao.findById(1);
        assertTrue(student.isPresent() , "return True");
        studentService.deleteStudent(1);
        student = studentDao.findById(1);
//        assertNull(student , " should be Null");
        assertFalse(student.isPresent() , " return False");
    }
    @Sql("/insertData.sql")
    @Test
    public void getAllStudent()
    {
        Iterable<CollegeStudent> students =studentService.getAllStudents();
        List<CollegeStudent> students1 = new ArrayList<>();
        for (CollegeStudent collegeStudent:students)
        {
            students1.add(collegeStudent);
        }
        assertEquals(5 ,students1.size(), "should be equal");
    }
    @Test
    public void createGradeService()
    {
        //create the grade
        assertTrue(studentService.createGrade(80.50,1,"math"));
        assertTrue(studentService.createGrade(75.55,1,"science"));
        assertTrue(studentService.createGrade(77.20 , 1,"history"));
        //get all grades with studentId
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(1);
        assertTrue(((Collection<MathGrade>) mathGrades).size() == 2,"student have 2 math grades");
        assertTrue(((Collection<ScienceGrade>) scienceGrades).size() == 2,"student have 2 science grades");
        assertTrue(((Collection<HistoryGrade>) historyGrades).size() == 2, "student have 2 history grades" );

    }
    @Test
    public void createGradeServiceReturnFalse(){
        assertFalse(studentService.createGrade(105,1,"math"));
        assertFalse(studentService.createGrade(100,3,"math"));
        assertFalse(studentService.createGrade(-5,1,"math"));
        assertFalse(studentService.createGrade(100,1,"literature"));
    }
    @AfterEach
    public void clearDatabase()
    {
        jdbcTemplate.execute("Delete from student");
        jdbcTemplate.execute("Delete from math_grade");
        jdbcTemplate.execute("Delete from history_grade");
        jdbcTemplate.execute("Delete from science_grade");
    }
}
