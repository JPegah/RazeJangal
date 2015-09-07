package razeJangal.record;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

import javax.swing.JOptionPane;

import razeJangal.game.Game;
import razeJangal.gui.Client;
import razeJangal.gui.Server;
import razeJangal.gui.protocol.InvocationVo;
import razeJangal.servers.MultiServer;
import razeJangal.servers.graphicalServer;


/**
 * Create the server, clients and game 
 * for running server args: server portNumber
 * for running graphical client: client gui
 * for running console client: client console playername serverIP portNumber
 * @author Pegah Jandaghi
 * 
 */
public class PlayRunner {
	public static void main(String[] args) throws Exception {
		if(args[0].equals("server")){
			
			ServerSocket serversocket = null;
			try {
				serversocket = new ServerSocket(Integer.parseInt(args[1]), 50);
			} catch (NumberFormatException e) {} catch (IOException e) {}
			
			while(true){
				Scanner scanner = new Scanner(System.in);
				System.out.println("how many viewer?");
				int n = scanner.nextInt();
				System.out.println("name of file?");
				String count = scanner.next();
				recordThread tr = new recordThread(n, serversocket, Integer.parseInt(args[1]), count);
				tr.start();
			}
		}else{
			Client myClient = null;
			if(args[1].equals("gui")){
				int n = Integer.parseInt(JOptionPane.showInputDialog("Please enter the port number"));
				String IP = JOptionPane.showInputDialog("Please enter the IP of the server");				
				myClient = new Client(n, IP, true, "gui", "");
				myClient.recieve();
			
			}else{
				int n = Integer.parseInt(args[4]);
				String IP = args[3];
					myClient = new Client(n, IP, true, "console", args[2]);
					myClient.recieve();
				
			
			}
		}
	}
}

class recordThread extends Thread{
	int n;
	int port;
	ServerSocket s;
	String count;
	public recordThread(int n, ServerSocket serversocket, int port, String count){
		this.n = n;
		this.s = serversocket;
		this.port = port;
		this.count = count;
	}
	public void run(){
		try {
			PlayRecorded s1 = new PlayRecorded(count, port, n, s);
		} catch (Exception e) {}
	}
}

