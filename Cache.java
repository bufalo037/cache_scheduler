/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package scheduler;

/**
 *
 * @author mihai
 */
public abstract class Cache {

    private Process[] proc;
    private int crt = 0;
    
        
    Cache(int nrlin) {
        proc = new Process[nrlin];
    }

    Process[] getProc() {
        return proc;
    }

    int getCrt() {
        return crt;
    }

    void setCrt(int crt) {
        this.crt = crt;
    }

    void setProc(Process[] proc) {
        this.proc = proc;
    }
    
    
    
    
    int search(Process x)
    {
        int  i;
        for(i = 0;i < this.crt;i++)
        {
            if(proc[i].equals(x)) //verifaca equals daca nu merge
            {
                break;
            }
        }
        if(i == crt) // ajunge aici doar daca nu s-a apelat break
            return -1;
        return i;
    }
    
    abstract Process add(Process x);
}


class LruCache extends Cache{

    public LruCache(int nrlin) {
        super(nrlin);
    }
     
    
    
    @Override
    Process add(Process x){ //daca l-a adaugat inseamna ca nu era in vector
        int i;
        Process[] proc = getProc();
        int len = proc.length;
        int crt = getCrt();
        Process aux;
        Process[] tmp;
        int j;
        i = search(x);
        
        if(i == -1) // nu e in vectorul de procese
        {
            if(len != crt)
            {
                proc[crt++] = x;
                setCrt(crt);
            }
            else
            {
                tmp = new Process[len];
                System.arraycopy(proc, 1 ,tmp, 0 , len - 1);
                tmp[len - 1] = x;
                setProc(tmp);
            }

            return null;
        }
        else
        {
            aux = proc[i];
            for(j = i;j < crt -1;j++){
                proc[j] = proc[j + 1];
            }
            proc[crt -1] = aux;
        }
        return aux;
    }
}


class LfuCache extends Cache{

    public LfuCache(int nrlin) {
        super(nrlin);
    }
     
    
    
    @Override
    Process add(Process x){ //daca l-a adaugat inseamna ca nu era in vector
        int i,j;
        Process[] proc = getProc();
        int len = proc.length;
        int crt = getCrt();
        Process aux, aux2;
        Process[] tmp;
        i = search(x);
        
        if(i == -1) // nu e in vectorul de procese
        {
            if(len != crt)
            {
                proc[crt] = x;
                j = crt;
                for(i =  j - 1;i >= 0;i--){
                    if(proc[j].getPondere() < proc[i].getPondere()){
                        aux = proc[i];
                        proc[i] = proc[j];
                        proc[j--] = aux;
                    }
                    else
                        break;
                }
                setCrt(++crt);
            }
            else
            {
                proc[0] = x;
            }
                
            return null;
        }
        else
        {
           // aux = proc[i];
            proc[i].incPondere();
            for(j = i + 1;j < crt -1;j++){
               if(proc[i].getPondere() > proc[j].getPondere()){
                   aux = proc[i];
                   proc[i++] = proc[j];
                   proc[j] = aux;
               }
               else
                   break;
            }
        }
        return proc[i];
    }
}

