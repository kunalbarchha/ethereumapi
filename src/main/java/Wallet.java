import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));
        String privateKey = "0xB25FD52AF697DA0E37EC40738A8AE961DBE26C68B56033738FD78ADDAA64178B";
        String publicKey = "0x34dD20C852f3595336eF945750Cf426d6Dd4EEA4";
        BigInteger nonce = getNonce(publicKey);
        System.out.println("Nonce >>>>> " + nonce);
//        BigInteger gasPrice=requestCurrentGasPrice();

//        entity.setGasLimit(gasPrice.multiply(BigInteger.valueOf(1)));

//        entity.setGasPrice(gasPrice);

        String etherValue = entity.getValue().toString();
        String amountWei="0x"+Convert.toWei(etherValue, Convert.Unit.ETHER).toBigInteger();
        System.out.println("Amount WEI >>>>>>> " +amountWei);

        BigInteger gasPrice = BigInteger.valueOf(21000);
        BigInteger gasLimit = gasPrice.multiply(BigInteger.valueOf(2));

        //create raw transaction
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce, gasPrice, gasLimit, "0x5B7415F8f61C29Fc7A01B7c67e4B5067ea26e3C9", amountWei);

//      System.out.println("Gas Price >>>> " + entity.getGasPrice() + " Gas Limit " + entity.getGasLimit());

        //set credentials

        Credentials credentials = Credentials.create(privateKey);

        //sign transaction
        byte[] txSignedBytes = TransactionEncoder.signMessage(rawTransaction, 4, credentials);
        String txSigned = Numeric.toHexString(txSignedBytes);

        System.out.println("Signed Transaction >>>>>> " + txSigned);

        //broadcast signed transaction
        try {
            EthSendTransaction sendTransaction = web3j.ethSendRawTransaction(txSigned).sendAsync().get();
//            String error = sendTransaction.getError().getMessage();
//            System.out.println("error >>>>> " +error);
            String txHash = sendTransaction.getTransactionHash();
            System.out.println("Send Transaction >>>> " + txHash);

            //  assertTrue(error == null);
            if (!txHash.isEmpty())
            {
                System.out.println("Transaction Hash >>>>> " + txHash);
                transferStatus=true;
                String balance=getBalance("0x5B7415F8f61C29Fc7A01B7c67e4B5067ea26e3C9");
                System.out.println("New Balance >>>>>> " +balance);
            }

        } catch (RuntimeException runtimeException) {

            runtimeException.printStackTrace();
        }

        // assertFalse(txHash.isEmpty());

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
        System.out.println("Get Nounce DATA >>>>>>> " +transactionCount.toString());
        entity.setNonce(transactionCount.getTransactionCount());
        return entity.getNonce();
    }

}

