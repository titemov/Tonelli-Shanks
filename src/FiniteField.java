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

    public boolean quadraticResidueCheck(int num){
        boolean isInArray=false;

        int[] squares = new int[this.p];
        for(int i=0;i<this.field.length;i++){
            squares[i] = modulo((int)Math.pow(this.field[i],2),this.p);
        }

        for(int i=0;i < squares.length;i++){
            if(num==squares[i]){
                isInArray=true;
                break;
            }
        }

        return isInArray;
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
        }else {
            if (field.quadraticResidueCheck(a)) {
                legendre = 1;
            }else{
                legendre = -1;
            }
        }

        return legendre;
    }
}

