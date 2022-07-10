import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    private static final int CACHE_SIZE = 10;
    private static final String FILE_NAME = "students.dat";
    private static final String DELIMITER = "///!///";

    private static StudentDAOImpl instance;


    private StudentDAOImpl(){};

    public static StudentDAOImpl getInstance() {
        if (instance == null){
            instance = new StudentDAOImpl();
        }
        return instance;
    }

    Cache<Integer, Student> cache = Cache.getInstance(CACHE_SIZE);
    FileService fileService = new FileService();


    @Override
    public void insertStudent(Student student, boolean toFile) throws IOException {
        if (toFile) {
            fileService.writeStudent(FILE_NAME, student, student.getId());
        } else {
            // Write least recently used entry to the file system when the cache is full
            if ((cache.size() == cache.getSize())) {
                Student studentToBeRemoved = cache.entrySet().iterator().next().getValue();
                fileService.writeStudent(FILE_NAME, studentToBeRemoved, studentToBeRemoved.getId());
            }
            cache.put(student.getId(), student);
        }
    }

    @Override
    public boolean updateStudent(Student student) {
        return cache.put(student.getId(), student) != null;
    }

    @Override
    public Student selectStudent(int id) throws IOException, StudentNotFoundException {
        if (cache.containsKey(id)){
            return cache.get(id);
        }else{
            Student student = fileService.readStudent(FILE_NAME, id, DELIMITER);
            if (student == null){
                throw new StudentNotFoundException();
            }
            if (student.getId() == id){
                cache.put(student.getId(), student);
                return student;
            }else{
                throw new StudentNotFoundException();
            }
        }
    }

    @Override
    public List<Student> selectAllStudents() throws IOException, StudentNotFoundException {
        List<Student> studentsList = new ArrayList<>();

        studentsList.addAll(cache.values());

        int numLines = fileService.numLines(FILE_NAME);
        for (int id = 1; id <= numLines; id++){
            if (!cache.containsKey(id)) {
                Student student = fileService.readStudent(FILE_NAME, id, DELIMITER);
                if (student != null) {
                    studentsList.add(student);
                }
            }
        }
        return studentsList;
    }

    @Override
    public void deleteStudent(int id) throws IOException, StudentNotFoundException {
        if (cache.containsKey(id)) {
            cache.remove(id);
        }



    }
}
