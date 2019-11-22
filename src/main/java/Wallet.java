import com.sun.org.apache.xalan.internal.xsltc.compiler.If;
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

public class Wallet {

    Entity entity=new Entity();

    public void createWallet() throws Exception {

        Scanner scanner=new Scanner(System.in);
        System.out.println("Please enter wallet name: ");
        String walletName=scanner.next();

        ECKeyPair keyPair=Keys.createEcKeyPair();
        entity.setPublicKey(keyPair.getPublicKey());
        entity.setPrivateKey(keyPair.getPrivateKey());
        entity.setPrivateKeyHex(Numeric.toHexStringWithPrefix(entity.getPrivateKey()));
        entity.setPublicKeyHex(Numeric.toHexStringWithPrefix(entity.getPublicKey()));


        System.out.println("Private Key to Hex >>> " +entity.getPrivateKeyHex());
        System.out.println("Public Key to Hex >>> " +entity.getPublicKeyHex());

        Credentials credentials=Credentials.create(new ECKeyPair(entity.getPublicKey(),entity.getPrivateKey()));
        entity.setWalletAddress(credentials.getAddress());

        Database database=new Database();
        database.insert(walletName, entity.getWalletAddress(),entity.getPrivateKeyHex(),entity.getPublicKeyHex());

        System.out.println("Wallet Address >>>>>>>"+entity.getWalletAddress());
    }
    public String getBalance(String walletAddress) throws Exception {

        String strTokenAmount=null;

        try {
            Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

            Database db=new Database();

            Scanner scanner=new Scanner(System.in);
            System.out.println("Enter wallet name to check balance : ");
            entity.setWalletName(scanner.nextLine());
            String walletName=entity.getWalletName();
            entity.setWalletAddress(db.walletDetails(walletName));

            EthGetBalance ethereum = web3j.ethGetBalance(entity.getWalletAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();

            BigInteger wei = ethereum.getBalance();

            BigDecimal tokenValue = Convert.fromWei(String.valueOf(wei), Convert.Unit.ETHER);
            strTokenAmount = String.valueOf(tokenValue);

            System.out.println("Ether Balance : " + strTokenAmount);

        }
        catch (Exception e){
                e.printStackTrace();
        }
        return strTokenAmount;

    }
    public boolean sendEther() throws Exception{

        boolean transactionStatus=false;

        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter from wallet name ");
        entity.setFromWallet(scanner.nextLine());

        System.out.println("Enter to Wallet name :");
        entity.setToWallet(scanner.nextLine());

        System.out.println("Enter Ether Value to transfer : ");
        entity.setValue(scanner.nextBigDecimal());

        Web3j web3j=Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

        entity.setGasPrice(web3j.ethGasPrice().send().getGasPrice());

        String etherBalance=getBalance();

//        Function function =new Function("sendFunds", inputParam, outputParam);
//        String encodedFunction= FunctionEncoder.encode(function);
//        System.out.println("Encoded Function >>>>>>" + encodedFunction.toString());
//        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter Ether amount to transfer: ");
        BigDecimal toAmount=scanner.nextBigDecimal();

        BigInteger nonce = BigInteger.valueOf(100);
//        gasPrice = BigInteger.valueOf(100);
        BigInteger gasLimit = BigInteger.valueOf(100);

        String fromAddress="0xb155694015ee7e71000863b8974dba1ebd3c723c";
        String toAddress="0x5B7415F8f61C29Fc7A01B7c67e4B5067ea26e3C9";

//        Transaction transaction=Transaction.createFunctionCallTransaction(fromAddress, nonce, entity.getGasPrice(), gasLimit, toAddress, encodedFunction );
        System.out.println("Private Key >>>>>>" + entity.getPrivateKeyHex());

        try {

            System.out.println("Inside Try Send transaction >>>>>>>> ");
//            EthSendTransaction sendTransaction = web3j.ethSendTransaction(transaction).sendAsync().get();
//            System.out.println("Send Transaction >>>>> " + sendTransaction.toString());
//            String transactionHash = sendTransaction.getTransactionHash();
//            System.out.println("Tx ID >>>>>>>>>>"+transactionHash);

        }
        catch (Exception e){
            e.printStackTrace();
        }
//        if (transactionHash!=null){
//            transactionStatus=true;
//            System.out.println("Tx ID >>>>>>" +transactionHash);
//        }
//        else
//        {
//            System.out.println("There is an error processing the request");
//        }

//        Credentials credentials=Credentials.create("7861634613974620093520148841001043475951986628885386719408162767677394110208");
//
//        EthSign signTx=web3j.ethSign("7861634613974620093520148841001043475951986628885386719408162767677394110208",fromAddress).sendAsync().get();
//        System.out.println("Sign Transaction >>>>>>" +signTx);

        return transactionStatus;
    }
    public boolean transferEther(String fromAddress, String toAddress, BigDecimal value) throws Exception {
        boolean transferStatus=false;

        entity.setFromWallet(fromAddress);
        entity.setToWallet(toAddress);
        entity.setValue(value);

        String fromWalletBalance=getBalance(entity.getWalletAddress());

        if (fromWalletBalance){

        }
        Web3j web3j=Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

       return transferStatus;
    }
}
