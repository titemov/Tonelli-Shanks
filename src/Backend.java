public class Backend {
    private final int prime;
    private final int number;
    private final int power;

    public Backend(int prime,int number,int power){
        this.prime = prime;
        this.number = number;
        this.power = power;
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
        if(mod == 1) return 0;
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

    private int getS(){
        int s = 0;
        int num = this.prime - 1;

        while(true){
            if(num%this.power==0){
                s+=1;
                num=num/this.power;
            }else{
                break;
            }
        }
        return s;
    }

    private int getQ(int s){
        return ((this.prime -1)/(int)Math.pow(this.power,s));
    }

    public int getOrder(){
        FastPow fastPow = new FastPow(this.prime);
        int result=-1;
        for(int i=2;i<this.prime;i++){
            if(modulo( fastPow.run(i,this.power) ,this.prime)==1){
                result=i;
                break;
            }
        }
        return result;
    }

    public int getD(){
        return(binaryEuclid(this.power,this.prime-1));
    }

    public int run(){
        Log logger = new Log();
        FiniteField finiteField = new FiniteField(this.prime);
        FastPow fastPow = new FastPow(this.prime);
        int result=-1;

        int d = getD();
        System.out.println("Amount of solutions: "+d);

        int s = getS();
        int Q = getQ(s);

        logger.writeLog("p - 1 = "+this.prime+" - 1 = "+(this.prime-1)+" = "+this.power+"^"+s+" * "+Q,true);

        int z = -1;
        for(int i = this.prime-1;i>=0;i--){
            if(finiteField.residueCheck(finiteField.getField()[i],this.power)!=1){
                z=finiteField.getField()[i];
                break;
            }
        }
        int e=getReverse(this.power,Q);

        if(z==-1) {
            logger.writeLog("ERROR! There is no non-residue in F_"+this.prime+" field",true);
            return -1;
        }
        if(e==-1) {
            logger.writeLog("ERROR! There is no e in F_"+Q+" field. GCD(power,Q)="+binaryEuclid(this.power,Q)
                    +". Must be ==1",true);
            return -1;
        }

        logger.writeLog("z: "+z,true);
        logger.writeLog("e: "+e,true);

        int M = s;
        int c = fastPow.run(z,Q) % this.prime;
        int R = fastPow.run(this.number,e) % this.prime;
        int t = fastPow.run(R,this.power) * getReverse(this.number,this.prime) % this.prime;

        logger.writeLog("M: "+M+"\nc: "+c+"\nt: "+t+"\nR: "+R,true);

        int k=0;
        while(t!=0 && t!=1){
            logger.writeLog("=== Iteration #"+k+" ===",true);
            int iValue=0;
            for(int i=1;i<=M;i++){
                if(modulo( fastPow.run(t,modulo( (int)Math.pow(this.power,i),this.prime) ) ,this.prime)==1){
                    iValue=i;
                    break;
                }
            }
            if(iValue==M) {
                logger.writeLog("lowest \"i\" equals to \"M\". No result",true);
                return -1;
            }
            logger.writeLog("i: "+iValue,true);

            int b = fastPow.run(c,modulo( (int) Math.pow(this.power,(M-iValue-1)) ,this.prime));
            M=iValue;
            c=fastPow.run(b,this.power)%this.prime;
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
