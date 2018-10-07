/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package scheduler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mihai
 */
public abstract class Scheduler {

    private final Cache cache;
    private final int NrOfEvents;
    private final String[] processname; //vetorul de nume de procese
    private int[] weight;
    private final int nrProc;
    private final int[] num; //vectorul de numere;
    private final BufferedWriter out;
    
    /**
     *
     * @param CacheT tipul cache-ului
     * @param NrOfEvents numarul tipurilor de procese diferite citite
     * @param process un vector de sting care retine numele procesor
     * @param weight importanta pe care o are un proces 
     * @param nrProc numarul proceselor (efective) adica numarul de inputuri citite din fisier
     * @param num numarul de inpututri
     * @param out un BufferedWriter, folosit pt scrierea in fisier
     */
    public Scheduler(Cache CacheT, int NrOfEvents,
            String[] process,int[] weight,int nrProc,int[] num,
            BufferedWriter out){
        this.cache = CacheT;
        this.NrOfEvents = NrOfEvents;
        this.processname = process;
        this.weight = weight; 
        this.nrProc = nrProc;
        this.num = num;
        this.out = out;
    }
    
    
    
    static final Process CreateProcess(String name,int x)
    {
        switch(name){
            case "CheckPrime":
                return new CheckPrime(x);
            case "NextPrime":
                return new NextPrime(x);
            case "Fibonacci":
                return new Fibonacci(x);
            case "Sqrt":
                return new Sqrt(x);
            case "Square":
                return new Square(x);
            case "Cube":
                return new Cube(x);
            case "Factorial":
                return new Factorial(x);
        }
        return null;
    }

    Cache getCache() {
        return cache;
    }

    int getNrProc() {
        return nrProc;
    }

    int getNrOfEvents() {
        return NrOfEvents;
    }

    String[] getProcessname() {
        return processname;
    }

    int[] getNum() {
        return num;
    }

    final void setWeight(int[] weight) {
        this.weight = weight;
    }

    /**
     *
     * @return
     */
    public int[] getWeight() {
        return weight;
    }

    /**
     *
     * @return
     */
    public BufferedWriter getOut() {
        return out;
    }
    
    /**
     * o functie care va scrie in urma inputului citit informatiile in fisier
     */
    public abstract void run(); //de adaugat un FILE* sau ceva de genul
}

class RandomScheduler extends Scheduler{
    
    public RandomScheduler(Cache CacheT, int NrOfEvents,
            String[] process, int[] weight, int nrProc, int[] num,
            BufferedWriter out) {
        
        super(CacheT, NrOfEvents, process, weight, nrProc, num, out);

    }
    
    @Override
    public void run(){
        Cache cache = getCache();
        int rand;
        int i;
        int result;
        int nrOfEvents = getNrOfEvents();
        String[] processname = getProcessname();
        int[] numbers = getNum();
        int nrProc = getNrProc();
        BufferedWriter out = getOut();
        Process procCrt;
        Process aux;
        for(i = 0;i < nrProc; i++) {
            
            rand = (int)(Math.random() * nrOfEvents);
            procCrt = CreateProcess(processname[rand], numbers[i]);
            if(cache != null)
            {
                aux = cache.add(procCrt);
                if(aux != null){
                    try {
                        out.write(numbers[i] + " " + aux.getNumeproc() + " "
                                + aux.getResult() + " FromCache");
                        out.newLine();
                    } catch (IOException ex) {
                        Logger.getLogger(RandomScheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    continue;
                }
                
            }
            
            result =  procCrt.calcResult();
            try {
                out.write(numbers[i] + " " + procCrt.getNumeproc() + " "
                        + result + " Computed");
                out.newLine();
            } catch (IOException ex) {
                Logger.getLogger(RandomScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            
        }
    }
}

class RoundRobinScheduler extends Scheduler{
    

    public RoundRobinScheduler(Cache CacheT, int NrOfEvents,
            String[] process, int[] weight, int nrProc, int[] num,
            BufferedWriter out) {
        
        super(CacheT, NrOfEvents, process, weight, nrProc, num, out);
    }
    
    @Override
    public void run(){
        int i,j;
        int result;
        int NrofEvents = getNrOfEvents();
        int nrnum = getNrProc();
        String[] NameProc = getProcessname();
        int[] numere = getNum();
        Cache cache = getCache();
        BufferedWriter out = getOut();
        Process procCrt, aux;
        
        for(i = 0 ;i < nrnum; i++){
            procCrt = CreateProcess(NameProc[i%NrofEvents], numere[i]);
            if(cache != null){
                aux = cache.add(procCrt);
                if(aux != null){
                    try {
                        out.write(numere[i] + " " + aux.getNumeproc() + " "
                                + aux.getResult() + " FromCache");
                        out.newLine();
                    } catch (IOException ex) {
                        Logger.getLogger(RandomScheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    continue;
                }
            }
            
            result =  procCrt.calcResult();
            try {
                out.write(numere[i] + " " + procCrt.getNumeproc() + " "
                        + result + " Computed");
                out.newLine();
            } catch (IOException ex) {
                Logger.getLogger(RandomScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }

    }
}

class WeightedScheduler extends Scheduler{

    private final int t;
    public WeightedScheduler(Cache CacheT, int NrOfEvents,
            String[] process, int[] weight, int nrProc, int[] num,
            BufferedWriter out) {
            
        super(CacheT, NrOfEvents, process, weight, nrProc, num, out);
        int len = weight.length;
        int i;
        this.t = cmmdcS(weight);
        for(i = 0;i < len;i++){
            weight[i] = weight[i] / this.t; 
        }
        this.setWeight(weight);
        
    }
    
    //algoritmul lui euclid    
    private int cmmdc(int a,int b){
        int t;
        while (b != 0)
        {
            t = b;
            b = a % b;
            a = t;
        }
    return a;
    }
    
    private int cmmdcS(int[] x){
        int len = x.length;
        int y,i;
        y = x[0]; 
        for(i = 1; i < len; i++)
            y = cmmdc(x[i],y);
        return y;
    }

    @Override
    public void run() {
        
        int i,j,k=0;
        
        int NrofEvents = getNrOfEvents();
        String[] NameProc = getProcessname();
        int[] weight = getWeight();
        
        int nrProc = getNrProc();
        int[] numbers = getNum();
        
        Cache cache = getCache();
        Process procCrt, aux;
        BufferedWriter out = getOut();
        int result;
        while(k < nrProc){
            for(i = 0;i < NrofEvents && k < nrProc ; i++){
                for(j = 0;j < weight[i] && k < nrProc;j++){
                    procCrt = CreateProcess(NameProc[i], numbers[k]);
                    k++;
                    if(cache != null ){
                        aux = cache.add(procCrt);
                        if(aux != null){
                            try {
                                out.write(numbers[k - 1] + " " + aux.getNumeproc() + " "
                                    + aux.getResult() + " FromCache");
                                out.newLine();
                            } catch (IOException ex) {
                                Logger.getLogger(RandomScheduler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            continue;
                        }
                    }
                    
                    result =  procCrt.calcResult();
                    try {
                        out.write(numbers[k - 1] + " " + procCrt.getNumeproc() + " "
                            + result + " Computed");
                        out.newLine();
                    } catch (IOException ex) {
                        Logger.getLogger(RandomScheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }  
            }
        }
        
    }
}