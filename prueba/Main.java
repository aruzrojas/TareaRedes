
import java.util.Scanner;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.IOException;

import java.lang.Thread;
import java.net.Socket;


public class Main {
 
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(7);
 
        for (int i = 0; i < 50; i++) {
            Task task = new Task(i);
            pool.execute(task);
        }
    }
}