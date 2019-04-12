// espera a que tenga algo la lista

public class PoolControl implements Runnable{

    private int nproc;

    public PoolControl(int i){
        nproc = i;
    }
    public void run(){
        System.out.println("Proceso" + nproc + "esta corriendo");
    }
}