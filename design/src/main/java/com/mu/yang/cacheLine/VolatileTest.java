package com.mu.yang.cacheLine;

//import sun.misc.Contended;

/**
 * Created by xuanda007 on 2016/8/29.
 */
public class VolatileTest {
    /**
     * 1, only work int jdk 1.8 , and you need the vm option -XX:-RestrictContended when you run the application.
     *
     * 2, comment this annotation to compare the executing time.
     *
     *
     */
//    @Contended
    class VolatileLong{
        volatile long msg = 1l;
    }

    class VolatileRunnable implements Runnable{
        private int index = 0;
        private int op_num = 0;
        public VolatileRunnable(int index, int op_num){
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
            tt[i] =  new Thread(new VolatileRunnable(i, op_num));
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
