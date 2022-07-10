import java.io.IOException;
import java.util.List;

public interface StudentDAO {

    // Create student
    void insertStudent(Student student, boolean toFile) throws IOException;

    // Update student
    boolean updateStudent(Student student) throws StudentNotFoundException;

    // Select student by id
    Student selectStudent(int id) throws IOException, StudentNotFoundException;

    // Select students
    List<Student> selectAllStudents() throws IOException, StudentNotFoundException;

    // Delete student
    void deleteStudent(int id) throws IOException, StudentNotFoundException;

}
