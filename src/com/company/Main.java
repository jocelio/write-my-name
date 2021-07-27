package com.company;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static com.company.Serializer.deserialize;
import static com.company.Serializer.serialize;
import static java.time.LocalDate.now;

public class Main {

    final static String datesFile =  System.getProperty("user.dir")+"_commit_sheet";
    final static String commitsFile =  System.getProperty("user.dir")+"/commit_lists";

    public static void main(String[] args) {

        Optional<ArrayList<LocalDate>> dates = deserialize(datesFile);
        Optional<ArrayList<LocalDate>> commitsList = deserialize(commitsFile);

        if(dates.isPresent()){
            System.out.println(dates.get());

            var today = now();
            // if today is commit day
            if(dates.get().stream().anyMatch(d -> d.isEqual(today))){
                System.out.println("fazendo a magica");
                //modify the commits_list with todays date
                if(commitsList.isPresent()){
                    var localDates = commitsList.get();
                    localDates.add(today);
                    serialize(localDates, System.getProperty("user.dir")+"/commit_lists");
                }else {
                    var list = new ArrayList<LocalDate>();
                    list.add(today);
                    serialize(list, System.getProperty("user.dir")+"/commit_lists");
                }

                try {
                    System.out.println("rodando script");
                    runScript();
                    System.out.println("rodou script");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else{
            new GUI(datesFile);
        }
    }

    public static void runScript() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("./commiter.sh");
        //Sets the source and destination for subprocess standard I/O to be the same as those of the current Java process.
        processBuilder.inheritIO();
        Process process = processBuilder.start();

        int exitValue = process.waitFor();
        if (exitValue != 0) {
            // check for errors
            new BufferedInputStream(process.getErrorStream());
            throw new RuntimeException("execution of script failed!");
        }
    }
}
