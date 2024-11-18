package com.luv2code.springmvc.service;

<<<<<<< HEAD
import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
=======
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.HistoryGradeDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradeDao;
>>>>>>> dc2358e0a179e6bb4fb7194124888d741177c440
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
>>>>>>> dc2358e0a179e6bb4fb7194124888d741177c440
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {
<<<<<<< HEAD

    @Autowired
    private StudentDao studentDao;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    private MathGradesDao mathGradeDao;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @Autowired
    StudentGrades studentGrades;

    public void createStudent(String firstname, String lastname, String emailAddress) {
        CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);
        student.setId(0);
        studentDao.save(student);
    }

    public boolean checkIfStudentIsNull(int id) {
        Optional<CollegeStudent> student = studentDao.findById(id);
        if (student.isPresent()) {
            return true;
        }
        return false;
    }

    public void deleteStudent(int id) {
        if (checkIfStudentIsNull(id)) {
            studentDao.deleteById(id);
            mathGradeDao.deleteByStudentId(id);
            scienceGradeDao.deleteByStudentId(id);
            historyGradeDao.deleteByStudentId(id);
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        Iterable<CollegeStudent> collegeStudents = studentDao.findAll();
        return collegeStudents;
    }

    public boolean createGrade(double grade, int studentId, String gradeType) {

        if (!checkIfStudentIsNull(studentId)) {
            return false;
        }

        if (grade >= 0 && grade <= 100) {
            if (gradeType.equals("math")) {
=======
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
>>>>>>> dc2358e0a179e6bb4fb7194124888d741177c440
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(studentId);
                mathGradeDao.save(mathGrade);
<<<<<<< HEAD
                return true;
            }
            if (gradeType.equals("science")) {
=======
            }
            if (gradeType.equals("science")){
>>>>>>> dc2358e0a179e6bb4fb7194124888d741177c440
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(studentId);
                scienceGradeDao.save(scienceGrade);
<<<<<<< HEAD
                return true;
            }
            if (gradeType.equals("history")) {
=======

            }
            if(gradeType.equals("history")){
>>>>>>> dc2358e0a179e6bb4fb7194124888d741177c440
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(studentId);
                historyGradeDao.save(historyGrade);
<<<<<<< HEAD
                return true;
            }
        }

        return false;
    }

    public int deleteGrade(int id, String gradeType) {
        int studentId = 0;

        if (gradeType.equals("math")) {
            Optional<MathGrade> grade = mathGradeDao.findById(id);
            if (!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            mathGradeDao.deleteById(id);
        }
        if (gradeType.equals("science")) {
            Optional<ScienceGrade> grade = scienceGradeDao.findById(id);
            if (!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            scienceGradeDao.deleteById(id);
        }
        if (gradeType.equals("history")) {
            Optional<HistoryGrade> grade = historyGradeDao.findById(id);
            if (!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            historyGradeDao.deleteById(id);
        }

        return studentId;
    }

    public GradebookCollegeStudent studentInformation(int id) {

        Optional<CollegeStudent> student = studentDao.findById(id);
        if(student.isPresent()) {
            Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(id);
            Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(id);
            Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(id);

            List<Grade> mathGradesList = new ArrayList<>();
            mathGrades.forEach(mathGradesList::add);

            List<Grade> scienceGradesList = new ArrayList<>();
            scienceGrades.forEach(scienceGradesList::add);

            List<Grade> historyGradesList = new ArrayList<>();
            historyGrades.forEach(historyGradesList::add);

            studentGrades.setMathGradeResults(mathGradesList);
            studentGrades.setScienceGradeResults(scienceGradesList);
            studentGrades.setHistoryGradeResults(historyGradesList);

            GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(student.get().getId(),
                    student.get().getFirstname(), student.get().getLastname(), student.get().getEmailAddress(),
                    studentGrades);
            return gradebookCollegeStudent;
        }

        return null;
=======

            }
            return true;
        }
        else return false;

>>>>>>> dc2358e0a179e6bb4fb7194124888d741177c440
    }
}
