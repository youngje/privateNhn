import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class BinaryUpgrader {
		
	public static final String DOWNLOAD_URL = "http://pds25.egloos.com/pds/201211/08/53/test.txt";
	public static final String VERSION_FILE = "version.txt";
	private String downloadFile;
	
	public BinaryUpgrader() {}
	
	public void upgrade() {
		if(isLatetestVersion()) {
			System.out.println(" 최신 버전입니다.");
			System.exit(1);
		}
		else {
			System.out.println(" 최신 버전이 존재합니다.");
			System.out.print(" 업그레이드 하시겠습니까?(y/n)  : ");
			if(GitUpgrader.getYesNoKey()) {
				System.out.println(" 업그레이드 완료 되었습니다.");
			}
			else {
				rm_rf(new File(downloadFile));
				System.out.println(" 업그레이드가 취소되었습니다.");
			}
		}
	}
	
	public boolean isLatetestVersion() {
		String localVersion = getLocalVersion();
		String remoteVersion = getRemoteVersion();
		System.out.println(" - Current version : " + localVersion);
		System.out.println(" - The latest version : " + remoteVersion);
		if(compareVersion(localVersion, remoteVersion)>0) 
			return true;
		else 
			return false;
	}
	
	public String getLocalVersion() {
		return checkVersion(VERSION_FILE);
	}
	
	public String getRemoteVersion() {
		this.downloadFile = FileDownload.download(DOWNLOAD_URL);
		return checkVersion(downloadFile);
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
	
	public static int compareVersion(String localVersion, String remoteVersion){
		double local = Double.valueOf(localVersion);
		double remote = Double.valueOf(remoteVersion);
		
		return local==remote?0:(Double.valueOf(localVersion)>Double.valueOf(remoteVersion)?-1:1);
	}
	
	private void rm_rf(File file) {
        assert file != null;
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            assert list != null;
            for(int i = 0; i < list.length; i++){
                rm_rf(list[i]);
            }
        }
        file.delete();
    }
	
}
