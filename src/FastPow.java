import java.util.ArrayList;

public class FastPow {
    private int base;
    private int power;
    private int mod;

    public FastPow(int mod){
        this.mod=mod;
    }

    private ArrayList<Integer> factorizeNum(){
        long power = this.power;
        ArrayList<Integer> powers = new ArrayList<>();

        int k=-1;
        while (power>0){
            k+=1;
            //System.out.println("current pow: " +pow);
            long temp1 = (long) Math.pow(2,k);
            long temp2 = (long) Math.pow(2,k+1);
            if(temp2>power){
                power=power-temp1;
                powers.add(k);
                k=-1;
            }
        }
        return powers;
    }

    private int getRemainingFromMultiplication(int rem1, int rem2){
        int mod = this.mod;
        if(rem1==0) rem1+=this.mod;
        if(rem2==0) rem2+=this.mod;//in case if rem1 and rem2 both equals to zero
        int a = Math.min(rem1,rem2);
        int b = Math.max(rem1,rem2);
        int c = 0;
        int[] currentStep = new int[]{a,b,c};
        int[] nextStep = new int[3];

        while(currentStep[0]!=1) {
            nextStep[0] = currentStep[0] / 2;
            nextStep[1] = (currentStep[1] * 2) % mod;
            if (currentStep[0] % 2 == 1) {
                nextStep[2] = (currentStep[1]+currentStep[2])%mod;
            } else {
                nextStep[2] = currentStep[2];
            }

            for(int i=0;i<currentStep.length;i++){
                currentStep[i]=nextStep[i];
                nextStep[i]=0;
            }
        }
        return (currentStep[1]+currentStep[2])%mod;
    }

    public int run(int base, int power){
        if(power==0) return 1;
        this.base = base;
        this.power = power;
        ArrayList<Integer> powers = factorizeNum();

        ArrayList<Integer> rems = new ArrayList<>();
        for(int i=0;i<powers.size();i++){
            rems.add(this.base % this.mod);
        }

        while(powers.getFirst()!=0){
            int temp = rems.getFirst();
            int rem = getRemainingFromMultiplication(temp,temp);

            for(int n=0;n<powers.size();n++)
            {
                if(powers.get(n)!=0){
                    rems.set(n,rem);
                    powers.set(n,powers.get(n)-1);
                }
            }
        }

        for(int i=1;i<rems.size();i++){
            rems.set(0,getRemainingFromMultiplication(rems.getFirst(),rems.get(i)));
        }

        return rems.getFirst();
    }
}
