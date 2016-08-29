package com.mu.yang.CachaLine;

/**
 * Created by xuanda007 on 2016/8/29.
 */
public class VolatileTest {

  //  @Contended
    class VolatileLong{
        volatile long msg = 1l;
    }

    class Test implements Runnable{
        private int index = 0;
        private int op_num = 0;
        public Test(int index, int op_num){
            this.index = index;
            this.op_num = op_num;
        }
        public void run() {
            while(op_num-- > 0)
                    array[index].msg++;
        }
    }

    private int op_num = 10000000;
    private int count = 2;
    public VolatileLong[] array = new VolatileLong[count];
    public void init(){
        for(int i = 0; i < count ; i++){
            array[i] = new VolatileLong();
        }
    }


    public void test() throws InterruptedException {
        init();
        Thread[] tt = new Thread[count];

        for(int i = 0; i < count; i++){
            tt[i] =  new Thread(new Test(i, op_num));
            tt[i].start();
        }

       for(Thread t: tt){
           t.join();
       }


    }

    public static void main(String [] args) throws InterruptedException {
        int all = 100;
        long total = 0;
        for(int i = 0; i < all; i++){
            long a = System.currentTimeMillis();
            new VolatileTest().test();
            long b = System.currentTimeMillis();
            total += (b-a);
            System.out.println(b-a);
        }

        System.out.println("average: " + total/all);

    }
}
