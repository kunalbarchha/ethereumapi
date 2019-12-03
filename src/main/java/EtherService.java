import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class EtherService {

    public boolean sendEther() throws ExecutionException, InterruptedException {

        boolean transferStatus=false;
        Web3j web3j=Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));

        String fromWallet="0x34dD20C852f3595336eF945750Cf426d6Dd4EEA4";
        String toWallet="0x5B7415F8f61C29Fc7A01B7c67e4B5067ea26e3C9";
        String privateKey="0xB25FD52AF697DA0E37EC40738A8AE961DBE26C68B56033738FD78ADDAA64178B";

        final BigInteger gasPrice = BigInteger.valueOf(20_000_000_000L);
        final BigInteger gasLimit = BigInteger.valueOf(4_300_000);

//        BigInteger gasPrice=BigInteger.valueOf(447000);
//        BigInteger gasLimit=gasPrice.multiply(BigInteger.valueOf(2));
        BigInteger value = Convert.toWei("1.0", Convert.Unit.ETHER).toBigInteger();
        //String amountWei= String.valueOf(Convert.toWei(value, Convert.Unit.ETHER));

        EthGetTransactionCount transactionCount=web3j.ethGetTransactionCount(fromWallet, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce=transactionCount.getTransactionCount();

        System.out.println("Nonce >>>>>> " +nonce);
        Credentials credentials=Credentials.create(privateKey);

        RawTransaction rawTransaction=RawTransaction.createEtherTransaction(nonce, gasPrice,gasLimit,toWallet, value);

//        RemoteCall<TransactionReceipt> transactionReceipt= Transfer.sendFunds(web3j, credentials,toWallet,value).send();

        byte[] signMessage=TransactionEncoder.signMessage(rawTransaction,credentials);
        String signedTransaction=Numeric.toHexString(signMessage);

        try{
            EthSendTransaction sendRawTransaction=web3j.ethSendRawTransaction(signedTransaction).sendAsync().get();

            String txHash=sendRawTransaction.getTransactionHash();

            if (!txHash.isEmpty())
            {
                System.out.println("Transaction Hash >>>>>> " + txHash);
                transferStatus=true;
            }
            else
            {
                String error=sendRawTransaction.getError().getMessage();
                System.out.println("Error in send transaction >>>>>> " +error);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return transferStatus;
        }

}
