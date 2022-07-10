import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.util.List;


class ServerThread extends Thread {

    Socket clientSocket;
    ServerThread(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    StudentService studentService = StudentService.getInstance();

    @Override
    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;

        try{

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String userInput;

            while(true){
                printUI(out);
                userInput = in.readLine();
                System.out.println("Sent from the client" + clientSocket + " | Message: " + userInput);

                int option;

                if (userInput.equals("quit")){
                    option = 5;
                }else {
                    option = Integer.parseInt(userInput);
                }
                switch (option){
                    case 1:
                        createStudent(in, out);
                        break;
                    case 2:
                        selectStudent(in, out);
                        break;
                    case 3:
                        updateStudent(in, out);
                        break;
                    case 4:
                        deleteStudent(in, out);
                        break;
                    case 5:
                        saveToHDD();
                        break;
                    default:
                        break;
                }


                out.println("\n\nPress any key to continue...");
                in.readLine();
            }

        }catch (IOException e){
            System.out.println("Client disconnected...");
        }catch (StudentNotFoundException e){
            out.println("Student not found");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            }catch (IOException e){
                System.out.println("Client disconnected...");
            }
        }
    }

    void printUI(PrintWriter out){
        out.println("Please choose one of the following operations:");
        out.println("1. Create");
        out.println("2. Read");
        out.println("3. Update");
        out.println("4. Delete");
        out.println("Type \"quit\" to leave...");
    }

    void createStudent(BufferedReader in, PrintWriter out) throws IOException{
        out.println("Student's Name:");
        String name = in.readLine();

        out.println("Student's Major:");
        String major = in.readLine();

        out.println("Student's GPA:");
        double gpa = Double.parseDouble(in.readLine());

        Student student = Student.newInstance();
        student.setName(name);
        student.setMajor(major);
        student.setGpa(gpa);
        studentService.insertStudent(student, false);
        out.println("Student added successfully");
        out.println("=========================================");
    }

    void selectStudent(BufferedReader in, PrintWriter out)throws IOException, StudentNotFoundException{
        out.println("Choose one of the following operations:");
        out.println("1. Select student by Id");
        out.println("2. Select all students");

        int userInput = Integer.parseInt(in.readLine());
        switch (userInput){
            case 1:
                selectStudentById(in, out);
                break;
            case 2:
                selectAllStudents(out);
                break;
        }
    }

    void selectStudentById(BufferedReader in, PrintWriter out) throws IOException, StudentNotFoundException{
        out.println("Student Id: ");
        int id = Integer.parseInt(in.readLine());
        Student student = studentService.selectStudent(id);

        out.println("=========================================");

        if (student != null) {
            out.println(student);
        }else{
            throw new StudentNotFoundException();
        }
        out.println("=========================================");
    }

    void selectAllStudents(PrintWriter out) throws IOException, StudentNotFoundException{
        List<Student> studentList = studentService.selectAllStudents();
        out.println("=========================================");
        out.println( studentList.size() + " results found");
        for(Student student: studentList){
            out.println(student);
        }
        out.println("=========================================");
    }

    void deleteStudent(BufferedReader in, PrintWriter out) throws IOException, StudentNotFoundException{
        out.println("Student Id:");
        int id = Integer.parseInt(in.readLine());

        Student student = studentService.selectStudent(id);
        out.println("=========================================");

        if (student != null) {
            out.println(student);
        }else{
            throw new StudentNotFoundException();
        }

        out.println("Do you want delete this record permanently...");
        out.println("Type \"yes\" to continue...");

        if (in.readLine().trim().equalsIgnoreCase("yes")){
            studentService.deleteStudent(id);
            out.println("Student deleted successfully");
        }
    }

    void updateStudent(BufferedReader in, PrintWriter out) throws IOException, StudentNotFoundException{
        out.println("Student Id:");
        int id = Integer.parseInt(in.readLine());
        Student student = studentService.selectStudent(id);
        out.println("=========================================");

        if (student != null) {
            out.println(student);
        }else{
            throw new StudentNotFoundException();
        }

        out.println("What do you want to change?");
        out.println("1. Name");
        out.println("2. Major");
        out.println("3. GPA");

        int userInput = Integer.parseInt(in.readLine());
        switch (userInput){
            case 1:
                out.println("New Name:");
                String name = in.readLine();
                updateName(student, name);
                break;
            case 2:
                out.println("New Major:");
                String major = in.readLine();
                updateMajor(student, major);
                break;
            case 3:
                out.println("New GPA:");
                double gpa = Double.parseDouble(in.readLine());
                updateGPA(student, gpa);
                break;
        }

    }

    private void updateName(Student student, String name){
        student.setName(name);
        System.out.println("Student updated Successfully...");
    }

    private void updateMajor(Student student, String major){
        student.setMajor(major);
        System.out.println("Student updated Successfully...");
    }

    private void updateGPA(Student student, double gpa){
        student.setGpa(gpa);
        System.out.println("Student updated Successfully...");
    }

    private void saveToHDD()throws IOException, StudentNotFoundException{

        List<Student> students = studentService.selectAllStudents();
        for (Student student: students){
            studentService.insertStudent(student, true);
        }

    }

}