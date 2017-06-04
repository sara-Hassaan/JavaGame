/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gamemodel;

import java.sql.*;

/**
 *
 * @author Ahmagdi
 */

public class GameState {
    private char[] state;
    private Player player1;
    private Player player2;
    private Player nextPlayer;
    private Connection con;
    private static Connection cn;

    public GameState() throws Exception{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        con = DriverManager.getConnection("jdbc:mysql://localhost/game?" + "user=root&password=");
    }
    
    public GameState(char[] state, Player player1, Player player2) throws Exception{
        this.state = state;
        this.player1 = player1;
        this.player2 = player2;
//        try {
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://localhost/game?" + "user=root&password=");
            
            
//        } catch(SQLException ex)
//        {
//            System.out.println("SQL Problem!!\n");
//            ex.printStackTrace();
//        }
//        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }
    public static char[][] getSavedGames()throws Exception{
    
        char[][] games;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        cn = DriverManager.getConnection("jdbc:mysql://localhost/game?" + "user=root&password=");
        Statement stmt = cn.createStatement() ;

            
            String queryString = new String("select * from game_state");
            
            ResultSet rs = stmt.executeQuery(queryString) ;
            String result[]=new String[0];
            int i =0;
            while(rs.next())
            {
                result=new String[i+1];
                result[i]=""+(char)rs.getByte(2);
                result[i]=result[i]+(char)rs.getByte(3);
                result[i]=result[i]+(char)rs.getByte(4);
                result[i]=result[i]+(char)rs.getByte(5);
                result[i]=result[i]+(char)rs.getByte(6);
                result[i]=result[i]+(char)rs.getByte(7);
                result[i]=result[i]+(char)rs.getByte(8);
                result[i]=result[i]+(char)rs.getByte(9);
                result[i]=result[i]+(char)rs.getByte(10);
                

            }
            stmt.close();
            cn.close();
            games=new char[i+1][9];
            for (i = 0; i < result.length; i++) {
            games[i] = result[i].toCharArray();
            
            }
        return games;
    }
    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
    
    public char[] getState() {
        return state;
    }

    public void setState(char[] state) {
        this.state = state;
    }
    public void updateState(int position,String symbol){
        
    }
    public Player getNextPlayer(){
        return this.nextPlayer;
    }

    public void setNextPlayer() {
        if(this.nextPlayer== this.player1)
            this.nextPlayer=this.player2;
        else
            this.nextPlayer=this.player1;
    }
    public void endGame(){
        
    }
    public void saveGame(){
        
    }
}
