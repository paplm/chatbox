import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

import java.sql.*;
import java.util.*;

public class server extends JFrame {

	JTextArea myTextArea = new JTextArea();
	public JButton Bsend;
	public JTextField myTextField = new JTextField();
	LinkedList<String> users = new LinkedList<String>();
	LinkedList clientOutputStreams = new LinkedList();
	String cname;
	private static int port = 5555;

	public void initgui() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(myTextArea), BorderLayout.CENTER);
		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		JLabel msg = new JLabel("Enter a msg : ");
		Bsend = new JButton("send");
		Bsend.setEnabled(true);
		myTextField.setEnabled(true);
		p1.add(myTextField, BorderLayout.CENTER);
		p1.add(msg, BorderLayout.WEST);
		p1.add(Bsend, BorderLayout.EAST);
		getContentPane().add(p1, BorderLayout.SOUTH);
		setTitle("Server");
		setSize(300, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		// Bsend.addActionListener((ActionListener) this);
		Bsend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg1 = myTextField.getText().trim();
				if (msg1.equals("/list")) {
					myTextArea.append("users : " + users.size() + '\n'); // show
					myTextArea.append(users.toString()); // show
					
					myTextField.setText(""); // set the TextField to be blank
				} else {
					myTextArea.append("server says : " + msg1 + '\n'); // show
					tellEveryone("server says : " + msg1);
					myTextField.setText(""); // set the TextField to be blank
				}
			}
		});
	}

	public server() {
		initgui();
		try {
			ServerSocket myServerSocket = new ServerSocket(port);
			myTextArea.append("Server start ........." + '\n');
			myTextArea
					.append("Please waiting connection from client ........." + '\n');

			while (true) {
				Socket connected = myServerSocket.accept();
				ControlClient multiThread = new ControlClient(connected);
				multiThread.start();

			}
		} catch (IOException e) {
			myTextArea.append(e.toString() + '\n');
		}
	}

	public static void main(String[] args) {
		port = Integer.parseInt(JOptionPane.showInputDialog(
				"enter port number", port));
		new server();
	}

	public void whisper(String from, String name, String msg) {
		try {
			PrintWriter writer = (PrintWriter) clientOutputStreams.get(users
					.indexOf(name));
			String tmp1 = "private msg from " + from + " : " + msg;
			writer.println(tmp1);
		} catch (Exception ex) {
			myTextArea.append("error wishper\n" + ex);
		}
	}

	public void tellEveryone(String message) {
		Iterator it = clientOutputStreams.iterator();

		while (it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			} catch (Exception ex) {
				myTextArea.append("error telling everyone");
			}
		}
	}

	public boolean editC(String aa[]) {
		boolean c1 = false;
		String h1, h2;
		String sql = "SELECT * FROM logins WHERE username='" + aa[0] + "'";
		String sql2 = "UPDATE logins SET password='" + aa[2]
				+ "' WHERE username='" + aa[0] + "'";

		Connection connection = null;
		ResultSet resultSet = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				h1 = (resultSet.getString("username").trim());
				h2 = (resultSet.getString("password").trim());
				if (h1.equals(aa[0]) && h2.equals(aa[1])) {
					statement.executeUpdate(sql2);
					c1 = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return c1;
	}

	public boolean isOnline(String name) {
		boolean r1 = false;
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).equals(name)) {
				r1 = true;
			}
		}
		return r1;
	}

	public boolean loginC(String aa[]) throws ClassNotFoundException {
		boolean c1 = false;
		String h1, h2;
		String sql = "SELECT * FROM logins WHERE username='" + aa[0] + "'";
		Connection connection = null;
		ResultSet resultSet = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				h1 = (resultSet.getString("username").trim());
				h2 = (resultSet.getString("password").trim());
				if (h1.equals(aa[0]) && h2.equals(aa[1])) {
					c1 = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return c1;
	}

	public boolean regC(String aa[]) {
		boolean c1 = false;
		String h1;
		String sql = "SELECT * FROM logins WHERE username='" + aa[0] + "'";
		Connection connection = null;
		ResultSet resultSet = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				h1 = (resultSet.getString("username").trim());
				if (h1.equals(aa[0])) {
					c1 = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return c1;
	}

	public void insertdb(String aa[]) {
		String sql = "INSERT INTO logins (username, password) VALUES ('"
				+ aa[0] + "','" + aa[1] + "')";
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class ControlClient extends Thread {
		Socket connectFromClient;
		String clientNumber;
		InetAddress host;

		public ControlClient(Socket socket) {
			connectFromClient = socket;
			clientNumber = "noname";
		}

		public void run() {
			try {
				BufferedReader fromClient = new BufferedReader(
						new InputStreamReader(
								connectFromClient.getInputStream()));
				PrintWriter toClient = new PrintWriter(
						connectFromClient.getOutputStream(), true);
				clientOutputStreams.add(toClient);
				while (true) {
					String message;
					String tmp, tmp2, tmpM;
					message = fromClient.readLine();
					tmpM = message;
					if (message != null) {
						myTextArea.append(clientNumber + " says : " + message
								+ '\n');
					}
					if (message.trim().toLowerCase().equals("hello")
							|| message.trim().toLowerCase().equals("hi")) {
						tmp = ("Hi,Client");
						toClient.println(tmp);
						myTextArea.append("        Message to client #"
								+ clientNumber + " : " + tmp + '\n');
					} else if (message.equals("prelogoutmsg")) {
						clientOutputStreams.remove(users.indexOf(clientNumber));
						fromClient.close();
						toClient.close();
						connectFromClient.close();
						users.remove(clientNumber);
						tellEveryone(clientNumber + " has offline.");
						tellEveryone("servermsgnewuser:" + users);

					} else if (message.startsWith("clienteditpassword")) {
						String rev = message.substring(18, message.length());
						String ax[] = rev.split(" ");
						String sd = "clienteditpassword:bad";
						if (editC(ax)) {
							sd = "clienteditpassword:good";
						}
						toClient.println(sd);
					} else if (message.startsWith("registercheckmsg")) {
						String rev = message.substring(16, message.length());
						String ax[] = rev.split(" ");
						String sd = "registersendback:bad";
						if (!regC(ax)) {
							insertdb(ax);
							sd = "registersendback:good";
						}

						toClient.println(sd);
						users.add(clientNumber);
						if (clientNumber.equals("noname")
								|| sd.equals("serversendback:notcomplete")) {
							clientOutputStreams.remove(users
									.indexOf(clientNumber));
							fromClient.close();
							toClient.close();
							connectFromClient.close();
							users.remove(clientNumber);
						}
					} else if (message.startsWith("authorizedcheckmsg")) {

						String rev = message.substring(18, message.length());
						String ax[] = rev.split(" ");
						String sd = "serversendback:notcomplete";
						if (loginC(ax) && !isOnline(ax[0])) {
							sd = "serversendback:complete";
							clientNumber = ax[0];
						}
						users.add(clientNumber);
						toClient.println(sd);
						if (clientNumber.equals("noname")
								|| sd.equals("serversendback:notcomplete")) {
							clientOutputStreams.remove(users
									.indexOf(clientNumber));
							fromClient.close();
							toClient.close();
							connectFromClient.close();
							users.remove(clientNumber);
						} else {
							tellEveryone(clientNumber + " has online.");
							tellEveryone("servermsgnewuser:" + users);
						}

					} else if (message.startsWith("/msg")) {
						tmp = clientNumber + " public msg: "
								+ (message.substring(4, message.length()));
						tellEveryone(tmp);
						myTextArea.append(tmp + '\n');
					} else if (message.startsWith("/whisperto")) {
						String tmp3 = message.substring(10, message.length());
						String ax[] = tmp3.split(" ");
						String from = clientNumber;
						whisper(from, ax[0], ax[1]);
					} else if (message.startsWith("/rvs")) {
						String rvs = message.substring(4, message.length());
						String tm1 = new StringBuffer(rvs).reverse().toString()
								.trim();
						tmp = clientNumber + " public reverse msg: " + tm1;
						tellEveryone(tmp);
						myTextArea.append(tmp + '\n');
					} else if (message.toLowerCase().equals("/list")) {
						toClient.println(users);
						myTextArea.append("        Message to client #"
								+ clientNumber + " : " + users + '\n');
					}
					//

					else if (message.toLowerCase().equals("/date")) {
						Calendar now = Calendar.getInstance();
						int day = now.get(Calendar.DAY_OF_WEEK);
						int d = now.get(Calendar.DAY_OF_MONTH);
						int m = now.get(Calendar.MONTH);
						int y = now.get(Calendar.YEAR);
						String sday = "day of week";
						switch (day) {
						case (1):
							sday = "Sunday";
							break;
						case (2):
							sday = "Monday";
							break;
						case (3):
							sday = "Tuesday";
							break;
						case (4):
							sday = "Wednesday";
							break;
						case (5):
							sday = "Thursday";
							break;
						case (6):
							sday = "Friday";
							break;
						case (0):
							sday = "Saturday";
							break;
						}

						tmp = ("Today is " + sday + "," + d + "/" + (m + 1)
								+ "/" + y);
						toClient.println(tmp);
						myTextArea.append("        Message to client #"
								+ clientNumber + " : " + tmp + '\n');
					} else if (message.toLowerCase().equals("/time")) {
						Calendar now = Calendar.getInstance();
						int h = now.get(Calendar.HOUR_OF_DAY);
						int m = now.get(Calendar.MINUTE);
						int s = now.get(Calendar.SECOND);
						tmp = (h + ":" + m + ":" + s);
						toClient.println(tmp);
						myTextArea.append("        Message to client #"
								+ clientNumber + " : " + tmp + '\n');
					} else if (message.toLowerCase().equals("/server")) {

						host = InetAddress.getLocalHost();
						String tmp1 = (host.getHostAddress());
						tmp = "The server is running on IP " + tmp1;
						toClient.println(tmp);
						myTextArea.append("        Message to client #"
								+ clientNumber + " : " + tmp + '\n');
					}
					//
					else {
						tmp = (message + " is unknown command");
						toClient.println(tmp);
						myTextArea.append("        Message to client #"
								+ clientNumber + " : " + tmp + '\n');
					}

				}
			} catch (Exception e) {
				if (e.toString().equals(
						"java.net.SocketException: Connection reset")) {
					myTextArea.append("client has closed the conection\n");
				} else if (e.toString()
						.equals("java.lang.NullPointerException")) {
					myTextArea.append("client has closed the conection\n");
				} else {
					myTextArea.append(e.toString() + '\n');
				}
			}
		}
	}
}
