import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jgit.lib.RepositoryBuilder;


public class Upgrade {

	public static void main(String[] args) {
		if(new RepositoryBuilder().findGitDir().getGitDir()==null) {
			System.out.println(" Git이 발견되지 않았습니다. \n  업그레이드를 종료합니다.");;
			BinaryUpgrader upgrade = new BinaryUpgrader(BinaryUpgrader.checkVersion("version.txt"));
			upgrade.download();
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
