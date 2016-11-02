package br.cefet.tcc.teste;

import java.io.IOException;

import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepository;
import org.gitective.core.CommitFinder;
import org.gitective.core.CommitUtils;
import org.gitective.core.PathFilterUtils;
import org.gitective.core.filter.commit.AndCommitFilter;
import org.gitective.core.filter.commit.AuthorSetFilter;
import org.gitective.core.filter.commit.BugFilter;
import org.gitective.core.filter.commit.CommitCountFilter;
import org.gitective.core.filter.commit.ParentCountFilter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	Repository repo = new FileRepository("/home/talita/git/guice/.git");
    	RevCommit latestCommit = CommitUtils.getHead(repo);
    	
    	long inicio = System.currentTimeMillis();
    	
    	AuthorSetFilter authors = new AuthorSetFilter();
    	AndCommitFilter filters = new AndCommitFilter();
    	filters.add(new ParentCountFilter(1), authors);
    	
    	/*AuthorSetFilter authors2 = new AuthorSetFilter();
    	AndCommitFilter filters2 = new AndCommitFilter();
    	filters2.add(new ParentCountFilter(1), authors2);*/

    	CommitFinder finder = new CommitFinder("/home/talita/git/guice/.git");
    	//CommitFinder finder2 = new CommitFinder("/home/talita/git/guice/.git");
    	
    	// Total de autores
    	finder.setFilter(filters).find();
    	/*System.out.println("Total de autores: "+ authors.getPersons().size());*/
    	
    	
    	// Total de autores para arquivos .java
    	/*finder2.setFilter(filters2)
		.setFilter(PathFilterUtils.andSuffix(".java")).find();
    	System.out.println("Total de autores Java: "+authors2.getPersons().size());*/
    	
    	//System.out.println("\nTempo de execução: "+ (termino - inicio) + "ms");
    	
    	
    	System.out.println("   Nome     -      Email        -          Data  ");
    	for (PersonIdent author : authors.getPersons()){
    	     System.out.println(author.getName() + "  - "+ author.getEmailAddress() + 
    	    		 "  - "+ author.getWhen());
    	}
    	
    	//int count = authors.getPersons().size();
    	
    	
    	
    	// System.out.println("Total de "+count);
    	// System.out.println("HEAD commit is " + latestCommit.name());
    	
    	long termino = System.currentTimeMillis();
    	
    	/*CommitCountFilter commitsJava = new CommitCountFilter();
    	finder.setFilter(PathFilterUtils.andSuffix(".java"));
    	
    	finder.setMatcher(commitsJava);
    	finder.find();

    	System.out.println(commitsJava.getCount() + " java bugs fixed");*/
    	System.out.println("\nTempo de execução: "+ (termino - inicio) + "ms");
    	
    	RevCommit base = CommitUtils.getBase(repo, "/origin/master");

    	CommitCountFilter count = new CommitCountFilter();
    	CommitFinder finder2 = new CommitFinder(repo).setFilter(count);

    	finder2.findBetween("/origin/master", base);
    	System.out.println("Commits in master since release1 was branched: " + count.getCount());

    	/*count.reset();
    	finder.findBetween("release1", base);
    	System.out.println("Commits in release1 since branched from master: " + count.getCount());*/    	
    	
    }
}
