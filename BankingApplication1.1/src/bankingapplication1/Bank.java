
package bankingapplication1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Bank {
    private String name;

    public Bank() {
    }

    public Bank(String name) {
        this.name = name;
    }
    public void listAccount(){
        Connection connection = BankingConnection.connect();
        Statement statement;
        try {
            statement = connection.createStatement();
            String sql ="select * from account";
            ResultSet results= statement.executeQuery(sql);
            while (results.next()){
                System.out.println(results.getString(1)+" "+results.getString(2)+" "+
                results.getString(3)+" "+results.getString(4));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void openAccount(Account account){
        Connection connection = BankingConnection.connect();
        String sql = "insert into account(accNumber,accName,accBalance,accType) values(?,?,?,?)";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getAccountName());
            preparedStatement.setDouble(3, account.getBalance());
            preparedStatement.setString(4, account.getAccountType());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    public void closeAccount(Account account){
         Connection connection = BankingConnection.connect();
         String sql = "delete from account where accNumber = ? ";
         PreparedStatement preparedStatement;
         
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account.getAccountNumber());
            preparedStatement.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    
    public void depositMoney(Account account,double amount){
        account.deposit(amount);       
        Connection connection = BankingConnection.connect();
        String sql = "update account set accBalance = ? where accNumber = ?";
        PreparedStatement preparedStatement;
       
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, account.getBalance());
            preparedStatement.setInt(2, account.getAccountNumber());
            System.out.println("Balance: "+ account.getBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void withdrawMoney(Account account,double amount){
        account.withdraw(amount);
        Connection connection = BankingConnection.connect();
        PreparedStatement preparedStatement;
        String sql = "update account set accBalance = ? where accNumber = ?";
       
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, account.getBalance());
            preparedStatement.setInt(2, account.getAccountNumber());
            preparedStatement.executeUpdate();
            System.out.println("Balance: "+ account.getBalance());
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Account getAccount(int accountNumber){
        Connection connection = BankingConnection.connect();
        String sql ="select * from account where accNumber =? ";
        PreparedStatement preparedStatement;
        Account account = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountNumber);
            ResultSet result= preparedStatement.executeQuery();
            
            result.next();
            String accountName = result.getString(2);
            double balance = result.getDouble(3);
            String accType = result.getString(4);
            if (accType.equals("SavingsAccount")){
            account = new SavingsAccount(accountNumber, accountName, balance);
        }
            else if (accType.equals("CurrentAccount")){
                account = new CurrentAccount(accountNumber, accountName, balance);
        }         
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return account;
    }
    
}
