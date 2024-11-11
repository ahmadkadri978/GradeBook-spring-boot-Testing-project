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

import java.util.ArrayList;
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
        jdbcTemplate.execute("insert into student(id,firstname,lastname) " +
                "values(1,'Eric','kadri')");
        jdbcTemplate.execute("insert into student(id,firstname,lastname) " +
                "values(2,'Ericop','kadri')");

    }
    @Test
    public void createStudentService(){
        studentService.createStudent("ahmad" , "kadri", "ahmad@example.com");
        CollegeStudent student = studentDao.findByEmailAddress("ahmad@example.com");
        assertEquals("ahmad@example.com" , student.getEmailAddress() , "Email should match");

    }
    @Test
    public void isStudentNull(){
        assertTrue(studentService.chekIfStudentIsNull(1));
        assertFalse((studentService.chekIfStudentIsNull(0)));
    }
    @Test
    public void deleteService()
    {
        Optional<CollegeStudent> student = studentDao.findById(1);
        assertTrue(student.isPresent() , "return FUCKing TrUe");
        studentService.deleteStudent(1);
        student = studentDao.findById(1);
//        assertNull(student , " should be Null");
        assertFalse(student.isPresent() , " return False");
    }
    @Test
    public void getAllStudent()
    {
        Iterable<CollegeStudent> students =studentService.getAllStudents();
        List<CollegeStudent> students1 = new ArrayList<>();
        for (CollegeStudent collegeStudent:students)
        {
            students1.add(collegeStudent);
        }
        assertEquals(2 ,students1.size(), "should be equal");
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
        assertTrue(mathGrades.iterator().hasNext(),"student has math grades");
        assertTrue(scienceGrades.iterator().hasNext(),"student has science grades");
        assertTrue(historyGrades.iterator().hasNext());


    }
    @AfterEach
    public void clearDatabase()
    {
        jdbcTemplate.execute("Delete from student");
    }
}
