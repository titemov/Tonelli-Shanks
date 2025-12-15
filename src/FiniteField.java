import java.util.*;

public class FiniteField {
    private final int p;
    private final int[] field;

    public FiniteField(int p){
        this.p = p;
        this.field = new int[p];
        for(int i=0;i<p;i++){
            this.field[i]=i;
        }
    }

    private int modulo(int num,int mod){
        if(num<0){
            return(num%mod+mod);
        }else{
            return(num%mod);
        }
    }

    public int[] getField(){
        return this.field;
    }

    public int quadraticResidueCheck(int num){
        FastPow fastPow = new FastPow(this.p);
        int result;

        result = modulo(fastPow.run(num,((this.p-1)/2)), this.p);

        return result;
    }

    public int legendreSymbol(int a){
        /*
        =0 если a делаится на p (???)
        =1 если а является квадратичным вычетом по модулю p, но при этом a не делится на p
        =-1 если a является квадратичным невычетом по модулю p
        */
        int legendre;
        FiniteField field = new FiniteField(p);

        if(modulo(a,this.p)==0){
            legendre=0;
        }else{
            int qrResult = field.quadraticResidueCheck(a);
            if (qrResult==1){
                legendre = 1;
            }else{
                if(qrResult==(this.p-1)) legendre = -1;
                else return -2;
            }
        }

        return legendre;
    }
}

