package bernard.colin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class Formatter {
	
	private static final String WORKING_DIR = "";
	
	public static void main(String[] args) {
		FTP4J ftp = new FTP4J();
		try {
			ftp.connectToServer();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			System.out.println("Error connecting to server.");
			e.printStackTrace();
		}
		
		Scanner scan = new Scanner(System.in);
		
		while(true) {
			System.out.println("------------------------\nPress ENTER to format, anything else to exit...");
			String readString = scan.nextLine();
			if(readString.isEmpty()) {
				// execute ruby script
//				try{
//					String[] command = {"cmd.exe", "/C", "Start", WORKING_DIR + "runruby.bat"};
//					Process p = Runtime.getRuntime().exec(command);
//				} catch (IOException e) {
//					
//				}
				
				// read in src and dest from file
				try {
					BufferedReader br = new BufferedReader(new FileReader(WORKING_DIR + "temp.txt"));
					
					String src = null;
					String dest = null;
					while ((src = br.readLine()) != null) {
						dest = br.readLine();
						System.out.println("Uploading: " + src);
						System.out.println("Dest: " + dest);
						src = WORKING_DIR + src;
						
						try {
							ftp.transfer(src, dest);
						} catch (IllegalStateException | FTPIllegalReplyException | FTPException
								| FTPDataTransferException | FTPAbortedException e) {
							System.out.println("Error uploading file to ftp server.");
							e.printStackTrace();
						}
						
					}
					br.close();
				} catch (IOException e) {
					System.out.println("Error reading temp file.");
					e.printStackTrace();
				}
				
			} else {
				System.out.println("EXITING by user cmd");
				break;
			}
		}
		
		scan.close();
		
		try {
			ftp.disconnectFromServer();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			System.out.println("Error disconnecting from server.");
			e.printStackTrace();
		}
	}
}
