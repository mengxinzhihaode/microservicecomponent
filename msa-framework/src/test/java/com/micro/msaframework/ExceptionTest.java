package com.micro.msaframework;

import java.io.File;

public class ExceptionTest {
    public static void main(String[] args) {
        ExceptionTest test = new ExceptionTest();

        File outFile = new File("D:\\1111.zip");
        test.add(outFile);

    }

    public int add(File file){
        try{
            return 1;
        }catch (Exception ex){
            throw  ex;
        }finally {
            file.delete();
            System.out.println(166);
        }
    }
}
