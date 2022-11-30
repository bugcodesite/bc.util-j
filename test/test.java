import site.bc.util.util;

public class test{
    public static void main(String...args){
        System.out.println(util.tohex("hello world!".getBytes()));
        System.out.println(util.round(1784732.2314321, 2));
        int  t=(int)util.round(1784732.2314321, 2);
        System.out.println(t);
    }
}