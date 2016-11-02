package br.cefet.tcc.aplication;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.io.DisabledOutputStream;

/*
 * Classe que coleta dados de autoria de software do Github utilizando o JGit
 * a partir de um repositorio e uma determinada tag 
 * (no caso foi escolhida a ultima tag publicada)
 * 
 * @author: Talita Santana Orfano
 * @date: set/2016
 */
public class BuildAuthorshipData {
	private Map<String, HashMap<String, Integer>> fileAndAuthorsMap = new HashMap<String, HashMap<String, Integer>>();
	//public String pathRepository = "/home/talita/git/tomcat/.git";
	//public String tagCurrentString = "TOMCAT_9_0_0_M10";
	//public String pathRepository = "/home/talita/git/glide/.git";
	//public String tagCurrentString = "v3.7.0";
	
	public String pathRepository = "/home/talita/git/androidannotations/.git";
	public String tagCurrentString = "androidannotations-4.1.0";
	
	public BuildAuthorshipData() {
		this.fileAndAuthorsMap = new HashMap<String, HashMap<String,Integer>>();
		this.pathRepository = "/home/talita/git/androidannotations/.git";
		this.tagCurrentString = "androidannotations-4.1.0";
	}	
	
	public BuildAuthorshipData(String pathRepository,
			String tagCurrentString) {
		this.fileAndAuthorsMap = new HashMap<String, HashMap<String,Integer>>();
		this.pathRepository = pathRepository;
		this.tagCurrentString = tagCurrentString;
	}
	
	/*
	 *	Constroi hashmap com os dados de autoria: 
	 *		nome do arquivo, autores e quantidades de modificacoes realizadas por autor nesta tag 
	 */
	public void Build(){
		
		try {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			
			// Scan up the file system tree
			Repository repository = builder.setGitDir(new File((pathRepository))).readEnvironment().findGitDir()
					.build();

			// Tag analisada
			Ref tagcurrent = repository.getRef("refs/tags/" + tagCurrentString);
			//System.out.println("Tag current: " + tagcurrent.getName() + " " + tagcurrent.getObjectId());

			// Caminha sobre os commits da tag definida
			RevWalk walk = new RevWalk(repository);
			walk.markStart(walk.parseCommit(tagcurrent.getObjectId()));
			int count = 0;
			
			for (RevCommit commit : walk) {
				count++;
				//System.out.println("\nTotal: " + count);
				//System.out.println("Commit: " + commits.name() + " " + commits.getAuthorIdent().getName());
				filesUpdatesForCommit(commit, repository, walk, commit.getAuthorIdent().getName());
			}

			System.out.println("Total de commits nesta tag: " + count);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 *  Retorna os arquivos modificados/criados em um commit
	 *  Não considera espacos em branco a direita, nem arquivos deletados
	 */
	private void filesUpdatesForCommit(RevCommit commit, Repository repository, RevWalk rw, String author) {
		RevCommit parent;
		try {

			if (commit.getParentCount() > 0) {
				parent = rw.parseCommit(commit.getParent(0).getId());

				DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
				df.setRepository(repository);
				df.setDiffComparator(RawTextComparator.WS_IGNORE_LEADING);
				df.setDetectRenames(true);
				
				List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
				for (DiffEntry diff : diffs) {
					//System.out.println(MessageFormat.format("({0} {1}", diff.getChangeType().name(), diff.getNewPath()));
					
					String nameFile = diff.getNewPath().trim();
					String changeTypeFile = diff.getChangeType().name();
					if(!changeTypeFile.equalsIgnoreCase("DELETE") && nameFile.contains(".java")){
						if(!fileAndAuthorsMap.containsKey(nameFile)){
							HashMap<String, Integer> mapAuthors = new HashMap<String, Integer>();
							mapAuthors.put(author, 1);
							
							fileAndAuthorsMap.put(nameFile, mapAuthors);
							//System.out.println("Add novo arquivo: "+nameFile+" "+author);
							
						}
						else{
							HashMap<String, Integer> authors = fileAndAuthorsMap.get(nameFile);
							if(authors.containsKey(author)){
								Integer qte = authors.get(author);
								qte++;
								fileAndAuthorsMap.get(nameFile).put(author, qte);
								//System.out.println("Add qte: "+nameFile+" "+author+" "+qte);
							}
							else{
								fileAndAuthorsMap.get(nameFile).put(author, 1);
								//System.out.println("Add novo autor: "+nameFile+" "+author);
							}
						}
					}
				}
			}
		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Lê hash contendo os dados de autoria 
	 */
	public void readMap() {
		 for (Entry<String, HashMap<String, Integer>> key : fileAndAuthorsMap.entrySet()) {
			 System.out.println("Key:"+ key.getKey());
			 for ( Entry<String, Integer> key2 : key.getValue().entrySet()) {
				 System.out.println(" " + key2.getKey()+": "+ key2.getValue());
			 }
		 }
	}

	public Map<String, HashMap<String, Integer>> getFileAndAuthorsMap() {
		return fileAndAuthorsMap;
	}

	public void setFileAndAuthorsMap(Map<String, HashMap<String, Integer>> fileAndAuthorsMap) {
		this.fileAndAuthorsMap = fileAndAuthorsMap;
	}

	public String getPathRepository() {
		return pathRepository;
	}

	public void setPathRepository(String pathRepository) {
		this.pathRepository = pathRepository;
	}

	public String getTagCurrentString() {
		return tagCurrentString;
	}

	public void setTagCurrentString(String tagCurrentString) {
		this.tagCurrentString = tagCurrentString;
	}
}
