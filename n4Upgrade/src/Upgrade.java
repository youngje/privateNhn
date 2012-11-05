import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jgit.lib.RepositoryBuilder;


public class Upgrade {

	public static void main(String[] args) {
		
		if(new RepositoryBuilder().findGitDir().getGitDir()==null) {
			System.out.println(" Git이 발견되지 않았습니다. \n  업그레이드를 종료합니다.");;
			try {
				BufferedReader  in = new BufferedReader(new FileReader("version.txt"));
				String version = in.readLine();
				version = version.split("version")[1];
			    System.out.println(version);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("버전 파일이 존재하지 않습니다.");
			}
			System.exit(1);
		}
		
		
		
		Upgrader upgrader = new Upgrader();
		if(args.length==0) {
			upgrader.upgrade();
		}
		else {
			upgrader.upgrade(args[0]);
		}
	}

}
