import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Slf4j
public class Wallet {

    Entity entity = new Entity();

    public void createWallet() throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter wallet name: ");
        String walletName = scanner.next();

        ECKeyPair keyPair = Keys.createEcKeyPair();
        entity.setPublicKey(keyPair.getPublicKey());
        entity.setPrivateKey(keyPair.getPrivateKey());

        entity.setPrivateKeyHex(Numeric.toHexStringWithPrefix(entity.getPrivateKey()));
        entity.setPublicKeyHex(Numeric.toHexStringWithPrefix(entity.getPublicKey()));

        System.out.println("Private Key to Hex >>> " + entity.getPrivateKeyHex());
        System.out.println("Public Key to Hex >>> " + entity.getPublicKeyHex());

        Credentials credentials = Credentials.create(new ECKeyPair(entity.getPublicKey(), entity.getPrivateKey()));
        entity.setWalletAddress(credentials.getAddress());

        Database database = new Database();
        database.insert(walletName, entity.getWalletAddress(), entity.getPrivateKeyHex(), entity.getPublicKeyHex());

        System.out.println("Wallet Address >>>>>>>" + entity.getWalletAddress());
    }

    public String getBalance() throws Exception {

        String strTokenAmount = null;

        try {
            Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

            Database db = new Database();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter wallet name to check balance : ");
            entity.setWalletName(scanner.nextLine());

            String walletName = entity.getWalletName();

            entity.setWalletAddress(db.walletDetails(walletName));

            EthGetBalance ethereum = web3j.ethGetBalance(entity.getWalletAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();

            BigInteger wei = ethereum.getBalance();

            BigDecimal tokenValue = Convert.fromWei(String.valueOf(wei), Convert.Unit.ETHER);
            strTokenAmount = String.valueOf(tokenValue);

            System.out.println("Ether Balance : " + strTokenAmount);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strTokenAmount;

    }

    public String getBalance(String walletAddress) throws Exception {

        String strTokenAmount = null;

        try {
            Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

            EthGetBalance ethereum = web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

            BigInteger wei = ethereum.getBalance();

            BigDecimal tokenValue = Convert.fromWei(String.valueOf(wei), Convert.Unit.ETHER);
            strTokenAmount = String.valueOf(tokenValue);

            System.out.println("Ether Balance : " + strTokenAmount);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strTokenAmount;
    }

    public boolean sendEther(String fromAddress, String toAddress, BigInteger value) throws Exception {

        boolean transferStatus = false;

        entity.setFromWallet(fromAddress);
        entity.setToWallet(toAddress);
        entity.setValue(value);

        Web3j web3j=Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

        BigInteger gasPrice=requestCurrentGasPrice();
        entity.setGasLimit(gasPrice.multiply(BigInteger.valueOf(2)));

        BigInteger nonce=getNonce(entity.getFromWallet());
        System.out.println("Nonce >>>> "+nonce);

        entity.setGasPrice(gasPrice);

        Transaction transaction=Transaction.createEtherTransaction(entity.getFromWallet(),nonce,entity.getGasPrice(),entity.getGasLimit(),entity.getToWallet(), entity.getValue());
        Credentials credentials=Credentials.create(entity.getPrivateKey().toString(),entity.getFromWallet());


        return transferStatus;
    }
    public BigInteger requestCurrentGasPrice() throws Exception {

        Web3j web3j=Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

        EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
        return ethGasPrice.getGasPrice();
    }
    private BigInteger getNonce(String from) throws ExecutionException, InterruptedException {

        Web3j web3j=Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = transactionCount.getTransactionCount();
        log.info("transfer nonce : "+ nonce, 4);
        return nonce;
    }
}

