package SHA1;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;

public class Main {
     public static void  main (String [] args) throws IOException {
         FileInputStream fileIn = new FileInputStream("text.txt");
         FileWriter fileOut = new FileWriter("text1.txt");
         ArrayList <Integer> b = new ArrayList<Integer>();
         while (fileIn.available() > 0)
             b.add(fileIn.read());
         Sha s = new Sha(b);
         String h = s.getHash();
         System.out.println(h);
         fileOut.write(h);

         fileIn.close();
         fileOut.close();
         //0x5B0E608526323C55
         int a=0x5B0E6085;
         int a1 =0x26323C55;
         //int [] v= new int[]{15, 0, 9, 6, 10, 5, 11,4,12,3,11,2,14,1,8,7};
         BigInteger n= BigInteger.valueOf(a);
         n =n.multiply(BigInteger.valueOf(16).pow(8));
         n =n.add(BigInteger.valueOf(a1));
         System.out.println(n.toString(16));}
}




