package SHA1;

import java.util.ArrayList;

public class Sha {
    private int [] h0;
    private int [] h1;
    private int [] h2;
    private int [] h3;
    private int [] h4;
    private int [] k1;
    private int [] k2;
    private int [] k3;
    private int [] k4;
    private ArrayList<int[]> blok;
    private String hash;

    public Sha(ArrayList<Integer> value){
        this.generationConst();
        this.generationBloks(value);
       // System.out.println(blok.size());
        for (int[] ints : blok) {
            int[] h = hash(ints);
            hash =bitToByte(h);
        }
    }
    public String getHash(){return hash;}
    private int [] hash (int [] bl){
        int [] a = h0;
        int [] b = h1;
        int [] c = h2;
        int [] d = h3;
        int [] e = h4;

        ArrayList<int[]> w = new ArrayList<int[]>();
        for (int i=0; i<16; i++){
            int [] tmp = new int[32];
            for (int j = 0; j<32; j++){
                tmp[j] = bl[i*32+j];
            }
            w.add(tmp);
        }
        for (int i=16; i<80; i++){
            int [] tmp = shift((xor(w.get(i-3),w.get(i-8),w.get(i-14),w.get(i-16))),1);
            w.add(tmp);
        }
//        System.out.println();
//        for(int i=0; i<w.size(); i++){
//            System.out.print(i+"- ");
//           // for (int j = 0; j < w.get(i).length; j++) {
//                System.out.print(numberX(w.get(i)));
//            //}
//            System.out.println();
//        }

        for (int i=0; i<80;i++){
            int [] f = new int[32];
            int [] k = new int[32];
            int [] tmp;
            if (i<20){
                f = or(and(b,c),and(not(b),d));
                b=not(b);
                k = k1;
            }
            else if(i>=20 && i<40){
                f = xor(b,c,d);
                k = k2;
            }
            else if(i>=40 && i<60){
                f = or(and(b,c),and(b,d),and(c,d));
                k = k3;
            }
            else if(i>=60 && i<80){
                f = xor(b,c,d);
                k = k4;
            }
            tmp = sum(sum(sum(sum(shift(a,5),f),e),k),w.get(i));

            e = d;
            d = c;
            c = shift(b,30);
            b = a;
            a = tmp;
//            System.out.println(i);
//            System.out.println(numberX(a));
//            System.out.println(numberX(b));
//            System.out.println(numberX(c));
//            System.out.println(numberX(d));
//            System.out.println(numberX(e));
        }

        int [] res = new int[160];

        h0 = sum(a,h0);
        h1 = sum(b,h1);
        h2 = sum(c,h2);
        h3 = sum(d,h3);
        h4 = sum(e,h4);
        System.arraycopy (h0, 0, res, 0, 32);
        System.arraycopy (h1, 0, res, 32, 32);
        System.arraycopy (h2, 0, res, 64, 32);
        System.arraycopy (h3, 0, res, 96, 32);
        System.arraycopy (h4, 0, res, 128, 32);

        return res;
    }
    private void generationBloks(ArrayList<Integer> value){
        blok = new ArrayList<int[]>();
        int size = value.size();
        int [] size_bit = new int[64];
        int [] res = new int[size*8];
        for(int i=0; i<size; i++){
            int by = value.get(i);
           // System.out.println(by);
            int [] tmp = new int[8];
            for (int j=0; j<8; j++){
                tmp[j]=by%2;
                by=(by-by%2)/2;
            }
            for (int j=0; j<8; j++) {
                res[i*8+j]=tmp[7-j];
              //  System.out.print(res[i*8+j]);
            }
           // System.out.println();
        }
        int [] tmp = new int[64];
        int by = size*8;
        for(int i=0; i<64; i++){
            tmp[i]=by%2;
            by=(by-by%2)/2;
        }
        for (int j=0; j<64; j++) {
            size_bit[j]=tmp[63-j];
        }
        by = size*8;
        for(int i=0; i<(by-(by%512))/512; i++){
            tmp = new int[512];
            for(int j=0; j<512; j++) {
                tmp[j]=res[i*512+j];
            }
            //System.out.println('*');
            blok.add(tmp);
        }
        tmp = new int[512];
        if((by%512)<448){
          //  System.out.println("**");

            for(int i=0; i<by%512; i++){
                tmp[i]=res[by-by%512+i];
               // System.out.print(tmp[i]);
            }

            tmp[by%512]=1;
           // System.out.print(tmp[by%512]);
           // System.out.println();
            for(int i=by%512+1; i<448; i++){
                tmp[i]=0;
               // System.out.print(tmp[i]);
            }
            for (int i=448; i<512; i++){
                tmp[i]=size_bit[i-448];
             //   System.out.print(tmp[i]);
            }
            blok.add(tmp);
           // System.out.println();
        }
        else{
          //  System.out.println("***");
           // tmp = new int[512];
            for(int i=0; i<by%512; i++){
                tmp[i]=res[by-by%512+i];
            }
            tmp[by%512]=1;
            for(int i=size%512+1; i<512; i++){
                tmp[i]=0;
            }
            blok.add(tmp);
            tmp = new int[512];
            for(int i=0; i<448; i++){
                tmp[i]=0;
            }
            for (int i=448; i<512; i++){
                tmp[i]=size_bit[i-448];
            }
            blok.add(tmp);
        }
//        for (int i=0; i<blok.size();i++){
//            System.out.println();
//            for (int j = 0; j < blok.get(i).length; j=j+8) {
//                int [] g = new int[8];
//                System.arraycopy (blok.get(i), j, g, 0, 8);
//                System.out.print(numberX(g)+" ");
//            }
//        }

    }

    private void generationConst(){
        h0 = new int [] {0,1,1,0,0,1,1,1,0,1,0,0,0,1,0,1,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,1};
        h1 = new int [] {1,1,1,0,1,1,1,1,1,1,0,0,1,1,0,1,1,0,1,0,1,0,1,1,1,0,0,0,1,0,0,1};
        h2 = new int [] {1,0,0,1,1,0,0,0,1,0,1,1,1,0,1,0,1,1,0,1,1,1,0,0,1,1,1,1,1,1,1,0};
        h3 = new int [] {0,0,0,1,0,0,0,0,0,0,1,1,0,0,1,0,0,1,0,1,0,1,0,0,0,1,1,1,0,1,1,0};
        h4 = new int [] {1,1,0,0,0,0,1,1,1,1,0,1,0,0,1,0,1,1,1,0,0,0,0,1,1,1,1,1,0,0,0,0};
        k1 = new int [] {0,1,0,1,1,0,1,0,1,0,0,0,0,0,1,0,0,1,1,1,1,0,0,1,1,0,0,1,1,0,0,1};
        k2 = new int [] {0,1,1,0,1,1,1,0,1,1,0,1,1,0,0,1,1,1,1,0,1,0,1,1,1,0,1,0,0,0,0,1};
        k3 = new int [] {1,0,0,0,1,1,1,1,0,0,0,1,1,0,1,1,1,0,1,1,1,1,0,0,1,1,0,1,1,1,0,0};
        k4 = new int [] {1,1,0,0,1,0,1,0,0,1,1,0,0,0,1,0,1,1,0,0,0,0,0,1,1,1,0,1,0,1,1,0};
    }

    public int[] xor (int []a, int []b, int []c, int []d){
        int []res = new int [32];
        for(int i=0; i<32; i++)
            res[i]=(a[i]+b[i]+c[i]+d[i])%2;
        return res;
    }
    public int[] xor (int []a, int []b, int []c){
        int []res = new int [32];
        for(int i=0; i<32; i++)
            res[i]=(a[i]+b[i]+c[i])%2;
        return res;
    }
    private int [] not (int [] value){
        for (int i=0; i<value.length; i++){
            if(value[i]==0)
                value[i]=1;
            else
                value[i]=0;
        }
        return value;
    }

    private int[] and (int []a, int []b){
        int []res = new int [32];
        for(int i=0; i<32; i++){
            res[i]=a[i]*b[i];
        }
        return res;
    }
    private int[] or (int []a, int []b){
        int []res = new int [32];
        for(int i=0; i<32; i++){
            if(a[i]==0 && b[i]==0)
                res[i] = 0;
            else res[i] = 1;
        }
        return res;
    }
    private int[] or (int []a, int []b, int []c){
        int []res = new int [32];
        for(int i=0; i<32; i++){
            if(a[i]==0 && b[i]==0 && c[i]==0)
                res[i] = 0;
            else res[i] = 1;
        }
        return res;
    }

    private int[] shift(int []value, int v){
        int []res = new int [value.length];
        v=v%value.length;
        for(int i=0; i<value.length; i++){
            res[i]=value[(i+v)%value.length];
        }
        return res;
    }
    private int[] sum (int []a, int []b){
        int []res = new int [32];
        int c=0;
        for(int i=31; i>=0; i--){
            if(i!=31){
                if((a[i+1]+b[i+1]+c)>=2) c=1;
                else c=0;
            }
            res[i]=(a[i]+b[i]+c)%2;
        }
        return res;
    }
    private String bitToByte(int []bit){
       // System.out.println();
       // for (int value : bit) System.out.print(value);
       // System.out.println();
        StringBuilder str= new StringBuilder();
        for (int i =0; i<bit.length/8; i++){
            int [] tmp = new int[8];
            System.arraycopy (bit, i*8, tmp, 0, 8);
            int num = numberX(tmp);
            if (num<16)
                str.append(Integer.toHexString(0));
           // System.out.println(' '+Integer.toHexString(num)+ ' '+num);
            str.append(Integer.toHexString(num));
        }
        str.append("\n");
        return str.toString();
    }


    private int numberX(int [] value){
        //for (int item : value) System.out.print(item);
        int res=0;
        for(int i=0;i<value.length; i++){
            res+=(value[value.length-i-1]*Math.pow(2, i));
        }
        return res;
    }
}
