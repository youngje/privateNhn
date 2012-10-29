

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jgit.api.DeleteTagCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidMergeHeadsException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.transport.RefSpec;

public class UpgradeCheck {

	private Repository local;
	private Git localGit;
	private ArrayList<String> localTags;
	private ArrayList<String> remoteTags;
	
	private String remoteURI;
	
	public void setRepositories() throws IOException, InvalidRemoteException, TransportException, GitAPIException{
		this.local = new RepositoryBuilder().findGitDir().build();
		//this.remoteURI = getRemote(local);
		this.remoteURI = "git://github.com/youngje/privateNhn.git";
		this.localGit = new Git(local);
		
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
		FetchCommand fetch = localGit.fetch();
		localGit.branchList().call();
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
	
	public boolean isNew() throws WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, CanceledException, RefNotFoundException, NoHeadException, TransportException, GitAPIException{
		this.localTags = getTags(local);
		fetchGit(local);
		this.remoteTags = getTags(local);
		
		return findLastTag(localTags).equals(findLastTag(remoteTags));
	}
	
	public ArrayList<String> getUpdatedTags(){
		for(String tag : localTags) {
			if(remoteTags.contains(tag)) {
				remoteTags.remove(tag);
			}
		}
		
		return remoteTags;
	}

	public void merge(ArrayList<String> updatedTags) throws IOException, NoHeadException, ConcurrentRefUpdateException, CheckoutConflictException, InvalidMergeHeadsException, WrongRepositoryStateException, NoMessageException, GitAPIException {
		Ref tag = local.getRef(findLastTag(updatedTags));
		MergeCommand merge = localGit.merge();
		merge.include(tag).call();
	}

	public void deleteTags(ArrayList<String> updatedTags) throws GitAPIException {
		for(String tag : updatedTags) {
			DeleteTagCommand delete = localGit.tagDelete();
			delete.setTags(tag).call();
		}
	}
	
}
