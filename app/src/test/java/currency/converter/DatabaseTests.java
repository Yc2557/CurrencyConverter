package currency.converter;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.io.FileNotFoundException;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class DatabaseTests {

    JSONObject database;

    FileReader fr;
    @BeforeAll
    static void buildSampleDatabase() {
        try {
            File file = new File("database.json");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            // fr.read(file);
            // this.database = database;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void test() {

    }


}
