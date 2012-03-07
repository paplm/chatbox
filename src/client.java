/* client and server connect with socket project.
 * this file is a part of the project.
 * it's a client.
 * develop by pretaget.
 * user specify the port and IP.
 * connect to the server that have the same IP and port 
 * client will get the msg back from the server when client send something to the server
 * client will exit when client send the word "exit" to the server
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.*;

public class client extends JFrame implements ActionListener{
	public static  JTextArea myTextArea = new JTextArea();
	public  JTextArea list = new JTextArea();
	public  JTextField myTextField = new JTextField();
	public static  String Cname="noname";
	public  String Cpw="1234";
	public  JButton okB,cancelB,Bsend,Blogin,Blogout;
	public  JTextField status,nameT,pwT,ipT,portT,nm,idRT,pwRT;
	public  JMenuItem mOpt,mEdit,mReg,mExit,oMsg,oRvs,oList,oDate,oTime,oServer,oWisper,hAbout,hHelp;
	public  JMenu command;
	public  JFrame menuL=new JFrame(),main=new JFrame();
	public JPanel t1,mainpanel;
	public JLabel pwL,idL;
	private static int port=5555;		//default port
	private static String ip="127.0.0.1";		//default IP
	private boolean connected=false;
	private Socket checkClient;
	private BufferedReader CfromServer;		//new BufferedReader to get msg from the server
	private PrintWriter CtoServer;			//new PrintWriter to send request to the server
	String message,listI;

	
	public client(){
		iniGUI();

	}
	public void iniGUI(){
		option();
		JMenuBar menubar=new JMenuBar();
		JMenu menu,help;
		menu=new JMenu("menu");
		command=new JMenu("Command");
		help=new JMenu("help");
		mOpt=new JMenuItem("Option");
		mEdit=new JMenuItem("Edit account");
		mReg=new JMenuItem("Register");
		mExit=new JMenuItem("Exit");
		oMsg=new JMenuItem("send to all");
		oRvs=new JMenuItem("send reverse to all");
		oList=new JMenuItem("list all");
		oDate=new JMenuItem("date");
		oTime=new JMenuItem("time");
		oServer=new JMenuItem("server");
		oWisper=new JMenuItem("private msg");
		hAbout=new JMenuItem("About us");
		hHelp=new JMenuItem("Help");
		menu.add(mEdit);
		menu.add(mReg);
		menu.add(mOpt);
		menu.add(mExit);
		command.setEnabled(false);
		command.add(oMsg);
		command.add(oRvs);
		command.add(oList);
		command.add(oDate);
		command.add(oTime);
		command.add(oServer);
		command.add(oWisper);
		help.add(hAbout);
		help.add(hHelp);
		menubar.add(menu);
		menubar.add(command);
		menubar.add(help);
		mainpanel = new JPanel();
		JPanel lp = new JPanel();
		JPanel p1 = new JPanel();
		JPanel chat = new JPanel();
		t1 = new JPanel();
		JPanel tt = new JPanel();
		mainpanel.setLayout(new BorderLayout());
		t1.setLayout(new FlowLayout());
		p1.setLayout(new BorderLayout());
		chat.setLayout(new BorderLayout());
		lp.setLayout(new BorderLayout());
		JLabel msg=new JLabel("Enter a msg : ");
		Bsend=new JButton("send");
		Bsend.setEnabled(false);
		myTextField.setEnabled(false);
		p1.add(myTextField, BorderLayout.CENTER);
		p1.add(msg, BorderLayout.WEST);
		p1.add(Bsend,BorderLayout.EAST);
		JScrollPane sc=new JScrollPane(myTextArea);
		myTextArea.setEditable(false);
		nm=new JTextField(7);
		nm.setEnabled(false);
		JScrollPane slist=new JScrollPane(list);
		slist.setEnabled(false);
		list.setEditable(false);
		lp.add(nm,BorderLayout.NORTH);
		lp.add(slist,BorderLayout.CENTER);
		chat.add(sc,BorderLayout.CENTER);
		chat.add(lp,BorderLayout.EAST);
		idL=new JLabel("id:");
		pwL=new JLabel("pw:");
		Blogin=new JButton("login");
		Blogout=new JButton("logout");
		status=new JTextField(2);
		status.setBackground(Color.black);
		nameT=new JTextField(10);
		nameT.setText(Cname);
		pwT=new JPasswordField(10);
		pwT.setText(Cpw);
		t1.add(idL);
		t1.add(nameT);
		t1.add(pwL);
		t1.add(pwT);
		t1.add(Blogin);
		t1.add(Blogout);
		t1.add(status);
		connectedstatus(false);
		mainpanel.add(p1,BorderLayout.SOUTH);
		mainpanel.add(chat,BorderLayout.CENTER);
		mainpanel.add(t1,BorderLayout.NORTH);
		main.setJMenuBar(menubar);
		main.add(mainpanel);
		main.setTitle("Chatbox");
		main.setSize(550, 300);
		main.setDefaultCloseOperation(EXIT_ON_CLOSE);
		main.setVisible(true);

		Blogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Cname=nameT.getText();
				Cpw=pwT.getText();
				if(check()){					
					connectedstatus(true);
				}
			}		
		});
		Blogout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				outC();
				connectedstatus(false);
			}		
		});
		mEdit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				edit ed=new edit();
			}		
		});
		mOpt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				menuL.setVisible(true);
			}		
		});
		mReg.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				reg rg=new reg();
			}		
		});
		mExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				outC();
				System.exit(1);
			}		
		});
		oMsg.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String aa="/msg"+myTextField.getText();
				myTextField.setText(aa);
			}		
		});
		oRvs.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String aa="/rvs"+myTextField.getText();
				myTextField.setText(aa);
			}		
		});
		oList.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String aa="/list";
				myTextField.setText(aa);
			}		
		});
		oDate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String aa="/date";
				myTextField.setText(aa);
			}		
		});
		oTime.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String aa="/time";
				myTextField.setText(aa);
			}		
		});
		oServer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String aa="/server";
				myTextField.setText(aa);
			}		
		});
		oWisper.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String aa="/whisperto";
				myTextField.setText(aa);
			}		
		});
		hAbout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				about ab=new about();
			}		
		});
		hHelp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				help ab=new help();
			}		
		});
		Bsend.addActionListener(this);
		myTextField.addActionListener(this);
	}	

	public void option(){
		JPanel mainL = new JPanel();
		mainL.setLayout(new GridLayout(4,2));
		JLabel logL,spaceL,ipL,portL;
		logL=new JLabel("Option");
		spaceL=new JLabel("");
		ipL=new JLabel("IP");
		portL=new JLabel("Port");
		ipT=new JTextField(ip);
		portT=new JTextField(""+port);
		okB=new JButton("ok");
		cancelB=new JButton("cancel");
		mainL.add(logL);
		mainL.add(spaceL);
		mainL.add(portL);
		mainL.add(portT);
		mainL.add(ipL);
		mainL.add(ipT);
		mainL.add(okB);
		mainL.add(cancelB);
		menuL.add(mainL);
		menuL.setTitle("Option");
		menuL.setSize(200,150);
		menuL.setVisible(false);
		okB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				port=Integer.parseInt(portT.getText());
				ip=ipT.getText();
				menuL.setVisible(false);
				
			}		
		});
		cancelB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				menuL.setVisible(false);
			}		
		});
	}

	public void connectedstatus(boolean bb){
		if(bb){
			connected=true;
			Bsend.setEnabled(true);
			myTextField.setEnabled(true);
			Bsend.setEnabled(true);
			nameT.setEnabled(false);
			pwT.setEnabled(false);
			mOpt.setEnabled(false);
			mEdit.setEnabled(true);
			command.setEnabled(true);
			status.setBackground(Color.green);
			Blogin.setEnabled(false);
			Blogout.setEnabled(true);
			myTextArea.setText("");
			nm.setText("online");
			main.repaint();
		}
		else{
			connected=false;
			Bsend.setEnabled(false);
			myTextField.setEnabled(false);
			myTextField.setText("");
			Bsend.setEnabled(false);
			nameT.setEnabled(true);
			pwT.setEnabled(true);
			mOpt.setEnabled(true);
			mEdit.setEnabled(false);
			command.setEnabled(false);
			status.setBackground(Color.black);
			Blogin.setEnabled(true);
			Blogout.setEnabled(false);
			myTextArea.setText("");
			list.setText("");
			nm.setText("offline");
			main.repaint();
			
		}
	}
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource()==myTextField||evt.getSource()==Bsend) {
			String msg1 = myTextField.getText().trim();
			if(msg1.equals("/logout")){
				outC();
				connectedstatus(false);
			}
			else if(msg1.equals("/exit")){
				outC();
				System.exit(1);
			}
			else{
			myTextArea.append(Cname+" says : " + msg1 + '\n');	//show on windows
			CtoServer.println(msg1);	//send request to the server
			myTextField.setText(""); //set the TextField to be blank
			}
			}
		}

	public void outC(){
		
		try{
			if(CtoServer!=null){
				CtoServer.println("prelogoutmsg");
				CtoServer.close();
				CtoServer=null;
			}
			if(CfromServer!=null){
				CfromServer.close();
				CfromServer=null;
			}
			if(checkClient!=null){
				checkClient.close();
				checkClient=null;
			}

		} catch (IOException e) {
				//myTextArea.append(e.toString() + '\n');
		}
	}
	
	public void listC(String aa){
		listI="";
		String ax[] = aa.substring(1, aa.length()-1).split(", ");

		for(int i=0;i<ax.length;i++){
			listI=listI+(ax[i]+"\n");
		}
		list.setText(listI);
		main.repaint();
	}
	
	public static void editC(String id,String pw,String newpw){
		
		try{
			Socket checkreg = new Socket(InetAddress.getByName(ip), port);	//new socket using IP and port
			BufferedReader CRfromServer = new BufferedReader(new InputStreamReader(checkreg.getInputStream()));	//set to get msg from the server
			PrintWriter CRtoServer = new PrintWriter(checkreg.getOutputStream(), true);		//set to send request to the server
			CRtoServer.println("clienteditpassword"+id+" "+pw+" "+newpw);
			if(CRfromServer.readLine().equals("clienteditpassword:good")){
				JOptionPane.showMessageDialog(null, "Your password had change.");
			}
			else{
				JOptionPane.showMessageDialog(null, "Can not change the password.");
				
			}
			checkreg.close();
			CRfromServer.close();
			CRtoServer.close();
			} 
		catch (IOException e) {
				myTextArea.append(e.toString() + '\n');
			}

	}
	
	public static boolean regcheck(String id,String pw){
		boolean c1=false;

		
		try{
			Socket checkreg = new Socket(InetAddress.getByName(ip), port);	//new socket using IP and port
			BufferedReader CRfromServer = new BufferedReader(new InputStreamReader(checkreg.getInputStream()));	//set to get msg from the server
			PrintWriter CRtoServer = new PrintWriter(checkreg.getOutputStream(), true);		//set to send request to the server
			CRtoServer.println("registercheckmsg"+id+" "+pw);
			
			if(CRfromServer.readLine().equals("registersendback:good")){
				c1=true;
				JOptionPane.showMessageDialog(null, "registered");
			}
			else{
				JOptionPane.showMessageDialog(null, "can not register");
				
			}
			checkreg.close();
			CRfromServer.close();
			CRtoServer.close();
			} 
		catch (IOException e) {
				myTextArea.append(e.toString() + '\n');
			}
		return c1;
	}
	public boolean check(){
		boolean c1=false;
		try{
			checkClient = new Socket(InetAddress.getByName(ip), port);
			CfromServer = new BufferedReader(new InputStreamReader(checkClient.getInputStream()));
			CtoServer = new PrintWriter(checkClient.getOutputStream(), true);
			CtoServer.println("authorizedcheckmsg"+Cname+" "+Cpw);
			String rst=CfromServer.readLine();
			
			if(rst.equals("serversendback:complete")){
				c1=true;
			}
			else if(rst.equals("serversendback:notcomplete")){
				myTextArea.append("check your ID or Password again,May be someone online this acount.\n");
				checkClient.close();
				CfromServer.close();
				CtoServer.close();
			}
			

			
		} 
		catch (IOException e) {
			if(e.toString().equals("java.net.ConnectException: Connection refused: connect")){
				myTextArea.append("The server is not online.\n");
			}
			else{
				myTextArea.append(e.toString() + '\n');
			}
		}
		new Thread(new Runnable() {
			public void run() {
				String line;
				try {
					while (connected && ((line = CfromServer.readLine()) != null)){
						if(line.startsWith("servermsgnewuser:")){
							String tmp=line.substring(17, line.length());
							listC(tmp);
						}
						else{
						myTextArea.append(line + "\n");
						}
						}
				} catch(IOException e) {
					//myTextArea.append(e.toString() + '\n');
					return;
				}
			}
		}).start();
		return c1;
	}

	public static void main(String[] args) 	{

			client asdf=new client();

	}
}
class edit{
	edit(){
		final JFrame editM=new JFrame();
		JPanel mainE=new JPanel();
		JLabel oldpw,new1,new2;
		final JTextField oldT;
		final JTextField n1T;
		final JTextField n2T;
		JButton Eok,Eca;
		oldpw=new JLabel("old password");
		new1=new JLabel("new password");
		new2=new JLabel("new again");
		oldT=new JPasswordField();
		n1T=new JPasswordField();
		n2T=new JPasswordField();
		Eok=new JButton("ok");
		Eca=new JButton("cancel");
		mainE.setLayout(new GridLayout(4,2));
		mainE.add(oldpw);
		mainE.add(oldT);
		mainE.add(new1);
		mainE.add(n1T);
		mainE.add(new2);
		mainE.add(n2T);
		mainE.add(Eok);
		mainE.add(Eca);
		editM.add(mainE);
		editM.setTitle("Change password");
		editM.setSize(250,200);
		editM.setVisible(true);
		Eok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String old,nw1,nw2;
				old=oldT.getText();
				nw1=n1T.getText();
				nw2=n2T.getText();
				if(n1T.getText().equals(n2T.getText())){
					client.editC(client.Cname, old, nw1);
				}
				else{
					JOptionPane.showMessageDialog(null, "Please enter the new passwords as the same.");
				}
				editM.setVisible(false);
					
			}		
		});
		Eca.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				editM.setVisible(false);
			}		
		});
	}
}
class about{
	about(){
		final JFrame about=new JFrame();
		JPanel mainA=new JPanel();
		JPanel top=new JPanel();
		top.setLayout(new BorderLayout());
		mainA.setLayout(new BorderLayout());
		ImageIcon yes=new ImageIcon("./img/yellow.jpg");
		JPanel adf=new JPanel(new BorderLayout());
		adf.add(new JLabel("Chatbox 5.5 beta\n"),BorderLayout.CENTER);
		top.add(adf,BorderLayout.NORTH);
		top.add(new JLabel(yes),BorderLayout.CENTER);
		String ab="Chatbox is the project about network protocol.\n" +
				"This using Socket to connect with each clients and server.\n" +
				"-include about and help" +
				"-this is the final version of the project" +
				"Software engineering,CAMT,Chaingmai University,Thailand\n" +
				"Summer/2012";
		JTextArea abL=new JTextArea(ab);
		JScrollPane sc=new JScrollPane();
		sc.setViewportView(abL);
		abL.setEditable(false);
		mainA.add(top,BorderLayout.NORTH);
		mainA.add(sc,BorderLayout.CENTER);
		about.add(mainA);
		about.setTitle("About Chatbox");
		about.setSize(250,300);
		about.setVisible(true);
	}
}
class help{
	help(){
		final JFrame help=new JFrame();
		JPanel mainH=new JPanel(new BorderLayout());
		String aaa="Welcome\n" +
				"First, if you open program it will Display about interface of client and have 3 menus bar, input username and password box and user’s status\n" +
				"\n" +
				"Menu bar\n" +
				"Menu -includes 4 items in menus bar.\n" +
				"1.	Edit account – you can change password of your old password\n" +
				"2.	Register – you can register new username and password in this menu\n" +
				"3.	Option – you can change your port and IP address of server\n" +
				"4.	Exit – quit the program\n\n" +
				"Command - includes 7 items in command bar\n" +
				"1.	Send to all- in this command user can show message to all client and server.\n" +
				"2.	Send reverse to all - in this command user can reverse their message and show to all clients and server.\n" +
				"3.	List all - in this command user can show all of users that connecting to server.\n" +
				"4.	Date - in this command program can display current date.\n" +
				"5.	Time - in this command program can display current time.\n" +
				"6.	Server - in this command server can display IP address that server run on.\n" +
				"7.	Private massage - in this command user can send private message to another specifically user.\n\n" +
				"Another command\n" +
				"•	/logout to logout from server\n\n" +
				"Help- includes 2 items in help bar\n" +
				"1.	About us - is show you about developers and another information of program\n" +
				"2.	Help - is command to help you to use and explain command of program";
		JTextArea textA=new JTextArea(aaa);
		textA.setEditable(false);
		JScrollPane sc=new JScrollPane();
		sc.setViewportView(textA);
		mainH.add(sc);
		help.add(mainH);
		help.setTitle("Help");
		help.setSize(500,500);
		help.setVisible(true);
	}
}
class reg{
	reg(){
	final JFrame regis;
	final JTextField idRT;
	final JTextField pwRT;
	JPanel mainR;
	JButton Ba,Bd;
	regis=new JFrame();
	mainR = new JPanel();
	mainR.setLayout(new GridLayout(3,2));
	JLabel idR,pwR;
	idR=new JLabel("id");
	pwR=new JLabel("pw");
	idRT=new JTextField("");
	pwRT=new JPasswordField("");
	Ba=new JButton("Acept");
	Bd=new JButton("Decline");
	mainR.add(idR);
	mainR.add(idRT);
	mainR.add(pwR);
	mainR.add(pwRT);
	mainR.add(Ba);
	mainR.add(Bd);
	regis.add(mainR);
	regis.setTitle("Register");
	regis.setSize(200,100);
	regis.setVisible(true);
	Ba.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(idRT.getText().equals("")||pwRT.getText().equals("")){
			}
			else{
				client.regcheck(idRT.getText(),pwRT.getText());
				regis.setVisible(false);
			}
			//System.out.println(idRT.getText()+" "+pwRT.getText());
		}		
	});
	Bd.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			regis.setVisible(false);
		}		
	});
	}
}