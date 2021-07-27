package com.company;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class Serializer {

    public static void serialize (Object object, String path){
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(object);
            out.close();
            fileOut.close();
            System.out.println("\nSerialization Successful... Checkout your specified output file..\n");
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static Optional deserialize (String path){
        // Let's deserialize an Object
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            var dates = (ArrayList<LocalDate>) in.readObject();
            in.close();
            fileIn.close();
            return Optional.of(dates);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
