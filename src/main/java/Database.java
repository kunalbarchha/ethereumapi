import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class Database {

    Connection connection;
    public void Connection() throws Exception{

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ethereum", "root", "Kunal@9120");

    }

    public void insert(String walletname, String walletAddress, String privateKey, String publicKey) throws Exception {

    Connection();
    Statement statement=connection.createStatement();
    String sqlInsert="INSERT INTO walletdetails (id, walletname, walletaddress, privatekey, publickey) VALUES ( null ,'"+walletname+"','"+walletAddress+"','"+privateKey+"','"+publicKey+"')";
    statement.executeUpdate(sqlInsert);
    connection.close();

    }
    public String walletDetails(String walletname) throws Exception {

        String walletAddress=null;
        Entity entity=new Entity();
        Connection();
        PreparedStatement preparedStatement=connection.prepareStatement("SELECT walletaddress FROM walletdetails WHERE walletname='"+walletname+"'");

        ResultSet resultSet=preparedStatement.executeQuery();

        while (resultSet.next()){
            walletAddress=resultSet.getString("walletaddress");
            log.info("Wallet Address : " +walletAddress);
        }
        return walletAddress;
    }
}