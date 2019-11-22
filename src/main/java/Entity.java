import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
public class Entity {

    private String publicKeyHex;
    private String privateKeyHex;
    private BigInteger publicKey;
    private BigInteger privateKey;
    private String fromWallet;
    private String toWallet;
    private BigDecimal value;
    private BigInteger gasLimit;
    private BigInteger gasPrice;
    private BigInteger nonce;
    private String txHash;
    private String walletAddress;
    private String walletName;
}
