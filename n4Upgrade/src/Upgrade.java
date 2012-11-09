
import org.eclipse.jgit.lib.RepositoryBuilder;


public class Upgrade {

	public static void main(String[] args) {
		if(new RepositoryBuilder().findGitDir().getGitDir()==null) {
			BinaryUpgrader upgrader = new BinaryUpgrader();
			upgrader.upgrade();
		}
		else {
			GitUpgrader upgrader = new GitUpgrader();
			if(args.length==0) {
				upgrader.upgrade();
			}
			else {
				upgrader.upgrade(args[0]);
			}
		}
	}

}
