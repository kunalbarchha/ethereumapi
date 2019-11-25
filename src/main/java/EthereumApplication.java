import java.util.Map.Entry;
import java.util.Scanner;

public class EthereumApplication {

    public static void main(String[] args) throws Exception {
        EthereumConnection ethConnection=new EthereumConnection();
        Entity entity=new Entity();
//        SubscribeBlocks ethBlock=new SubscribeBlocks();
//        ethBlock.SubscribeBlocks();

        Wallet wallet=new Wallet();
//        wallet.createWallet();
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter from wallet: ");
        entity.setFromWallet(scanner.next());

        System.out.println("Enter To wallet:  ");
        entity.setToWallet(scanner.next());

        System.out.println("Enter Ether amount:  ");
        entity.setValue(scanner.nextBigDecimal());

        wallet.getBalance();
        wallet.sendEther(entity.getFromWallet(), entity.getToWallet(), entity.getValue());
    }
}
