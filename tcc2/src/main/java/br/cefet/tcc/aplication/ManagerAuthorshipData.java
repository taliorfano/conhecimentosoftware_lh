package br.cefet.tcc.aplication;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * Classe que gerencia os dados de autoria e propriedade 
 * segundo modelo proposto por Bird e replicado por Foucault,
 * Alem de realizar a gravacao destes dados em arquivos CVS
 * 
 * @author: Talita Santana Orfano
 * @date: set/2016
 */
public class ManagerAuthorshipData {
	
	public static void main(String[] args) {
		String ownershipFile = "ownership_androidannotations";
		String authorsFile = "authors_androidannotations";
		
		Map<String, HashMap<String, Integer>> hashAuthorship;
		BuildAuthorshipData buildAuthorshipData = new BuildAuthorshipData();
		CVSOperations cvsOperations = new CVSOperations();
		CalculateOwnershipMetric calculateOwnershipMetric = new CalculateOwnershipMetric();
		ArrayList<OwnershipMetric> ownershipListMetric = new ArrayList<OwnershipMetric>();
		
		buildAuthorshipData.Build();
		buildAuthorshipData.readMap();
		hashAuthorship = buildAuthorshipData.getFileAndAuthorsMap();
		
		cvsOperations.createAuthorshipCsvFile(authorsFile, hashAuthorship);
		
		calculateOwnershipMetric.Calculate(hashAuthorship);
		ownershipListMetric = calculateOwnershipMetric.getOwnershipMetricsList();		
		cvsOperations.createOwnershipMetricCsvFile(ownershipFile, ownershipListMetric);
		
		System.out.println("Total de arquivos:"+hashAuthorship.values().size());
	}
}
