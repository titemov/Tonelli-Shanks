public class Backend {
    private final int prime;
    private final int number;

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

    public int run(){
        Log logger = new Log();
        FiniteField finiteField = new FiniteField(this.prime);
        FastPow fastPow = new FastPow(this.prime);
        int result=-1;
        int s = getS();
        int Q = getQ(s);

        logger.writeLog("p - 1 = "+this.prime+" - 1 = "+(this.prime-1)+" = 2^"+s+" * "+Q,true);

        int z = -1;
        for(int i = this.prime-1;i>=0;i--){
            if(finiteField.legendreSymbol(finiteField.getField()[i])==-1){
                z=finiteField.getField()[i];
                break;
            }
        }
        if(z==-1) {
            logger.writeLog("ERROR! There is no quadratic non-residue in F_"+this.prime+" field",true);
            return -1;
        }

        logger.writeLog("z: "+z,true);

        int M = s;
        int c = fastPow.run(z,Q);
        int t = fastPow.run(this.number,Q);
        int R = fastPow.run(this.number,(Q+1)/2);

        logger.writeLog("M: "+M+"\nc: "+c+"\nt: "+t+"\nR: "+R,true);

        int k=0;
        while(t!=0 && t!=1){
            logger.writeLog("=== Iteration #"+k+" ===",true);
            int iValue=0;
            for(int i=1;i<=M;i++){
                if(modulo( fastPow.run(t,modulo( (int)Math.pow(2,i),this.prime) ) ,this.prime)==1){
                    iValue=i;
                    break;
                }
            }
            if(iValue==M) {
                logger.writeLog("lowest \"i\" equals to \"M\". No result",true);
                return -1;
            }
            logger.writeLog("i: "+iValue,true);

            int b = fastPow.run(c,modulo( (int) Math.pow(2,(M-iValue-1)) ,this.prime));
            M=iValue;
            c=fastPow.run(b,2)%this.prime;
            t=(t*c)%this.prime;
            R=(R*b)%this.prime;

            logger.writeLog("b: "+b+"\nM: "+M+"\nc: "+c+"\nt: "+t+"\nR: "+R,true);

            k+=1;
        }
        if(t==0) result=0;
        if(t==1) result=R;
        logger.writeLog("\"t\" equals to "+t+". Stopping...",true);

        logger.writeLog("\n=== RESULT: "+ result+" ===",true);

        return result;
    }
}
