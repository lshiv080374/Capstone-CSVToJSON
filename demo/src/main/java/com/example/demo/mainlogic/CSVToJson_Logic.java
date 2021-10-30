package com.example.demo.mainlogic;

//https://stackoverflow.com/questions/9524191/converting-an-csv-file-to-a-json-object-in-java

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

public class CSVToJson_Logic extends Component {

    private static final long serialVersionUID = 1L;
    //Change your file source path in the following line
    //private static File CSVFile=new File("C:\\Users\\Charu\\Documents\\order.csv"); public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";
    // private static File CSVFile = new File("/Users/lavanyashivram/IdeaProjects/demo/uploads/biostats.csv");
    private static File CSVFile;
    private static BufferedReader read;
    private static BufferedWriter write;

    //String CSVFile=""
    public CSVToJson_Logic() {}

    public File convert(File CSVFile) {
        this.CSVFile = CSVFile;
        File f1=null;
       // System.out.println("Welcome to the logic");
        try {
            read = new BufferedReader(new FileReader(CSVFile));
            String outputName = CSVFile.toString().substring(0,
                    CSVFile.toString().lastIndexOf(".")) + ".json";
            f1 = new File(outputName);
            //write = new BufferedWriter(new FileWriter(new File(outputName)));
            write = new BufferedWriter(new FileWriter(f1));
            //System.out.println("Json file absolute path: " + f1.getAbsolutePath());
            String line;
            String columns[]; //contains column names
            int num_cols;
            String tokens[];

            line = read.readLine();

            // columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            String otherThanQuote = " [^\"] ";
            String quotedString = String.format(" \" %s* \" ", otherThanQuote);
            String regex = String.format("(?x) " + // enable comments, ignore white spaces
                            ",                         " + // match a comma
                            "(?=                       " + // start positive look ahead
                            "  (?:                     " + //   start non-capturing group 1
                            "    %s*                   " + //     match 'otherThanQuote' zero or more times
                            "    %s                    " + //     match 'quotedString'
                            "  )*                      " + //   end group 1 and repeat it zero or more times
                            "  %s*                     " + //   match 'otherThanQuote'
                            "  $                       " + // match the end of the string
                            ")                         ", // stop positive look ahead
                    otherThanQuote, quotedString, otherThanQuote);

            columns = line.split(regex, -1);
            // System.out.println(Arrays.toString(columns));
            num_cols = columns.length;

            // CODE ADDED HERE - START-1
            for (int i = 0; i < num_cols; i++) {
                //This condition checks if it double quotes at the start && at the end.
                if (columns[i] != null && columns[i].startsWith("\"") && columns[i].endsWith("\"")) {
                    columns[i] = columns[i].replace("\"", "");
                }

            }
            //END-1

            //System.out.println("The new arrays "+ Arrays.toString(columns));

            write.write("["); //begin file as array
            write.newLine();
            line = read.readLine();
            while (true) {
                tokens = line.split(regex, -1);

                //System.out.println("Column = " + Arrays.toString(tokens));

                if (tokens.length == num_cols) { //if number columns equal to number entries
                    write.write("\t{");
                    write.newLine();

                    for (int k = 0; k < num_cols; ++k) { //for each column

                        // CODE ADDED HERE - START-2
                        if (tokens[k] != null && tokens[k].startsWith("\"") && tokens[k].endsWith("\"")) {
                            tokens[k] = tokens[k].replace("\"", "");
                        }
                        //END-2

                        if (tokens[k].length() == 0) { //code to handle null values
                            write.write("\t\t\"" + columns[k] + "\": \"\"");
                            if (k < num_cols - 1) write.write(", ");
                            write.newLine();
                        } else if (tokens[k].matches("^-?[0-9]*\\.?[0-9]*$")) { //if a number
                            write.write("\t\t\"" + columns[k] + "\": " + tokens[k]);
                            if (k < num_cols - 1) write.write(", ");
                            write.newLine();
                        } else { //if a string
                            write.write("\t\t\"" + columns[k] + "\": \"" + tokens[k] + "\"");
                            if (k < num_cols - 1) write.write(", ");
                            write.newLine();
                        }

                    }

                    if ((line = read.readLine()) != null) {//if not last line
                        write.write("\t},");
                        write.newLine();
                    } else {
                        write.write("\t}");//if last line
                        write.newLine();
                        write.write("]");
                        write.newLine();
                        break;
                    }
                } else {
                    System.exit(-1); //error message
                }
            }

            write.close();
            read.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f1;
    }
}
