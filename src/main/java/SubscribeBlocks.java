import io.reactivex.disposables.Disposable;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.ConnectException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.CountDownLatch;

public class SubscribeBlocks {

    private static final int COUNT = 10;
    public final Web3j web3j;

    public SubscribeBlocks() {
        web3j = null;
    }

    public void SubscribeBlocks () throws ConnectException {

        String wssUrl="wss://rinkeby.infura.io/ws/v3/2b319b8acaca45ecbd5d9ed2f99c85ca";
        WebSocketService web3WebSocket=new WebSocketService(wssUrl,true);
        web3WebSocket.connect();
        Web3j web3j = Web3j.build(web3WebSocket);

        web3j.blockFlowable(false).subscribe(block -> {
            System.out.println("NEW BLOCK -> " + block.getBlock().getNumber().intValue());
        });
        }

}

