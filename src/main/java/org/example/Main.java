package org.example;


import org.example.model.FirstTask;
import org.example.model.SecondTask;

public class Main {
    public static void main(String[] args) throws Exception {


        SecondTask st = new SecondTask();
        st.sortAndWriteFineStat("src/main/resources/json");
        FirstTask ft = new FirstTask();
        ft.concatNameAndSurname("src/main/resources/xml/person.xml");

    }
}
