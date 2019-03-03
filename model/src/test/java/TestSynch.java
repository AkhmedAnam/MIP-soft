public class TestSynch {


    public static void main(String[] args) {
        final A a = new A();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (a){
                    while (true){}
                }
            }
        });
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                a.a();
            }
        });
        thread.start();
        thread1.start();
    }
}

class A{
    public synchronized void a(){
        System.out.println("A");
        b();
    }

    public synchronized void b(){
        System.out.println("B");
    }
}
