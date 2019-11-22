

public class EthereumApplication {

    public static void main(String[] args) throws Exception {
        EthereumConnection ethConnection=new EthereumConnection();

//        SubscribeBlocks ethBlock=new SubscribeBlocks();
//        ethBlock.SubscribeBlocks();

        Wallet wallet=new Wallet();
//        wallet.createWallet();
        wallet.getBalance();
//        wallet.sendEther();
    }
}
