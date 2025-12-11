import java.util.Random;

public class Backend {
    private int prime;
    private int number;

    public Backend(int prime,int number){
        this.prime = prime;
        this.number = number;
    }

    private int modulo(int num,int mod){
        if(num<0){
            return(num%mod+mod);
        }else{
            return(num%mod);
        }
    }

    private int getS(){
        int s = 0;
        int num = this.prime - 1;

        while(true){
            if(num%2==0){
                s+=1;
                num=num/2;
            }else{
                break;
            }
        }
        return s;
    }

    private int getQ(int s){
        return ((this.prime -1)/(int)Math.pow(2,s));
    }

    private int getRandomNum(){
        int r = new Random().nextInt(this.prime);
        if(r==0) r=1;
        return r;
    }

    public void run(){
        FiniteField finiteField = new FiniteField(this.prime);
        FastPow fastPow = new FastPow(this.prime);
        int s = getS();
        int Q = getQ(s);

        System.out.println("p - 1 = "+this.prime+" - 1 = "+(this.prime-1)+" = 2^"+s+" * "+Q);

        int z = -1;
        for(int i = prime-1;i>=0;i--){
            if(finiteField.legendreSymbol(finiteField.getField()[i])==-1){
                z=finiteField.getField()[i];
                break;
            }
        }
        if(z==-1) return;

        System.out.println(z);

        int M = s;
        int c = fastPow.run(z,Q);
        int t = fastPow.run(this.number,Q);
        int R = fastPow.run(this.number,(Q+1)/2);

        System.out.println("M:"+M+"; c: "+c+"; t: "+t+"; R: "+R);

//        while(){
//
//        }

        return;
    }
}
