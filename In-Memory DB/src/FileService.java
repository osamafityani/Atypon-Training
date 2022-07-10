import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileService {
    private static final int CHARACTERS_PER_LINE = 50;  // The total length of a student object when converted to string
    private static FileService instance;

    public static FileService getInstance(){
        if (instance == null){
            instance = new FileService();
        }
        return instance;
    }

    public Student readStudent(String fileName, int line, String delimiter) throws  IOException, StudentNotFoundException{
        File file = new File(fileName);
        String data = "";

        // Adding two bytes to the total length because of the '\n' character at the end of the line
        int bytesPerLine = CHARACTERS_PER_LINE + 2;

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.seek(bytesPerLine * line);
        data = randomAccessFile.readLine();

        if (data == null || data.trim().equals("deleted") ){
            throw new StudentNotFoundException();
        }

        String[] studentInformation = data.split("\\|");

        int id = Integer.parseInt(studentInformation[0]);
        String name = studentInformation[1].trim();
        String major = studentInformation[2].trim();
        double gpa = Double.parseDouble(studentInformation[3].trim());

        Student student = Student.newInstance();
        student.setName(name);
        student.setMajor(major);
        student.setGpa(gpa);
        student.setId(id);

        return student;
    }

    public void writeStudent(String fileName, Student student, int line) throws IOException {
        File file = new File(fileName);

        String data = String.format("%03d", student.getId()) + "|" + String.format("%15s", student.getName()) + "|"
                + String.format("%15s", student.getMajor()) + "|" + Double.toString(student.getGpa()) + "|";

        // Adding two bytes to the total length because of the '\n' character at the end of the line
        int bytesPerLine = CHARACTERS_PER_LINE + 2;

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        long cursor;

        if (line == -1){
            cursor = randomAccessFile.length();
        }else{
            cursor = bytesPerLine * line;
        }

        randomAccessFile.seek(cursor);
        randomAccessFile.write(data.getBytes());
        randomAccessFile.writeBytes(System.getProperty("line.separator"));
        randomAccessFile.close();
    }



    public int numLines(String fileName) throws IOException {
        File file = new File(fileName);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

        return (int) (randomAccessFile.length() / CHARACTERS_PER_LINE);
    }
}
