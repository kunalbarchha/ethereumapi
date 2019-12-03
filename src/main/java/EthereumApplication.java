import java.math.BigDecimal;
import java.util.Map.Entry;
import java.util.Scanner;

public class EthereumApplication {

    public static void main(String[] args) throws Exception {

        EthereumConnection ethConnection=new EthereumConnection();
        Entity entity=new Entity();

//        Erc20Service erc20Service=new Erc20Service();
//        erc20Service.getErc20Balance();
//        SubscribeBlocks ethBlock=new SubscribeBlocks();
//        ethBlock.SubscribeBlocks();

        Wallet wallet=new Wallet();
//        wallet.createWallet();
        Scanner scanner=new Scanner(System.in);
//        System.out.println("Enter from wallet: ");
//        entity.setFromWallet(scanner.next());
//
//        System.out.println("Enter To wallet:  ");
//        entity.setToWallet(scanner.next());
//
        System.out.println("Enter Ether amount:  ");
        entity.setValue(scanner.nextBigInteger());

        wallet.getBalance();
        wallet.sendEther("0x5B7415F8f61C29Fc7A01B7c67e4B5067ea26e3C9", "0xdA35deE8EDDeAA556e4c26268463e26FB91ff74f", entity.getValue());
    }
}
