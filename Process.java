/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package scheduler;

    import java.util.Objects;

/**
 *
 * @author mihai
 */
public abstract class Process {

    /**
     * clasa parinte a tuturor proceselor care vor fi definite
     */
   
    private int nr;
    private String numeproc;
    private int result;
    private int pondere = 1;
    
    Process(){
        
    }

    /**
     *
     * @param nr numarul primit ca input
     * @param numeproc numele procesului
     */
    public Process(int nr, String numeproc) {
        this.nr = nr;
        this.numeproc = numeproc;

    }

    /**
     *
     * @param obj obectul de comparat cu procesul respectiv
     * @return returneaza true daca  numele si inputul a 2 procese sunt identice, insemnand ca au aceasi functioonalitate
     */
    @Override
    public boolean equals(Object obj) {
        
        if(obj instanceof Process)
        {
            if(this.nr == ((Process)obj).nr &&
                    this.numeproc.equals(((Process)obj).numeproc))
                return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.nr;
        hash = 53 * hash + Objects.hashCode(this.numeproc);
        return hash;
    }

    /**
     *
     * @return
     */
    public int getNr() {
        return nr;
    }

    /**
     *
     * @return
     */
    public String getNumeproc() {
        return numeproc;
    }

    void incPondere() {
        this.pondere++;
    }

    int getPondere() {
        return pondere;
    }
   
    
    void setResult(int result) {
        this.result = result;
    }

    /**
     *
     * @return
     */
    public int getResult() {
        return result;
    }
    
    
    abstract int calcResult();
}

class CheckPrime extends Process{
    CheckPrime(int nr){
        super(nr,"CheckPrime");
    }

    @Override
    int calcResult() {
        int i;
        int nr = getNr();
        int n = (int)Math.sqrt(nr) + 1;
        if(nr < 2)
        {
            setResult(0);
            return 0;

        }
        else
            for (i = 2;i < n;i++) 
            {
                if((nr % i) == 0) {
                    setResult(0);
                    return 0;
                }
            }
        setResult(1);
        return 1;
    }
}

class NextPrime extends Process{
    
    NextPrime(int nr){
        super(nr,"NextPrime");
    }
    @Override
    int calcResult(){
        int nr = getNr();
        if(nr < 2)
        {
            setResult(2);
            return 2; //economiseste timp

        }
        nr++;
        Process aux;
        int rez;
        while(true)
        {
            aux = new CheckPrime(nr);
            rez = aux.calcResult();
            if(rez == 1)
                break;
            nr++;
        }
        setResult(nr);
        return nr;
    }
}

class Fibonacci extends Process{

    Fibonacci(int nr) {
        super(nr,"Fibonacci");
    }

    @Override
    int calcResult() {
        int nr = getNr();
        int x,y,tmp;
        final int mod = 9973;
        if(nr < 0){
            setResult(-1);
            return -1;
        }
        x=0;
        y=1;
        if(nr == 0)
        {
            setResult(x);
            return x;
        }
        if (nr == 1){
            setResult(y);
            return y;
        }
        nr--;
        for(;nr > 0 ;nr--){
            tmp=y;
            y=x+y;
            y=y%mod;
            x=tmp;
           
        }
        setResult(y);
        return y;
    }
}

class Sqrt extends Process{

    public Sqrt(int nr) {
        super(nr,"Sqrt");
    }

    @Override
    int calcResult() {
        int nr = getNr();
        nr = (int)Math.abs(nr);
        setResult((int)Math.sqrt(nr));
        return (int)Math.sqrt(nr); //floor?
    }
}

class Square extends Process{

    public Square(int nr) {
        super(nr,"Square");
    }

    @Override
    int calcResult() {
        int nr= getNr();
        setResult(nr * nr);
        return nr * nr;
    }
}

class Cube extends Process{

    public Cube(int nr) {
        super(nr,"Cube");
    }

    @Override
    int calcResult() {
        int nr = getNr();
        setResult(nr* nr *nr);
        return nr * nr * nr;
    }
       
}

class Factorial extends Process{

    public Factorial(int nr) {
        super(nr,"Factorial");
    }

    @Override
    int calcResult() {
        final int mod = 9973; 
        int nr = getNr();
        int rez = 1;
        if(nr < 0)
        {
            setResult(0);
            return 0;
        }
        if(nr == 0)
        {
            setResult(1);
            return 1;
        }
        for(;nr > 0;nr--)
        {
            rez*=nr;
            rez%=mod;
        }
        setResult(rez);
        return rez;
    }
}
