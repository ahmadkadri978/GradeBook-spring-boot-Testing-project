package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.HistoryGradeDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {
    private final StudentDao studentDao;
    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;
    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;
    @Autowired
    private ScienceGrade scienceGrade;
    @Autowired
    private MathGradeDao mathGradeDao;
    @Autowired
    private ScienceGradeDao scienceGradeDao;
    @Autowired
    private HistoryGradeDao historyGradeDao;

    public StudentAndGradeService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void createStudent(String firstname ,String lastname , String email){
        CollegeStudent student = new CollegeStudent(firstname,lastname,email);
        studentDao.save(student);
    }

    public boolean chekIfStudentIsNull(int i) {
        Optional<CollegeStudent> student = studentDao.findById(i);
        if(student.isPresent()) return true;
        return false;
    }

    public void deleteStudent(int i) {
        Optional<CollegeStudent> student = studentDao.findById(i);
        if(student.isPresent())
        studentDao.deleteById(i);

    }

    public Iterable<CollegeStudent> getAllStudents() {
        Iterable<CollegeStudent> students = studentDao.findAll();
        return  students;
    }

    public boolean createGrade(double grade, int studentId, String gradeType) {
        if(!chekIfStudentIsNull(studentId)) return false;
        if(grade>=0 && grade<=100){
            if (gradeType.equals("math")){
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(studentId);
                mathGradeDao.save(mathGrade);
            }
            if (gradeType.equals("science")){
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(studentId);
                scienceGradeDao.save(scienceGrade);

            }
            if(gradeType.equals("history")){
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(studentId);
                historyGradeDao.save(historyGrade);

            }
            return true;
        }
        else return false;

    }
}
