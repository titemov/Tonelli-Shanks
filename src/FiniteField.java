public class FiniteField {
    private final int prime;
    private final int[] field;

    public FiniteField(int prime){
        this.prime = prime;
        this.field = new int[prime];
        for(int i=0;i<prime;i++){
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

    private int binaryEuclid(int m,int n){
        if(m==0) return n;
        if(n==0) return m;
        if(m==n) return m;

        if(m%2==0 && n%2==0){
            return(2*binaryEuclid(m/2,n/2));
        }
        if(m%2==0){
            return(binaryEuclid(m/2,n));
        }
        if(n%2==0){
            return(binaryEuclid(m,n/2));
        }
        if(m<n){
            return(binaryEuclid(m,(n-m)/2));
        }
        return (binaryEuclid((m-n)/2, n));

    }

    private int getReverse(int number,int mod){
        if(binaryEuclid(number,mod)!=1) return -1;
        if(mod == 1) return 1;
        number = modulo(number,mod);
        //each step = [r,x,y,q]
        int[] step0 = new int[]{mod,1,0,0};
        int[] step1 = new int[]{number,0,1,mod/number};
        int[] newStep = new int[4];

        int stop;
        while(true){
            stop=step0[0]%step1[0];
            if(stop==0) break;
            newStep[0]=stop;
            newStep[1]=step0[1]-(step1[1]*step1[3]);
            newStep[2]=step0[2]-(step1[2]*step1[3]);
            newStep[3]=step1[0]/newStep[0];

            step0[0]=step1[0];
            step0[1]=step1[1];
            step0[2]=step1[2];
            step0[3]=step1[3];

            step1[0]=newStep[0];
            step1[1]=newStep[1];
            step1[2]=newStep[2];
            step1[3]=newStep[3];

        }
        return modulo(step1[2],mod);
    }

    public int[] getField(){
        return this.field;
    }

    public int residueCheck(int num,int power){
        FastPow fastPow = new FastPow(this.prime);
        int result;
        int reverse = getReverse(power,this.prime);

        result = fastPow.run(num,((this.prime - 1)*reverse)%this.prime);

        return result;
    }
}

