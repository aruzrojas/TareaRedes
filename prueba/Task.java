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

public class Task implements Runnable {
 
    private int num;
 
    public Task(int n) {
        num = n;
    }
 
    public void run() {
        System.out.println("Task " + num + " is running.");
    }
}