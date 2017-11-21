package TestCode2;


public class TestThread {

   public static void main(String args[]) {
	   mThread R1 = new mThread( "Thread-1");
      R1.start();
      
      mThread R2 = new mThread( "Thread-2");
      R2.start();
   }
   
}
