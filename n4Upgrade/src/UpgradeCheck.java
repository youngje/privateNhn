

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TagCommand;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefSpec;

public class UpgradeCheck {

	Repository local; 
	Repository master;
	private String remoteURI;
	
	public void setRepositories() throws IOException, InvalidRemoteException, TransportException, GitAPIException{
		this.local = new RepositoryBuilder().findGitDir().build();
		//this.remoteURI = getRemote(local);
		this.remoteURI = "git://github.com/youngje/privateNhn.git";
		fetchGit(local);
		
	}
	
	public String getRemote(Repository repo){
		Config storedConfig = repo.getConfig();
		Set<String> remotes = storedConfig.getSubsections("remote");
		String url = null;
		for(String remoteName : remotes){
			url = storedConfig.getString("remote", remoteName, "url");
		}
		return url;
		
	}
	
	public String fetchGit(Repository repo) throws InvalidRemoteException, TransportException, GitAPIException{
		Git git = new Git(repo);
		FetchCommand fetch = git.fetch();
		git.branchList().call();
		return fetch.setRemote(remoteURI).setRefSpecs(new RefSpec("refs/heads/master")).call().getMessages();
	}
	
	public ArrayList<String> getTags(Repository repository){
		Iterator<String> tagKeys = repository.getTags().keySet().iterator();
		ArrayList<String> tags = new ArrayList<String>();
		
		while(tagKeys.hasNext()){
			tags.add(tagKeys.next());
		}

		for(String tag : tags){
			System.out.println(tag);
		}
		return tags;
	}

	public String findLastTag(ArrayList<String> tags){
		Collections.sort(tags);
		return tags.get(0);
	}
	
	public void isNew() throws WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, CanceledException, RefNotFoundException, NoHeadException, TransportException, GitAPIException{
		String localTag = findLastTag(getTags(local));
		String masterTag = findLastTag(getTags(master));
		
		System.out.println();
		
		if(localTag.equals(masterTag)){
			System.out.println("최신 버전입니다");
		}
		else{
			System.out.println("최신 버전이 존재합니다.");
			Git git = new Git(local);
			if(git.pull().setTimeout(500).call().isSuccessful()){
				System.out.println("upgrade가 성공하였습니다");
			}
			else{
				System.out.println("upgrade를 실패하였습니다");
			}
		}
	}
	
}
