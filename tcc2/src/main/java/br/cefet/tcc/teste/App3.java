package br.cefet.tcc.teste;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.WalkTransport;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.StringUtils;
import org.eclipse.jgit.util.io.DisabledOutputStream;

public class App3 {

	public static void main(String[] args) throws IOException, NoHeadException, JGitInternalException {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		Repository repository = builder.setGitDir(new File(("/home/talita/git/Wikidata-Toolkit/.git"))).readEnvironment() 
				.findGitDir() // scan up the file system tree
				.build();
		
		Git git = new Git(repository);
		// List<RevTag> a = git.tagList().call();
		Ref HEAD = repository.getRef("refs");

		// Retorna todas as tags
		Map<String, Ref> mapRefs = repository.getAllRefs();
		for (Map.Entry<String, Ref> pair : mapRefs.entrySet()) {
			// System.out.println(""+ pair.getKey()+" - " + pair.getValue());
		}

		// System.out.println(" head " +HEAD.getName());

		ObjectId head = repository.resolve("HEAD");

		// System.out.println("h:"+head.name()+" w: "+walk.);

		Ref tagcurrent = repository.getRef("refs/tags/v0.7.0");
		System.out.println("Tag current: " + tagcurrent.getName() + " " + tagcurrent.getObjectId());

		RevWalk walk = new RevWalk(repository);
		walk.markStart(walk.parseCommit(tagcurrent.getObjectId()));

		int count = 0;
		RevCommit commitCurrent = null;
		for (RevCommit commits : walk) {
			System.out.println("C: " + commits.name() + " " + commits.getAuthorIdent().getName());
			commitCurrent = commits;
			listFilesUpdates(repository, commitCurrent);
			count++;
		}

		System.out.println("count: " + count + " " + commitCurrent.name() + commitCurrent);

		

		// refs/tags/r4.12 e e87c6fc6a52c891759fedd05267c868691573f4f
		// RevTag tag = walk.parseTag(head);

		// System.out.println("t"+tag.getName()+" "+tag.getTagName());

		// Mais uma tentativa
		System.out.println(" Nooow ");
		RevWalk rw = new RevWalk(repository);
		ObjectId tagCurrent = tagcurrent.getObjectId();

		RevCommit commit = rw.parseCommit(tagCurrent);
		RevCommit parent = rw.parseCommit(commit.getParent(0).getId());
		DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
		df.setRepository(repository);
		df.setDiffComparator(RawTextComparator.DEFAULT);
		df.setDetectRenames(true);
		List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
		for (DiffEntry diff : diffs) {
			System.out.println(MessageFormat.format("({0} {1} {2}", diff.getChangeType().name(),
					diff.getNewMode().getBits(), diff.getNewPath()));
			
		}
		
		
		// Retorna msg
		//retornaMsgCommit(git, walk);
	}
	
	
	public static void listFilesUpdates(Repository repository, RevCommit commitCurrent){
		List<String> listFiles = getFilesInPath(repository, "refs/tags/r4.12/", commitCurrent);

		System.out.print(listFiles.size());
		for (String file : listFiles) {
			System.out.print(file + " ");
		}
	}

	/**
	 * Returns the list of files in the specified folder at the specified
	 * commit. If the repository does not exist or is empty, an empty list is
	 * returned.
	 *
	 * @param repository
	 * @param path
	 *            if unspecified, root folder is assumed.
	 * @param commit
	 *            if null, HEAD is assumed.
	 * @return list of files in specified path
	 */
	public static List<String> getFilesInPath(Repository repository, String path, RevCommit commit) {
		List<String> list = new ArrayList<String>();

		final TreeWalk tw = new TreeWalk(repository);
		try {
			tw.addTree(commit.getTree());
			if (!StringUtils.isEmptyOrNull(path)) {
				PathFilter f = PathFilter.create(path);
				tw.setFilter(f);
				tw.setRecursive(false);
				boolean foundFolder = false;
				while (tw.next()) {
					if (!foundFolder && tw.isSubtree()) {
						tw.enterSubtree();
					}
					if (tw.getPathString().equals(path)) {
						foundFolder = true;
						continue;
					}
					if (foundFolder) {
						list.add(getPathModel(tw, path, commit));
					}
				}
			} else {
				tw.setRecursive(false);
				while (tw.next()) {
					list.add(getPathModel(tw, null, commit));
				}
			}
		} catch (IOException e) {
			// error(e, repository, "{0} failed to get files for commit {1}",
			// commit.getName());
		} finally {

		}
		Collections.sort(list);
		return list;
	}

	/**
	 * Returns a path model of the current file in the treewalk.
	 *
	 * @param tw
	 * @param basePath
	 * @param commit
	 * @return a path model of the current file in the treewalk
	 */
	private static String getPathModel(TreeWalk tw, String basePath, RevCommit commit) {
		String name;
		long size = 0;

		if (StringUtils.isEmptyOrNull(basePath)) {
			name = tw.getPathString();
		} else {
			name = tw.getPathString().substring(basePath.length() + 1);
		}
		ObjectId objectId = tw.getObjectId(0);
		// FilestoreModel filestoreItem = null;

		try {
			if (!tw.isSubtree() && (tw.getFileMode(0) != FileMode.GITLINK)) {

				size = tw.getObjectReader().getObjectSize(objectId, Constants.OBJ_BLOB);

				// filestoreItem =
				// getFilestoreItem(tw.getObjectReader().open(objectId));

			}
		} catch (Throwable t) {
			// error(t, null, "failed to retrieve blob size for " +
			// tw.getPathString());
		}
		// return new PathModel(name, tw.getPathString(), filestoreItem, size,
		// tw.getFileMode(0).getBits(),
		// objectId.getName(), commit.getName());
		return name;
	}
 
	// Retorna todos mensagens de commits
	public static void retornaMsgCommit(Git git, RevWalk walk) throws NoHeadException, JGitInternalException, MissingObjectException, IncorrectObjectTypeException, IOException {

		Iterable<RevCommit> logs = git.log().call();
		Iterator<RevCommit> i = logs.iterator();
		RevCommit commit = null;
		System.out.println("");
		
		while (i.hasNext()) {
			commit = walk.parseCommit(i.next());
			System.out.println(commit.getFullMessage());

		}
	}

}
