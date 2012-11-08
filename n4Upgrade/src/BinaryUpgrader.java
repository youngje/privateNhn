import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class BinaryUpgrader {
	
	private String localVersion;
	public static final String DOWNLOAD_URL = "http://pds25.egloos.com/pds/201211/08/53/test.txt";
	
	public BinaryUpgrader() {}
	
	public BinaryUpgrader(String version) {
		this.localVersion = version;
	}

	public String getLocalVersion() {
		return localVersion;
	}
	
	public void download(String localversion) {
		String downloadFile = FileDownload.download(DOWNLOAD_URL);
		String remoteVersion = checkVersion(downloadFile);
		
	}
	
	public static String checkVersion(String filename) {
		String version = null;
		try {
			BufferedReader  in = new BufferedReader(new FileReader(filename));
			version = in.readLine().split("version")[1];
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("버전 파일이 존재하지 않습니다.");
		}
		 return version;
	}
	
}
