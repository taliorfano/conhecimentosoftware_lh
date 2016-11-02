package br.cefet.tcc.teste;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.ListTagCommand;
import org.eclipse.jgit.api.TagCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class App2 {

	public static void main(String[] args) throws IOException, GitAPIException {

		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = new FileRepositoryBuilder().
				setGitDir(new File("/home/talita/git/guice/.git")).build();
		/*Repository repository = builder.readEnvironment() // scan environment
															// GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();*/

		System.out.println("Listing local branches:");
		Git git = new Git(repository);

		List<Ref> call = git.branchList().call();
		for (Ref ref : call) {
			System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
		}

		System.out.println("Now including remote branches:");
		call = git.branchList().setListMode(ListMode.ALL).call();
		for (Ref ref : call) {
			System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
		}

		// tags
		
		List<RevTag> call2 = git.tagList().call();
		/*for (RevTag ref : call2) {
		    System.out.println("Tag: " + ref + " " + ref.getName() + " " );
		}*/
		//if(call2 == null){
			System.out.println("null");
		//}
		//getCommitsByTree("master/4.1");
	}
	
	public static void getCommitsByTree(String treeName) throws NoHeadException, JGitInternalException, MissingObjectException, IncorrectObjectTypeException, AmbiguousObjectException, IOException {

        Repository repository = new FileRepository("/home/talita/git/guice/.git");
        
        Git git = new Git(repository);
		Iterable<RevCommit> revCommits = git.log()
                .add(repository.resolve(treeName))
                .call();
		
		
        for(RevCommit revCommit : revCommits){
            System.out.println("Tags "+revCommit.getName());
        }

    }
}