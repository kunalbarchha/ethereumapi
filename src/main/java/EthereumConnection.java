import com.fasterxml.jackson.annotation.JsonCreator;
import org.reactivestreams.Subscription;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EthereumConnection {

    public EthereumConnection() throws IOException {

        Web3j web3Url = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b319b8acaca45ecbd5d9ed2f99c85ca"));
        Web3ClientVersion web3ClientVersion = web3Url.web3ClientVersion().send();
        System.out.println("Connected to Infura Client with Version >>> " + web3ClientVersion.getWeb3ClientVersion());

   }
}